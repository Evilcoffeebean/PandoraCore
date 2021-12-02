package dev.pandora.core.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Zvijer on 17.3.2017..
 */
public class CustomGlow extends Enchantment {

    public CustomGlow(int id) {
        super(id);
    }

    @Override
    public boolean canEnchantItem(ItemStack i) {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment e) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }
}
