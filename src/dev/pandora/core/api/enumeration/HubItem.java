package dev.pandora.core.api.enumeration;

import dev.pandora.core.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum HubItem {

    SERVER_SELECTOR(new ItemUtil(Material.WATCH).build("&aServer Selector"), 1),
    GAME_SELECTOR(new ItemUtil(Material.COMPASS).build("&aGame Selector"), 4),
    USER_PREFERENCES(new ItemUtil(Material.REDSTONE_COMPARATOR).build("&aUser Preferences"), 7);

    private ItemStack item;
    private int inventorySlot;

    HubItem(ItemStack item, int inventorySlot) {
        this.item = item;
        this.inventorySlot = inventorySlot;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }
}
