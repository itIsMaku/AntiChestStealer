package cz.maku.anticheststealer.gui;

import cz.maku.anticheststealer.AntiChestStealer;
import lombok.Getter;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class ProtectionGUI implements Listener {

    int[] possibleRows = new int[]{1, 2, 3, 4};
    @Getter
    Inventory inventory;
    @Getter
    int slot;
    @Getter
    ItemStack item;

    public void create() {
        inventory = Bukkit.createInventory(null, 9 * possibleRows[new Random().nextInt(possibleRows.length)], RandomStringUtils.random(32));
        setItem();
    }

    private void setItem() {
        Random random = new Random();
        int[] possibleSlots = IntStream.rangeClosed(0, inventory.getSize()).toArray();
        slot = possibleSlots[random.nextInt(possibleSlots.length - 1)];
        item = getItemStack();
        inventory.setItem(slot, item);
    }

    private ItemStack getItemStack() {
        Random random = new Random();
        Material[] possibleMaterials = Arrays.stream(Material.values()).filter(Material::isEdible).toArray(Material[]::new);
        ItemStack stack = new ItemStack(possibleMaterials[random.nextInt(possibleMaterials.length)], 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomStringUtils.random(random.nextInt(32)));
        stack.setItemMeta(meta);
        return stack;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().getTitle().equals(inventory.getTitle())) {
                if (e.getSlot() == slot && e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                    Player cheater = (Player) e.getWhoClicked();
                    AntiChestStealer antiChestStealer = AntiChestStealer.getPlugin();
                    FileConfiguration config = antiChestStealer.getConfig();
                    for (String message : config.getStringList("detection-broadcast")) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{player}", cheater.getName()).replace("{version}", antiChestStealer.getDescription().getVersion())));
                    }
                    for (String command : config.getStringList("detection-commands")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', command.replace("{player}", cheater.getName()).replace("{version}", antiChestStealer.getDescription().getVersion())));
                    }
                }
            }
        }
    }
}
