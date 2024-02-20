package com.mortisdevelopment.duelplugin.arenas;

import com.mortisdevelopment.duelplugin.Nameable;
import com.mortisdevelopment.duelplugin.kits.Kit;
import com.mortisdevelopment.duelplugin.kits.KitManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@Getter @Setter
public class Arena implements Nameable {

    private final String name;
    private final String worldName;
    private ArenaLocation spawn1;
    private ArenaLocation spawn2;
    private String kitName;
    private boolean available;

    public Arena(String name, String worldName) {
        this.name = name;
        this.worldName = worldName;
        this.available = true;
    }

    public boolean isValid() {
        return hasSpawn1() && hasSpawn2() && hasKit();
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public Location getSpawnLocation1() {
        return spawn1.getLocation(getWorld());
    }

    public Location getSpawnLocation2() {
        return spawn2.getLocation(getWorld());
    }

    public boolean isKit(String name) {
        return name.equalsIgnoreCase(kitName);
    }

    public Kit getKit(KitManager kitManager) {
        return kitManager.getObject(kitName);
    }

    public boolean hasSpawn1() {
        return spawn1 != null;
    }

    public boolean hasSpawn2() {
        return spawn2 != null;
    }

    public boolean hasKit() {
        return kitName != null;
    }
}
