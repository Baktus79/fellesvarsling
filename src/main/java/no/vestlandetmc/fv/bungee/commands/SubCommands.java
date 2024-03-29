package no.vestlandetmc.fv.bungee.commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import no.vestlandetmc.fv.bungee.FVBungee;
import no.vestlandetmc.fv.bungee.MessageHandler;
import no.vestlandetmc.fv.bungee.Permissions;
import no.vestlandetmc.fv.bungee.config.Config;
import no.vestlandetmc.fv.bungee.database.MySQLHandler;
import no.vestlandetmc.fv.bungee.database.MySqlPool;
import no.vestlandetmc.fv.bungee.util.NameFetcher;
import no.vestlandetmc.fv.bungee.util.UUIDFetcher;

public class SubCommands {

	private final boolean isConsole;
	private ProxiedPlayer player;
	private final MySQLHandler sql = new MySQLHandler();
	private final String sqlMessage = "&cMySQL er ikke aktivert. Aktiver MySQL før du bruker denne kommandoen.";

	public SubCommands(CommandSender sender) {
		if(!(sender instanceof ProxiedPlayer)) { this.isConsole = true; }
		else {
			this.isConsole = false;
			this.player = (ProxiedPlayer) sender;
		}
	}

	public void add(String[] args) {
		if(!MySQLHandler.sqlEnabled) {
			if(!isConsole) { MessageHandler.sendMessage(player, sqlMessage); }
			else { MessageHandler.sendConsole(sqlMessage); }
			return;
		}

		if(!isConsole) {
			if(!this.player.hasPermission(Permissions.ADD)) {
				MessageHandler.sendMessage(this.player, "&cBeklager, men du har ikke tillatelse til å bruke denne kommandoen.");
				return;
			}
		}

		if(args.length < 2) {
			if(!isConsole) { MessageHandler.sendMessage(player, "&cSkriv inn et gyldig spillernavn."); }
			else { MessageHandler.sendConsole("&cSkriv inn et gyldig spillernavn."); }

			return;
		}

		ProxyServer.getInstance().getScheduler().runAsync(FVBungee.getInstance(), () -> {
			final UUID uuid = UUIDFetcher.getUUID(args[1]);
			final String playerName = uuid != null ? NameFetcher.getName(uuid) : args[1];
			final String reason = reason(args);
			final String meldingRegistrert = "&eVarsling for &6" + playerName + " &eer registrert med årsak: " + reason;
			final String meldingUgyldig = "&cSpiller " + args[1] + " er ikke et gyldig spillernavn.";

			if(uuid == null) {
				if(!isConsole) { MessageHandler.sendMessage(player, meldingUgyldig); }
				else { MessageHandler.sendConsole(meldingUgyldig); }

				return;
			}

			try {
				sql.setUser(uuid, "manuell", 0, reason);
			} catch (final SQLException e) {
				e.printStackTrace();
			}

			if(!isConsole) { MessageHandler.sendMessage(player, meldingRegistrert); }
			else { MessageHandler.sendConsole(meldingRegistrert); }
		});

	}

	public void hjelp() {
		if(isConsole) {
			MessageHandler.sendConsole(
					"&e--- ==== &6Felles Varslingsystem &e==== ---",
					"&6/fv lookup &f[spiller] - &eSe siste registrerte varslinger på spiller.",
					"&6/fv slett &f[id] - &eSlett oppføring på spiller.",
					"&6/fv hjelp &f- &eSe denne hjelpesiden.",
					"&6/fv add &f[spiller] [årsak] - &eLegg til en varsling på spiller.",
					"&6/fv reload &f- &eLast om konfigurasjonsfilen.");
		}

		else {
			MessageHandler.sendMessage(this.player,
					"&e--- ==== &6Felles Varslingsystem &e==== ---",
					"&6/fv lookup &f[spiller] - &eSe siste registrerte varslinger på spiller.",
					"&6/fv slett &f[id] - &eSlett oppføring på spiller.",
					"&6/fv hjelp &f- &eSe denne hjelpesiden.",
					"&6/fv add &f[spiller] [årsak] - &eLegg til en varsling på spiller.",
					"&6/fv reload &f- &eLast om konfigurasjonsfilen.");
		}
	}

	public void lookup(String[] args) {
		if(!MySQLHandler.sqlEnabled) {
			if(!isConsole) { MessageHandler.sendMessage(player, sqlMessage); }
			else { MessageHandler.sendConsole(sqlMessage); }
			return;
		}

		if(!isConsole) {
			if(!this.player.hasPermission(Permissions.LOOKUP)) {
				MessageHandler.sendMessage(this.player, "&cBeklager, men du har ikke tillatelse til å bruke denne kommandoen.");
				return;
			}
		}

		if(args.length < 2) {
			if(!isConsole) { MessageHandler.sendMessage(player, "&cSkriv inn et gyldig spillernavn."); }
			else { MessageHandler.sendConsole("&cSkriv inn et gyldig spillernavn."); }

			return;
		}

		ProxyServer.getInstance().getScheduler().runAsync(FVBungee.getInstance(), () -> {
			final UUID uuid = UUIDFetcher.getUUID(args[1]);
			final String meldingUgyldig = "&cSpiller " + args[1] + " er ikke et gyldig spillernavn.";

			if(uuid == null) {
				if(!isConsole) { MessageHandler.sendMessage(player, meldingUgyldig); }
				else { MessageHandler.sendConsole(meldingUgyldig); }

				return;
			}

			try {
				if(isConsole) { sql.getInfo(null, uuid); }
				else { sql.getInfo(player, uuid); }
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public void reload() {
		if(!isConsole) {
			if(!this.player.hasPermission(Permissions.RELOAD)) {
				MessageHandler.sendMessage(this.player, "&cBeklager, men du har ikke tillatelse til å bruke denne kommandoen.");
				return;
			}
		}

		final String message = "&eKonfigurasjonsfilen er lastet om på nytt.";

		final String sqlEnabled = "&eMySQL: &8[&aAktivert&8]";
		final String sqlDisable = "&eMySQL: &8[&cDeaktivert&8]";
		final String sqlSS = Config.ENABLE_SSL ? "&eMySQL SSL: &8[&aAktivert&8]" : "&eMySQL SSL: &8[&cDeaktivert&8]";
		final String litebans = Config.LITEBANS_ENABLE ? "&eLitebans: &8[&aAktivert&8]" : "&eLitebans: &8[&cDeaktivert&8]";

		try {
			Config.initialize();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		if(!isConsole) { MessageHandler.sendMessage(player, message); }
		else { MessageHandler.sendConsole(message); }

		try {
			new MySqlPool().initialize();
		} catch (final SQLException e) {
			e.printStackTrace();
		}

		if(!isConsole) { MessageHandler.sendMessage(player, sqlEnabled); }
		else { MessageHandler.sendConsole(sqlEnabled); }

		if(!isConsole) { MessageHandler.sendMessage(player, sqlSS); }
		else { MessageHandler.sendConsole(sqlSS); }

		MySQLHandler.sqlEnabled = true;

		if(!isConsole) { MessageHandler.sendMessage(player, sqlDisable); }
		else {
			MySQLHandler.sqlEnabled = false;
			MessageHandler.sendConsole(sqlDisable);
		}

		if(!isConsole) { MessageHandler.sendMessage(player, litebans); }
		else { MessageHandler.sendConsole(litebans); }

	}

	public void slett(String[] args) {
		if(!MySQLHandler.sqlEnabled) {
			if(!isConsole) { MessageHandler.sendMessage(player, sqlMessage); }
			else { MessageHandler.sendConsole(sqlMessage); }
			return;
		}

		if(!isConsole) {
			if(!this.player.hasPermission(Permissions.SLETT)) {
				MessageHandler.sendMessage(this.player, "&cBeklager, men du har ikke tillatelse til å bruke denne kommandoen.");
				return;
			}
		}

		final String messageError = "&cSkriv inn id for varslingen som skal slettes.";
		final String messageErrorTall = "&cSkriv inn et gyldig tall";

		if(args.length < 2) {
			if(!isConsole) { MessageHandler.sendMessage(player, messageError); }
			else { MessageHandler.sendConsole(messageError); }

			return;
		}

		else if(!isInt(args[1])) {
			if(!isConsole) { MessageHandler.sendMessage(player, messageErrorTall); }
			else { MessageHandler.sendConsole(messageErrorTall); }

			return;
		}

		final int id = convertToInt(args[1]);

		ProxyServer.getInstance().getScheduler().runAsync(FVBungee.getInstance(), () -> {
			final String messageTrue = "&eVarsling med id &6" + id + " &eble slettet.";
			final String messageFalse = "&cVarsling med id " + id + " kunne ikke slettes. Merk: Du kan ikke slette varslinger som ikke er utført av din server.";

			try {
				if(sql.deleteUser(id)) {
					if(!isConsole) { MessageHandler.sendMessage(player, messageTrue); }
					else { MessageHandler.sendConsole(messageTrue); }
				} else {
					if(!isConsole) { MessageHandler.sendMessage(player, messageFalse); }
					else { MessageHandler.sendConsole(messageFalse); }
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		});

	}

	private String reason(String[] args) {
		final StringBuilder message = new StringBuilder();
		for(int i = 2; i < args.length; i++) {
			message.append(args[i] + " ");
		}

		return message.length() == 0 ? "Ingen årsak angitt" : message.toString();
	}

	private boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (final NumberFormatException e) {
			return false;
		}
	}

	private int convertToInt(String str) {
		return Integer.valueOf(str);
	}

}
