package me.robert.tb.command.commands;

import java.io.File;

import me.robert.configuration.Configuration;
import me.robert.tb.Launch;
import me.robert.tb.command.Command;

public class AddCommand extends Command {

    public AddCommand() {
	super("addCom", "", true, true);
    }

    @Override
    public void onCommand(String channel, String sender, String login, String hostname, String message) {
	String[] args = message.split(" ");

	if (args.length <= 3) {
	    Launch.getWindow().getBot(channel)
		    .message("To add a command use: !addCom <commandName> <needsMod> <response>");
	    System.out.println("Not enough args");
	    return;
	} else {
	    String path = Launch.getWindow().getBot(channel).getCommandManager().getFolder().getPath();

	    Configuration temp = new Configuration(path, new File(path + "\\" + args[1] + ".yml"));
	    temp.set("commandName", args[1]);
	    temp.set("needsMod", Boolean.parseBoolean(args[2]));

	    String response = "";

	    for (int i = 3; i < args.length; i++) {
		response += " " + args[i];
	    }

	    temp.set("response", response.substring(1));
	    temp.set("isEnabled", true);

	    Launch.getWindow().getBot(channel).getCommandManager().addCommand(new Command(temp));
	}
    }

}
