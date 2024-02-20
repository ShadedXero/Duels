package com.mortisdevelopment.duelplugin.duels;

import com.mortisdevelopment.duelplugin.Menu;
import com.mortisdevelopment.duelplugin.arenas.Arena;
import com.mortisdevelopment.duelplugin.utils.ColorUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class BotMenu extends Menu {

    private final DuelManager duelManager;
    private final Inventory inventory;
    private final int difficultyOneSlot = 11;
    private final int difficultyTwoSlot = 13;
    private final int difficultyThreeSlot = 15;

    public BotMenu(DuelManager duelManager) {
        this.duelManager = duelManager;
        this.inventory = createInventory();
    }

    private Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, ColorUtils.getComponent("&aDuel Bot"));
        ItemStack filter = getFilterItem();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, filter);
        }
        inventory.setItem(difficultyOneSlot, getDifficulty1());
        inventory.setItem(difficultyTwoSlot, getDifficulty2());
        inventory.setItem(difficultyThreeSlot, getDifficulty3());
        return inventory;
    }

    private ItemStack getDifficulty1() {
        ItemStack item = new ItemStack(Material.COBBLESTONE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.getComponent("&eDifficulty 1: Reach 3.0"));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getDifficulty2() {
        ItemStack item = new ItemStack(Material.COBBLESTONE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.getComponent("&eDifficulty 2: Reach 3.1"));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getDifficulty3() {
        ItemStack item = new ItemStack(Material.COBBLESTONE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.getComponent("&eDifficulty 3: Reach 3.3"));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getFilterItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.getComponent(" "));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void click(Player player, int slot) {
        Arena arena = duelManager.getArenaManager().getRandomAvailableBotArena();
        if (arena == null) {
            return;
        }
        switch (slot) {
            case difficultyOneSlot:
                duelManager.getGameManager().startGame(arena, player, duelManager.getReachOne());
                close(player);
                break;
            case difficultyTwoSlot:
                duelManager.getGameManager().startGame(arena, player, duelManager.getReachTwo());
                close(player);
                break;
            case difficultyThreeSlot:
                duelManager.getGameManager().startGame(arena, player, duelManager.getReachThree());
                close(player);
                break;
        }
    }
}
