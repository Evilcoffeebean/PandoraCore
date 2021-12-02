package dev.pandora.core.utils;

/**
 * Created by Zvijer on 3.4.2017..
 */
public class IntUtils {

    public static int parse(String[] args, int index) {
        try {
            return Integer.parseInt(args[index]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
