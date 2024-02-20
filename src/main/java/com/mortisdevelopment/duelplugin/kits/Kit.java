package com.mortisdevelopment.duelplugin.kits;

import com.mortisdevelopment.duelplugin.Nameable;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
public class Kit implements Nameable {

    private final String name;
    private final Inventory inventory;

    public Kit(String name, Inventory inventory) {
        this.name = name;
        this.inventory = inventory;
    }

    public Kit(String name, Player player) {
        this.name = name;
        this.inventory = createInventory(player);
    }

    private Inventory createInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER);
        ItemStack[] oldContent = player.getInventory().getContents();
        ItemStack[] newContent = new ItemStack[oldContent.length];
        for (int i = 0; i < oldContent.length; i++) {
            ItemStack item = oldContent[i];
            if (item == null) {
                continue;
            }
            newContent[i] = item.clone();
        }
        inventory.setContents(newContent);
        return inventory;
    }

    public void give(Player player) {
        player.getInventory().setContents(inventory.getContents());
    }
}
