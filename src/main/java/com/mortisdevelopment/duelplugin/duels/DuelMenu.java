package com.mortisdevelopment.duelplugin.duels;

import com.mortisdevelopment.duelplugin.Menu;
import com.mortisdevelopment.duelplugin.utils.ColorUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class DuelMenu extends Menu {

    private final DuelManager duelManager;
    private final Inventory inventory;
    private final int playerSlot = 11;
    private final int botSlot = 15;

    public DuelMenu(DuelManager duelManager) {
        this.duelManager = duelManager;
        this.inventory = createInventory();
    }

    private Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, ColorUtils.getComponent("&aDuel"));
        ItemStack filter = getFilterItem();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, filter);
        }
        inventory.setItem(playerSlot, getPlayerItem());
        inventory.setItem(botSlot, getBotItem());
        return inventory;
    }

    private ItemStack getFilterItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.getComponent(" "));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPlayerItem() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.getComponent("&cDuel a Player"));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getBotItem() {
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.getComponent("&cDuel a Bot"));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void click(Player player, int slot) {
        switch (slot) {
            case playerSlot:
                new PlayerMenu(duelManager).open(player);
                break;
            case botSlot:
                new BotMenu(duelManager).open(player);
                break;
        }
    }
}
