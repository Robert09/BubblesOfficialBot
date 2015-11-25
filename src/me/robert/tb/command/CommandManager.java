package me.robert.tb.command;

import java.io.File;
import java.util.ArrayList;

import me.robert.configuration.Configuration;
import me.robert.tb.command.commands.AddCommand;
import me.robert.tb.command.commands.DeleteCommand;

public class CommandManager {

    private ArrayList<Command> cmds;

    private File folder;
    private File[] commandFiles;

    public CommandManager(String channelName) {
	cmds = new ArrayList<Command>();

	folder = new File("C:\\twitchbot\\commands\\" + channelName);

	if (!folder.exists()) {
	    folder.mkdirs();
	}

	loadCommands();
    }

    private void loadCommands() {
	cmds.clear();
	System.out.println("Loading commands");
	commandFiles = folder.listFiles();

	addCommand(new AddCommand());
	addCommand(new DeleteCommand());

	for (File f : commandFiles) {
	    Configuration temp = new Configuration(folder.getPath(), f);
	    addCommand(new Command(temp));
	}
    }

    public void addCommand(Command command) {
	cmds.add(command);
    }

    public void deleteCommand(String commandName) {
	File temp = new File(commandName);
	for (File f : commandFiles) {
	    if (f.getName().equalsIgnoreCase(commandName + ".yml")) {
		temp = f;
	    }
	}
	cmds.clear();
	commandFiles = null;
	temp.delete();
	loadCommands();
    }

    public void onCommand(String channel, String sender, String login, String hostname, String message) {

	String[] msg = message.split(" ");

	for (Command command : cmds) {
	    if (msg[0].substring(1).equalsIgnoreCase(command.getName())) {
		command.onCommand(channel, sender, login, hostname, message);
		return;
	    }
	}
    }

    public File getFolder() {
	return folder;
    }

    public void setFolder(File folder) {
	this.folder = folder;
    }

}
