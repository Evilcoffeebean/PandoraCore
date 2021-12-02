package dev.pandora.core.punish.events;

import dev.pandora.core.punish.PunishPlayerCache;
import dev.pandora.core.punish.PunishTable;
import dev.pandora.core.utils.StringUtils;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Zvijer on 2.4.2017..
 */
public class PunishSpeak implements Listener {

    private PunishTable table;

    public PunishSpeak(PunishTable table) {
        this.table = table;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSpeakAttempt(AsyncPlayerChatEvent e) {
        String uuid = e.getPlayer().getUniqueId().toString();
        if (table.isMuted(uuid)) {
            e.setCancelled(true);
            final String message = StringUtils.fromBuilder(new String[]{
                    " ",
                    " •  &7Muted by: &e" + table.getPunisherName(uuid),
                    " •  &7Reason: &e" + table.getPunishReason(uuid),
                    " •  &7Date: &e" + table.getPunishDate(uuid),
                    " •  &7Duration: &e" + PunishPlayerCache.formatDuration(table.getPunishmentDuration(uuid)),
                    " ",
                    " •  &7If you feel you've been wrongfully banned, appeal at&e",
                    " •  &ahttp://pandoranetwork.net/"
            });
            UtilLogger.help(e.getPlayer(), "Chat Module", "You're forbidden to speak!");
            e.getPlayer().sendMessage(message);
        }
    }
}
