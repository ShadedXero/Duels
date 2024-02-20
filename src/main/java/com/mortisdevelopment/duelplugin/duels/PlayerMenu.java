package com.mortisdevelopment.duelplugin.duels;

import com.mortisdevelopment.duelplugin.Menu;
import com.mortisdevelopment.duelplugin.game.Game;
import com.mortisdevelopment.duelplugin.utils.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

@Getter @Setter
public class PlayerMenu extends Menu {

    private final DuelManager duelManager;
    private final Map<Integer, List<UUID>> playersByPage;
    private final Inventory inventory;
    private final int previousPageSlot = 48;
    private final int nextPageSlot = 50;
    private final int closeSlot = 49;
    private final int pageSize = 45;
    private int currentPage;

    public PlayerMenu(DuelManager duelManager) {
        this.duelManager = duelManager;
        this.playersByPage = createEntries();
        this.inventory = createInventory();
        update(1);
    }

    private Map<Integer, List<UUID>> createEntries() {
        Map<Integer, List<UUID>> playersByPage = new HashMap<>();
        int page = 1;
        List<UUID> players = new ArrayList<>();
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (int i = 0; i < onlinePlayers.size(); i++) {
            Player player = onlinePlayers.get(i);
            players.add(player.getUniqueId());
            if (i >= (onlinePlayers.size() - 1)) {
                playersByPage.put(page, new ArrayList<>(players));
                break;
            }
            if (players.size() >= pageSize) {
                playersByPage.put(page, new ArrayList<>(players));
                players.clear();
                page++;
            }
        }
        return playersByPage;
    }

    private Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ColorUtils.getComponent("&aDuel Player"));
        ItemStack filter = getFilterItem();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, filter);
        }
        inventory.setItem(previousPageSlot, getPreviousPageItem());
        inventory.setItem(nextPageSlot, getNextPageItem());
        inventory.setItem(closeSlot, getCloseItem());
        return inventory;
    }

    private boolean hasNextPage() {
        return playersByPage.get(currentPage + 1) != null;
    }

    private boolean hasPreviousPage() {
        return playersByPage.get(currentPage - 1) != null;
    }

    private void update(int page) {
        this.currentPage = page;
        for (int i = 0; i < pageSize; i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
        }
        List<UUID> uuids = playersByPage.get(currentPage);
        if (uuids != null) {
            for (int i = 0; i < uuids.size(); i++) {
                UUID uuid = uuids.get(i);
                ItemStack head = getPlayerHeadItem(uuid);
                inventory.setItem(i, head);
            }
        }
    }

    private ItemStack getNextPageItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.getComponent("&aNext Page"));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPreviousPageItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.getComponent("&aPrevious Page"));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getCloseItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.getComponent("&cClose"));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPlayerHeadItem(UUID uuid) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        meta.setOwningPlayer(player);
        meta.displayName(ColorUtils.getComponent("&e" + player.getName()));
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
        switch (slot) {
            case closeSlot:
                close(player);
                return;
            case previousPageSlot:
                if (!hasPreviousPage()) {
                    return;
                }
                update(currentPage - 1);
                return;
            case nextPageSlot:
                if (!hasNextPage()) {
                    return;
                }
                update(currentPage + 1);
                return;
        }
        List<UUID> entries = playersByPage.get(currentPage);
        if (entries == null || slot > entries.size()) {
            return;
        }
        Player target = Bukkit.getPlayer(entries.get(slot));
        if (target == null) {
            return;
        }
        if (player.equals(target)) {
            player.sendMessage(ColorUtils.getComponent("&cYou cannot duel yourself"));
            return;
        }
        Game game = duelManager.getGameManager().getGameByPlayer().get(target);
        if (game != null) {
            player.sendMessage(ColorUtils.getComponent("&cThe target is currently unavailable for a duel"));
            return;
        }
        duelManager.addRequest(player, target);
        player.sendMessage(ColorUtils.getComponent("&aSent the duel request to the target"));
        target.sendMessage(ColorUtils.getComponent("&a" + player.getName() + " sent you a duel request. Use /duel accept to accept the request"));
        close(player);
    }
}
