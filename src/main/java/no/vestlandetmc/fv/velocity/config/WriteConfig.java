package no.vestlandetmc.fv.velocity.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class WriteConfig {

	private final PrintWriter writer;

	public WriteConfig(File file) throws FileNotFoundException {
		this.writer = new PrintWriter(file);
	}

	public void execute() {
		final Map<String, Object> dataMap = new HashMap<>();
		final List<String> blacklist = new ArrayList<>();
		final Yaml yaml = new Yaml();

		dataMap.put("mysql.engine", "mariadb");
		dataMap.put("mysql.host", "localhost");
		dataMap.put("mysql.port", 3306);
		dataMap.put("mysql.user", "root");
		dataMap.put("mysql.password", "minecraft");
		dataMap.put("mysql.database", "Minecraft");
		dataMap.put("mysql.enable-ssl", false);

		dataMap.put("mysql.pool.max-pool-size", 10);
		dataMap.put("mysql.pool.connection-timeout", 25);
		dataMap.put("mysql.pool.max-lifetime", 1800);

		dataMap.put("servername", "Vestlandet");

		dataMap.put("filter.blacklist", true);

		blacklist.add("test");
		dataMap.put("filter.wordfilter", blacklist);

		dataMap.put("tilkoblinger.litebans.enable", true);
		dataMap.put("tilkoblinger.litebans.type.ban", true);
		dataMap.put("tilkoblinger.litebans.type.kick", false);
		dataMap.put("tilkoblinger.litebans.type.mute", false);
		dataMap.put("tilkoblinger.litebans.type.warn", false);

		yaml.dump(dataMap, this.writer);
	}

}
