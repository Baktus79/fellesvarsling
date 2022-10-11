package no.vestlandetmc.fv.velocity;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import no.vestlandetmc.fv.velocity.utils.DownloadLibs;
public class FVVelocity {

	private final ProxyServer server;
	private final Logger logger;
	private final Path dataDirectory;

	@Inject
	public FVVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
		this.server = server;
		this.logger = logger;
		this.dataDirectory = dataDirectory;

	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		try {
			downloadLibs();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		sendServer();
	}

	private void sendServer() {
		new DataStorage(this.server, this.logger, this.dataDirectory);
	}

	private void downloadLibs() throws IOException {
		final HashMap<String, String> libs = new HashMap<>();

		libs.put("mariadb-java-client-2.7.4.jar", "https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/2.7.4/mariadb-java-client-2.7.4.jar");
		libs.put("mysql-connector-java-8.0.27.jar", "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.27/mysql-connector-java-8.0.27.jar");
		libs.put("HikariCP-5.0.0.jar", "https://repo1.maven.org/maven2/com/zaxxer/HikariCP/5.0.0/HikariCP-5.0.0.jar");
		libs.put("snakeyaml-1.29.jar", "https://repo1.maven.org/maven2/org/yaml/snakeyaml/1.29/snakeyaml-1.29.jar");

		for(final String filename : libs.keySet()) {
			final DownloadLibs dl = new DownloadLibs(filename);
			final String url = libs.get(filename);

			if(!dl.exist()) {
				dl.url(url);

				//MessageHandler.sendConsole("&6[" + getDescription().getName() + "] &eBibliotek " + filename + " ble lastet ned...");
			}
		}
	}

}
