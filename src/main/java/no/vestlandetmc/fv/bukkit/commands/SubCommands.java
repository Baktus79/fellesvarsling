package no.vestlandetmc.fv.bukkit.commands;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import no.vestlandetmc.fv.bukkit.FVBukkit;
import no.vestlandetmc.fv.bukkit.MessageHandler;
import no.vestlandetmc.fv.bukkit.MySQLHandler;
import no.vestlandetmc.fv.bukkit.UUIDFetcher;
import no.vestlandetmc.fv.bukkit.config.Config;

public class SubCommands {

	private final boolean isConsole;
	private Player player;
	private final MySQLHandler sql = new MySQLHandler();

	public SubCommands(CommandSender sender) {
		if(!(sender instanceof Player)) { this.isConsole = true; }
		else {
			this.isConsole = false;
			this.player = (Player) sender;
		}
	}

	public void add(String[] args) {
		if(!isConsole) {
			if(!this.player.hasPermission("fellesvarsling.add")) {
				MessageHandler.sendMessage(this.player, "&cBeklager, men du har ikke tillatelse til å bruke denne kommandoen.");
				return;
			}
		}

		if(args.length < 2) {
			if(!isConsole) { MessageHandler.sendMessage(player, "&cSkriv inn et gyldig spillernavn."); }
			else { MessageHandler.sendConsole("&cSkriv inn et gyldig spillernavn."); }

			return;
		}

		new BukkitRunnable() {

			@Override
			public void run() {
				final UUID uuid = UUIDFetcher.getUUID(args[1]);
				final String playerName = uuid != null ? UUIDFetcher.getName(uuid) : args[1];
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

			}
		}.runTaskAsynchronously(FVBukkit.getInstance());

	}

	public void hjelp() {
		if(isConsole) {
			MessageHandler.sendConsole(
					"&e--- ==== &6Felles Varslingsystem &e==== ---",
					"&6/fv lookup &f[spiller] - &eSe siste registrerte varslinger på spiller.",
					"&6/fv slett &f[id] - &eSlett oppføring på spiller.",
					"&6/fv hjelp &f- &eSe denne hjelpesiden.",
					"&6/fv add &f[spiller] [årsak] - &eLegg til en varsling på spiller.",
					"&6/fv reload &f- &eLast om de fleste konfigurasjoner. (MySQL krever omstart)");
		}

		else {
			MessageHandler.sendMessage(this.player,
					"&e--- ==== &6Felles Varslingsystem &e==== ---",
					"&6/fv lookup &f[spiller] - &eSe siste registrerte varslinger på spiller.",
					"&6/fv slett &f[id] - &eSlett oppføring på spiller.",
					"&6/fv hjelp &f- &eSe denne hjelpesiden.",
					"&6/fv add &f[spiller] [årsak] - &eLegg til en varsling på spiller.",
					"&6/fv reload &f- &eLast om de fleste konfigurasjoner. (MySQL krever omstart)");
		}
	}

	public void lookup(String[] args) {
		if(!isConsole) {
			if(!this.player.hasPermission("fellesvarsling.lookup")) {
				MessageHandler.sendMessage(this.player, "&cBeklager, men du har ikke tillatelse til å bruke denne kommandoen.");
				return;
			}
		}

		if(args.length < 2) {
			if(!isConsole) { MessageHandler.sendMessage(player, "&cSkriv inn et gyldig spillernavn."); }
			else { MessageHandler.sendConsole("&cSkriv inn et gyldig spillernavn."); }

			return;
		}

		new BukkitRunnable() {

			@Override
			public void run() {
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
			}
		}.runTaskAsynchronously(FVBukkit.getInstance());
	}

	public void reload() {
		if(!isConsole) {
			if(!this.player.hasPermission("fellesvarsling.reload")) {
				MessageHandler.sendMessage(this.player, "&cBeklager, men du har ikke tillatelse til å bruke denne kommandoen.");
				return;
			}
		}

		final String message = "&eKonfigurasjonsfilen er lastet om på nytt.";

		final String sqlEnabled = "&eMySQL: &8[&aAktivert&8]";
		final String sqlDisable = "&eMySQL: &8[&cDeaktivert&8]";
		final String sqlSS = Config.ENABLE_SSL ? "&eMySQL SSL: &8[&aAktivert&8]" : "&eMySQL SSL: &8[&cDeaktivert&8]";
		final String litebans = Config.LITEBANS_ENABLE ? "&eLitebans: &8[&aAktivert&8]" : "&eLitebans: &8[&cDeaktivert&8]";

		Config.initialize();

		if(!isConsole) { MessageHandler.sendMessage(player, message); }
		else { MessageHandler.sendConsole(message); }

		if(new MySQLHandler().initialize()) {
			if(!isConsole) { MessageHandler.sendMessage(player, sqlEnabled); }
			else { MessageHandler.sendConsole(sqlEnabled); }

			if(!isConsole) { MessageHandler.sendMessage(player, sqlSS); }
			else { MessageHandler.sendConsole(sqlSS); }
		} else {
			if(!isConsole) { MessageHandler.sendMessage(player, sqlDisable); }
			else { MessageHandler.sendConsole(sqlDisable); }
		}

		if(!isConsole) { MessageHandler.sendMessage(player, litebans); }
		else { MessageHandler.sendConsole(litebans); }

	}

	public void slett(String[] args) {
		if(!isConsole) {
			if(!this.player.hasPermission("fellesvarsling.slett")) {
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

		new BukkitRunnable() {

			@Override
			public void run() {
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

			}
		}.runTaskAsynchronously(FVBukkit.getInstance());

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
