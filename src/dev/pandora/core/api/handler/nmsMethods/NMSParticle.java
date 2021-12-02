package dev.pandora.core.api.handler.nmsMethods;

import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class NMSParticle {

    //Vengeancey

    private String particleType;
    private float[] coordinateData;
    private int speed, amount;

    public NMSParticle(String type, float[] coordinateData, int speed, int amount) {
        this.particleType = type;
        this.coordinateData = coordinateData;
        this.speed = speed;
        this.amount = amount;
    }

    public PacketPlayOutWorldParticles getParticlePacket() {
        return new PacketPlayOutWorldParticles(particleType,
                coordinateData[0], coordinateData[1], coordinateData[2], //offsets
                coordinateData[3], coordinateData[4], coordinateData[5], //xyzs
                speed, amount);
    }

    public void send(Player... receivers) {
        if (receivers != null) {
            Stream.of(receivers).forEach((p) -> {
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(getParticlePacket());
            });
        }
    }
}
