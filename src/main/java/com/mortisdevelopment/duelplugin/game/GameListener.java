package com.mortisdevelopment.duelplugin.game;

import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.Objects;

public class GameListener implements Listener {

    private final GameManager gameManager;

    public GameListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null || game.isStarted()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null) {
            return;
        }
        if (!game.isStarted() || game.isEnded()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null || game.isEnded()) {
            return;
        }
        e.setCancelled(true);
        game.setWinner(game.getOpponentName(player));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null) {
            return;
        }
        game.setWinner(game.getOpponentName(player));
    }

    @EventHandler
    public void onHit(NPCDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        NPC npc = e.getNPC();
        Game game = gameManager.getGameByBot().get(npc.getUniqueId());
        if (game == null) {
            return;
        }
        Player bot = (Player) npc.getEntity();
        if (bot.getHealth() >= 10) {
            return;
        }
        heal(bot);
    }

    private ItemStack getPotion() {
        ItemStack item = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionType(PotionType.INSTANT_HEAL);
        item.setItemMeta(meta);
        return item;
    }

    private void spawnFakePotion(Location location) {
        ThrownPotion potion = location.getWorld().spawn(location, ThrownPotion.class);
        potion.splash();
    }

    private void heal(Player bot) {
        ItemStack potion = getPotion();
        if (bot.getInventory().contains(potion)) {
            bot.getInventory().removeItem(potion);
            spawnFakePotion(bot.getLocation());
            double maxHealth = Objects.requireNonNull(bot.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
            bot.setHealth(Math.min(maxHealth, bot.getHealth() + 5));
        }
    }

    @EventHandler
    public void onBotDeath(NPCDeathEvent e) {
        NPC npc = e.getNPC();
        Game game = gameManager.getGameByBot().get(npc.getUniqueId());
        if (game == null) {
            return;
        }
        game.setWinner(game.getPlayerOne().getName());
    }
}
