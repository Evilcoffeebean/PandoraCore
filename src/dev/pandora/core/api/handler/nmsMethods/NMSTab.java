package dev.pandora.core.api.handler.nmsMethods;

import dev.pandora.core.utils.StringUtils;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * Created by Zvijer on 24.3.2017..
 */
public class NMSTab {

    //Vengeancey

    private String header, footer;

    private String toJSON(String input) {
        return "{\"text\":\"" + input + "\"}";
    }

    public NMSTab(String header, String footer) {
        this.header = StringUtils.colorize(header);
        this.footer = StringUtils.colorize(footer);
    }

    //Using reflection will work for any post 1.8 version

    public void display(Player... receivers) {
        IChatBaseComponent h = ChatSerializer.a(toJSON(header));
        IChatBaseComponent f = ChatSerializer.a(toJSON(footer));
        ProtocolInjector.PacketTabHeader headerPacket = new ProtocolInjector.PacketTabHeader(h, f);
        Stream.of(receivers).forEach((p) -> {
            try {
                Field field = headerPacket.getClass().getDeclaredField("b");
                field.setAccessible(true);
                field.set(headerPacket, footer);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(headerPacket);
            }
        });
    }
}
