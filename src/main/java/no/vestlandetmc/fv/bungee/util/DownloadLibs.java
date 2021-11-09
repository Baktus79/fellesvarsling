package no.vestlandetmc.fv.bungee.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import no.vestlandetmc.fv.bungee.FVBungee;

public class DownloadLibs {

	private final FVBungee plugin = FVBungee.getInstance();
	private final File outputFileName;

	public DownloadLibs(String outputFileName) {
		this.outputFileName = new File(plugin.getDataFolder() + "/libs/" + outputFileName);
		final File folder = new File(plugin.getDataFolder() + "/libs");

		if(!folder.exists()){
			folder.mkdirs();
		}
	}

	public void url(String url) throws IOException {
		final URL address = new URL(url);

		try(InputStream in = address.openStream();
				ReadableByteChannel rbc = Channels.newChannel(in);
				FileOutputStream fos = new FileOutputStream(this.outputFileName))
		{
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		}
	}

	public boolean exist() {
		return outputFileName.exists();
	}
}
