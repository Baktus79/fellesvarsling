package no.vestlandetmc.fv.velocity;

import java.nio.file.Path;

import org.slf4j.Logger;

import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

public class DataStorage {

	private static ProxyServer server;
	private static Logger logger;
	private static Path dataDirectory;

	public DataStorage(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
		DataStorage.server = server;
		DataStorage.logger = logger;
		DataStorage.dataDirectory = dataDirectory;
	}

	public static ProxyServer getProxy() {
		return server;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static Path getDataPath() {
		return dataDirectory;
	}

}
