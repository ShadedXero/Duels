package com.mortisdevelopment.duelplugin.kits;

import com.mortisdevelopment.duelplugin.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KitCommand extends Command {

    private final KitManager kitManager;
    private final Component noPermission = ColorUtils.getComponent("&cYou don't have permission to use this");
    private final Component commandSuccess = ColorUtils.getComponent("&cSuccessfully processed the command");

    public KitCommand(KitManager kitManager) {
        super("kit");
        this.kitManager = kitManager;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtils.getComponent("&cThis command can only be executed by a player"));
            return false;
        }
        if (args.length < 2) {
            player.sendMessage(ColorUtils.getComponent("&cPlease enter valid command arguments"));
            return false;
        }
        if (args[0].equalsIgnoreCase("create")) {
            if (!player.hasPermission("duels.kit.create")) {
                player.sendMessage(noPermission);
                return false;
            }
            Kit kit = new Kit(args[1], player);
            kitManager.addObject(kit);
            sender.sendMessage(commandSuccess);
            return true;
        }
        if (args[0].equalsIgnoreCase("delete")) {
            if (!player.hasPermission("duels.kit.delete")) {
                player.sendMessage(noPermission);
                return false;
            }
            kitManager.deleteObject(args[1]);
            player.sendMessage(commandSuccess);
            return true;
        }
        if (args[0].equalsIgnoreCase("get")) {
            if (!player.hasPermission("duels.kit.get")) {
                player.sendMessage(noPermission);
                return false;
            }
            Kit kit = kitManager.getObject(args[1]);
            if (kit == null) {
                player.sendMessage(ColorUtils.getComponent("&cPlease enter a valid kit name"));
                return false;
            }
            kit.give(player);
            player.sendMessage(commandSuccess);
            return true;
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("create");
            arguments.add("delete");
            arguments.add("get");
            return arguments;
        }
        if (args.length == 2) {
            return new ArrayList<>(kitManager.getObjectByName().keySet());
        }
        return super.tabComplete(sender, alias, args);
    }
}
