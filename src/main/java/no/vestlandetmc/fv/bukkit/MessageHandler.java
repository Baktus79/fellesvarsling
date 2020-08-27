package no.vestlandetmc.fv.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageHandler {

	/**
	 * Sender melding til en spesifikk spiller.
	 *
	 * @param player Spilleren som skal motta.
	 * @param messages Meldingen som sendes. For flere linjer, avskill med komma.
	 */
	public static void sendMessage(Player player, String... messages) {
		for(final String message : messages) {
			player.sendMessage(colorize(message));
		}
	}

	/**
	 * Send en melding til alle spillere som er pålogget.
	 * Brukes gjerne når man ønsker å offentliggjøre en beskjed.
	 *
	 * @param messages Meldingen som skal sendes. For flere linjer, avskill med komma.
	 */
	public static void sendAnnounce(String... messages) {
		for(final Player player : Bukkit.getOnlinePlayers()) {
			for(final String message : messages) {
				player.sendMessage(colorize(message));
			}
		}
	}

	/**
	 * Send en melding til konsollen.
	 *
	 * @param messages Meldingen som skal sendes. For flere linjer, avskill med komma.
	 */
	public static void sendConsole(String... messages) {
		for(final String message : messages) {
			FVBukkit.getInstance().getServer().getConsoleSender().sendMessage(colorize(message));
		}
	}

	/**
	 * Oversetter Minecraft fargekoder til chatcolor format.
	 *
	 * @param message Meldingen som inneholder fargekoder.
	 * @return Teksten oversatt fra fargekoder til chatcolor format.
	 */
	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * Sender en klikkbar tekst til alle spillere med en spesifikk permission.
	 * Definer en kommando som skal utføres ved å klikke på teksten.
	 *
	 * @param click Meldingen som skal kunne trykkes på.
	 * @param command Kommandoen som skal utføres. (Inkluderer / foran)
	 */
	public static void clickableAnnounce(String click, String command) {
		final TextComponent message = new TextComponent( colorize(click) );
		message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, command ) );

		for(final Player player : Bukkit.getOnlinePlayers()) {
			if(player.hasPermission("fellesvarsling.varsel"))
				player.spigot().sendMessage( message );
		}
	}

	/**
	 * Sender en klikkbar tekst til alle spillere med en spesifikk permission.
	 * Definer en kommando som skal utføres ved å klikke på teksten.
	 * I tillegg til klikkbar tekst så kan man også legge til hover tekst.
	 *
	 * @param player Spilleren som skal motta meldingen.
	 * @param click Meldingen som skal kunne trykkes på.
	 * @param hover Meldingen som skal vises når man holder musepekeren over teksten.
	 * @param command Kommandoen som skal utføres. (Inkluderer / foran)
	 */
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
