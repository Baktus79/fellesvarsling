package no.vestlandetmc.fv.bungee.listeners;

import java.sql.SQLException;
import java.util.UUID;

import litebans.api.Entry;
import litebans.api.Events;
import net.md_5.bungee.api.ProxyServer;
import no.vestlandetmc.fv.bungee.FVBungee;
import no.vestlandetmc.fv.bungee.MySQLHandler;
import no.vestlandetmc.fv.bungee.config.Config;

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

				ProxyServer.getInstance().getScheduler().runAsync(FVBungee.getInstance(), () -> {
					try {
						sql.setUser(uuid, type, expire, reason);
					} catch (final SQLException e1) {
						e1.printStackTrace();
					}
				});
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
