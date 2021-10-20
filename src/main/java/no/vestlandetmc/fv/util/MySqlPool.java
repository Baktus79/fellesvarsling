package no.vestlandetmc.fv.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import no.vestlandetmc.fv.bukkit.config.Config;

public class MySqlPool {

	private static HikariConfig cfg = new HikariConfig();
	private static HikariDataSource ds;

	public MySqlPool() {}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	public void initialize() {
		cfg.setMaximumPoolSize(Config.MAX_POOL);
		cfg.setConnectionTimeout(Config.CON_TIMEOUT);
		cfg.setMaxLifetime(Config.CON_LIFETIME);
		cfg.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
		cfg.addDataSourceProperty("serverName", Config.HOST);
		cfg.addDataSourceProperty("port", Config.PORT);
		cfg.addDataSourceProperty("databaseName", Config.DATABASE);
		cfg.addDataSourceProperty("user", Config.USER);
		cfg.addDataSourceProperty("password", Config.PASSWORD);
		cfg.addDataSourceProperty("requireSSL", Config.ENABLE_SSL);

		ds = new HikariDataSource(cfg);

		final String sql = "CREATE TABLE IF NOT EXISTS fellesvarsling("
				+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
				+ "uuid TEXT,"
				+ "type TEXT,"
				+ "server TEXT,"
				+ "timestamp BIGINT,"
				+ "expire BIGINT,"
				+ "reason TEXT"
				+ ")";

		try {
			Connection connection;
			connection = getConnection();
			connection.createStatement().execute(sql);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
}
