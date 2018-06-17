package net.upd4ting.gameapi.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.upd4ting.gameapi.Game;

/**
 * This custom listener will be called
 * when a player take damage
 * @author Upd4ting
 *
 */
public class GameDamageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Player player;
    private final Game game;
    private boolean cancelled;

    public GameDamageEvent(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
