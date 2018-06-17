package net.upd4ting.gameapi.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DirectionUtil {

    public enum Direction {
        DOWN, UP, RIGHT, LEFT, ALL
    }

    public static Direction getDirection(Player me, Location to) {
        Vector vMe = me.getEyeLocation().getDirection().setY(0);
        Vector vTeam = to.toVector().subtract(me.getLocation().toVector()).normalize().setY(0);

        Double angle = Math.toDegrees(vMe.angle(vTeam));

        while (angle < 0) // Au cas ou sa return des trucs en néfatifs
            angle += 360;

        if (angle <= 45)
            return Direction.UP;
        else if (angle > 45 && angle <= 135)
            if (vTeam.crossProduct(vMe).getY() > 0)
                return Direction.RIGHT;
            else
                return Direction.LEFT;
        else if (angle > 135)
            return Direction.DOWN;

        return null;
    }
}
