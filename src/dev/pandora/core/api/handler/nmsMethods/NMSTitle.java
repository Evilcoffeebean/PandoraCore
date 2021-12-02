package dev.pandora.core.api.handler.nmsMethods;

import dev.pandora.core.utils.StringUtils;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;

import java.util.stream.Stream;

/**
 * Created by Zvijer on 21.3.2017..
 */
public class NMSTitle {

    private String title, subtitle;

    public NMSTitle(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return StringUtils.colorize(title);
    }

    public String getSubtitle() {
        return StringUtils.colorize(subtitle);
    }

    private String toJSON(String input) {
        return "{\"text\":\"" + input + "\"}";
    }

    public ProtocolInjector.PacketTitle getTitlePacket() {
        return new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE,
                ChatSerializer.a(toJSON(getTitle())));
    }

    public ProtocolInjector.PacketTitle getSubtitlePacket() {
        return new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE,
                ChatSerializer.a(toJSON(getSubtitle())));
    }

    public void send() {
        send(null);
    }

    //So we can send the titles to the entire server, or just a group of players, or a single player
    public void send(Player... receivers) {
        if (receivers == null) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(getTitlePacket());
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(getSubtitlePacket());
            });
        } else {
            Stream.of(receivers).forEach(p -> {
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(getTitlePacket());
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(getSubtitlePacket());
            });
        }
    }
}
