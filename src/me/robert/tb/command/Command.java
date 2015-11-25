package me.robert.tb.command;

import me.robert.configuration.Configuration;
import me.robert.tb.Launch;

public class Command {

    private String commandName;
    private String response;

    private boolean needsMod;
    private boolean isEnabled;

    public Command(String commandName, String response, boolean needsMod, boolean isEnabled) {
	this.commandName = commandName;
	this.response = response;
	this.needsMod = needsMod;
	this.isEnabled = isEnabled;
    }

    public Command(Configuration config) {
	this.commandName = config.getString("commandName");
	this.response = config.getString("response");
	this.needsMod = config.getBoolean("needsMod");
	this.isEnabled = config.getBoolean("isEnabled");
    }

    public void onCommand(String channel, String sender, String login, String hostname, String message) {
	Launch.getWindow().getBot(channel).message(getResponse());
    }

    public String getName() {
	return this.commandName;
    }

    public String getResponse() {
	return this.response;
    }

    public boolean needsMod() {
	return needsMod;
    }

    public boolean isEnabled() {
	return isEnabled;
    }

}
