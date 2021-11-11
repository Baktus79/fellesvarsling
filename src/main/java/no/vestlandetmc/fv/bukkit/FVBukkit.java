package no.vestlandetmc.fv.bukkit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import no.vestlandetmc.fv.bukkit.commands.FellesVarsling;
import no.vestlandetmc.fv.bukkit.config.Config;
import no.vestlandetmc.fv.bukkit.database.MySQLHandler;
import no.vestlandetmc.fv.bukkit.database.MySqlPool;
import no.vestlandetmc.fv.bukkit.listeners.CMIListener;
import no.vestlandetmc.fv.bukkit.listeners.LitebansAPI;
import no.vestlandetmc.fv.bukkit.listeners.PlayerListener;
import no.vestlandetmc.fv.bukkit.util.DownloadLibs;

public class FVBukkit extends JavaPlugin {

	private static FVBukkit instance;

	public static FVBukkit getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		try { downloadLibs(); } catch (final IOException e) { e.printStackTrace(); }

		this.getCommand("fellesvarsling").setExecutor(new FellesVarsling());
		this.getCommand("fellesvarsling").setTabCompleter(new FellesVarsling());
		Config.initialize();

		MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] Kontakter databasen...");

		try {
			new MySqlPool().initialize();
			MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] Kontakt med database er oppnådd");
			this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
			MySQLHandler.sqlEnabled = true;
		} catch (final SQLException e) {
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

	private void downloadLibs() throws IOException {
		final HashMap<String, String> libs = new HashMap<>();

		libs.put("mariadb-java-client-2.7.4.jar", "https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/2.7.4/mariadb-java-client-2.7.4.jar");
		libs.put("mysql-connector-java-8.0.27.jar", "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.27/mysql-connector-java-8.0.27.jar");
		libs.put("HikariCP-5.0.0.jar", "https://repo1.maven.org/maven2/com/zaxxer/HikariCP/5.0.0/HikariCP-5.0.0.jar");

		for(final String filename : libs.keySet()) {
			final DownloadLibs dl = new DownloadLibs(filename);
			final String url = libs.get(filename);

			if(!dl.exist()) {
				dl.url(url);

				MessageHandler.sendConsole("&6[" + getDescription().getPrefix() + "] &eBibliotek " + filename + " ble lastet ned...");
			}
		}
	}

	@Override
	public void onDisable() {
		try {
			MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] Stenger databasen...");

			if(!MySqlPool.getDataSource().getConnection().isClosed()) {
				MySqlPool.getDataSource().getConnection().close();
			}

			MessageHandler.sendConsole("[" + getDescription().getPrefix() + "] Kontakten til database er stengt...");

		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
}
