package dev.pandora.core.punish;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Zvijer on 2.4.2017..
 */
public class PunishPlayerCache {

    private UUID uuid;
    private String name, reason;
    private PunishType type;
    private long duration;
    private String date;

    public PunishPlayerCache(UUID uuid, String name, String reason, PunishType type, long duration, String date) {
        this.uuid = uuid;
        this.name = name;
        this.reason = reason != null ? reason : "The ban hammer has spoken";
        this.type = type;
        this.duration = duration;
        this.date = date;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public PunishType getPunishType() {
        return type;
    }

    public long getDuration() {
        return duration;
    }

    public String getDate() {
        return new SimpleDateFormat("EEE, MM d").format(new Date());
    }

    public static String formatDuration(long duration) {
        if (duration >= 9999) {
            return "Permanent";
        } else {
            if (duration >= 24) {
                return (duration/24) + " Days";
            } else {
                return duration + " Hours";
            }
        }
    }
}
