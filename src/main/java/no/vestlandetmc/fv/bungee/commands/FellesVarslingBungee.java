package no.vestlandetmc.fv.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class FellesVarslingBungee extends Command {

	public FellesVarslingBungee() {
		super("fellesvarsling", "fellesvarsling.bruk", "fv", "varsle", "varsling");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		final SubCommands sub = new SubCommands(sender);

		if(args.length == 0) {
			sub.hjelp();
			return;
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

	}

}
