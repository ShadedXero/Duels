package com.mortisdevelopment.duelplugin;

import com.mortisdevelopment.duelplugin.arenas.ArenaCommand;
import com.mortisdevelopment.duelplugin.arenas.ArenaManager;
import com.mortisdevelopment.duelplugin.databases.Database;
import com.mortisdevelopment.duelplugin.duels.DuelCommand;
import com.mortisdevelopment.duelplugin.duels.DuelListener;
import com.mortisdevelopment.duelplugin.duels.DuelManager;
import com.mortisdevelopment.duelplugin.game.GameListener;
import com.mortisdevelopment.duelplugin.game.GameManager;
import com.mortisdevelopment.duelplugin.game.MatchInfoCommand;
import com.mortisdevelopment.duelplugin.kits.KitCommand;
import com.mortisdevelopment.duelplugin.kits.KitManager;
import com.mortisdevelopment.duelplugin.utils.CoreLocation;
import com.mortisdevelopment.duelplugin.utils.CoreWorld;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class DuelPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveConfigs();
        File dataFolder = getDataFolder();

        KitManager kitManager = new KitManager(new File(dataFolder, "/kits/"));
        getServer().getCommandMap().register("kit", new KitCommand(kitManager));

        File file = new File(getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = Objects.requireNonNull(config.getConfigurationSection("lobby"));
        String world = section.getString("world");
        String rawLocation = Objects.requireNonNull(section.getString("location"));
        String[] raw = rawLocation.split(",");
        CoreLocation lobbyLocation = new CoreLocation(new CoreWorld(world), Double.parseDouble(raw[0]), Double.parseDouble(raw[1]), Double.parseDouble(raw[2]));

        Database database = getDatabase(Objects.requireNonNull(config.getConfigurationSection("database")));
        GameManager gameManager = new GameManager(this, kitManager, database, lobbyLocation);
        getServer().getPluginManager().registerEvents(new GameListener(gameManager), this);
        getServer().getCommandMap().register("matchinfo", new MatchInfoCommand(database));

        ArenaManager arenaManager = new ArenaManager(new File(dataFolder, "/arenas/"));
        getServer().getCommandMap().register("arena", new ArenaCommand(arenaManager));

        DuelManager duelManager = new DuelManager(this, gameManager, arenaManager, config.getDouble("reach-1"), config.getDouble("reach-2"), config.getDouble("reach-3"));
        getServer().getPluginManager().registerEvents(new DuelListener(duelManager), this);
        getServer().getCommandMap().register("duel", new DuelCommand(duelManager));
    }

    private void saveConfigs() {
        saveResource("config.yml", false);
        saveResource("kits/Iron.yml", false);
        saveResource("kits/NoDebuff.yml", false);
    }

    private Database getDatabase(ConfigurationSection section) {
        String host = section.getString("host");
        int port = section.getInt("port");
        String database = section.getString("database");
        String username = section.getString("username");
        String password = section.getString("password");
        return new Database(this, host, port, database, username, password);
    }
}
