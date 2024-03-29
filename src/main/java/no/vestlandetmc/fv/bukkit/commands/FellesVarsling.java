package no.vestlandetmc.fv.bukkit.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class FellesVarsling implements CommandExecutor, TabCompleter {

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

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		final ArrayList<String> list = new ArrayList<>();

		list.add("add");
		list.add("hjelp");
		list.add("lookup");
		list.add("l");
		list.add("reload");
		list.add("slett");

		if(args.length == 0) {
			return list;

		} else if(args.length == 1) {
			final List<String> commandList = new ArrayList<>();

			for(final String commands : list) {
				if(commands.startsWith(args[0])) { commandList.add(commands); }
			}

			return commandList;

		}

		return null;
	}

}
