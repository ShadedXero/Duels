package com.mortisdevelopment.duelplugin.game;

import com.mortisdevelopment.duelplugin.utils.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

import java.time.LocalDateTime;
import java.util.Random;

@Getter @Setter
public class GameData {

    private final Random random = new Random();
    private final String idCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final String id;
    private final String playerOne;
    private final String playerTwo;
    private final String startedAt;
    private final String endedAt;
    private final String winner;

    public GameData(PlayerGame game) {
        this.id = createId();
        this.playerOne = game.getPlayerOne().getName();
        this.playerTwo = game.getPlayerTwo().getName();
        this.startedAt = createTime(game.getStartedAt());
        this.endedAt = createTime(game.getEndedAt());
        this.winner = game.getWinner();
    }

    public GameData(BotGame game) {
        this.id = createId();
        this.playerOne = game.getPlayerOne().getName();
        this.playerTwo = "Bot";
        this.startedAt = createTime(game.getStartedAt());
        this.endedAt = createTime(game.getEndedAt());
        this.winner = game.getWinner();
    }

    public GameData(String id, String playerOne, String playerTwo, String startedAt, String endedAt, String winner) {
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.winner = winner;
    }

    private String createId() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            char randomChar = idCharacters.charAt(random.nextInt(0, idCharacters.length()));
            id.append(randomChar);
        }
        return id.toString();
    }

    private String createTime(LocalDateTime time) {
        return time.getDayOfMonth() + " " + time.getMonth().name() + " " + time.getHour() + ":" + time.getMinute() + ", " + time.getYear();
    }

    public void show(CommandSender sender) {
        sender.sendMessage(ColorUtils.getComponent("&aMatch Id: &f" + id));
        sender.sendMessage(ColorUtils.getComponent("&aWinner: &f" + winner));
        sender.sendMessage(ColorUtils.getComponent("&aPlayer One: &f" + playerOne));
        sender.sendMessage(ColorUtils.getComponent("&aPlayer Two: &f" + playerTwo));
        sender.sendMessage(ColorUtils.getComponent("&aStarted At: &f" + startedAt));
        sender.sendMessage(ColorUtils.getComponent("&aEnded At: &f" + endedAt));
    }
}
