package dev.pandora.core.api.handler.reflectMethods;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

/**
 * Created by Zvijer on 14.3.2017..
 */
public class ReflectionParticle extends ReflectionMethod {

    private String particleName;
    private float[] coords, offsets;
    private float data;
    private int amount;

    public ReflectionParticle(String name, float[] coords, float[] offsets, float data, int amount) {
        this.particleName = name;
        this.coords = coords;
        this.offsets = offsets;
        this.data = data;
        this.amount = amount;
    }

    public void send(Player... receivers) {
        try {
            Class<?> c = getNMS("PacketPlayOutWorldParticles");
            Constructor<?> constructor = c.getConstructor(String.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class);
            Object packet = constructor.newInstance(particleName, coords[0], coords[1], coords[2], offsets[0], offsets[1], offsets[2], data, amount);
            Stream.of(receivers).forEach(p -> sendPacket(p, packet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
