package dev.pandora.core.events;

import dev.pandora.core.utils.UtilLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by Zvijer on 7.4.2017..
 */
public class CommandProcess implements Listener {

    private static final String[] blacklist = {
            "me",
            "ver",
            "pl",
            "list",
            "online",
            "op",
            "deop",
            "bukkit",
            "help",
            "?",
            "icanhasbukkit",
            "core"
    };

    @EventHandler(priority = EventPriority.HIGH)
    public void onProcess(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().replaceAll("/", "").split(" ")[0].toLowerCase();
        for (String s : blacklist) {
            if (s.contains(command) || s.startsWith(command) || s.equalsIgnoreCase(command)) {
                e.setCancelled(true);
                UtilLogger.help(e.getPlayer(), "Player Module", "Don't be so nosy...");
                return;
            }
        }
    }
}
