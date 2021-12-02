package dev.pandora.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Zvijer on 14.3.2017..
 */
public class UtilLogger {

    public static void log(String head, String body) {
        help(null, head, '>', body, true);
    }

    public static void broadcast(String head, String body) {
        Bukkit.getOnlinePlayers().forEach(p -> help(p, head, body));
    }

    public static void help(CommandSender caller, String head, String message) {
        help(caller, head, '>', message, true);
    }

    public static void help(CommandSender caller, String head, char transition, String message, boolean ignoreColor) {
        if (caller != null) {
            if (ignoreColor) {
                caller.sendMessage(ChatColor.GOLD + head + transition + " " + ChatColor.GRAY + message);
            } else {
                caller.sendMessage(StringUtils.colorize(head + transition) + " " + StringUtils.colorize(message));
            }
        } else {
            System.out.println(head + transition + " " + message);
        }
    }
}
