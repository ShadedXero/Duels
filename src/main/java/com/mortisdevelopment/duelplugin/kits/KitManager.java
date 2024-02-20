package com.mortisdevelopment.duelplugin.kits;

import com.mortisdevelopment.duelplugin.Manager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Getter
public class KitManager extends Manager<Kit> {

    public KitManager(File dataFolder) {
        super(dataFolder);
    }

    @Override
    public Kit getObject(FileConfiguration config) {
        String name = config.getString("name");
        Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER);
        ConfigurationSection section = Objects.requireNonNull(config.getConfigurationSection("inventory"));
        for (String rawSlot : section.getKeys(false)) {
            int slot = Integer.parseInt(rawSlot);
            ItemStack item = deserialize(section.getString(rawSlot));
            inventory.setItem(slot, item);
        }
        return new Kit(name, inventory);
    }

    @Override
    public void saveObject(Kit kit) {
        try {
            String name = kit.getName();
            File file = getFile(name);
            file.createNewFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("name", name);
            ConfigurationSection section = config.createSection("inventory");
            Inventory inventory = kit.getInventory();
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item == null) {
                    continue;
                }
                section.set(String.valueOf(i), serialize(item));
            }
            config.save(file);
        }catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    private String serialize(ItemStack item) {
        return Base64.getEncoder().encodeToString(item.serializeAsBytes());
    }

    private ItemStack deserialize(String rawItem) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(rawItem));
    }
}
