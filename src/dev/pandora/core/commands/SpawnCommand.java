package dev.pandora.core.commands;

import dev.pandora.core.Main;
import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.api.handler.command.CommandBase;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.utils.ConfigUtil;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * Created by Zvijer on 31.3.2017..
 */
public class SpawnCommand extends CommandBase {

    private DatabaseManager manager;
    private final ConfigUtil util;

    public ConfigUtil getUtil() {
        return util;
    }

    public SpawnCommand(DatabaseManager manager) {
        super("setspawn", "/setspawn <name>", null, new Rank[]{Rank.DEV}, 1, true, false);
        this.manager = manager;
        this.util = new ConfigUtil(new File(Main.get().getDataFolder(), "spawn-data.yml"));
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
            if (args.length != getLength()) {
                UtilLogger.help(caller, "Command Center", "Usage: " + getUsage());
                return true;
            }
            util.setValue("spawn.creator", p.getName());
            util.setValue("spawn.spawn-name", args[0]);
            util.setValue("spawn.world", p.getWorld().getName());
            util.setValue("spawn.pos.x", p.getLocation().getX());
            util.setValue("spawn.pos.y", p.getLocation().getY());
            util.setValue("spawn.pos.z", p.getLocation().getZ());
            util.setValue("spawn.pos.pitch", p.getLocation().getPitch());
            util.setValue("spawn.pos.yaw", p.getLocation().getYaw());
            UtilLogger.help(p, "Spawn", "You've set a spawn "+ ChatColor.YELLOW + args[0] + ChatColor.GRAY + ". See the spawn configuration for more info.");
            return true;
        }
        return true;
    }
}
