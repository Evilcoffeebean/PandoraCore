package dev.pandora.core.api.networking.database;

import dev.pandora.core.api.enumeration.Rank;
import dev.pandora.core.punish.PunishTable;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Zvijer on 30.3.2017..
 */
public class DatabaseManager {

    //vengeanceys revamp

    //server credentials: "149.56.243.156", 3306, "fbd2918c_RfrHz15", "1jUjfKnRddQEqvaS", "s15_core"
    //local credentials: "localhost", 3306, "root", "", "pandora_db"
    private static final MySQL DB_PATH = new MySQL("149.56.243.156", 3306, "fbd2918c_RfrHz15", "1jUjfKnRddQEqvaS", "s15_core");
    private PunishTable punish;

    public DatabaseManager() {
        establishConnection();
    }

    public PunishTable getPunishTable() {
        return punish != null ? punish : new PunishTable(DB_PATH);
    }

    private final void bootPunishments() {
        punish = new PunishTable(DB_PATH);
        punish.buildPunishmentTable();
    }

    private final void establishConnection() {
        DB_PATH.openConnection();
        buildTables();
        bootPunishments();
        UtilLogger.log("Database", "Successfully built the required DB structure.");
    }

    private final void buildTables() {
        try {
            if (!DB_PATH.checkConnection()) DB_PATH.openConnection();
            String tableQuery = "CREATE TABLE IF NOT EXISTS accounts " +
                    "(id INT NOT NULL AUTO_INCREMENT, " +
                    "uuid MEDIUMTEXT, " +
                    "coins INT, " +
                    "rubies INT, " +
                    "rank TINYTEXT, " +
                    "PRIMARY KEY(id));";
            DB_PATH.getConnection().createStatement().executeUpdate(tableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isRegistered(Player player) {
        final String UUID = player.getUniqueId().toString();
        try {
            if (!DB_PATH.checkConnection()) DB_PATH.openConnection();
            String query = "SELECT * FROM accounts WHERE uuid='" + UUID + "';";
            ResultSet rs = DB_PATH.getConnection().createStatement().executeQuery(query);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerUser(Player player) {
        final String UUID = player.getUniqueId().toString();
        try {
            if (!DB_PATH.checkConnection()) DB_PATH.openConnection();
            String query = "INSERT INTO accounts (uuid, coins, rubies, rank) " +
                    "VALUES ('" + UUID + "', '250', '0', '" + Rank.ALL + "');";
            DB_PATH.getConnection().createStatement().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCoins(Player player) {
        final String UUID = player.getUniqueId().toString();
        try {
            if (!DB_PATH.checkConnection()) DB_PATH.openConnection();
            String query = "SELECT coins FROM accounts WHERE uuid='" + UUID + "'";
            ResultSet rs = DB_PATH.getConnection().createStatement().executeQuery(query);
            return rs.next() ? rs.getInt("coins") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setCoins(Player player, int amount) {
        final String UUID = player.getUniqueId().toString();
        try {
            if (!DB_PATH.checkConnection()) DB_PATH.openConnection();
            String query = "UPDATE accounts SET coins='" + amount + "' WHERE uuid='" + UUID + "';";
            DB_PATH.getConnection().createStatement().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getRubies(Player player) {
        final String UUID = player.getUniqueId().toString();
        try {
            if (!DB_PATH.checkConnection()) DB_PATH.openConnection();
            String query = "SELECT rubies FROM accounts WHERE uuid='" + UUID + "'";
            ResultSet rs = DB_PATH.getConnection().createStatement().executeQuery(query);
            return rs.next() ? rs.getInt("rubies") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setRubies(Player player, int amount) {
        final String UUID = player.getUniqueId().toString();
        try {
            if (!DB_PATH.checkConnection()) DB_PATH.openConnection();
            String query = "UPDATE accounts SET rubies='" + amount + "' WHERE uuid='" + UUID + "';";
            DB_PATH.getConnection().createStatement().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Rank getRank(Player player) {
        final String UUID = player.getUniqueId().toString();
        try {
            if (!DB_PATH.checkConnection()) DB_PATH.openConnection();
            String query = "SELECT rank FROM accounts WHERE uuid='" + UUID + "';";
            ResultSet rs = DB_PATH.getConnection().createStatement().executeQuery(query);
            return rs.next() ? Rank.valueOf(rs.getString("rank")) : Rank.ALL;
        } catch (SQLException e) {
            e.printStackTrace();
            return Rank.ALL;
        }
    }

    public void setRank(Player player, Rank rank) {
        final String UUID = player.getUniqueId().toString();
        try {
            if (!DB_PATH.checkConnection()) DB_PATH.openConnection();
            String query = "UPDATE accounts SET rank='" + rank + "' WHERE uuid='" + UUID + "';";
            DB_PATH.getConnection().createStatement().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
