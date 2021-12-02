package dev.pandora.core.punish;

import dev.pandora.core.utils.StringUtils;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

/**
 * Created by Zvijer on 3.4.2017..
 */
public class PunishMessage {

    public static void punishMessage(Player punisher, OfflinePlayer target, String reason, long duration, PunishType type, Player... receivers) {
        final String output = StringUtils.fromBuilder(new String[]{
                "&4-=-=-=-=-=-=[&cPandora Punish System&4]-=-=-=-=-=-=-=",
                " ",
                "   &e" + punisher.getName() + " &7has " + (type == PunishType.BAN ? "banned" : "punished") + " &e" + target.getName(),
                "   &7for &e" + reason,
                "   &7for &e" + PunishPlayerCache.formatDuration(duration),
                " ",
                "   &7If you feel " + punisher.getName() + " has committed an unfair,",
                "   &7or misjudged decision in regards to punishing " + target.getName(),
                "   &7Please screenshot this and provide it to the",
                "   &7server administration.",
                " ",
                "&4-=-=-=-=-=-=[&cPandora Punish System&4]-=-=-=-=-=-=-="
        });
        if (receivers != null)
            Stream.of(receivers).forEach(p -> p.sendMessage(StringUtils.colorize(output)));
        else Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(StringUtils.colorize(output)));
    }

    public static void unpunishMessage(Player punisher, String target, String reason, Player... receivers) {
        final String output = StringUtils.fromBuilder(new String[]{
                target + " has been unpunished by " + punisher.getName() + " for " + reason
        });
        Stream.of(receivers).forEach(p -> UtilLogger.help(p, "Punish", output));
    }
}
