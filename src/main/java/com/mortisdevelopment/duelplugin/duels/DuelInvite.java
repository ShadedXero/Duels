package com.mortisdevelopment.duelplugin.duels;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter @Setter
public class DuelInvite {

    private final Player inviter;
    private final Player invited;
    private long timer;

    public DuelInvite(Player inviter, Player invited) {
        this.inviter = inviter;
        this.invited = invited;
    }
}
