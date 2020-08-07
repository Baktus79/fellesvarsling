package no.vestlandetmc.fv.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FellesVarsling implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		final SubCommands sub = new SubCommands(sender);

		if(args.length == 0) {
			sub.hjelp();
			return true;
		}

		switch (args[0]) {
		case "add":
			sub.add(args);
			break;

		case "hjelp":
			sub.hjelp();
			break;

		case "lookup":
			sub.lookup(args);
			break;

		case "l":
			sub.lookup(args);
			break;

		case "reload":
			sub.reload();
			break;

		case "slett":
			sub.slett(args);
			break;

		default:
			sub.hjelp();
			break;
		}

		return true;
	}

}
