package com.mortisdevelopment.duelplugin.game;

import com.mortisdevelopment.duelplugin.arenas.Arena;
import com.mortisdevelopment.duelplugin.kits.KitManager;
import com.mortisdevelopment.duelplugin.utils.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter @Setter
public abstract class Game {

    private final GameManager gameManager;
    private final Arena arena;
    private final Player playerOne;
    private long time;
    private long endTime;
    private String winner;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public Game(GameManager gameManager, Arena arena, Player playerOne) {
        this.gameManager = gameManager;
        this.arena = arena;
        this.playerOne = playerOne;
        arena.setAvailable(false);
    }

    public abstract String getOpponentName(Player player);

    public void reset(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setAbsorptionAmount(0);
        player.damage(0.0001);
        player.setFallDistance(0);
    }

    public void reset() {
        reset(playerOne);
    }

    public void teleport() {
        playerOne.teleport(arena.getSpawnLocation1());
    }

    public void giveKit(KitManager kitManager, Player player) {
        arena.getKit(kitManager).give(player);
    }

    public void giveKit(KitManager kitManager) {
        giveKit(kitManager, playerOne);
    }

    public void showCountdown() {
        showCountdown(playerOne);
    }

    public void resetTitle() {
        resetTitle(playerOne);
    }

    public void showResult() {
        showResult(playerOne);
    }

    public void sendResultMessage() {
        sendResultMessage(playerOne);
    }

    public void showCountdown(Player player) {
        long timeLeft = gameManager.getStartTime() - time;
        player.showTitle(Title.title(ColorUtils.getComponent("&aStarting in"), ColorUtils.getComponent("&a" + timeLeft)));
    }

    public void resetTitle(Player player) {
        player.clearTitle();
    }

    public void showResult(Player player) {
        player.showTitle(Title.title(ColorUtils.getComponent("&bWinner:"), ColorUtils.getComponent("&f" + winner), Title.Times.times(Duration.ZERO, Duration.ofSeconds(7), Duration.ZERO)));
    }

    public void sendResultMessage(Player player) {
        if (player.getName().equals(winner)) {
            player.sendMessage(ColorUtils.getComponent("&bYou won"));
        }else {
            player.sendMessage(ColorUtils.getComponent("&cYou lost"));
        }
    }

    public void teleportLobby(Player player) {
        player.teleport(gameManager.getLobby().getLocation());
    }

    public void sendDataMessage(Player player, GameData data) {
        player.sendMessage(ColorUtils.getComponent("&aYour match data was saved with the id: " + data.getId()));
    }

    public void end(GameManager gameManager, GameData data) {
        arena.setAvailable(true);
        teleportLobby(playerOne);
        sendDataMessage(playerOne, data);
        gameManager.getGameByPlayer().remove(playerOne);
    }

    public boolean isStarted() {
        return startedAt != null;
    }

    public boolean isEnded() {
        return endedAt != null;
    }

    public void setStarted() {
        this.startedAt = LocalDateTime.now();
    }

    public void setEnded() {
        this.endedAt = LocalDateTime.now();
        sendResultMessage();
        showResult();
    }

    public void setWinner(String name) {
        this.winner = name;
        setEnded();
        reset();
    }
}
