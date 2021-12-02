package dev.pandora.core.punish.events;

import dev.pandora.core.punish.PunishPlayerCache;
import dev.pandora.core.punish.PunishTable;
import dev.pandora.core.utils.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * Created by Zvijer on 2.4.2017..
 */
public class PunishLogin implements Listener {

    private PunishTable table;

    public PunishLogin(PunishTable table) {
        this.table = table;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onLoginAttempt(AsyncPlayerPreLoginEvent e) {
        String uuid = e.getUniqueId().toString();
        if (table.isBanned(uuid)) {
            final String message = StringUtils.fromBuilder(new String[]{
                    "&c&lYou've been disallowed access to Pandora!",
                    "&c&lYou've been banned. Information:",
                    "",
                    "&cBanned by: &e" + table.getPunisherName(uuid),
                    "&cReason: &e" + table.getPunishReason(uuid),
                    "&cDate: &e" + table.getPunishDate(uuid),
                    "&cDuration: &e" + PunishPlayerCache.formatDuration(table.getPunishmentDuration(uuid)),
                    "",
                    "&bIf you feel you've been wrongfully banned, please appeal at",
                    "&ahttp://pandoranetwork.net/"
            });
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);
        }
    }
}
