package dev.pandora.core.commands;

import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.api.handler.command.CommandBase;
import dev.pandora.core.api.handler.nmsMethods.NMSTitle;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.utils.StringUtils;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Zvijer on 24.3.2017..
 */
public class AnnounceCommand extends CommandBase {

    private DatabaseManager manager;

    public AnnounceCommand(DatabaseManager manager) {
        super("announce", "/announce <message>", new String[]{"a", "global"}, new Rank[]{Rank.SNR_MOD}, 1, true, true);
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
                    return true;
                } else {
                    new NMSTitle("&6Announcement", "&7" + StringUtils.fromArgs(args, 0)).send();
                    return true;
                }
            } else {
                if (args.length < getLength()) {
                    UtilLogger.help(caller, "Command Center", "Usage: " + getUsage());
                    return true;
                } else {
                    new NMSTitle("&6Announcement", "&7" + StringUtils.fromArgs(args, 0)).send();
                    return true;
                }
            }
        }
        return true;
    }
}
