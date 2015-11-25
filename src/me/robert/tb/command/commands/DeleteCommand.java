package me.robert.tb.command.commands;

import me.robert.tb.Launch;
import me.robert.tb.command.Command;

public class DeleteCommand extends Command {

    public DeleteCommand() {
	super("delCom", "", true, true);
    }

    @Override
    public void onCommand(String channel, String sender, String login, String hostname, String message) {
	String[] args = message.split(" ");

	if (args.length <= 1) {
	    Launch.getWindow().getBot(channel).message("To delete a command use: !delCom <commandName>");
	    System.out.println("Not enough args");
	    return;
	} else {
	    Launch.getWindow().getBot(channel).getCommandManager().deleteCommand(args[1]);
	}
    }

}
