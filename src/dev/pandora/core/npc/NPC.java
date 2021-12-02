package dev.pandora.core.npc;

import dev.pandora.core.utils.StringUtils;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Zvijer on 7.4.2017..
 */
public class NPC {

    private Player player;
    private double x, y, z;
    private float pitch, yaw;
    private Location location;

    public NPC(Player player, double[] coords, float pitch, float yaw) {
        this.player = player;
        this.x = coords[0];
        this.y = coords[1];
        this.z = coords[2];
        this.pitch = pitch;
        this.yaw = yaw;
        this.location = new Location(player.getWorld(), x, y, z, pitch, yaw);
    }

    public void build(OfflinePlayer target, String name) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        UUID uuid = target.getUniqueId();
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(uuid, StringUtils.colorize(name)), new PlayerInteractManager(nmsWorld));
        Bukkit.getOnlinePlayers().forEach(p -> {
            PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(); //Test
            PacketPlayOutNamedEntitySpawn entitySpawn = new PacketPlayOutNamedEntitySpawn(npc);
            PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
            connection.sendPacket(playerInfo);
            connection.sendPacket(entitySpawn);
        });
    }
}
