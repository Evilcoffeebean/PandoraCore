package dev.pandora.core.utils;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.Packet;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;

import java.util.stream.Stream;

/**
 * Created by Zvijer on 30.3.2017..
 */
public class ServerUtil {

    private static String toJSON(String input) {
        return "{\"text\":\"" + input + "\"}";
    }

    public static boolean find(Player target) {
        return target != null;
    }

    public static boolean cast(OfflinePlayer target) {
        return (target instanceof Player);
    }

    public static void sendPacket(Player player, Packet... packets) {
        Stream.of(packets).forEach(p -> ((CraftPlayer)player).getHandle().playerConnection.sendPacket(p));
    }

    public static void sendPacketToAll(Packet... packets) {
        Bukkit.getOnlinePlayers().forEach(p -> sendPacket(p, packets));
    }

    public static void setHeaderFooter(String top, String bottom) {
        IChatBaseComponent header, footer;
        header = ChatSerializer.a(toJSON(StringUtils.colorize(top)));
        footer = ChatSerializer.a(toJSON(StringUtils.colorize(bottom)));
        ProtocolInjector.PacketTabHeader packet = new ProtocolInjector.PacketTabHeader(header, footer);
        sendPacketToAll(packet);
    }
}
