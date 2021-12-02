package dev.pandora.core;

import dev.pandora.core.api.networking.Bungeecord;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.commands.*;
import dev.pandora.core.events.CommandProcess;
import dev.pandora.core.events.PlayerChat;
import dev.pandora.core.events.PlayerJoin;
import dev.pandora.core.events.Random;
import dev.pandora.core.punish.RecordTable;
import dev.pandora.core.punish.commands.*;
import dev.pandora.core.punish.events.PunishLogin;
import dev.pandora.core.punish.events.PunishSpeak;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

/**
 * Created by Synch on 2017-03-13.
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private DatabaseManager manager;
    private RecordTable punishments;
    //private ProtocolManager protocolManager;

    public static Main get(){
        return instance;
    }

    /*

    Utilize protocol entry?

    public void setupAdapter() throws NoClassDefFoundError {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.protocolManager.addPacketListener(
                new PacketAdapter(this, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Server.TAB_COMPLETE}) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if ((event.getPacketType() == PacketType.Play.Server.TAB_COMPLETE)
                                && !event.getPlayer().isOp()) {
                            event.setCancelled(true);
                        }
                    }
                }
        );
    }

    */

    public DatabaseManager getManager() {
        return manager != null ? manager : new DatabaseManager();
    }

    public RecordTable getPunishments(){
        if(punishments==null){
            punishments = new RecordTable();
            return punishments;
        }
        else{
            return punishments;
        }
    }

    public void onEnable(){
        instance = this;
        manager = new DatabaseManager();
        //Register the events
        register(new PlayerJoin(manager), new Random(manager), new PlayerChat(manager), new CommandProcess());
        register(new PunishLogin(manager.getPunishTable()), new PunishSpeak(manager.getPunishTable()));
        //Instantiate the command classes
        new AnnounceCommand(manager);
        new RankCommand(manager);
        new CoinCommand(manager);
        new RubyCommand(manager);
        new SpawnCommand(manager);
        new MsgCommand(manager);
        new GameModeCommand(manager);
        new BanCommand(manager);
        new UnbanCommand(manager);
        new MuteCommand(manager);
        new UnmuteCommand(manager);
        new KickCommand(manager);
        new ReportCommand(manager);
        new LookupCommand(manager);
        //Bungee
        registerBungee();
    }

    private void register(Listener... events) {
        if (events == null) throw new NullPointerException("No events found.");
        Stream.of(events).forEach(l -> getServer().getPluginManager().registerEvents(l, this));
    }

    private void registerBungee() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Bungeecord());
        Bungeecord.updater();
    }
}
