package com.mortisdevelopment.duelplugin.databases;

import com.mortisdevelopment.duelplugin.DuelPlugin;
import com.mortisdevelopment.duelplugin.game.GameData;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import com.mysql.cj.jdbc.Driver;
import java.util.*;

@Getter
public class Database {

    private final DuelPlugin plugin;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;
    private final Map<String, GameData> dataById;

    public Database(DuelPlugin plugin, String host, int port, String database, String username, String password) {
        this.plugin = plugin;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.connection = getConnection();
        this.dataById = new HashMap<>();
        initialize();
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("autoReconnect", "true");
        if (username != null) {
            properties.setProperty("user", username);
        }
        if (password != null) {
            properties.setProperty("password", password);
        }
        return properties;
    }

    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            Driver driver = new Driver();
            this.connection = driver.connect("jdbc:mysql://" + host + ":" + port + "/" + database, getProperties());
            return connection;
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    private void initialize() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Statement statement = getConnection().createStatement();
                    statement.execute("CREATE TABLE IF NOT EXISTS Games(id varchar(255) primary key, player_one varchar(255), player_two varchar(255), started_at varchar(255), ended_at varchar(255), winner varchar(255))");
                } catch (SQLException exp) {
                    throw new RuntimeException(exp);
                }
                loadGames();
            }
        }.runTaskAsynchronously(plugin);
    }

    private void loadGames() {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Games");
            while (result.next()) {
                String id = result.getString("id");
                String playerOne = result.getString("player_one");
                String playerTwo = result.getString("player_two");
                String startedAt = result.getString("started_at");
                String endedAt = result.getString("ended_at");
                String winner = result.getString("winner");
                GameData data = new GameData(id, playerOne, playerTwo, startedAt, endedAt, winner);
                dataById.put(id, data);
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    public GameData getData(String id) {
        return dataById.get(id);
    }

    public void addData(GameData data) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = getConnection().prepareStatement("INSERT INTO Games(id, player_one, player_two, started_at, ended_at, winner) VALUES (?, ?, ?, ?, ?, ?)");
                    statement.setString(1, data.getId());
                    statement.setString(2, data.getPlayerOne());
                    statement.setString(3, data.getPlayerTwo());
                    statement.setString(4, data.getStartedAt());
                    statement.setString(5, data.getEndedAt());
                    statement.setString(6, data.getWinner());
                    statement.executeUpdate();
                } catch (SQLException exp) {
                    throw new RuntimeException(exp);
                }
                dataById.put(data.getId(), data);
            }
        }.runTaskAsynchronously(plugin);
    }
}
