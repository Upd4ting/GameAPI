package net.upd4ting.gameapi.util;

import org.bukkit.ChatColor;

import java.util.Random;

public class CC {
    public static final String aqua     = "§b";
    public static String black    = "§0";
    public static String blue     = "§9";
    public static String d_aqua   = "§3";
    public static String d_blue   = "§1";
    public static String d_gray   = "§8";
    public static String d_green  = "§2";
    public static String d_purple = "§5";
    public static String d_red    = "§4";
    public static final String gold     = "§6";
    public static final String gray     = "§7";
    public static String green    = "§a";
    public static String l_purple = "§d";
    public static String red      = "§c";
    public static String white    = "§f";
    public static String yellow   = "§e";
    public static String bold      = "§l";
    public static String italic    = "§o";
    public static String magic     = "§k";
    public static String reset     = "§r";
    public static String underline = "§n";
    public static String strike    = "§m";
    public static String arrow = "\u27bd";
    /**
     * donner la couleur arc en ciel Ã  un string
     *
     * @param string le texte
     * @return le texte colore
     */
    public static String rainbowlize(String string) {
        int lastColor = 0;
        int currColor;
        StringBuilder newString = new StringBuilder();
        String colors = "123456789abcde";
        for (int i = 0; i < string.length(); i++) {
            do {
                currColor = new Random().nextInt(colors.length() - 1) + 1;
            }
            while (currColor == lastColor);
            newString.append(ChatColor.RESET.toString()).append(ChatColor.getByChar(colors.charAt(currColor))).append(string.charAt(i));
        }
        return newString.toString();
    }
}