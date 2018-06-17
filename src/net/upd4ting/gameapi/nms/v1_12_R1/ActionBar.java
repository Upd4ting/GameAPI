package net.upd4ting.gameapi.nms.v1_12_R1;

import com.google.gson.JsonPrimitive;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.upd4ting.gameapi.nms.common.ActionBarHandler;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar implements ActionBarHandler {

    @Override
    public void send(Player p, String str) {
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a(new JsonPrimitive(str).toString());
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, ChatMessageType.GAME_INFO);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

}
