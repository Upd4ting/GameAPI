package net.upd4ting.gameapi.achook;

import org.bukkit.entity.Player;

interface AbstractHandler {
    void startExempting(Player player);

    void stopExempting(Player player);
}