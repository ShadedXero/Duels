package com.mortisdevelopment.duelplugin.game;

import com.mortisdevelopment.duelplugin.arenas.Arena;
import com.mortisdevelopment.duelplugin.kits.Kit;
import com.mortisdevelopment.duelplugin.kits.KitManager;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@Getter
public class BotGame extends Game {

    private final NPC bot;

    public BotGame(GameManager gameManager, KitManager kitManager, Arena arena, Player playerOne, double reach) {
        super(gameManager, arena, playerOne);
        this.bot = createBot(reach);
        reset();
        teleport();
        giveKit(kitManager);
    }

    private NPC createBot(double reach) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Bot");
        npc.getNavigator().getDefaultParameters().attackRange(reach);
        npc.setProtected(false);
        return npc;
    }

    @Override
    public void setStarted() {
        super.setStarted();
        bot.getNavigator().setTarget(getPlayerOne(), true);
    }

    @Override
    public String getOpponentName(Player player) {
        return "Bot";
    }

    @Override
    public void teleport() {
        super.teleport();
        bot.spawn(getArena().getSpawnLocation2());
    }

    @Override
    public void giveKit(KitManager kitManager) {
        super.giveKit(kitManager);
        Kit kit = getArena().getKit(kitManager);
        kit.give((Player) bot.getEntity());
        Inventory inventory = kit.getInventory();
        bot.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, inventory.getItem(39));
        bot.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, inventory.getItem(38));
        bot.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, inventory.getItem(37));
        bot.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, inventory.getItem(36));
        bot.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, inventory.getItem(0));
        bot.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.OFF_HAND, inventory.getItem(40));
    }

    @Override
    public void end(GameManager gameManager, GameData data) {
        super.end(gameManager, data);
        bot.destroy();
        gameManager.getGameByBot().remove(bot.getUniqueId());
    }
}
