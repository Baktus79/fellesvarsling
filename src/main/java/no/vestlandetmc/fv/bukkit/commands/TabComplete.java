package no.vestlandetmc.fv.bukkit.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabComplete implements TabCompleter {

	private final ArrayList<String> list = new ArrayList<>();

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

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
