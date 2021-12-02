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
public class RankCommand extends CommandBase {

    private DatabaseManager manager;

    public RankCommand(DatabaseManager manager) {
        super("rank", "/rank <player> <rank>", new String[]{"updaterank", "setrank"},
                new Rank[]{Rank.DEV}, 2, true, true);
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
                    UtilLogger.help(p, "Command Center", "Usage: " + getUsage());
                    UtilLogger.help(p, "Rank", "List of available ranks");
                    StringBuilder sb = new StringBuilder();
                    sb.append(ChatColor.GRAY + "Rank names as specified in code").append("\n");
                    for (Rank r : Rank.values()) {
                        sb.append(ChatColor.GRAY + r.toString()).append("\n");
                    }
                    caller.sendMessage(sb.toString());
                    return true;
                }
                try {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    Rank specified = Rank.valueOf(args[1]);
                    if (manager.isRegistered((Player)target)) {
                        manager.setRank((Player)target, specified);
                        UtilLogger.help(caller, "Staff Interface", "You set " + target.getName() + "'s rank to " +
                                (specified != Rank.ALL ? specified.getTag(false) : "the default rank"));
                    } else {
                        UtilLogger.help(caller, "Staff Interface", "That user isn't registered in the server's database");
                        return true;
                    }
                } catch (Exception e) {
                    UtilLogger.help(caller, "Staff Interface", "Invalid rank parameter input.");
                    return true;
                }
            } else {
                if (args.length < getLength()) {
                    UtilLogger.help(caller, "Command Center", "Usage: " + getUsage());
                    return true;
                } else {
                    try {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                        Rank specified = Rank.valueOf(args[1]);
                        if (manager.isRegistered((Player)target)) {
                            manager.setRank((Player)target, specified);
                            UtilLogger.help(caller, "Staff Interface", "You set " + target.getName() + "'s rank to " +
                                    (specified != Rank.ALL ? specified.getTag(false) : "the default rank"));
                        } else {
                            UtilLogger.help(caller, "Staff Interface", "That user isn't registered in the server's database");
                            return true;
                        }
                    } catch (Exception e) {
                        UtilLogger.help(caller, "Staff Interface", "Invalid rank parameter input.");
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
