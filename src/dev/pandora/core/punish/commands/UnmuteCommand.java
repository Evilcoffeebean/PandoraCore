package dev.pandora.core.punish.commands;

import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.api.handler.command.CommandBase;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.punish.PunishMessage;
import dev.pandora.core.punish.PunishTable;
import dev.pandora.core.punish.PunishType;
import dev.pandora.core.utils.StringUtils;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Zvijer on 6.4.2017..
 */
public class UnmuteCommand extends CommandBase {

    private DatabaseManager manager;
    private PunishTable table;

    public UnmuteCommand(DatabaseManager manager) {
        super("unmute", new Rank[]{Rank.DEV}, 2);
        this.manager = manager;
        this.table = manager.getPunishTable();
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
                    UtilLogger.help(p, "Command Center", "Usage: /unmute <player> <reason>");
                    return true;
                }
                final String UUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
                if (args[1] != null) {
                    String reason = StringUtils.fromArgs(args, 1);
                    if (table.isMuted(UUID)) {
                        table.unpunishUser(UUID, PunishType.MUTE);
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (manager.getRank(all).has(Rank.MOD)) {
                                PunishMessage.unpunishMessage(p, Bukkit.getOfflinePlayer(java.util.UUID.fromString(UUID)).getName(), reason, all);
                                return true;
                            }
                        }
                        return true;
                    } else {
                        UtilLogger.help(p, "Punish", "That player isn't muted.");
                        return true;
                    }
                }
            } else {
                if (args.length < getLength()) {
                    UtilLogger.log("Command Center", "Usage: /unmute <player> <reason>");
                    return true;
                }
                final String UUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
                if (args[1] != null) {
                    String reason = StringUtils.fromArgs(args, 1);
                    if (table.isMuted(UUID)) {
                        table.unpunishUser(UUID, PunishType.MUTE);
                        UtilLogger.log("Command Center", "You unpunished " + Bukkit.getOfflinePlayer(java.util.UUID.fromString(UUID)).getName() + " for " + reason);
                        return true;
                    } else {
                        UtilLogger.log("Punish", "That player isn't banned.");
                        return true;
                    }
                } else {
                    if (table.isMuted(UUID)) {
                        table.unpunishUser(UUID, PunishType.MUTE);
                        UtilLogger.log("Command Center", "You unpunished " + Bukkit.getOfflinePlayer(java.util.UUID.fromString(UUID)).getName());
                        return true;
                    } else {
                        UtilLogger.log("Punish", "That player isn't banned.");
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
