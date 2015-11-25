package me.robert.tb;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.robert.tb.command.CommandManager;
import me.robert.tb.ui.ViewerTab;
import me.robert.tb.util.Followers;
import me.robert.tb.util.HTTPConnect;

public class Bot extends PircBot {

    private String channel;

    private CommandManager commandManager;

    private ViewerTab viewerTab;
    private Followers followers;

    public Bot(String channel) {
	setChannel("#" + channel);

	initBot();
    }

    private void initBot() {
	this.setVerbose(true);

	this.setName(Launch.getWindow().getBotName());
	this.setLogin(Launch.getWindow().getBotName());

	try {
	    this.connect("irc.twitch.tv", 6667, Launch.getWindow().getOAuth());
	} catch (IOException | IrcException e) {
	    e.printStackTrace();
	}

	this.joinChannel(this.channel);

	commandManager = new CommandManager(getChannel(false));

	followers = new Followers(getChannel(false));
	followers.initFollowerTracker();
	viewerTab = new ViewerTab(getChannel(false));
	loadViewers();
	Launch.getWindow().baseViewerTab.addTab(getChannel(false), viewerTab);
    }

    public Followers getFollowers() {
	return this.followers;
    }

    public void message(String message) {
	this.sendMessage(getChannel(true), message);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
	super.onMessage(channel, sender, login, hostname, message);
	Launch.getWindow().channelTab.getChatTab(getChannel(false)).append(sender, message);
	if (message.startsWith("!"))
	    getCommandManager().onCommand(getChannel(false), sender, login, hostname, message);
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
	super.onJoin(channel, sender, login, hostname);
	loadViewers();
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
	super.onPart(channel, sender, login, hostname);
	loadViewers();
    }

    public String getChannel(boolean includeSymbol) {
	return includeSymbol ? this.channel : this.channel.substring(1);
    }

    public void setChannel(String channel) {
	this.channel = channel;
    }

    public CommandManager getCommandManager() {
	return this.commandManager;
    }

    public ViewerTab getViewerTab() {
	return this.viewerTab;
    }

    public void loadViewers() {
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		JsonObject obj = Launch.getWindow().getJson()
			.parse(HTTPConnect
				.getResponse("https://tmi.twitch.tv/group/user/" + getChannel(false) + "/chatters"))
			.getAsJsonObject();
		obj = obj.get("chatters").getAsJsonObject();
		JsonArray mods = obj.get("moderators").getAsJsonArray();
		JsonArray staff = obj.get("staff").getAsJsonArray();
		JsonArray admins = obj.get("admins").getAsJsonArray();
		JsonArray globalMod = obj.get("global_mods").getAsJsonArray();
		JsonArray watchers = obj.get("viewers").getAsJsonArray();

		for (int i = 0; i < mods.size(); i++)
		    viewerTab.addViewer(mods.get(i).getAsString(), true);
		for (int i = 0; i < staff.size(); i++)
		    viewerTab.addViewer(staff.get(i).getAsString(), true);
		for (int i = 0; i < admins.size(); i++)
		    viewerTab.addViewer(admins.get(i).getAsString(), true);
		for (int i = 0; i < globalMod.size(); i++)
		    viewerTab.addViewer(globalMod.get(i).getAsString(), true);
		for (int i = 0; i < watchers.size(); i++)
		    viewerTab.addViewer(watchers.get(i).getAsString(), false);

		viewerTab.refresh();
	    }
	}).start();
    }
}
