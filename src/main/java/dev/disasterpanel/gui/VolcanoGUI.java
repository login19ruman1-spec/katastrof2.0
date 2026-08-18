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

public class VolcanoGUI {
    
    private final DisasterPanel plugin;
    
    public VolcanoGUI(DisasterPanel plugin) {
        this.plugin = plugin;
    }
    
    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "🌋 VOLCANO CONTROL 🌋");
        
        // Background decoration
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, createGlassPane(Material.BLACK_STAINED_GLASS_PANE));
        }
        
        // Title and info
        gui.setItem(4, createInfoItem(
            Material.MAGMA_BLOCK,
            ChatColor.RED + "🌋 VOLCANO SYSTEM",
            Arrays.asList(
                ChatColor.GRAY + "Lava rise and fall",
                ChatColor.GRAY + "Obsidian formation",
                ChatColor.DARK_RED + "⚠ Break obsidian = Re-eruption (2min)"
            )
        ));
        
        // Lava levels
        gui.setItem(19, createLavaLevelItem(
            Material.LAVA_BUCKET,
            ChatColor.GOLD + "🌋 LAVA RISE",
            "Rise Level: " + ChatColor.YELLOW + "20 blocks"
        ));
        
        gui.setItem(20, createLavaLevelItem(
            Material.WATER_BUCKET,
            ChatColor.AQUA + "⬇ LAVA FALL",
            "Fall Level: " + ChatColor.AQUA + "10 blocks"
        ));
        
        // Eruption timer
        gui.setItem(28, createTimerItem(
            Material.CLOCK,
            ChatColor.YELLOW + "⏰ ERUPTION DELAY",
            "Delay: " + ChatColor.GREEN + "10s"
        ));
        
        // Obsidian warning
        gui.setItem(37, createObsidianItem(
            Material.OBSIDIAN,
            ChatColor.DARK_RED + "⚠ OBSIDIAN TRAP",
            "Break obsidian to trigger re-eruption"
        ));
        
        // Control buttons
        gui.setItem(48, createControlItem(
            Material.GREEN_CONCRETE,
            ChatColor.GREEN + "🌋 ERUPT",
            "Start volcanic eruption"
        ));
        
        gui.setItem(49, createControlItem(
            Material.RED_CONCRETE,
            ChatColor.RED + "■ STOP",
            "Stop volcano"
        ));
        
        gui.setItem(50, createControlItem(
            Material.OAK_DOOR,
            ChatColor.YELLOW + "↩ BACK",
            "Return to main menu"
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
    
    private ItemStack createLavaLevelItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createTimerItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createObsidianItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
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
