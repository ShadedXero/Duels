package com.mortisdevelopment.duelplugin.duels;

import com.mortisdevelopment.duelplugin.Menu;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

@Getter
public class DuelListener implements Listener {

    private final DuelManager duelManager;

    public DuelListener(DuelManager duelManager) {
        this.duelManager = duelManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        duelManager.removeRequest(e.getPlayer());
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();
        if (inventory == null || !(e.getClickedInventory().getHolder() instanceof Menu menu)) {
            return;
        }
        e.setCancelled(true);
        menu.click((Player) e.getWhoClicked(), e.getSlot());
    }
}
