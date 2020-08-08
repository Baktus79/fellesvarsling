package no.vestlandetmc.fv.bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import no.vestlandetmc.fv.bukkit.config.Config;

public class MySQLHandler {

	private Connection connection;

	private Connection getNewConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			final String enableSSL = !Config.ENABLE_SSL ? "&useSSL=false" : "";
			final String url = "jdbc:mysql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DATABASE + "?autoReconnect=true" + enableSSL;
			final Connection connection = DriverManager.getConnection(url, Config.USER, Config.PASSWORD);

			return connection;

		} catch (ClassNotFoundException | SQLException e) {
			return null;
		}
	}

	public MySQLHandler() {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					if (connection != null && !connection.isClosed()) {
						connection.createStatement().execute("SELECT 1");
					}
				} catch (final SQLException e) {
					connection = getNewConnection();
				}
			}
		}.runTaskAsynchronously(FVBukkit.getInstance());
	}

	public void close() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	public boolean execute(String sql) throws SQLException {
		final boolean success = connection.createStatement().execute(sql);
		return success;
	}

	public boolean checkConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = getNewConnection();

			if (connection == null || connection.isClosed()) {
				return false;
			}

			execute("CREATE TABLE IF NOT EXISTS fellesvarsling("
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
					+ "uuid TEXT,"
					+ "type TEXT,"
					+ "server TEXT,"
					+ "timestamp BIGINT,"
					+ "expire BIGINT,"
					+ "reason TEXT"
					+ ")");
		}

		return true;
	}

	public boolean initialize() {
		try {
			return checkConnection();
		} catch (final SQLException e) {
			return false;
		}
	}

	public boolean erVarslet(UUID uuid) throws SQLException {
		try {
			checkConnection();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		final String sql = "SELECT uuid FROM fellesvarsling WHERE uuid=?";
		final PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, uuid.toString());

		final ResultSet set = statement.executeQuery();

		while (set.next()) {
			return true;
		}

		set.close();

		return false;

	}

	public void getInfo(Player player, UUID uuid) throws SQLException {
		try {
			checkConnection();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		final String sql = "SELECT id, type, server, expire, reason FROM fellesvarsling WHERE uuid=? ORDER BY timestamp DESC";
		final PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, uuid.toString());

		final String user = UUIDFetcher.getName(uuid);
		final ResultSet set = statement.executeQuery();
		final long unixTime = System.currentTimeMillis() / 1000L;
		int i = 1;

		if(player == null) { MessageHandler.sendConsole("&e--- ==== &6Varslinger - " + user + " &e==== ---"); }
		else { MessageHandler.sendMessage(player, "&e--- ==== &6Varslinger - " + user + " &e==== ---"); }

		while (set.next()) {
			final int id = set.getInt("id");
			final String type = set.getString("type");
			final String server = set.getString("server");
			String expire = set.getLong("expire") <= unixTime ? " &8[&aAktiv&8] " : " &8[&cUtgått&8] ";
			final String reason = set.getString("reason");

			String typeText = null;

			if(set.getLong("expire") == -1) { expire = " &8[&aAktiv&8] "; }
			else if(type.equals("manuell")) { expire = " "; }

			if(type.equals("ban")) { typeText = "&eHar blitt utestengt" + expire + "&efra"; }
			else if(type.equals("mute")) { typeText = "&eHar fått mute" + expire + "&epå"; }
			else if(type.equals("warning")) { typeText = "&eHar fått advarsel" + expire + "&epå"; }
			else if(type.equals("kick")) { typeText = "&eHar fått kick på"; }
			else if(type.equals("manuell")) { typeText = "&eBle lagt til av"; }

			final String messageConsole = "&eID: &6" + id + " " + typeText + " &6" + server + "&e: " + reason;
			final String messagePlayer = "&eID: &6" + id + " " + typeText + " &6" + server + ".";

			if(player == null) { MessageHandler.sendConsole(messageConsole); }
			else { MessageHandler.clickableMessage(player, messagePlayer, reason, null); }

			i++;

			if(i == 10) { break; }
		}

		set.close();

	}

	public void setUser(UUID uuid, String type, long expire, String reason) throws SQLException {
		try {
			checkConnection();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		final long timestamp = System.currentTimeMillis() / 1000L;
		final String sql = "INSERT INTO fellesvarsling (uuid, type, server, timestamp, expire, reason) VALUES (?, ?, ?, ?, ?, ?)";
		final PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, uuid.toString());
		statement.setString(2, type);
		statement.setString(3, Config.SERVERNAME);
		statement.setLong(4, timestamp);
		statement.setLong(5, expire);
		statement.setString(6, reason);

		statement.executeUpdate();
		statement.close();

	}

	public boolean deleteUser(int id) throws SQLException {
		try {
			checkConnection();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		boolean complete;
		final String sql = "DELETE FROM fellesvarsling WHERE id=? AND server=?";
		final PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, id);
		statement.setString(2, Config.SERVERNAME);

		if(statement.executeUpdate() == 0) {
			complete = false;
		} else { complete = true; }


		statement.close();

		return complete;
	}

	public void deleteOld(int days) throws SQLException {
		try {
			checkConnection();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		final long daysSeconds = (days * 24) * 60 * 60;
		final long unixTime = (System.currentTimeMillis() / 1000L) - daysSeconds;
		final String sql = "DELETE FROM fellesvarsling WHERE timestamp<?";
		final PreparedStatement statement = connection.prepareStatement(sql);

		statement.setLong(1, unixTime);
		statement.close();

	}

}
