package com.mortisdevelopment.duelplugin.game;

import com.mortisdevelopment.duelplugin.databases.Database;
import com.mortisdevelopment.duelplugin.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MatchInfoCommand extends Command {

    private final Database database;

    public MatchInfoCommand(Database database) {
        super("matchinfo");
        this.database = database;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("duels.matchinfo")) {
            sender.sendMessage(ColorUtils.getComponent("&cYou don't have permission to use this"));
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(ColorUtils.getComponent("&cPlease enter valid command arguments"));
            return false;
        }
        GameData data = database.getData(args[0]);
        if (data == null) {
            sender.sendMessage(ColorUtils.getComponent("&cPlease enter a valid game id"));
            return false;
        }
        data.show(sender);
        return true;
    }
}
