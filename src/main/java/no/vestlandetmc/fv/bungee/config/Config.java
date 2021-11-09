package no.vestlandetmc.fv.bungee.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import no.vestlandetmc.fv.bungee.FVBungee;

public class Config {

	private final Configuration config;
	private final Plugin plugin = FVBungee.getInstance();

	private Config(String fileName) throws IOException {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();

		final File file = new File(plugin.getDataFolder(), fileName);

		if (!file.exists()) {
			try (InputStream in = plugin.getResourceAsStream(fileName)) {
				Files.copy(in, file.toPath());
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
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

	private void onLoad() {

		SERVERNAME = this.config.getString("servername");
		SQLTYPE = this.config.getString("mysql.engine");
		HOST = this.config.getString("mysql.host");
		USER = this.config.getString("mysql.user");
		PASSWORD = this.config.getString("mysql.password");
		DATABASE = this.config.getString("mysql.database");
		PORT = this.config.getInt("mysql.port");
		WORD_FILTER = this.config.getStringList("filter.wordfilter");
		BLACKLIST = this.config.getBoolean("filter.blacklist");
		ENABLE_SSL = this.config.getBoolean("mysql.enable-ssl");
		MAX_POOL = this.config.getInt("mysql.pool.max-pool-size");
		CON_TIMEOUT = this.config.getInt("mysql.pool.connection-timeout") * 1000;
		CON_LIFETIME = this.config.getInt("mysql.pool.max-lifetime") * 1000;

		//Litebans oppsett
		LITEBANS_BAN = this.config.getBoolean("tilkoblinger.litebans.type.ban");
		LITEBANS_KICK = this.config.getBoolean("tilkoblinger.litebans.type.kick");
		LITEBANS_MUTE = this.config.getBoolean("tilkoblinger.litebans.type.mute");
		LITEBANS_WARN = this.config.getBoolean("tilkoblinger.litebans.type.warn");
		LITEBANS_ENABLE = this.config.getBoolean("tilkoblinger.litebans.enable");

	}

	public static void initialize() throws IOException {
		new Config("config.yml").onLoad();
	}

}
