package me.robert.tb.util;

import java.io.File;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.robert.configuration.Configuration;
import me.robert.tb.Launch;

public class Followers implements Runnable {
    public static boolean run = false;
    private int i = 0;
    private Thread thread;

    private String channelName;
    private String path;

    private Configuration followerFile;

    public Followers(String channelName) {
	this.channelName = channelName;
	path = "C:\\twitchbot\\follower_tracker\\" + channelName + "\\";
	followerFile = new Configuration(path, new File(path + channelName + ".yml"));
    }

    public Configuration getFollowerFile() {
	return this.followerFile;
    }

    /**
     * Starts the follower tracker for the bot in the channel
     */
    public void initFollowerTracker() {
	if (this.followerFile.isEmpty())
	    loadFollowers();
	else
	    checkFollowers(false);

	run = true;
	if (thread == null || !thread.isAlive()) {
	    thread = new Thread(this);
	    thread.start();
	}
    }

    /**
     * Stops the follower tracker for the bot in the channel
     */
    public void stopFollowerTracker() {
	run = false;
    }

    /**
     * Starts the thread the controls the follower check
     */
    @Override
    public void run() {
	while (run) {
	    checkFollowers(true);
	    System.out.println("Checked for new followers");
	    try {
		synchronized (this) {
		    this.wait(10000);
		}
	    } catch (InterruptedException e) {
		e.printStackTrace();
		this.stopFollowerTracker();
	    }
	}
	try {
	    thread.interrupt();
	    thread.join();
	} catch (InterruptedException e) {
	}
    }

    /**
     * Checks for any new followers since last time the bot ran.
     */
    public void loadFollowers() {
	JsonObject obj = Launch.getWindow().getJson()
		.parse(HTTPConnect
			.getResponse("https://api.twitch.tv/kraken/channels/" + channelName + "/follows?limit=100"))
		.getAsJsonObject();

	int total = obj.get("_total").getAsInt();
	int current = 0;
	String nexturl = "https://api.twitch.tv/kraken/channels/" + channelName + "/follows?limit=100";
	while (current < total) {
	    boolean success = false;
	    int loops = 0;
	    while (!success) {
		try {
		    obj = Launch.getWindow().getJson().parse(HTTPConnect.getResponse(nexturl)).getAsJsonObject();
		    success = true;
		} catch (IllegalStateException e) {
		    loops++;
		    if (loops > 10)
			return;
		}
	    }
	    try {
		nexturl = obj.get("_links").getAsJsonObject().get("next").getAsString();
	    } catch (IndexOutOfBoundsException e) {
		nexturl = obj.get("_links").getAsJsonObject().get("next").getAsString();
	    }
	    JsonArray list = obj.get("follows").getAsJsonArray();
	    for (int i = 0; i < list.size(); i++) {
		String temp = list.get(i).getAsJsonObject().get("user").getAsJsonObject().get("display_name")
			.getAsString();
		this.followerFile.set(temp.toLowerCase(),
			list.get(i).getAsJsonObject().get("created_at").getAsString());
	    }
	    current += 100;
	}
	// ConsoleTab.output(Level.Info, "Updated follower list for " +
	// Launch.botWindow.getBot().getChannel(false));
    }

    /**
     * Checks for new followers.
     */
    public void checkFollowers(boolean output) {
	JsonObject obj;
	try {
	    obj = Launch.getWindow().getJson()
		    .parse(HTTPConnect.getResponse(
			    "https://api.twitch.tv/kraken/channels/" + channelName + "/follows?limit=2" + i))
		    .getAsJsonObject();
	    i = i == 0 ? 1 : 0;
	} catch (IllegalStateException ex) {
	    return;
	}
	JsonArray list = obj.get("follows").getAsJsonArray();
	for (int i = 0; i < list.size(); i++) {

	    String temp = list.get(i).getAsJsonObject().get("user").getAsJsonObject().get("display_name").getAsString();
	    if (!followerFile.contains(temp.toLowerCase())) {
		if (output) {
		    // Launch.getWindow().getBot(channelName).message(temp + "
		    // Has just followed!!!");
		    System.out.println(temp + " Has just followed!!!");
		}
		this.followerFile.set(temp.toLowerCase(),
			list.get(i).getAsJsonObject().get("created_at").getAsString());
	    }
	}
    }

    public boolean isRunning() {
	return run;
    }
}