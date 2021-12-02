package dev.pandora.core.api.handler.reflectMethods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Zvijer on 14.3.2017..
 */
public abstract class ReflectionMethod {

    protected String toJSON(String input) {
        return "{\"text\":\"" + input + "\"}";
    }

    protected Class<?> getNMS(String className) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            return Class.forName("net.minecraft.server." + version + "." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendPacket(Player receiver, Object packet) {
        try {
            Object handle = receiver.getClass().getMethod("getHandle").invoke(receiver);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMS("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) { //Too many exceptions for me to care to catch them all
            e.printStackTrace();
        }
    }
}
