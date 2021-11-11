package no.vestlandetmc.fv.bungee.listeners;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import no.vestlandetmc.fv.bungee.FVBungee;
import no.vestlandetmc.fv.bungee.MessageHandler;
import no.vestlandetmc.fv.bungee.database.MySQLHandler;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PostLoginEvent e) {
		final ProxiedPlayer player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
		final MySQLHandler sql = new MySQLHandler();

		ProxyServer.getInstance().getScheduler().schedule(FVBungee.getInstance(), () -> {
			if(!player.isConnected()) { return; }

			try {
				if(sql.erVarslet(uuid)) {
					MessageHandler.clickableAnnounce("&c" + player.getName() + " har varslinger. Klikk her for mer informasjon.", "/fellesvarsling lookup " + player.getName());
				}
			} catch (final SQLException ex) {
				ex.printStackTrace();
			}
		}, 1, TimeUnit.SECONDS);
	}
}
