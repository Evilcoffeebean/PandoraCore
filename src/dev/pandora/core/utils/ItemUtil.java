package dev.pandora.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zvijer on 17.3.2017..
 */
public class ItemUtil {

    private Material type;

    public ItemUtil(Material type) {
        this.type = type;
        register();
    }

    protected final void register() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            //Enchantment.registerEnchantment(new CustomGlow(type.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Material getType() {
        return type;
    }


    public ItemStack buildWithGlow(String name, String[] description, int amount) {
        ItemStack i = new ItemStack(getType(), amount);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(StringUtils.colorize(name));
        if (description != null) {
            List<String> lore = new ArrayList<>();
            for (String s : description) {
                lore.add(StringUtils.colorize(s));
            }
            im.setLore(lore);
            im.addEnchant(new CustomGlow(getType().getId()), 1, true);
            i.setItemMeta(im);
        } else {
            i.setItemMeta(im);
        }
        return i;
    }

    public ItemStack build(String name) {
        return build(name, null);
    }

    public ItemStack build(String name, String[] description) {
        return build(name, description, 1);
    }

    public ItemStack build(String name, String[] description, int amount) {
        ItemStack i = new ItemStack(getType(), amount);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(StringUtils.colorize(name));
        if (description != null) {
            List<String> lore = new ArrayList<>();
            for (String s : description) {
                lore.add(StringUtils.colorize(s));
            }
            im.setLore(lore);
            i.setItemMeta(im);
        } else {
            i.setItemMeta(im);
        }
        return i;
    }

    public ItemStack playerHead(OfflinePlayer target, String name, String[] description) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(target.getName());
        meta.setDisplayName((name != null) ? StringUtils.colorize(name) : ChatColor.YELLOW + target.getName());
        if (description != null) {
            List<String> lore = new ArrayList<>();
            for (String s : description) {
                lore.add(StringUtils.colorize(s));
            }
            meta.setLore(lore);
            skull.setItemMeta(meta);
        } else {
            skull.setItemMeta(meta);
        }
        return skull;
    }
}
