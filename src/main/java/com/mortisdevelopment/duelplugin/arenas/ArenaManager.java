package com.mortisdevelopment.duelplugin.arenas;

import com.mortisdevelopment.duelplugin.Manager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
public class ArenaManager extends Manager<Arena> {

    private final Random random = new Random();

    public ArenaManager(File dataFolder) {
        super(dataFolder);
    }

    public List<Arena> getAvailableArenas() {
        List<Arena> arenas = new ArrayList<>();
        for (Arena arena : getObjects()) {
            if (arena.isValid() && arena.isAvailable()) {
                arenas.add(arena);
            }
        }
        return arenas;
    }

    public Arena getRandomAvailableArena() {
        return getRandomArena(getAvailableArenas());
    }

    public List<Arena> getAvailableArenasWithKit(String kitName) {
        List<Arena> arenas = new ArrayList<>();
        for (Arena arena : getAvailableArenas()) {
            if (arena.isValid() && arena.isAvailable() && arena.isKit(kitName)) {
                arenas.add(arena);
            }
        }
        return arenas;
    }

    public Arena getRandomAvailableArenaWithKit(String kitName) {
        return getRandomArena(getAvailableArenasWithKit(kitName));
    }

    public Arena getRandomAvailableBotArena() {
        return getRandomAvailableArenaWithKit("NoDebuff");
    }

    private Arena getRandomArena(List<Arena> arenas) {
        if (arenas.isEmpty()) {
            return null;
        }
        return arenas.get(random.nextInt(0, arenas.size()));
    }

    @Override
    public void saveObject(Arena arena) {
        try {
            String name = arena.getName();
            File file = getFile(name);
            file.createNewFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("name", name);
            config.set("world", arena.getWorldName());
            config.save(file);
        }catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public Arena getObject(FileConfiguration config) {
        String name = config.getString("name");
        String world = config.getString("world");
        Arena arena = new Arena(name, world);
        if (config.contains("spawn1")) {
            arena.setSpawn1(getLocation(Objects.requireNonNull(config.getString("spawn1"))));
        }
        if (config.contains("spawn2")) {
            arena.setSpawn2(getLocation(Objects.requireNonNull(config.getString("spawn2"))));
        }
        if (config.contains("kit")) {
            arena.setKitName(config.getString("kit"));
        }
        return arena;
    }

    private ArenaLocation getLocation(String rawVector) {
        String[] raw = rawVector.split(",");
        return new ArenaLocation(Double.parseDouble(raw[0]), Double.parseDouble(raw[1]), Double.parseDouble(raw[2]), Float.parseFloat(raw[3]), Float.parseFloat(raw[4]));
    }

    private String getLocation(ArenaLocation location) {
        return new StringJoiner(",")
                .add(String.valueOf(location.getX()))
                .add(String.valueOf(location.getY()))
                .add(String.valueOf(location.getZ()))
                .add(String.valueOf(location.getPitch()))
                .add(String.valueOf(location.getYaw()))
                .toString();
    }

    public void updateConfig(Arena arena) {
        File file = getFile(arena.getName());
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (arena.hasSpawn1()) {
            config.set("spawn1", getLocation(arena.getSpawn1()));
        }
        if (arena.hasSpawn2()) {
            config.set("spawn2", getLocation(arena.getSpawn2()));
        }
        if (arena.hasKit()) {
            config.set("kit", arena.getKitName());
        }
        try {
            config.save(file);
        }catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }
}
