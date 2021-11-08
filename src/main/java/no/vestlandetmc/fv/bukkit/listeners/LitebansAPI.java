package no.vestlandetmc.fv.bukkit.listeners;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import litebans.api.Entry;
import litebans.api.Events;
import no.vestlandetmc.fv.bukkit.FVBukkit;
import no.vestlandetmc.fv.bukkit.config.Config;
import no.vestlandetmc.fv.bukkit.database.MySQLHandler;

public class LitebansAPI {

	public LitebansAPI() {
		Events.get().register(new Events.Listener() {

			@Override
			public void entryAdded(Entry e) {
				final MySQLHandler sql = new MySQLHandler();
				final String type = e.getType();
				final UUID uuid = UUID.fromString(e.getUuid());
				final long expire = e.getDateEnd();
				final String reason = e.getReason();

				if(!MySQLHandler.sqlEnabled) { return; }
				if(!entryType(type)) { return; }
				if(filter(reason)) { return; }

				new BukkitRunnable() {
					@Override
					public void run() {
						try {
							sql.setUser(uuid, type, expire, reason);
						} catch (final SQLException e1) {
							e1.printStackTrace();
						}
					}
				}.runTaskAsynchronously(FVBukkit.getInstance());
			}
		});
	}

	private boolean filter(String filter) {
		for(final String f : Config.WORD_FILTER) {
			if(filter.contains(f)) {
				return true;
			}
		}

		return false;
	}

	private boolean entryType(String entrytype) {
		if(entrytype.equals("ban")) { return Config.LITEBANS_BAN; }
		else if(entrytype.equals("mute")) { return Config.LITEBANS_MUTE; }
		else if(entrytype.equals("warning")) { return Config.LITEBANS_WARN; }
		else if(entrytype.equals("kick")) { return Config.LITEBANS_KICK; }

		return false;
	}
}
