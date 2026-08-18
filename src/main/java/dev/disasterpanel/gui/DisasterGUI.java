package dev.disasterpanel.gui;

import dev.disasterpanel.DisasterPanel;
import dev.disasterpanel.disaster.EarthquakeDisaster;
import dev.disasterpanel.disaster.MeteoriteDisaster;
import dev.disasterpanel.disaster.VolcanoDisaster;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class DisasterGUI {
    
    private final DisasterPanel plugin;
    
    public DisasterGUI(DisasterPanel plugin) {
        this.plugin = plugin;
    }
    
    public void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.DARK_RED + "☣ " + ChatColor.RED + "Disaster Control" + ChatColor.DARK_RED + " ☣");
        
        // Glass panes for decoration
        ItemStack blackGlass = createGlassPane(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack grayGlass = createGlassPane(Material.GRAY_STAINED_GLASS_PANE);
        
        // Fill background
        for (int i = 0; i < 27; i++) {
            gui.setItem(i, grayGlass);
        }
        
        // Decorative borders
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, blackGlass);
            gui.setItem(i + 18, blackGlass);
        }
        
        // Earthquake button
        ItemStack earthquakeItem = createMenuItem(
            Material.CRACKED_STONE_BRICKS,
            ChatColor.RED + "⚡ EARTHQUAKE",
            Arrays.asList(
                ChatColor.GRAY + "7 Intensity Levels",
                ChatColor.GRAY + "Sound + Shake + Fissures",
                ChatColor.GOLD + "Click to configure",
                "",
                ChatColor.DARK_RED + "⚠" + ChatColor.RED + " Level 5-7: Fissures appear"
            )
        );
        gui.setItem(11, earthquakeItem);
        
        // Meteorite button
        ItemStack meteoriteItem = createMenuItem(
            Material.FIRE_CHARGE,
            ChatColor.RED + "☄ METEORITE",
            Arrays.asList(
                ChatColor.GRAY + "Drop from Y=2000",
                ChatColor.GRAY + "Explosion + Fire + Danger Zone",
                ChatColor.GOLD + "Click to configure",
                "",
                ChatColor.RED + "🔥" + ChatColor.GOLD + " 60s Fire Duration"
            )
        );
        gui.setItem(13, meteoriteItem);
        
        // Volcano button
        ItemStack volcanoItem = createMenuItem(
            Material.MAGMA_BLOCK,
            ChatColor.RED + "🌋 VOLCANO ERUPTION",
            Arrays.asList(
                ChatColor.GRAY + "Lava Rise + Fall",
                ChatColor.GRAY + "Obsidian Formation",
                ChatColor.GOLD + "Click to configure",
                "",
                ChatColor.DARK_RED + "⚠" + ChatColor.RED + " Break Obsidian to Restart"
            )
        );
        gui.setItem(15, volcanoItem);
        
        // Stop button
        ItemStack stopItem = createMenuItem(
            Material.BARRIER,
            ChatColor.RED + "✖ STOP ALL DISASTERS",
            Arrays.asList(
                ChatColor.GRAY + "Stop all active disasters",
                ChatColor.DARK_RED + "⚠" + ChatColor.RED + " Cannot be undone!"
            )
        );
        gui.setItem(26, stopItem);
        
        player.openInventory(gui);
    }
    
    private ItemStack createMenuItem(Material material, String name, java.util.List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
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
