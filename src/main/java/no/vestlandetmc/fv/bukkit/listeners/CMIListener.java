package no.vestlandetmc.fv.bukkit.listeners;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.Zrips.CMI.events.CMIPlayerBanEvent;

import no.vestlandetmc.fv.bukkit.FVBukkit;
import no.vestlandetmc.fv.bukkit.MySQLHandler;
import no.vestlandetmc.fv.bukkit.config.Config;

public class CMIListener implements Listener {

	@EventHandler
	public void ban(CMIPlayerBanEvent e) {
		final MySQLHandler sql = new MySQLHandler();
		final String type = "ban";
		final UUID uuid = e.getBanned();
		final long expire = e.getUntil();
		final String reason = e.getReason();

		if(!MySQLHandler.sqlEnabled) { return; }
		if(!Config.CMI_BAN) { return; }
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

	private boolean filter(String filter) {
		for(final String f : Config.WORD_FILTER) {
			if(filter.contains(f)) {
				return true;
			}
		}

		return false;
	}
}
