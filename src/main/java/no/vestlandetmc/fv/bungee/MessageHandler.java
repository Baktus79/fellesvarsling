package no.vestlandetmc.fv.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageHandler {

	public static void sendMessage(ProxiedPlayer player, String... messages) {
		for(final String message : messages) {
			final TextComponent text = new TextComponent(colorize(message));
			player.sendMessage(text);
		}
	}

	public static void sendAnnounce(String... messages) {
		for(final ProxiedPlayer player : FVBungee.getInstance().getProxy().getPlayers()) {
			for(final String message : messages) {
				final TextComponent text = new TextComponent(colorize(message));
				player.sendMessage(text);
			}
		}
	}

	public static void sendConsole(String... messages) {
		for(final String message : messages) {
			final TextComponent text = new TextComponent(colorize(message));
			FVBungee.getInstance().getProxy().getConsole().sendMessage(text);
		}
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void clickableAnnounce(String click, String command) {
		final TextComponent message = new TextComponent( colorize(click) );
		message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, command ) );

		for(final ProxiedPlayer player : FVBungee.getInstance().getProxy().getPlayers()) {
			if(player.hasPermission(Permissions.VARSEL))
				player.sendMessage(message);
		}
	}

	@SuppressWarnings("deprecation")
	public static void clickableMessage(ProxiedPlayer player, String click, String hover, String command) {
		final TextComponent message = new TextComponent( colorize(click) );

		if(command != null)
			message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, command ) );

		if(hover != null)
			message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( colorize(hover) ).create() ) );

		player.sendMessage(message);
	}

}
