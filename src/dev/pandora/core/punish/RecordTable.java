package dev.pandora.core.punish;


import dev.pandora.core.Main;
import dev.pandora.core.api.networking.SQLTYPE;
import dev.pandora.core.api.networking.Table;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Synch on 2017-04-10.
 */
public class RecordTable {
    private Table recordTable;
    private HashMap<OfflinePlayer,List<List<String>>> cachedUsers;
    public RecordTable(){
        recordTable = new Table("recordPunishments");
        init();
        try {
            recordTable.closeConnection();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void init(){
        try {
            recordTable.addSection("uuid", SQLTYPE.TEXT);
            recordTable.addSection("punisher",SQLTYPE.TEXT);
            recordTable.addSection("type",SQLTYPE.TEXT);
            recordTable.addSection("reason",SQLTYPE.TEXT);
            recordTable.addSection("duration",SQLTYPE.TEXT);
            recordTable.addSection("date",SQLTYPE.TEXT);
        }
        catch (SQLException e){
            e.printStackTrace();
            Main.get().getLogger().info("PandoraNetwork Failed to initialize on-record punishments.");
        }
        cachedUsers = new HashMap<>();
    }

    public synchronized void recordPunishment(Player p, OfflinePlayer target, PunishType punishType, int duration, String reason){
        String date = new SimpleDateFormat("EEE, MM, d").format(new Date());
        try {
            recordTable.openConnection();
            recordTable.getConnection().createStatement().executeUpdate(String.format("INSERT INTO %s (uuid,punisher,type,reason,duration,date) values('"+"%s"+"','"+"%s"+"','"+"%s"+"','"+"%s"+"','"+"%s"+"','"+"%s"+"')",recordTable.getTable(),target.getUniqueId(),p.getName(),punishType,reason,duration,date));
            ResultSet r = recordTable.getQuery(String.format("SELECT * FROM %s WHERE uuid='"+"%s"+"' ORDER BY id DESC",recordTable.getTable(),target.getUniqueId().toString(),recordTable.getTable()));
            cacheUser(target,r);
            recordTable.closeConnection();
        }
        catch (SQLException e){
            e.printStackTrace();
            Main.get().getLogger().info("PandoraNetwork Failed to record a punishment.");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
            Main.get().getLogger().info("An invalid Database Driver is being used!");
        }
    }

    public synchronized void queryPlayer(CommandSender searcher, OfflinePlayer player){
        try {
            List<List<String>> punishments = cachedUsers.get(player);
            if(punishments==null) {
                recordTable.openConnection();
                ResultSet r = recordTable.getQuery(String.format("SELECT * FROM %s WHERE uuid='" + "%s" + "' ORDER BY id DESC", recordTable.getTable(), player.getUniqueId().toString(), recordTable.getTable()));
                cacheUser(player,r);
                punishments = cachedUsers.get(player);
                recordTable.closeConnection();
            }
            if(!punishments.isEmpty()) {
                searcher.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "|" + player.getName() + "'s Punishment Record|");
                for (Iterator<List<String>> itr = punishments.iterator(); itr.hasNext(); ) {
                    List<String> punishmentDetails = itr.next();
                    searcher.sendMessage(ChatColor.GOLD + "--" + punishmentDetails.get(0) + "--");
                    searcher.sendMessage(ChatColor.RED + "DATE: " + ChatColor.YELLOW + punishmentDetails.get(1));
                    searcher.sendMessage(ChatColor.RED + "REASON: " + ChatColor.YELLOW + punishmentDetails.get(2));
                    searcher.sendMessage(ChatColor.RED + "PUNISHED BY: " + ChatColor.YELLOW + punishmentDetails.get(3));
                    searcher.sendMessage(ChatColor.RED + "SENTENCE: " + ChatColor.YELLOW + punishmentDetails.get(4) + " HOUR(s)");
                }
            }

            else{
                searcher.sendMessage(ChatColor.GOLD+"That player seems to have nothing on his record.");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private synchronized void cacheUser(OfflinePlayer p, ResultSet punishments){
        List<List<String>> punishmentSentences = new ArrayList<>();
        try {
            while (punishments.next()){
                List<String> punishmentDetails = new ArrayList<>();
                punishmentDetails.add(punishments.getString("type"));
                punishmentDetails.add(punishments.getString("date"));
                punishmentDetails.add(punishments.getString("reason"));
                punishmentDetails.add(punishments.getString("punisher"));
                punishmentDetails.add(punishments.getString("duration"));
                punishmentSentences.add(punishmentDetails);
            }
            punishments.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        cachedUsers.put(p,punishmentSentences);
    }

}
