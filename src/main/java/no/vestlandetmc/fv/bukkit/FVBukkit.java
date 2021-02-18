package no.vestlandetmc.fv.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import no.vestlandetmc.fv.bukkit.commands.FellesVarsling;
import no.vestlandetmc.fv.bukkit.commands.TabComplete;
import no.vestlandetmc.fv.bukkit.config.Config;
import no.vestlandetmc.fv.bukkit.listeners.CMIListener;
import no.vestlandetmc.fv.bukkit.listeners.LitebansAPI;
import no.vestlandetmc.fv.bukkit.listeners.PlayerListener;

public class FVBukkit extends JavaPlugin {

	private static FVBukkit instance;

	public static FVBukkit getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		this.getCommand("fellesvarsling").setExecutor(new FellesVarsling());
		this.getCommand("fellesvarsling").setTabCompleter(new TabComplete());
		Config.initialize();

		MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] Kontakter databasen...");

		if(new MySQLHandler().initialize()) {
			MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] Kontakt med database er oppnådd");
			this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
			MySQLHandler.sqlEnabled = true;
		} else {
			MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] Kontakt med database feilet");
			MySQLHandler.sqlEnabled = false;
		}

		if(getServer().getPluginManager().getPlugin("Litebans") != null) {
			if(Config.LITEBANS_ENABLE) {
				new LitebansAPI();
				MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] Litebans ble funnet og koblet til");
			} else { MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] Litebans ble funnet men er ikke koblet til. Se config.yml --> enable=true/false"); }
		}

		if(getServer().getPluginManager().getPlugin("CMI") != null) {
			if(Config.CMI_ENABLE) {
				this.getServer().getPluginManager().registerEvents(new CMIListener(), this);
				MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] CMI ble funnet og koblet til");
			} else { MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] CMI ble funnet men er ikke koblet til. Se config.yml --> enable=true/false"); }
		}

	}
}
