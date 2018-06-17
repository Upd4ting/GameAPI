package net.upd4ting.gameapi.nms.v1_9_R1;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_9_R1.PlayerConnection;
import net.upd4ting.gameapi.nms.common.TitleHandler;

public class TitleUtils implements TitleHandler {

    /**
	 * Envois le title
	 * @param player le joueur
	 * @param title Le titre
	 * @param subTitle Sous titre
	 * @param fadeIn Temps d'apparition
	 * @param fadeOut Temps de disparition
	 * @param time durée de l'affichage
	 */
	public void sendTitle(Player player, String title, String subTitle, int fadeIn, int fadeOut, int time) {
		// Les simples quotes le jason aime po :o
		title = title.replace("'", "\\'");
		subTitle = subTitle.replace("'", "\\'");
		CraftPlayer craftplayer = (CraftPlayer) player;
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		PacketPlayOutTitle timesPacket = new PacketPlayOutTitle(EnumTitleAction.TIMES, CraftChatMessage.fromString(title)[0], fadeIn, time, fadeOut);
		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, CraftChatMessage.fromString(title)[0]);
		PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, CraftChatMessage.fromString(subTitle)[0]);
		connection.sendPacket(timesPacket);
		connection.sendPacket(titlePacket);
		connection.sendPacket(subtitlePacket);
	}
	
	/**
	 * Titre et sous titre avec les valeurs par défaut (modifiables)
	 * @param player le joueur
	 * @param title le titre
	 * @param subTitle le sous titre
	 */
	public void sendTitle(Player player, String title, String subTitle) {
        int defaultTimeStay = 60;
        int defaultFadeOut = 20;
        int defaultFadeIn = 20;
        sendTitle(player, title, subTitle, defaultFadeIn, defaultFadeOut, defaultTimeStay);
	}
	
	/**
	 * Envoyer à plusieurs joueurs
	 * @param players liste de joueurs
	 * @param title titre
	 * @param subTitle sous titre
	 */
	public void sendTitleToPlayers(Player[] players, String title, String subTitle) {
		for (Player player : players) {
			sendTitle(player, title, subTitle);
		}
	}

	/**
	 * Envoyer à plusieurs joueurs (configurable)
	 * @param players joueur
	 * @param title titre
	 * @param subTitle sous titre
	 * @param fadeIn temps debut
	 * @param fadeOut temps fin
	 * @param time temps
	 */
	public void sendTitleToPlayers(Player[] players, String title, String subTitle, int fadeIn, int fadeOut, int time) {
		for (Player player : players) {
			sendTitle(player, title, subTitle, fadeIn, fadeOut, time);
		}
	}
	public void sendTitleToPlayers(List<Player> players, String title, String subTitle, int fadeIn, int fadeOut, int time) {
		for (Player player : players) {
			sendTitle(player, title, subTitle, fadeIn, fadeOut, time);
		}
	}

	/**
	 * Envoyer un title à tous les joueurs (configurable)
	 * @param title titre
	 * @param subTitle sous titre
	 * @param fadeIn temps debut
	 * @param fadeOut temps fin
	 * @param time temps
	 */
	public void sendTitleToAllPlayers(String title, String subTitle, int fadeIn, int fadeOut, int time) {
		sendTitleToPlayers(Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]), title, subTitle, fadeIn, fadeOut, time);
	}

	/**
	 * Envoyer un title à tous les joueurs
	 * @param title titre
	 * @param subTitle sous titre
	 */
	public void sendTitleToAllPlayers(String title, String subTitle) {
		sendTitleToPlayers(Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]), title, subTitle);
	}

	/**
	 * Retire le title
	 * @param player le joueur
	 */
	public void clearTitle(Player player) {
		sendTitle(player, "", "");
	}

}
