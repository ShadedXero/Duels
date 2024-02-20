package com.mortisdevelopment.duelplugin.arenas;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

@Getter
public class ArenaLocation {

    private final double x;
    private final double y;
    private final double z;
    private final float pitch;
    private final float yaw;

    public ArenaLocation(double x, double y, double z, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public ArenaLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public Location getLocation(World world) {
        return new Location(world, x, y, z, pitch, yaw);
    }
}
