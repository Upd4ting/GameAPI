package net.upd4ting.gameapi.listener.listeners;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.customevents.GameDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;

        Player p = (Player) e.getEntity();
        Game game = GameManager.getPlayerGame(p);

        if (game == null)
            return;

        GameDamageEvent ec = new GameDamageEvent(p, game);
        Bukkit.getPluginManager().callEvent(ec);

        if (ec.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        double health = p.getHealth();
        double damage = e.getDamage();

        if (health - damage <= 0) {
            makeDie(game, p, null, e);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;

        Player damaged = (Player) e.getEntity();
        Game game = GameManager.getPlayerGame(damaged);

        if (game == null)
            return;

        Player damager = e.getDamager() instanceof Player ? (Player) e.getDamager() : e.getDamager() instanceof Projectile ? ((Projectile)e.getDamager()).getShooter() instanceof Player ? (Player)
                ((Projectile) e.getDamager()).getShooter() : null : null;

        if (!game.isFriendlyFire() && damager != null && game.getTeam(damaged).getPlayers().contains(damager)) {
            e.setCancelled(true);
            return;
        }

        double damage = e.getDamage();
        double health = damaged.getHealth();

        if (health - damage <= 0) {
            makeDie(game, damaged, damager, e);
        }
    }

    private void makeDie(Game game, Player damaged, Player damager, Cancellable e) {
        boolean success = game.die(damaged, damager);
        e.setCancelled(true);

        // We need to do that only if the player has lost depending on triggers
        if (success) {
            // NOTE to myself: To make the system compatible with custom death message plugin
            // They need the listener to make a custom message...
            PlayerDeathEvent event = new PlayerDeathEvent(damaged, new ArrayList<>(), 0, damaged.getName() + " is dead.");
            Bukkit.getPluginManager().callEvent(event);

            // We need to display the message (message that will be set by custom death message plugins)
            for (Player pl : game.getInsides())
                pl.sendMessage(event.getDeathMessage());
        }
    }
}
