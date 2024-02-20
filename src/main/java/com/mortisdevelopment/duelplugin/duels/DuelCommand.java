package com.mortisdevelopment.duelplugin.duels;

import com.mortisdevelopment.duelplugin.arenas.Arena;
import com.mortisdevelopment.duelplugin.game.Game;
import com.mortisdevelopment.duelplugin.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DuelCommand extends Command {

    private final DuelManager duelManager;

    public DuelCommand(DuelManager duelManager) {
        super("duel");
        this.duelManager = duelManager;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtils.getComponent("&cThis command can only be executed by a player"));
            return false;
        }
        if (args.length == 0) {
            Game game = duelManager.getGameManager().getGameByPlayer().get(player);
            if (game != null) {
                sender.sendMessage(ColorUtils.getComponent("&cCannot execute this command while in game"));
                return false;
            }
            new DuelMenu(duelManager).open(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("accept")) {
            DuelInvite invite = duelManager.getInvite(player);
            if (invite == null) {
                player.sendMessage(ColorUtils.getComponent("&cCould not find any invites"));
                return false;
            }
            Game game = duelManager.getGameManager().getGameByPlayer().get(invite.getInviter());
            if (game != null) {
                sender.sendMessage(ColorUtils.getComponent("&cCannot execute this command while in game"));
                return false;
            }
            Arena arena = duelManager.getArenaManager().getRandomAvailableArena();
            if (arena == null) {
                sender.sendMessage(ColorUtils.getComponent("&cThere are no available arenas right now"));
                return false;
            }
            duelManager.getGameManager().startGame(arena, invite.getInviter(), invite.getInvited());
            duelManager.removeRequest(invite.getInviter());
            return true;
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("accept");
        }
        return super.tabComplete(sender, alias, args);
    }
}
