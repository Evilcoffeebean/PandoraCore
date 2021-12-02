package dev.pandora.core.api.networking;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.pandora.core.Main;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Waffle on 13/03/2017.
 */
public class Bungeecord implements PluginMessageListener {
    public static ArrayList<String> globalPList = new ArrayList<String>();
    public static HashMap<String, Integer> pSizes = new HashMap<String, Integer>();
    public static Map<UUID, String> serverCache = Maps.newHashMap();
    protected String[] allServers = new String[] {};

    public Bungeecord() {
        serverCache.forEach((uuid, string) -> serverCache.clear());
    }

    public String[] getServers() {
        return allServers;
    }

    public ArrayList<String> getAllPlayers() {
        return globalPList;
    }

    public void connect(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(Main.get(), "BungeeCord", out.toByteArray());
    }

    public void sendBungeeMessage(String player, String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Message");
        out.writeUTF(player);
        out.writeUTF(message);
        Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(Main.get(), "BungeeCord", out.toByteArray());
    }

    public void kickBungeePlayer(String player, String reason) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickPlayer");
        out.writeUTF(player);
        out.writeUTF(reason);
        Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(Main.get(), "BungeeCord", out.toByteArray());
    }

    public int getBungeeCount(String server) throws Exception {
        if(server.equalsIgnoreCase("all")) {
            int size = 0;
            for(String ls : pSizes.keySet()) {
                size += pSizes.get(ls);
            } return size;
        } else {
            if(pSizes.get(server) == null) {
                return 0;
            } return pSizes.get(server);
        }
    }

    public String getCurrentServer(Player player) {
        String path = serverCache.get(player.getUniqueId());
        return path != null ? path : "Default Gateway";
    }

    public static void updater() {
        new BukkitRunnable() {
            public void run() {
                if(Bukkit.getOnlinePlayers().size() >= 1) {
                    ByteArrayDataOutput out1 = ByteStreams.newDataOutput();
                    out1.writeUTF("PlayerList");
                    out1.writeUTF("ALL");
                    Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(Main.get(), "BungeeCord", out1.toByteArray());

                    ByteArrayDataOutput out2 = ByteStreams.newDataOutput();
                    out2.writeUTF("GetServers");
                    Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(Main.get(), "BungeeCord", out2.toByteArray());
                }
            }
        }.runTaskTimer(Main.get(), 0, 20);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if(subchannel.equals("PlayerList")) {
            String server = in.readUTF();
            if(server.equals("ALL")) {
                String utf = in.readUTF();

                String[] globalPListS = new String[] {};
                globalPListS = utf.split(", ");

                globalPList = new ArrayList<String>();
                for(String gpls : globalPListS) {
                    globalPList.add(gpls);
                }
            }
        } else if(subchannel.equals("GetServers")) {
            allServers = in.readUTF().split(", ");

            for(String server : allServers) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("PlayerCount");
                out.writeUTF(server);
                Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(Main.get(), "BungeeCord", out.toByteArray());
            }
        } else if(subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            int count = in.readInt();
            pSizes.put(server, count);
        } else if (subchannel.equalsIgnoreCase("GetServer")) {
            String server = in.readUTF();
            serverCache.put(player.getUniqueId(), server);
            UtilLogger.log("Proxy", player.getName() + " cached in server " + getCurrentServer(player));
            serverCache.remove(player.getUniqueId());
        }
    }
}




