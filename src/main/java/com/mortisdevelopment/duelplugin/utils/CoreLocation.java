package com.mortisdevelopment.duelplugin.utils;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class CoreLocation {

    private final CoreWorld coreWorld;
    private final double x;
    private final double y;
    private final double z;

    public CoreLocation(CoreWorld coreWorld, double x, double y, double z) {
        this.coreWorld = coreWorld;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoreLocation(Location location) {
        this.coreWorld = new CoreWorld(location.getWorld());
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public Location getLocation() {
        return new Location(coreWorld.getWorld(), x, y, z);
    }

    public static CoreLocation getCoreLocation(ConfigurationSection section) {
        if (section == null) {
            return null;
        }
        String worldName = section.getString("world");
        if (worldName == null) {
            return null;
        }
        CoreWorld coreWorld = new CoreWorld(worldName);
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        return new CoreLocation(coreWorld, x, y, z);
    }

    public static String serialize(CoreLocation location) {
        return location.getCoreWorld().getWorldName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }

    public static CoreLocation deserialize(String rawLocation) {
        String[] raw = rawLocation.split(",");
        if (raw.length < 4) {
            return null;
        }
        CoreWorld coreWorld = new CoreWorld(raw[0]);
        double x;
        double y;
        double z;
        try {
            x = Double.parseDouble(raw[1]);
            y = Double.parseDouble(raw[2]);
            z = Double.parseDouble(raw[3]);
        }catch (NumberFormatException exp) {
            return null;
        }
        return new CoreLocation(coreWorld, x, y, z);
    }
}
