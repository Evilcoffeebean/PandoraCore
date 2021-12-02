package dev.pandora.core.events;

import dev.pandora.core.api.networking.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zvijer on 31.3.2017..
 */
public class PlayerChat implements Listener {

    private DatabaseManager manager;

    public PlayerChat(DatabaseManager manager) {
        this.manager = manager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent e) {
        String tag = manager.getRank(e.getPlayer()).getTag(true);
        final String FORMAT = tag + e.getPlayer().getName() + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " » " + ChatColor.GRAY + e.getMessage();
        e.setFormat(FORMAT);
        List<Player> playersInMessage = usersInChat(e.getMessage());
        for(Player p : playersInMessage){
            e.getRecipients().remove(p);
            p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.0F);
            int begin = FORMAT.indexOf(ChatColor.BOLD + " » ");
            p.sendMessage(FORMAT.substring(0,begin)+ FORMAT.substring(begin).replace(p.getName(),ChatColor.AQUA+p.getName()+ChatColor.GRAY));
        }
    }

    private List<Player> usersInChat(String message){
        List<Player> playersInMessage = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            String name = p.getName();
            if(message.toLowerCase().contains(name.toLowerCase())){
                playersInMessage.add(p);
            }

        }
        return playersInMessage;

    }
}
