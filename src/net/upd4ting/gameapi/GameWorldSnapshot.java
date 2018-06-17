package net.upd4ting.gameapi;

import net.upd4ting.gameapi.util.Util;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;

public class GameWorldSnapshot implements GameSnapshot {
    @Override
    public void restore(Game game) {
        restore(game.getWorld().getName(), game.getName());
    }

    @Override
    public void restore(String world, String name) {
        File worldFolder = new File(Bukkit.getWorldContainer(), world);
        File save = new File(GameAPI.getInstance().getDataFolder(), world + "-save");

        if (!save.exists())
            return;

        Bukkit.unloadWorld(world, false);

        try {
            FileUtils.deleteDirectory(worldFolder);
            FileUtils.copyDirectory(save, worldFolder);
            FileUtils.deleteDirectory(save);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Load world again if not default world
        if (!world.equals("world"))
            new WorldCreator(world).createWorld();

        Util.log(ChatColor.GOLD + "World of " + name + " restored!");
    }

    @Override
    public void save(Game game) {
        World world = game.getWorld();

        File worldFolder = new File(Bukkit.getWorldContainer(), world.getName());

        File save = new File(GameAPI.getInstance().getDataFolder(), world.getName() + "-save");

        try {
            FileUtils.deleteDirectory(save);
            FileUtils.copyDirectory(worldFolder, save);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Util.log(ChatColor.GOLD + "World of " + game.getName() + " saved!");
    }
}
