package net.upd4ting.gameapi.nms.v1_7_R4;

import java.lang.reflect.Field;
import java.util.Collection;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;
import net.upd4ting.gameapi.nms.common.NametagHandler;

public class NametagEdit extends NametagHandler {

    private PacketPlayOutScoreboardTeam packet;

    public void init(String teamName, String displayName, NameTagColor color) {
        packet = new PacketPlayOutScoreboardTeam();
        setField(packet, "a",teamName);
        setField(packet, "b",displayName);
        setField(packet, "c","§"+color.getChar());
        setField(packet, "d","§r");
        setField(packet, "f",0);
        setField(packet, "g",1);
        clearPlayer();
    }

    public void sendToPlayer(Player pl){
        try { ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(packet);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void addPlayer(Player pl) {
    	try {
            Field f = packet.getClass().getDeclaredField("e");
            f.setAccessible(true);
            ((Collection) f.get(packet)).add(pl.getName());
    	} catch (NoSuchFieldException | IllegalAccessException e) { e.printStackTrace(); }
    }
    
    @SuppressWarnings("rawtypes")
	private void clearPlayer() {
    	try {
            Field f = packet.getClass().getDeclaredField("e");
            f.setAccessible(true);
            ((Collection) f.get(packet)).clear();
    	} catch (NoSuchFieldException | IllegalAccessException e) { e.printStackTrace(); }
    }
}