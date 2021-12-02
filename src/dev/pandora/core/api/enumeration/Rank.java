package dev.pandora.core.api.enumeration;

import org.bukkit.ChatColor;

/**
 * Created by Zvijer on 14.3.2017..
 */
public enum Rank {

    //Vengeancey

    ALL("", ChatColor.GRAY),
    //Donator

    DONOR("Donor", ChatColor.LIGHT_PURPLE),
    PANDORA("Pandora", ChatColor.DARK_PURPLE),
    PANDORA_PLUS("Pandora+", ChatColor.BLUE),

    //Staff hierarchy
    YOUTUBE("YouTube", ChatColor.RED, true),
    TWITCH("Twitch", ChatColor.DARK_PURPLE, true),
    FAMOUS("Famous", ChatColor.GREEN, true),
    TRIAL_UHC("T.Uhc", ChatColor.YELLOW, true),
    TRIAL_MOD("T.Mod", ChatColor.YELLOW, true),
    BUILDER("Builder", ChatColor.GREEN, true),
    MOD("Mod", ChatColor.GOLD, ChatColor.YELLOW, true),
    UHC_STAFF("Uhc", ChatColor.LIGHT_PURPLE, true),
    SNR_UHC("Sr.Uhc", ChatColor.DARK_PURPLE, true),
    SNR_MOD("Sr.Mod", ChatColor.GOLD, true),
    DEV("Dev", ChatColor.DARK_RED, ChatColor.RED, true),
    ADMIN("Admin", ChatColor.DARK_RED, ChatColor.RED, true),
    OWNER("Owner", ChatColor.DARK_RED, true);

    private String name;
    private ChatColor tagColor, nameColor;
    private boolean staff;

    Rank(String name, ChatColor tagColor) {
        this(name, tagColor, false);
    }

    Rank(String name, ChatColor tagColor, boolean staff) {
        this(name, tagColor, null, staff);
    }

    Rank(String name, ChatColor tagColor, ChatColor nameColor, boolean staff) {
        this.name = name;
        this.tagColor = tagColor;
        this.nameColor = (nameColor != null) ? nameColor : tagColor;
        this.staff = staff;
    }

    public boolean has(Rank rank) {
        return has(rank, null);
    }

    public boolean has(Rank rank, Rank[] specific) {
        if (specific != null) {
            for (Rank r : specific) {
                if (compareTo(r) == 0) {
                    return true;
                }
            }
        }
        return compareTo(rank) >= 0;
    }

    public String getTag(boolean space) {
        if (this == ALL) return getTagColor().toString();
        return getTagColor().toString()
                + "["
                + getName()
                + "]"
                + (space ? " " : "")
                + getNameColor().toString();
    }

    public String getName() {
        return name;
    }

    public ChatColor getTagColor() {
        return tagColor;
    }

    public ChatColor getNameColor() {
        return nameColor;
    }

    public boolean isStaff() {
        return staff;
    }
}
