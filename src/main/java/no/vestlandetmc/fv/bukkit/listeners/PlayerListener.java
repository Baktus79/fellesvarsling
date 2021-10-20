package no.vestlandetmc.fv.bukkit.listeners;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import no.vestlandetmc.fv.bukkit.FVBukkit;
import no.vestlandetmc.fv.bukkit.MessageHandler;
import no.vestlandetmc.fv.bukkit.database.MySQLHandler;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		final Player player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
		final MySQLHandler sql = new MySQLHandler();

		Bukkit.getScheduler().runTaskAsynchronously(FVBukkit.getInstance(), () -> {
			try {
				if(sql.erVarslet(uuid)) {
					MessageHandler.clickableAnnounce("&c" + player.getName() + " har varslinger. Klikk her for mer informasjon.", "/fellesvarsling lookup " + player.getName());
				}
			} catch (final SQLException exept) {
				exept.printStackTrace();
			}
		});
	}
}
