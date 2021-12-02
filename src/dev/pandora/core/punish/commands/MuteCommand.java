package dev.pandora.core.punish.commands;

import dev.pandora.core.Main;
import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.api.handler.command.CommandBase;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.punish.*;
import dev.pandora.core.utils.IntUtils;
import dev.pandora.core.utils.StringUtils;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Zvijer on 6.4.2017..
 */
public class MuteCommand extends CommandBase {

    private DatabaseManager manager;
    private PunishTable table;

    public MuteCommand(DatabaseManager manager) {
        super("mute", new Rank[]{Rank.DEV}, 3);
        this.manager = manager;
        this.table = manager.getPunishTable();
    }

    private void inform(Player punisher, OfflinePlayer target, long duration) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (manager.getRank(p).has(Rank.MOD)) {
                UtilLogger.help(p, "Punish", punisher.getName() + " has muted " + target.getName() + " for " + PunishPlayerCache.formatDuration(duration));
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
                if (args.length < getLength()) {
                    UtilLogger.help(p, "Punish Beta", "Please specify every punish parameter.");
                    return true;
                }
                OfflinePlayer targetPlayer;
                long duration;
                String reason;
                //Set the data
                targetPlayer = Bukkit.getOfflinePlayer(args[0]);
                if (!targetPlayer.hasPlayedBefore()) {
                    UtilLogger.help(p, "Punish", "That player has never played before.");
                    return true;
                }
                if (table.isMuted(targetPlayer.getUniqueId().toString())) {
                    UtilLogger.help(p, "Punish", "That player is currently muted.");
                    return true;
                }
                duration = !(args[1].contains("perm")) ? Integer.parseInt(args[1]) : 9999;
                reason = args[2] != null ? StringUtils.fromArgs(args, 2) : "The mute hammer has spoken!";
                duration = IntUtils.parse(args, 1);
                reason = StringUtils.fromArgs(args, 2);
                table.mutePlayer(p, targetPlayer, reason, duration);
                Main.get().getPunishments().recordPunishment(p,targetPlayer,PunishType.MUTE,(int) duration,reason);
                inform(p, targetPlayer, duration);
                return true;
            }
        }
        return true;
    }
}
