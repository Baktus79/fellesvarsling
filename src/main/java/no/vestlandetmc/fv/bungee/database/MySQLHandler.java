package no.vestlandetmc.fv.bungee.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import no.vestlandetmc.fv.bungee.MessageHandler;
import no.vestlandetmc.fv.bungee.config.Config;
import no.vestlandetmc.fv.util.MySqlPool;
import no.vestlandetmc.fv.util.NameFetcher;

public class MySQLHandler {

	public static boolean sqlEnabled = false;
	private Connection connection;

	public MySQLHandler() {
		try {
			if(connection == null) {
				connection = MySqlPool.getConnection();
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
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

	/**
	 * Sjekk om en spiller har blitt varslet.
	 *
	 * @param uuid Hvilken spiller som skal sjekkes.
	 * @return True eller false.
	 * @throws SQLException
	 */
	public boolean erVarslet(UUID uuid) throws SQLException {
		final String sql = "SELECT uuid FROM fellesvarsling WHERE uuid=?";
		final PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, uuid.toString());

		final ResultSet set = statement.executeQuery();

		while (set.next()) {
			return true;
		}

		if(connection != null)
			connection.close();

		if(statement != null)
			statement.close();

		return false;

	}

	/**
	 * Sjekk om en spiller har blitt varslet.
	 *
	 * @param player Spilleren som mottar varslingen. Hvis player == null vil konsollen motta meldingen.
	 * @param uuid Hvilken spiller som skal sjekkes for varslinger.
	 * @throws SQLException
	 */
	public void getInfo(ProxiedPlayer player, UUID uuid) throws SQLException {
		final String sql = "SELECT id, type, server, expire, reason FROM fellesvarsling WHERE uuid=? ORDER BY timestamp DESC";
		final PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, uuid.toString());

		final String user = NameFetcher.getName(uuid);
		final ResultSet set = statement.executeQuery();
		final long unixTime = System.currentTimeMillis();
		int i = 1;

		if(player == null) { MessageHandler.sendConsole("&e--- ==== &6Varslinger -" + user + " &e==== ---"); }
		else { MessageHandler.sendMessage(player, "&e--- ==== &6Varslinger -" + user + " &e==== ---"); }

		while (set.next()) {
			final int id = set.getInt("id");
			final String type = set.getString("type");
			final String server = set.getString("server");
			String expire = set.getLong("expire") >= unixTime ? " &8[&aAktiv&8] " : " &8[&cUtgått&8] ";
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

		if(connection != null)
			connection.close();

		if(statement != null)
			statement.close();

	}

	/**
	 * Registrer en spiller i databasen.
	 *
	 * @param uuid Spilleren som registreres.
	 * @param type Definer hva slags registrering dette er.
	 * @param expire Når straffen utgår på Litebans.
	 * @param reason Årsaken til registreringen.
	 * @throws SQLException
	 */
	public void setUser(UUID uuid, String type, long expire, String reason) throws SQLException {
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

		if(connection != null)
			connection.close();

		if(statement != null)
			statement.close();

	}

	/**
	 * Fjern en oppføring i databasen.
	 *
	 * @param id ID på oppføringen.
	 * @return True eller false.
	 * @throws SQLException
	 */
	public boolean deleteUser(int id) throws SQLException {
		boolean complete;
		final String sql = "DELETE FROM fellesvarsling WHERE id=? AND server=?";
		final PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, id);
		statement.setString(2, Config.SERVERNAME);

		if(statement.executeUpdate() == 0) {
			complete = false;
		} else { complete = true; }


		if(connection != null)
			connection.close();

		if(statement != null)
			statement.close();

		return complete;
	}

	/**
	 * Fjern oppføringer som er eldre enn gitt tidsperiode.
	 *
	 * @param days Fjern alle oppføringer som er eldre enn x.
	 * @throws SQLException
	 */
	public void deleteOld(int days) throws SQLException {
		final long daysSeconds = days * 24 * 60 * 60;
		final long unixTime = System.currentTimeMillis() / 1000L - daysSeconds;
		final String sql = "DELETE FROM fellesvarsling WHERE timestamp<?";
		final PreparedStatement statement = connection.prepareStatement(sql);

		statement.setLong(1, unixTime);

		if(connection != null)
			connection.close();

		if(statement != null)
			statement.close();

	}

}
