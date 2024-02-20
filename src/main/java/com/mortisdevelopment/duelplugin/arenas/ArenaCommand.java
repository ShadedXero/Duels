package com.mortisdevelopment.duelplugin.arenas;

import com.mortisdevelopment.duelplugin.utils.ColorUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArenaCommand extends Command {

    private final ArenaManager arenaManager;
    private final Component noPermission = ColorUtils.getComponent("&cYou don't have permission to use this");
    private final Component wrongUsage = ColorUtils.getComponent("&cPlease enter valid command arguments");
    private final Component commandSuccess = ColorUtils.getComponent("&cSuccessfully processed the command");
    private final Component invalidArena = ColorUtils.getComponent("&cPlease enter a valid arena name");

    public ArenaCommand(ArenaManager arenaManager) {
        super("arena");
        this.arenaManager = arenaManager;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(wrongUsage);
            return false;
        }
        if (args[0].equalsIgnoreCase("bindKit")) {
            if (!sender.hasPermission("duels.arena.bindkit")) {
                sender.sendMessage(noPermission);
                return false;
            }
            if (args.length < 3) {
                sender.sendMessage(wrongUsage);
                return false;
            }
            Arena arena = arenaManager.getObject(args[1]);
            arena.setKitName(args[2]);
            arenaManager.updateConfig(arena);
            sender.sendMessage(commandSuccess);
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtils.getComponent("&cThis command can only be executed by a player"));
            return false;
        }
        if (args[0].equalsIgnoreCase("create")) {
            if (!player.hasPermission("duels.arena.create")) {
                player.sendMessage(noPermission);
                return false;
            }
            Arena arena = new Arena(args[1], player.getWorld().getName());
            arenaManager.addObject(arena);
            sender.sendMessage(commandSuccess);
            return true;
        }
        if (args[0].equalsIgnoreCase("delete")) {
            if (!player.hasPermission("duels.arena.delete")) {
                player.sendMessage(noPermission);
                return false;
            }
            arenaManager.deleteObject(args[1]);
            sender.sendMessage(commandSuccess);
            return true;
        }
        if (args[0].equalsIgnoreCase("setSpawn1")) {
            if (!player.hasPermission("duels.arena.setspawn1")) {
                player.sendMessage(noPermission);
                return false;
            }
            Arena arena = arenaManager.getObject(args[1]);
            if (arena == null) {
                sender.sendMessage(invalidArena);
                return false;
            }
            arena.setSpawn1(new ArenaLocation(player.getLocation()));
            arenaManager.updateConfig(arena);
            sender.sendMessage(commandSuccess);
            return true;
        }
        if (args[0].equalsIgnoreCase("setSpawn2")) {
            if (!player.hasPermission("duels.arena.setspawn2")) {
                player.sendMessage(noPermission);
                return false;
            }
            Arena arena = arenaManager.getObject(args[1]);
            if (arena == null) {
                sender.sendMessage(invalidArena);
                return false;
            }
            arena.setSpawn2(new ArenaLocation(player.getLocation()));
            arenaManager.updateConfig(arena);
            sender.sendMessage(commandSuccess);
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
            arguments.add("setspawn1");
            arguments.add("setspawn2");
            arguments.add("bindKit");
            return arguments;
        }
        if (args.length == 2) {
            return new ArrayList<>(arenaManager.getObjectByName().keySet());
        }
        return super.tabComplete(sender, alias, args);
    }
}
