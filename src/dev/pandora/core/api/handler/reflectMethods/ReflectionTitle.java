package dev.pandora.core.api.handler.reflectMethods;

import dev.pandora.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

/**
 * Created by Zvijer on 13.3.2017..
 */
public class ReflectionTitle extends ReflectionMethod {

    private String title, subtitle;

    public ReflectionTitle(String title, String subtitle) {
        this.title = StringUtils.colorize(title);
        this.subtitle = StringUtils.colorize(subtitle);
    }

    public Object getTitlePacket() {
        try {
            return getNMS("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getSubtitlePacket() {
        try {
            return getNMS("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getTitleComponent() {
        try {
            return getNMS("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, toJSON(title));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getSubtitleComponent() {
        try {
            return getNMS("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, toJSON(subtitle));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Constructor<?> getConstructor() {
        try {
            return getNMS("PacketPlayOutTitle").getConstructor(getNMS("PacketPlayOutTitle").getDeclaredClasses()[0],
                    getNMS("IChatBaseComponent"), int.class, int.class, int.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendToAll() {
        send(null);
    }

    public void send(Player... receivers) {
        send(2, 7, 2, receivers);
    }

    public void send(int fadeIn, int duration, int fadeOut, Player... receivers) {
        try {
            Object t, s;
            t = getConstructor().newInstance(getTitlePacket(), getTitleComponent(), 20*fadeIn, 20*duration, 20*fadeOut);
            s = getConstructor().newInstance(getSubtitlePacket(), getSubtitleComponent(), 20*fadeIn, 20*duration, 20*fadeOut);
            if (receivers != null) {
                Stream.of(receivers).forEach(p -> {
                    sendPacket(p, t);
                    sendPacket(p, s);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
