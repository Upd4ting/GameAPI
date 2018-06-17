package net.upd4ting.gameapi.nms.common;

import java.util.List;

import org.bukkit.entity.Player;

public interface TitleHandler {
	void sendTitle(Player player, String title, String subTitle, int fadeIn, int fadeOut, int time);
	void sendTitle(Player player, String title, String subTitle);
	void sendTitleToPlayers(Player[] players, String title, String subTitle);
	void sendTitleToPlayers(Player[] players, String title, String subTitle, int fadeIn, int fadeOut, int time);
	void sendTitleToPlayers(List<Player> players, String title, String subTitle, int fadeIn, int fadeOut, int time);
	void sendTitleToAllPlayers(String title, String subTitle, int fadeIn, int fadeOut, int time);
	void sendTitleToAllPlayers(String title, String subTitle);
	void clearTitle(Player player);
}
