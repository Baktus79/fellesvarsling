package no.vestlandetmc.fv.velocity.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Helper-class for getting UUIDs of players.
 */
public final class UUIDFetcher {

	private UUIDFetcher() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the UUID of the searched player.
	 *
	 * @param player The player.
	 * @return The UUID of the given player.
	 */
	public static UUID getUUID(ProxiedPlayer player) {
		return getUUID(player.getName());
	}

	/**
	 * Returns the UUID of the searched player.
	 *
	 * @param playername The name of the player.
	 * @return The UUID of the given player.
	 * @throws ParseException
	 */
	public static UUID getUUID(String playername) {
		final String output = callURL("https://api.mojang.com/users/profiles" + "/minecraft/" + playername);
		final String u = readData(output);

		if(u.isBlank()) { return null; }

		String uuid = "";
		for (int i = 0; i <= 31; i++) {
			uuid = uuid + u.charAt(i);
			if (i == 7 || i == 11 || i == 15 || i == 19) {
				uuid = uuid + "-";
			}
		}
		return UUID.fromString(uuid);
	}

	private static String readData(String toRead) {
		String[] uuid = null;

		try {
			final String[] parts = toRead.split(":");
			uuid = parts[2].split("}");
		} catch (final ArrayIndexOutOfBoundsException e) {
			return "";
		}

		return uuid[0].replaceAll("\"", "");
	}

	private static String callURL(String urlStr) {
		final StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			final URL url = new URL(urlStr);
			urlConn = url.openConnection();
			if (urlConn != null) {
				urlConn.setReadTimeout(60 * 1000);
			}
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				final BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}