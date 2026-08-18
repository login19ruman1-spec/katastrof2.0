package dev.disasterpanel.gui;

import dev.disasterpanel.DisasterPanel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EarthquakeGUI {
    
    private final DisasterPanel plugin;
    
    public EarthquakeGUI(DisasterPanel plugin) {
        this.plugin = plugin;
    }
    
    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "⚡ EARTHQUAKE CONTROL ⚡");
        
        // Background decoration
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, createGlassPane(Material.BLACK_STAINED_GLASS_PANE));
        }
        
        // Title and info
        gui.setItem(4, createInfoItem(
            Material.CRACKED_STONE_BRICKS,
            ChatColor.RED + "⚡ EARTHQUAKE SYSTEM",
            Arrays.asList(
                ChatColor.GRAY + "7 Intensity Levels",
                ChatColor.GREEN + "Level 1-4: Vibrations",
                ChatColor.RED + "Level 5-7: Fissures + Max Power"
            )
        ));
        
        // Intensity Slider (Level 1-7)
        for (int level = 1; level <= 7; level++) {
            String color = level <= 4 ? ChatColor.GREEN.toString() : ChatColor.RED.toString();
            Material material = level <= 4 ? Material.LEVER : Material.REDSTONE;
            
            ItemStack levelItem = createMenuItem(
                material,
                color + "Level " + level,
                Arrays.asList(
                    ChatColor.GRAY + "Intensity: " + color + level + "/7",
                    ChatColor.GRAY + "Radius: " + (level * 15) + " blocks",
                    ChatColor.GRAY + "Duration: " + (level * 5) + "s",
                    "",
                    ChatColor.YELLOW + "Click to activate level " + level
                )
            );
            gui.setItem(19 + (level - 1), levelItem);
        }
        
        // Control buttons
        gui.setItem(48, createControlItem(
            Material.GREEN_CONCRETE,
            ChatColor.GREEN + "▶ START",
            ChatColor.GRAY + "Start earthquake at current level"
        ));
        
        gui.setItem(49, createControlItem(
            Material.RED_CONCRETE,
            ChatColor.RED + "■ STOP",
            ChatColor.GRAY + "Stop earthquake"
        ));
        
        gui.setItem(50, createControlItem(
            Material.OAK_DOOR,
            ChatColor.YELLOW + "↩ BACK",
            ChatColor.GRAY + "Return to main menu"
        ));
        
        player.openInventory(gui);
    }
    
    private ItemStack createInfoItem(Material material, String name, java.util.List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createMenuItem(Material material, String name, java.util.List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createControlItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createGlassPane(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }
}
