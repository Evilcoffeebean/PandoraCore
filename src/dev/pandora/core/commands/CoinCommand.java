package dev.pandora.core.commands;

import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.api.handler.command.CommandBase;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Zvijer on 30.3.2017..
 */
public class CoinCommand extends CommandBase {

    private DatabaseManager manager;

    public CoinCommand(DatabaseManager manager) {
        super("coin", "/coin <player> <amount>", new Rank[]{Rank.DEV}, 2);
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender caller, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(getCommand())) {
            if (caller instanceof Player) {
                Player p = (Player) caller;
                Rank current = manager.getRank(p);
                if (!current.has(getRequired()[0])) {
                    UtilLogger.help(p, "Permissions", "This requires Permission Rank " + getRequired()[0].getTag(false));
                    return true;
                }
                if (args.length < getLength()) {
                    UtilLogger.help(caller, "Command Center", "Usage: " + getUsage());
                    return true;
                }
                try {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (!manager.isRegistered((Player)target)) {
                        UtilLogger.help(caller, "Staff Interface", "That user isn't registered in the server's database.");
                        return true;
                    }
                    int amount = Integer.parseInt(args[1]);
                    manager.setCoins((Player)target, amount);
                    UtilLogger.help(caller, "Coin", "You set " + target.getName() + "'s coin amount to " + ChatColor.YELLOW + amount);
                } catch (Exception e) {
                    UtilLogger.help(caller, "Staff Interface", "Invalid amount parameter input.");
                    return true;
                }
            } else {
                if (args.length < getLength()) {
                    UtilLogger.help(caller, "Command Center", "Usage: " + getUsage());
                    return true;
                } else {
                    try {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                        if (!manager.isRegistered((Player)target)) {
                            UtilLogger.help(caller, "Staff Interface", "That user isn't registered in the server's database.");
                            return true;
                        }
                        int amount = Integer.parseInt(args[1]);
                        manager.setCoins((Player)target, amount);
                    } catch (Exception e) {
                        UtilLogger.help(caller, "Staff Interface", "Invalid amount parameter input.");
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
