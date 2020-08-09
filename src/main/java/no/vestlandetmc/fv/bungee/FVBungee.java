package no.vestlandetmc.fv.bungee;

import java.io.IOException;

import net.md_5.bungee.api.plugin.Plugin;
import no.vestlandetmc.fv.bungee.commands.FellesVarslingBungee;
import no.vestlandetmc.fv.bungee.config.Config;
import no.vestlandetmc.fv.bungee.listeners.LitebansAPI;
import no.vestlandetmc.fv.bungee.listeners.PlayerListener;

public class FVBungee extends Plugin {

	private static FVBungee instance;

	public static FVBungee getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		try {
			Config.initialize();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		getProxy().getPluginManager().registerCommand(this, new FellesVarslingBungee());
		this.getProxy().getPluginManager().registerListener(this, new PlayerListener());

		getLogger().info("Kontakter databasen...");

		if(new MySQLHandler().initialize()) {
			getLogger().info("Kontakt med database er oppnådd");
			MySQLHandler.sqlEnabled = true;
		} else {
			getLogger().info("Kontakt med database feilet");
			MySQLHandler.sqlEnabled = false;
		}

		if(getProxy().getPluginManager().getPlugin("LiteBans") != null) {
			if(Config.LITEBANS_ENABLE) {
				new LitebansAPI();
				getLogger().info("Litebans ble funnet og koblet til");
			} else { getLogger().info("Litebans ble funnet men er ikke koblet til. Se config.yml --> enable=true/false"); }
		}
	}
}
