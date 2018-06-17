package net.upd4ting.gameapi.nms.v1_10_R1;

import com.google.gson.JsonPrimitive;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.upd4ting.gameapi.nms.common.ActionBarHandler;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar implements ActionBarHandler {

    @Override
    public void send(Player p, String str) {
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a(new JsonPrimitive(str).toString());
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

}
