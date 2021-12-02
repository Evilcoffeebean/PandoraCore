package dev.pandora.core.utils;

import org.bukkit.ChatColor;

import java.util.stream.Stream;

/**
 * Created by Zvijer on 13.3.2017..
 */
public class StringUtils {

    public static String colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String fromArgs(String[] args, int beginIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = beginIndex; i < args.length; i++)
            sb.append(args[i]).append(" ");
        return sb.toString();
    }

    public static String fromBuilder(String[] array) {  //For multiple ban messages on the ban screen, per say
        StringBuilder sb = new StringBuilder();
        Stream.of(array).forEach(s -> sb.append(colorize(s)).append("\n"));
        return sb.toString();
    }
}
