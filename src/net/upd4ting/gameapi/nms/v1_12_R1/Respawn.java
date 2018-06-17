package net.upd4ting.gameapi.nms.v1_12_R1;

import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand.EnumClientCommand;
import net.upd4ting.gameapi.nms.common.RespawnHandler;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Respawn implements RespawnHandler {
	@Override
	public void respawn(Player player) {
        if (player != null && player.isDead() && player.isOnline()) {
            PacketPlayInClientCommand packet = new PacketPlayInClientCommand((EnumClientCommand.PERFORM_RESPAWN));
            ((CraftPlayer)player).getHandle().playerConnection.a(packet);
        }
	}
}
