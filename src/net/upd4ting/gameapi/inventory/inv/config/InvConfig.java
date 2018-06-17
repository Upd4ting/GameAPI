package net.upd4ting.gameapi.inventory.inv.config;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.inventory.Inventory;
import net.upd4ting.gameapi.util.Reflector;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory that all InvConfig type
 * must extends of.
 * Represent an inventory that will modify a field value of the
 * game object.
 */
public abstract class InvConfig extends Inventory {

    protected final Game game;
    protected final String field;

    public InvConfig(Game game, String field, String title, Integer size, Player player, Boolean autoRefresh) {
        super(title, size, player, autoRefresh);
        this.game = game;
        this.field = field;
    }

    public void setObject(Object o) {
        try {
            getField().set(game, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getObject() {
        try {
            return getField().get(game);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Field getField() {
        Class<?> clazz = game.getClass();
        Field f = Reflector.getFieldFromAll(clazz, field);

        Game.Changeable red = f.getAnnotation(Game.Changeable.class);

        if (!red.redirect().equals("")) {
            f = Reflector.getFieldFromAll(clazz, red.redirect());
        }

        f.setAccessible(true);

        return f;
    }
}
