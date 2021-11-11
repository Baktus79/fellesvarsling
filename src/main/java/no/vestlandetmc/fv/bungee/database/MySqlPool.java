package no.vestlandetmc.fv.bungee.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import no.vestlandetmc.fv.bungee.config.Config;

public class MySqlPool {

	private static HikariConfig cfg = new HikariConfig();
	private static HikariDataSource ds;

	public MySqlPool() { }

	public static HikariDataSource getDataSource() throws SQLException {
		return ds;
	}

	public void initialize() throws SQLException {
		if(Config.SQLTYPE.equalsIgnoreCase("mysql")) {
			cfg.addDataSourceProperty("cachePrepStmts", "true");
			cfg.addDataSourceProperty("prepStmtCacheSize", "250");
			cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			cfg.addDataSourceProperty("useServerPrepStmts", "true");
			cfg.addDataSourceProperty("useLocalSessionState", "true");
			cfg.addDataSourceProperty("rewriteBatchedStatements", "true");
			cfg.addDataSourceProperty("cacheResultSetMetadata", "true");
			cfg.addDataSourceProperty("cacheServerConfiguration", "true");
			cfg.addDataSourceProperty("elideSetAutoCommits", "true");
			cfg.addDataSourceProperty("maintainTimeStats", "false");
			cfg.addDataSourceProperty("requireSSL", Config.ENABLE_SSL);
			cfg.setJdbcUrl("jdbc:mysql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DATABASE);
		}

		else if(Config.SQLTYPE.equalsIgnoreCase("mariadb")) {
			cfg.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
			cfg.addDataSourceProperty("serverName", Config.HOST);
			cfg.addDataSourceProperty("port", Config.PORT);
			cfg.addDataSourceProperty("databaseName", Config.DATABASE);
		}

		else { return; }

		cfg.setMaximumPoolSize(Config.MAX_POOL);
		cfg.setConnectionTimeout(Config.CON_TIMEOUT);
		cfg.setMaxLifetime(Config.CON_LIFETIME);
		cfg.addDataSourceProperty("user", Config.USER);
		cfg.addDataSourceProperty("password", Config.PASSWORD);

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

		Connection con;
		con = getDataSource().getConnection();
		con.createStatement().execute(sql);

		if(con != null)
			con.close();

	}
}
