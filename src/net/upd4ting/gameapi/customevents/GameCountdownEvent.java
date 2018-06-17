package net.upd4ting.gameapi.customevents;

import net.upd4ting.gameapi.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameCountdownEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Game game;
    private int sec;

    public GameCountdownEvent(Game game, int sec) {
        this.game = game;
        this.sec = sec;
    }

    public Game getGame() {
        return game;
    }

    public int getSec() {
        return sec;
    }
}
