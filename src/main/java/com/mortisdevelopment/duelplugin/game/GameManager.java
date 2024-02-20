package com.mortisdevelopment.duelplugin.game;

import com.mortisdevelopment.duelplugin.DuelPlugin;
import com.mortisdevelopment.duelplugin.arenas.Arena;
import com.mortisdevelopment.duelplugin.databases.Database;
import com.mortisdevelopment.duelplugin.kits.KitManager;
import com.mortisdevelopment.duelplugin.utils.CoreLocation;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Getter
public class GameManager {

    private final KitManager kitManager;
    private final Database database;
    private final List<Game> games;
    private final Map<Player, Game> gameByPlayer;
    private final Map<UUID, Game> gameByBot;
    private final long startTime = 7;
    private final long endTime = 10;
    private final CoreLocation lobby;

    public GameManager(DuelPlugin plugin, KitManager kitManager, Database database, CoreLocation lobby) {
        this.kitManager = kitManager;
        this.database = database;
        this.games = new ArrayList<>();
        this.gameByPlayer = new HashMap<>();
        this.gameByBot = new HashMap<>();
        this.lobby = lobby;
        check(plugin);
    }

    private void check(DuelPlugin plugin) {
        GameManager gameManager = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Game> iterator = games.iterator();
                while (iterator.hasNext()) {
                    Game game = iterator.next();
                    game.setTime(game.getTime() + 1);
                    if (!game.isStarted()) {
                        if (game.getTime() >= startTime) {
                            game.resetTitle();
                            game.setStarted();
                        }else {
                            game.showCountdown();
                        }
                    }
                    if (game.isEnded()) {
                        game.setEndTime(game.getEndTime() + 1);
                        if (game.getEndTime() >= endTime) {
                            iterator.remove();
                            game.resetTitle();
                            GameData data = null;
                            if (game instanceof PlayerGame playerGame) {
                                data = new GameData(playerGame);
                            }else if (game instanceof BotGame botGame) {
                                data = new GameData(botGame);
                            }
                            database.addData(data);
                            game.end(gameManager, data);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void startGame(Arena arena, Player playerOne, Player playerTwo) {
        PlayerGame game = new PlayerGame(this, kitManager, arena, playerOne, playerTwo);
        games.add(game);
        gameByPlayer.put(playerOne, game);
        gameByPlayer.put(playerTwo, game);
    }

    public void startGame(Arena arena, Player playerOne, double reach) {
        BotGame game = new BotGame(this, kitManager, arena, playerOne, reach);
        games.add(game);
        gameByPlayer.put(playerOne, game);
        gameByBot.put(game.getBot().getUniqueId(), game);
    }
}
