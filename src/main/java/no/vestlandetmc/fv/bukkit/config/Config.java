package no.vestlandetmc.fv.bukkit.config;

import java.util.List;

public class Config extends ConfigHandler {

	private Config(String fileName) {
		super(fileName);
	}

	public static List<String>
	WORD_FILTER;

	public static String
	HOST,
	USER,
	PASSWORD,
	DATABASE,
	SERVERNAME;

	public static int
	PORT;

	public static boolean
	BLACKLIST,
	ENABLE_SSL;

	public static boolean
	LITEBANS_BAN,
	LITEBANS_KICK,
	LITEBANS_MUTE,
	LITEBANS_WARN,
	LITEBANS_ENABLE,
	CMI_ENABLE,
	CMI_BAN;

	private void onLoad() {

		SERVERNAME = getString("servername");
		HOST = getString("mysql.host");
		USER = getString("mysql.user");
		PASSWORD = getString("mysql.password");
		DATABASE = getString("mysql.database");
		PORT = getInt("mysql.port");
		WORD_FILTER = getStringList("filter.wordfilter");
		BLACKLIST = getBoolean("filter.blacklist");
		ENABLE_SSL = getBoolean("mysql.enable-ssl");

		//Litebans oppsett
		LITEBANS_BAN = getBoolean("tilkoblinger.litebans.type.ban");
		LITEBANS_KICK = getBoolean("tilkoblinger.litebans.type.kick");
		LITEBANS_MUTE = getBoolean("tilkoblinger.litebans.type.mute");
		LITEBANS_WARN = getBoolean("tilkoblinger.litebans.type.warn");
		LITEBANS_ENABLE = getBoolean("tilkoblinger.litebans.enable");

		//CMI oppsett
		CMI_BAN = getBoolean("tilkoblinger.cmi.type.ban");
		CMI_ENABLE = getBoolean("tilkoblinger.cmi.enable");

	}

	public static void initialize() {
		new Config("config.yml").onLoad();
	}

}
