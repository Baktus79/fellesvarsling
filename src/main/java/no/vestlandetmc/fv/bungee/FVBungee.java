package no.vestlandetmc.fv.bungee;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import net.md_5.bungee.api.plugin.Plugin;
import no.vestlandetmc.fv.bungee.commands.FellesVarslingBungee;
import no.vestlandetmc.fv.bungee.config.Config;
import no.vestlandetmc.fv.bungee.database.MySQLHandler;
import no.vestlandetmc.fv.bungee.database.MySqlPool;
import no.vestlandetmc.fv.bungee.listeners.LitebansAPI;
import no.vestlandetmc.fv.bungee.listeners.PlayerListener;
import no.vestlandetmc.fv.bungee.util.DownloadLibs;

public class FVBungee extends Plugin {

	private static FVBungee instance;

	public static FVBungee getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		try { downloadLibs(); } catch (final IOException e) { e.printStackTrace(); }

		try { Config.initialize(); } catch (final IOException e) { e.printStackTrace(); }

		getProxy().getPluginManager().registerCommand(this, new FellesVarslingBungee());
		this.getProxy().getPluginManager().registerListener(this, new PlayerListener());

		getLogger().info("Kontakter databasen...");

		try {
			new MySqlPool().initialize();
			getLogger().info("Kontakt med database er oppnådd");
			MySQLHandler.sqlEnabled = true;
		} catch (final SQLException e) {
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

				MessageHandler.sendConsole("&6[" + getDescription().getName() + "] &eBibliotek " + filename + " ble lastet ned...");
			}
		}
	}

	@Override
	public void onDisable() {
		try {
			MessageHandler.sendConsole("[" + getDescription().getName() + "] Stenger databasen...");

			if(!MySqlPool.getDataSource().getConnection().isClosed()) {
				MySqlPool.getDataSource().getConnection().close();
			}

			MessageHandler.sendConsole("[" + getDescription().getName() + "] Kontakten til database er stengt...");

		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
}
