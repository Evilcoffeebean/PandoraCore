package dev.pandora.core.events;

import dev.pandora.core.api.networking.database.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Zvijer on 31.3.2017..
 */
public class Random implements Listener {

    private DatabaseManager manager;

    public Random(DatabaseManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        String rankTag = manager.getRank(e.getPlayer()).getTag(true);
        //UtilLogger.broadcast(ChatColor.RED + "Quit", rankTag + e.getPlayer().getName());
    }
}
