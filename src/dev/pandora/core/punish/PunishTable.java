package dev.pandora.core.punish;

import dev.pandora.core.api.networking.database.MySQL;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Zvijer on 2.4.2017..
 */
public class PunishTable {

    private MySQL db;

    public PunishTable(MySQL db) {
        this.db = db;
    }

    public void buildPunishmentTable() {
        try {
            if (!db.checkConnection()) db.openConnection();
            String tableQuery = "CREATE TABLE IF NOT EXISTS punishments " +
                    "(" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "uuid MEDIUMTEXT, " +
                    "punisher TINYTEXT, " +
                    "type TINYTEXT, " +
                    "reason MEDIUMTEXT, " +
                    "duration LONG, " +
                    "date TINYTEXT, " +
                    "PRIMARY KEY(id)" +
                    ");";
            String eventQuery = "SET GLOBAL EVENT_SCHEDULER='ON';";
            db.getConnection().createStatement().executeUpdate(tableQuery);
            db.getConnection().createStatement().executeUpdate(eventQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void permBanPlayer(Player punisher, OfflinePlayer target, String reason) {
        punishPlayer(punisher, target, PunishType.BAN, reason, 9999);
    }

    public void permMutePlayer(Player punisher, OfflinePlayer target, String reason) {
        punishPlayer(punisher, target, PunishType.MUTE, reason, 9999);
    }

    public void banPlayer(Player punisher, OfflinePlayer target, String reason, long duration) {
        punishPlayer(punisher, target, PunishType.BAN, reason, duration);
    }

    public void mutePlayer(Player punisher, OfflinePlayer target, String reason, long duration) {
        punishPlayer(punisher, target, PunishType.MUTE, reason, duration);
    }

    public void warnPlayer(Player punisher, OfflinePlayer target, String reason) {
        punishPlayer(punisher, target, PunishType.WARN, reason, 0);
    }

    public void punishPlayer(Player punisher, OfflinePlayer target, PunishType type, String reason, long duration) {
        String date = new SimpleDateFormat("EEE, MM, d").format(new Date());
        PunishPlayerCache cache = new PunishPlayerCache(target.getUniqueId(), target.getName(), reason, type, duration, date);
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "INSERT INTO punishments (uuid, punisher, type, reason, duration, date) "
                        + "VALUES ("
                        + "'" + cache.getUUID() + "', "
                        + "'" + punisher.getName() + "', "
                        + "'" + cache.getPunishType() + "', "
                        + "'" + cache.getReason() + "', "
                        + "'" + cache.getDuration() + "', "
                        + "'" + cache.getDate() + "'"
                        + ");";
            db.getConnection().createStatement().executeUpdate(query);
            executeEvent(duration, target.getUniqueId().toString(), reason);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unpunishUser(String uuid, PunishType type) {
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "DELETE FROM punishments WHERE uuid='" + uuid + "' AND type='" + type + "';";
            db.getConnection().createStatement().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeEvent(long duration, String uuid, String reason) {
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "CREATE EVENT punishevent" + new Random().nextInt(1000)
                            + " ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL " + duration
                            + " HOUR DO DELETE FROM punishments WHERE uuid='" + uuid + "'"
                            + " AND reason='" + reason + "';";
            db.getConnection().createStatement().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isBanned(String uuid) {
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "SELECT * FROM punishments WHERE uuid='" + uuid + "' AND type='" + PunishType.BAN + "';";
            ResultSet rs = db.getConnection().createStatement().executeQuery(query);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isMuted(String uuid) {
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "SELECT * FROM punishments WHERE uuid='" + uuid + "' AND type='" + PunishType.MUTE + "';";
            ResultSet rs = db.getConnection().createStatement().executeQuery(query);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UUID getUUID(String uuid) {
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "SELECT uuid FROM punishments WHERE uuid='" + uuid + "';";
            ResultSet rs = db.getConnection().createStatement().executeQuery(query);
            return rs.next() ? UUID.fromString(rs.getString("uuid")) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPunisherName(String uuid) {
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "SELECT punisher FROM punishments WHERE uuid='" + uuid + "';";
            ResultSet rs = db.getConnection().createStatement().executeQuery(query);
            return rs.next() ? rs.getString("punisher") : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPunishType(String uuid) {
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "SELECT type FROM punishments WHERE uuid='" + uuid + "';";
            ResultSet rs = db.getConnection().createStatement().executeQuery(query);
            return rs.next() ? rs.getString("type") : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPunishReason(String uuid) {
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "SELECT reason FROM punishments WHERE uuid='" + uuid + "';";
            ResultSet rs = db.getConnection().createStatement().executeQuery(query);
            return rs.next() ? rs.getString("reason") : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long getPunishmentDuration(String uuid) {
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "SELECT duration FROM punishments WHERE uuid='" + uuid + "';";
            ResultSet rs = db.getConnection().createStatement().executeQuery(query);
            return rs.next() ? rs.getLong("duration") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getPunishDate(String uuid) {
        try {
            if (!db.checkConnection()) db.openConnection();
            String query = "SELECT date FROM punishments WHERE uuid='" + uuid + "';";
            ResultSet rs = db.getConnection().createStatement().executeQuery(query);
            return rs.next() ? rs.getString("date") : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
