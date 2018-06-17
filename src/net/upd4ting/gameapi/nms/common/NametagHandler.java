package net.upd4ting.gameapi.nms.common;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;

public abstract class NametagHandler {

    public enum NameTagColor {

        BLACK("BLACK", '0', 0),
        DARK_BLUE("DARK_BLUE", '1', 1),
        DARK_GREEN("DARK_GREEN", '2', 2),
        DARK_AQUA("DARK_AQUA", '3', 3),
        DARK_RED("DARK_RED", '4', 4),
        DARK_PURPLE("DARK_PURPLE", '5', 5),
        GOLD("GOLD", '6', 6),
        GRAY("GRAY", '7', 7),
        DARK_GRAY("DARK_GRAY", '8', 8),
        BLUE("BLUE", '9', 9),
        GREEN("GREEN", 'a', 10),
        AQUA("AQUA", 'b', 11),
        RED("RED", 'c', 12),
        LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13),
        YELLOW("YELLOW", 'e', 14),
        WHITE("WHITE", 'f', 15),
        RESET("RESET", 'r', -1);

        private final String name;
        private final Character c;
        private final Integer id;

        NameTagColor(String name, Character c, Integer id){
            this.name = name;
            this.c = c;
            this.id = id;
        }

        public String getName() { return name; }
        public char getChar() { return c; }
        public int getId() { return id; }
    }
    
    public abstract void init(String displayName, String teamName, NameTagColor color);
    public abstract void addPlayer(Player pl);
    public abstract void sendToPlayer(Player pl);

    public void updateAll(Game game) {
        for (Player pl : game.getInsides())
            sendToPlayer(pl);
    }
    
    public void setField(Object o, String field, Object value) {
        try {
            Field f = o.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(o, value);
            f.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}