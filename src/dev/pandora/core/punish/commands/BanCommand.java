package dev.pandora.core.punish.commands;

import dev.pandora.core.Main;
import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.api.handler.command.CommandBase;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.punish.PunishMessage;
import dev.pandora.core.punish.PunishPlayerCache;
import dev.pandora.core.punish.PunishTable;
import dev.pandora.core.punish.PunishType;
import dev.pandora.core.utils.ServerUtil;
import dev.pandora.core.utils.StringUtils;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Zvijer on 3.4.2017..
 */
public class BanCommand extends CommandBase {

    private DatabaseManager manager;
    private PunishTable table;

    public BanCommand(DatabaseManager manager) {
        super("ban", new Rank[]{Rank.DEV}, 3);
        this.manager = manager;
        this.table = manager.getPunishTable();
    }

    private void inform(Player punisher, OfflinePlayer target, String reason, long duration) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (manager.getRank(p).has(Rank.MOD)) {
                PunishMessage.punishMessage(punisher, target, reason, duration, PunishType.BAN, p);
            }
        }
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
                //Argument length is not directly specified
                if (args.length < getLength()) {
                    UtilLogger.help(p, "Punish Beta", "Please specify every punish parameter.");
                    return true;
                }
                //Every argument is specified
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (!target.hasPlayedBefore()) {
                    UtilLogger.help(p, "Punish", "That player has never played before.");
                    return true;
                }
                long duration = !(args[1].equalsIgnoreCase("perm")) ? Long.parseLong(args[1]) : 9999;
                String reason = StringUtils.fromArgs(args, 2);
                table.banPlayer(p, target, reason, duration);
                Main.get().getPunishments().recordPunishment(p,target,PunishType.BAN,(int) duration,reason);
                if (ServerUtil.cast(target)) {
                    final String message = StringUtils.fromBuilder(new String[]{
                            "&c&lYou've been disallowed access to Pandora!",
                            "&c&lYou've been banned. Information:",
                            "",
                            "&cBanned by: &e" + table.getPunisherName(p.getUniqueId().toString()),
                            "&cReason: &e" + table.getPunishReason(p.getUniqueId().toString()),
                            "&cDate: &e" + table.getPunishDate(p.getUniqueId().toString()),
                            "&cDuration: &e" + PunishPlayerCache.formatDuration(table.getPunishmentDuration(p.getUniqueId().toString())),
                            "",
                            "&bIf you feel you've been wrongfully banned, please appeal at",
                            "&ahttp://pandoranetwork.net/"
                    });
                    ((Player) target).kickPlayer(message);
                }
                inform(p, target, reason, duration);
            }
        }
        return true;
    }
}
