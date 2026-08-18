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

public class MeteoriteGUI {
    
    private final DisasterPanel plugin;
    
    public MeteoriteGUI(DisasterPanel plugin) {
        this.plugin = plugin;
    }
    
    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "☄ METEORITE CONTROL ☄");
        
        // Background decoration
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, createGlassPane(Material.BLACK_STAINED_GLASS_PANE));
        }
        
        // Title and info
        gui.setItem(4, createInfoItem(
            Material.FIRE_CHARGE,
            ChatColor.RED + "☄ METEORITE SYSTEM",
            Arrays.asList(
                ChatColor.GRAY + "Drop from Y=2000",
                ChatColor.GRAY + "Impact radius: 30 blocks",
                ChatColor.GOLD + "Fire duration: 60s",
                ChatColor.DARK_RED + "Danger zone: 60s"
            )
        ));
        
        // Height indicator
        String[] heights = {"2000", "1500", "1000", "500", "0"};
        for (int i = 0; i < heights.length; i++) {
            String color = i < 2 ? ChatColor.GREEN.toString() : (i < 4 ? ChatColor.YELLOW.toString() : ChatColor.RED.toString());
            Material material = i < 2 ? Material.GREEN_WOOL : (i < 4 ? Material.YELLOW_WOOL : Material.RED_WOOL);
            
            ItemStack heightItem = createMenuItem(
                material,
                color + "Height: " + heights[i],
                Arrays.asList(
                    ChatColor.GRAY + "Altitude: " + heights[i],
                    color + "Sound: " + (i < 2 ? "Low" : (i < 4 ? "Medium" : "MAX")),
                    ChatColor.GRAY + "Status: " + (i == heights.length - 1 ? ChatColor.RED + "IMPACT!" : ChatColor.GREEN + "Falling...")
                )
            );
            gui.setItem(19 + i, heightItem);
        }
        
        // Status indicators
        gui.setItem(28, createStatusItem(
            Material.ORANGE_WOOL,
            ChatColor.YELLOW + "📊 METEORITE STATUS",
            "Status: " + ChatColor.GREEN + "Ready"
        ));
        
        gui.setItem(37, createStatusItem(
            Material.RED_WOOL,
            ChatColor.RED + "🔥 DANGER ZONE",
            "Duration: 60s"
        ));
        
        // Control buttons
        gui.setItem(48, createControlItem(
            Material.GREEN_CONCRETE,
            ChatColor.GREEN + "☄ LAUNCH",
            "Start meteorite strike"
        ));
        
        gui.setItem(49, createControlItem(
            Material.RED_CONCRETE,
            ChatColor.RED + "■ STOP",
            "Stop meteorite"
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
    
    private ItemStack createMenuItem(Material material, String name, java.util.List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createStatusItem(Material material, String name, String... lore) {
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
