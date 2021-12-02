package dev.pandora.core.punish.commands;

import dev.pandora.core.Main;
import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.api.handler.command.CommandBase;
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
public class LookupCommand extends CommandBase {

    private DatabaseManager manager;

    public LookupCommand(DatabaseManager manager){
        super("lookup", new Rank[]{Rank.ALL}, 1);
        this.manager = manager;
    }


    @Override
    public boolean onCommand(CommandSender caller, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(getCommand())) {
            if (args.length < getLength()) {
                UtilLogger.help(caller, "Command Center", "Usage: /lookup <player>");
                return true;
            }
            OfflinePlayer targetPlayer;

            targetPlayer = Bukkit.getOfflinePlayer(args[0]);
            if (!targetPlayer.hasPlayedBefore()) {
                UtilLogger.help(caller, "Lookup", "That player has never played before.");
                return true;
            }
            Main.get().getPunishments().queryPlayer(caller,targetPlayer);


        }

        return true;
    }
}

