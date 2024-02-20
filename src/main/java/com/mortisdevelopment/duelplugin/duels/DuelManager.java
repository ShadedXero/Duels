package com.mortisdevelopment.duelplugin.duels;

import com.mortisdevelopment.duelplugin.DuelPlugin;
import com.mortisdevelopment.duelplugin.arenas.ArenaManager;
import com.mortisdevelopment.duelplugin.game.GameManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DuelManager {

    private final GameManager gameManager;
    private final ArenaManager arenaManager;
    private final double reachOne;
    private final double reachTwo;
    private final double reachThree;
    private final Map<Player, DuelInvite> inviteByInviter;
    private final Map<Player, DuelInvite> inviteByInvited;

    public DuelManager(DuelPlugin plugin, GameManager gameManager, ArenaManager arenaManager, double reachOne, double reachTwo, double reachThree) {
        this.gameManager = gameManager;
        this.arenaManager = arenaManager;
        this.reachOne = reachOne;
        this.reachTwo = reachTwo;
        this.reachThree = reachThree;
        this.inviteByInviter = new HashMap<>();
        this.inviteByInvited = new HashMap<>();
        check(plugin);
    }

    public List<DuelInvite> getInvites() {
        return new ArrayList<>(inviteByInviter.values());
    }

    private void check(DuelPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (DuelInvite invite : getInvites()) {
                    if (invite.getTimer() >= 30) {
                        removeRequest(invite.getInviter());
                        invite.getInviter().sendMessage("&cYour duel request expired");
                    }
                    invite.setTimer(invite.getTimer() + 1);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public DuelInvite getInvite(Player invited) {
        return inviteByInvited.get(invited);
    }

    public void addRequest(Player inviter, Player invited) {
        DuelInvite invite = new DuelInvite(inviter, invited);
        inviteByInviter.put(inviter, invite);
        inviteByInvited.put(invited, invite);
    }

    public void removeRequest(Player player) {
        DuelInvite invite = inviteByInviter.getOrDefault(player, inviteByInvited.get(player));
        if (invite == null) {
            return;
        }
        inviteByInviter.remove(invite.getInviter());
        inviteByInvited.remove(invite.getInvited());
    }
}
