package no.vestlandetmc.fv.velocity.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import no.vestlandetmc.fv.velocity.DataStorage;

public class Config {

	private final Path dataPath = DataStorage.getDataPath();
	private Map<String, Object> data;

	private Config(String fileName) throws IOException {
		final Yaml yaml = new Yaml();

		if (!dataPath.toFile().exists())
			dataPath.toFile().mkdir();

		final File file = new File(dataPath.toFile(), fileName);

		if (!file.exists()) {
			file.createNewFile();

			final WriteConfig write = new WriteConfig(file);
			write.execute();
		} else {
			final InputStream inputStream = new FileInputStream(file);
			this.data = yaml.load(inputStream);

		}
	}

	public static List<String>
	WORD_FILTER;

	public static String
	HOST,
	USER,
	PASSWORD,
	DATABASE,
	SERVERNAME,
	SQLTYPE;

	public static int
	PORT,
	MAX_POOL,
	CON_TIMEOUT,
	CON_LIFETIME;

	public static boolean
	BLACKLIST,
	ENABLE_SSL;

	public static boolean
	LITEBANS_BAN,
	LITEBANS_KICK,
	LITEBANS_MUTE,
	LITEBANS_WARN,
	LITEBANS_ENABLE;

	@SuppressWarnings("unchecked")
	private void onLoad() {

		SERVERNAME = (String) data.get("servername");
		SQLTYPE = (String) data.get("mysql.engine");
		HOST = (String) data.get("mysql.host");
		USER = (String) data.get("mysql.user");
		PASSWORD = (String) data.get("mysql.password");
		DATABASE = (String) data.get("mysql.database");
		PORT = (int) data.get("mysql.port");
		WORD_FILTER = (List<String>) data.get("filter.wordfilter");
		BLACKLIST = (boolean) data.get("filter.blacklist");
		ENABLE_SSL = (boolean) data.get("mysql.enable-ssl");
		MAX_POOL = (int) data.get("mysql.pool.max-pool-size");
		CON_TIMEOUT = (int) data.get("mysql.pool.connection-timeout") * 1000;
		CON_LIFETIME = (int) data.get("mysql.pool.max-lifetime") * 1000;

		//Litebans oppsett
		LITEBANS_BAN = (boolean) data.get("tilkoblinger.litebans.type.ban");
		LITEBANS_KICK = (boolean) data.get("tilkoblinger.litebans.type.kick");
		LITEBANS_MUTE = (boolean) data.get("tilkoblinger.litebans.type.mute");
		LITEBANS_WARN = (boolean) data.get("tilkoblinger.litebans.type.warn");
		LITEBANS_ENABLE = (boolean) data.get("tilkoblinger.litebans.enable");

	}

	public static void initialize() throws IOException {
		new Config("config.yml").onLoad();
	}

}
