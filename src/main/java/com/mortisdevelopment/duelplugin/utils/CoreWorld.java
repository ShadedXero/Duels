package com.mortisdevelopment.duelplugin.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;

@Getter
public class CoreWorld {

    private final String worldName;

    public CoreWorld(String worldName) {
        this.worldName = worldName;
    }

    public CoreWorld(World world) {
        this.worldName = world.getName();
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }
}
