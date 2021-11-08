package no.vestlandetmc.fv.bungee.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import no.vestlandetmc.fv.bungee.config.Config;

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
		cfg.addDataSourceProperty("user", Config.USER);
		cfg.addDataSourceProperty("password", Config.PASSWORD);
		cfg.addDataSourceProperty("requireSSL", Config.ENABLE_SSL);
		cfg.addDataSourceProperty("cachePrepStmts", "true");
		cfg.addDataSourceProperty("prepStmtCacheSize", "250");
		cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		cfg.setJdbcUrl("jdbc:mysql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DATABASE);

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
