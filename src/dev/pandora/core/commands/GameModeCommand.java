package dev.pandora.core.commands;

import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.api.handler.command.CommandBase;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.utils.IntUtils;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Zvijer on 3.4.2017..
 */
public class GameModeCommand extends CommandBase {

    private void adjustGamemode(Player player, int i) {
        switch (i) {
            case 0:
                player.setGameMode(GameMode.SURVIVAL);
                break;
            case 1:
                player.setGameMode(GameMode.CREATIVE);
                break;
            case 2:
                player.setGameMode(GameMode.ADVENTURE);
                break;
        }
    }

    private DatabaseManager manager;

    public GameModeCommand(DatabaseManager manager) {
        super("gamemode", "/gamemode <0/1/2>", new String[]{"gm"}, new Rank[]{Rank.DEV}, 2);
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender caller, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(getCommand())) {
            if (!(caller instanceof Player)) return true;
            Player p = (Player) caller;
            if (!manager.getRank(p).has(getRequired()[0])) {
                UtilLogger.help(p, "Permissions", "This requires Permission Rank " + getRequired()[0].getTag(false));
                return true;
            }
            switch (args.length) {
                case 0:
                    UtilLogger.help(caller, "Command Center", "Usage: " + getUsage());
                    break;
                case 1:
                    adjustGamemode(p, IntUtils.parse(args, 0));
                    UtilLogger.help(p, "Staff Module", "You set your own gamemode to " + p.getGameMode());
                    break;
                case 2:
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target != null) {
                        adjustGamemode(target, IntUtils.parse(args, 1));
                        UtilLogger.help(p, "Staff Module", "You set " + target.getName() + "'s gamemode to " + p.getGameMode());
                        UtilLogger.help(target, "Staff Module", manager.getRank(p).getTag(true) + p.getName() + ChatColor.GRAY + " has updated your gamemode");
                        return true;
                    } else {
                        UtilLogger.help(p, "Online Player Search", "No such player could have been found.");
                    }
                    break;
            }
        }
        return true;
    }
}
