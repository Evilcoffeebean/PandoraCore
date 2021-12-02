package dev.pandora.core.punish.commands;

import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.api.handler.command.CommandBase;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.utils.StringUtils;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Zvijer on 9.4.2017..
 */
public class KickCommand extends CommandBase {

    private DatabaseManager manager;

    public KickCommand(DatabaseManager manager) {
        super("kick", new Rank[]{Rank.DEV}, 2);
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender caller, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(getCommand())) {
            if (!(caller instanceof Player)) return true;
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
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                UtilLogger.help(p, "Online Player Search", args[0] + " couldn't have been found online.");
                return true;
            }
            String reason = StringUtils.fromArgs(args, 1);
            final String message = StringUtils.fromBuilder(new String[]{
                    "&c&lYou've been kicked off the server!",
                    "",
                    "&c&lKicked by: &e" + p.getName(),
                    "&c&lKicked for: &e" + reason,
                    "",
                    "&c&lPlease note that continuation of this behaviour",
                    "&4&lwill &c&lresult in punishment."
            });
            target.kickPlayer(message);
            Bukkit.getOnlinePlayers().forEach(all -> {
                if (manager.getRank(all).has(Rank.MOD)) {
                    UtilLogger.help(all, "Punish Module", p.getName() + " has kicked " + target.getName() + " for " + reason);
                }
            });
        }
        return true;
    }
}
