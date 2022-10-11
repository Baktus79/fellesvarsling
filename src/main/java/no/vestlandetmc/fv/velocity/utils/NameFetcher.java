package no.vestlandetmc.fv.velocity.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Helper-class for getting names of players.
 */
public final class NameFetcher {

	private NameFetcher() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the name of the searched player.
	 *
	 * @param uuid The UUID of a player.
	 * @return The name of the given player.
	 */
	public static String getName(UUID uuid) {
		return getName(uuid.toString());
	}

	/**
	 * Returns the name of the searched player.
	 *
	 * @param uuid The UUID of a player (can be trimmed or the normal version).
	 * @return The name of the given player.
	 */
	public static String getName(String uuid) {
		uuid = uuid.replace("-", "");
		final String output = callURL("https://sessionserver.mojang.com/session/" + "minecraft/profile/" + uuid);

		return readData(output);
	}

	private static String readData(String toRead) {
		final String[] parts = toRead.split(":");
		final String[] player = parts[2].split(",");

		return player[0].replaceAll("\"", "");
	}

	private static String callURL(String urlStr) {
		final StringBuilder sb = new StringBuilder();
		URLConnection urlConn;
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
				int cp;
				while ((cp = bufferedReader.read()) != -1) {
					sb.append((char) cp);
				}
				bufferedReader.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}