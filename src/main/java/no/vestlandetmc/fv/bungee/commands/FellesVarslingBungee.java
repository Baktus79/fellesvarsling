package no.vestlandetmc.fv.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import no.vestlandetmc.fv.bungee.MessageHandler;
import no.vestlandetmc.fv.bungee.Permissions;

public class FellesVarslingBungee extends Command {

	public FellesVarslingBungee() {
		super("fellesvarsling", "fellesvarsling.bruk", "fv", "varsle", "varsling");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			if(!sender.hasPermission(Permissions.ADMIN) ||
					!sender.hasPermission(Permissions.MOD) ||
					!sender.hasPermission(Permissions.BRUK)) {
				MessageHandler.sendMessage((ProxiedPlayer) sender, "&cBeklager, men du har ikke tillatelse til å bruke denne kommandoen.");
				return;
			}
		}

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
