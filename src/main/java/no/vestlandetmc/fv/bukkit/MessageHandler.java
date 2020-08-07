package no.vestlandetmc.fv.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageHandler {

	public static void sendMessage(Player player, String... messages) {
		for(final String message : messages) {
			player.sendMessage(colorize(message));
		}
	}

	public static void sendAnnounce(String... messages) {
		for(final Player player : Bukkit.getOnlinePlayers()) {
			for(final String message : messages) {
				player.sendMessage(colorize(message));
			}
		}
	}

	public static void sendConsole(String... messages) {
		for(final String message : messages) {
			FVBukkit.getInstance().getServer().getConsoleSender().sendMessage(colorize(message));
		}
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void clickableAnnounce(String click, String command) {
		final TextComponent message = new TextComponent( colorize(click) );
		message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, command ) );

		for(final Player player : Bukkit.getOnlinePlayers()) {
			if(player.hasPermission("fellesvarsling.varsel"))
				player.spigot().sendMessage( message );
		}
	}

	@SuppressWarnings("deprecation")
	public static void clickableMessage(Player player, String click, String hover, String command) {
		final TextComponent message = new TextComponent( colorize(click) );

		if(command != null)
			message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, command ) );

		if(hover != null)
			message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( colorize(hover) ).create() ) );

		player.spigot().sendMessage( message );
	}

}
