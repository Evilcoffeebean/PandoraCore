package dev.pandora.core.punish.commands;

import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.api.handler.command.CommandBase;
import dev.pandora.core.api.networking.Bungeecord;
import dev.pandora.core.api.networking.SQLTYPE;
import dev.pandora.core.api.networking.Table;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.punish.PunishTable;
import dev.pandora.core.utils.StringUtils;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

/**
 * Created by Synch on 2017-04-08.
 */
public class ReportCommand extends CommandBase {

    private DatabaseManager manager;
    private Table reportTable;
    private Bungeecord bungeecord;

    public ReportCommand(DatabaseManager manager){
        super("report", new Rank[]{Rank.ALL}, 2);
        this.manager = manager;
        try {
            reportTable = new Table("Reports");
            reportTable.addSection("player", SQLTYPE.TEXT);
            reportTable.addSection("reporter", SQLTYPE.TEXT);
            reportTable.addSection("reason", SQLTYPE.TEXT);
            reportTable.addSection("server",SQLTYPE.TEXT);
            reportTable.closeConnection();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        bungeecord = new Bungeecord();
    }

    private void inform(CommandSender c, OfflinePlayer target, String reason) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (manager.getRank(p).has(Rank.MOD)) {
                p.sendMessage(ChatColor.AQUA+""+c.getName() + ChatColor.YELLOW+" reported "+ChatColor.AQUA+""+target.getName()+ChatColor.YELLOW+" for '"+reason+"'.");
            }
        }
    }
    private synchronized void reportPlayer(CommandSender c, OfflinePlayer target, String reason) throws SQLException,ClassNotFoundException{
        String reporter = c instanceof Player ? ((Player) c).getUniqueId().toString() : c.getName();
        String server = c instanceof Player ? bungeecord.getCurrentServer((Player)c) : "Console";
        reason = reason.replace("'","");
        reason = reason.replace("\"","");
        reportTable.openConnection();
        reportTable.getConnection().createStatement().executeUpdate(String.format("INSERT INTO %s (player,reporter,reason,server) values('"+"%s"+"','"+"%s"+"','"+"%s"+"','"+"%s"+"')",reportTable.getTable(),target.getUniqueId(),reporter,reason,server));
        reportTable.closeConnection();
    }

    @Override
    public boolean onCommand(CommandSender caller, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(getCommand())) {
                if (args.length < getLength()) {
                    UtilLogger.help(caller, "Command Center", "Usage: /report <player> <reason>");
                    return true;
                }
                OfflinePlayer targetPlayer;
                String reason;

                targetPlayer = Bukkit.getOfflinePlayer(args[0]);
                if (!targetPlayer.hasPlayedBefore()) {
                    UtilLogger.help(caller, "Report", "That player has never played before.");
                    return true;
                }
                reason = args[1] != null ? StringUtils.fromArgs(args, 1) : "No reason specified";
                //table.banPlayer(p, targetPlayer, reason, duration);
                try {
                    reportPlayer(caller,targetPlayer,reason);
                    inform(caller, targetPlayer, reason);
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e){
                    e.printStackTrace();
                }

            }

        return true;
    }
}

