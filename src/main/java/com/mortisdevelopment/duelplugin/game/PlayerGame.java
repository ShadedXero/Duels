package com.mortisdevelopment.duelplugin.game;

import com.mortisdevelopment.duelplugin.arenas.Arena;
import com.mortisdevelopment.duelplugin.kits.KitManager;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PlayerGame extends Game {

    private final Player playerTwo;

    public PlayerGame(GameManager gameManager, KitManager kitManager, Arena arena, Player playerOne, Player playerTwo) {
        super(gameManager, arena, playerOne);
        this.playerTwo = playerTwo;
        reset();
        teleport();
        giveKit(kitManager);
    }

    @Override
    public void reset() {
        super.reset();
        reset(playerTwo);
    }

    @Override
    public String getOpponentName(Player player) {
        if (player.equals(getPlayerOne())) {
            return playerTwo.getName();
        }else if (player.equals(playerTwo)) {
            return getPlayerOne().getName();
        }
        return null;
    }

    @Override
    public void teleport() {
        super.teleport();
        playerTwo.teleport(getArena().getSpawnLocation2());
    }

    @Override
    public void giveKit(KitManager kitManager) {
        super.giveKit(kitManager);
        giveKit(kitManager, playerTwo);
    }

    @Override
    public void resetTitle() {
        super.resetTitle();
        resetTitle(playerTwo);
    }

    @Override
    public void showCountdown() {
        super.showCountdown();
        showCountdown(playerTwo);
    }

    @Override
    public void showResult() {
        super.showResult();
        showResult(playerTwo);
    }

    @Override
    public void sendResultMessage() {
        super.sendResultMessage();
        sendResultMessage(playerTwo);
    }

    @Override
    public void end(GameManager gameManager, GameData data) {
        super.end(gameManager, data);
        teleportLobby(playerTwo);
        sendDataMessage(playerTwo, data);
        gameManager.getGameByPlayer().remove(playerTwo);
    }
}
