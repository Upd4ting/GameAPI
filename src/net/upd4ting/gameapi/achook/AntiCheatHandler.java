package net.upd4ting.gameapi.achook;

import net.dynamicdev.anticheat.api.AntiCheatAPI;
import org.bukkit.entity.Player;

public class AntiCheatHandler implements AbstractHandler {

    static {
        AntiCheatAPI.getManager();
    }

    @Override
    public void startExempting(Player player) {
        if (!AntiCheatAPI.isExempt(player, net.dynamicdev.anticheat.check.CheckType.FLY)) {
            AntiCheatAPI.exemptPlayer(player, net.dynamicdev.anticheat.check.CheckType.FLY);
        }

    }

    @Override
    public void stopExempting(Player player) {
        if (AntiCheatAPI.isExempt(player, net.dynamicdev.anticheat.check.CheckType.FLY)) {
            AntiCheatAPI.unexemptPlayer(player, net.dynamicdev.anticheat.check.CheckType.FLY);
        }
    }
}
