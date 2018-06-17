package net.upd4ting.gameapi.nms.v1_8_R1;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R1.EnumClientCommand;
import net.minecraft.server.v1_8_R1.PacketPlayInClientCommand;
import net.upd4ting.gameapi.nms.common.RespawnHandler;

public class Respawn implements RespawnHandler {
	@Override
	public void respawn(Player player) {
        if (player != null && player.isDead() && player.isOnline()) {
            PacketPlayInClientCommand packet = new PacketPlayInClientCommand((EnumClientCommand.PERFORM_RESPAWN));
            ((CraftPlayer)player).getHandle().playerConnection.a(packet);
        }
	}
}
