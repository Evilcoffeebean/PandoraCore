package dev.pandora.core.events;

import dev.pandora.core.api.handler.nmsMethods.NMSTitle;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.commands.SpawnCommand;
import dev.pandora.core.utils.ConfigUtil;
import dev.pandora.core.utils.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

/**
 * Created by Zvijer on 16.3.2017..
 */
public class PlayerJoin implements Listener {

    private DatabaseManager manager;

    public PlayerJoin(DatabaseManager manager) {
        this.manager = manager;
    }

    void teleport(Player player) {
        ConfigUtil util = new SpawnCommand(manager).getUtil();
        if (util.getValue("spawn") != null) {
            double x, y, z;
            double yaw, pitch;
            World world;
            x = util.getValue("spawn.pos.x");
            y = util.getValue("spawn.pos.y");
            z = util.getValue("spawn.pos.z");
            yaw = util.getValue("spawn.pos.yaw");
            pitch = util.getValue("spawn.pos.pitch");
            world = Bukkit.getWorld((String)util.getValue("spawn.world"));
            player.teleport(new Location(world, x, y, z, (float) yaw, (float) pitch));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        teleport(e.getPlayer());
        String tabTag = manager.getRank(e.getPlayer()).getTagColor().toString();
        String tag = manager.getRank(e.getPlayer()).getTag(true);
        //UtilLogger.broadcast(ChatColor.AQUA + "Join", tag + e.getPlayer().getName());
        ServerUtil.setHeaderFooter("&6&lPandora&b&lNetwork", "&a&oBeta Release 1.0");
        e.getPlayer().setPlayerListName(tabTag + e.getPlayer().getName());
        if (!manager.isRegistered(e.getPlayer()) || !e.getPlayer().hasPlayedBefore()) {
            manager.registerUser(e.getPlayer());
            new NMSTitle("&6&lPandora&b&lNetwork", "&a&nWelcome to our server!").send(e.getPlayer());
        } else {
            final String[] subtitles = {
                    "&bBrand new server!",
                    "&a&nAwesome games and features!",
                    "&cDedicated staff team &4&l<3",
                    "&dEnjoy your stay here!",
                    "&7&oThe devz like milk too"
            };
            int index = new Random().nextInt(subtitles.length);
            new NMSTitle("&6&lPandora&b&lNetwork", subtitles[index]).send(e.getPlayer());
        }
    }
}
