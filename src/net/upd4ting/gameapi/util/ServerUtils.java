package net.upd4ting.gameapi.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Paths;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerList;
import net.minecraft.server.v1_8_R3.PropertyManager;

public class ServerUtils {
    private static void changeServerPropertie(final String key, final String value) {
        final CraftServer server = (CraftServer)Bukkit.getServer();
        final MinecraftServer minecraftServer = server.getServer();
        final PropertyManager manager = minecraftServer.getPropertyManager();
        manager.properties.setProperty(key, value);
        try {
            final String baseDir = Paths.get("").toAbsolutePath().toString() + File.separator;
            final File f = new File(baseDir + "server.properties");
            final OutputStream out = new FileOutputStream(f);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void changeServerName(final String serverName) {
        changeServerPropertie("server-name", serverName);
    }
    
    public static void changeMaxPlayers(final int maxPlayers) {
        if (maxPlayers < 0) {
            return;
        }
        changeServerPropertie("max-players", Integer.toString(maxPlayers));
        final CraftServer server = (CraftServer)Bukkit.getServer();
        final MinecraftServer minecraftServer = server.getServer();
        final PlayerList playerList = minecraftServer.getPlayerList();
        final Field field = Reflector.getField(PlayerList.class, "maxPlayers");
        try {
            field.setAccessible(true);
            field.set(playerList, maxPlayers);
            field.setAccessible(false);
        }
        catch (IllegalArgumentException | IllegalAccessException ex2) {
            ex2.printStackTrace();
        }
    }
}