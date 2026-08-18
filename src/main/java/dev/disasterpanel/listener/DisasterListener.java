package dev.disasterpanel.listener;

import dev.disasterpanel.DisasterPanel;
import dev.disasterpanel.gui.DisasterGUI;
import dev.disasterpanel.gui.EarthquakeGUI;
import dev.disasterpanel.gui.MeteoriteGUI;
import dev.disasterpanel.gui.VolcanoGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class DisasterListener implements Listener {
    
    private final DisasterPanel plugin;
    private final DisasterGUI mainGUI;
    private final EarthquakeGUI earthquakeGUI;
    private final MeteoriteGUI meteoriteGUI;
    private final VolcanoGUI volcanoGUI;
    
    public DisasterListener(DisasterPanel plugin) {
        this.plugin = plugin;
        this.mainGUI = new DisasterGUI(plugin);
        this.earthquakeGUI = new EarthquakeGUI(plugin);
        this.meteoriteGUI = new MeteoriteGUI(plugin);
        this.volcanoGUI = new VolcanoGUI(plugin);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        // Cancel click if it's one of our GUIs
        if (title.contains("Disaster Control") || 
            title.contains("EARTHQUAKE") || 
            title.contains("METEORITE") || 
            title.contains("VOLCANO")) {
            event.setCancelled(true);
        }
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Handle main menu clicks
        if (title.contains("Disaster Control")) {
            handleMainMenuClick(player, clickedItem);
        }
        // Handle earthquake GUI clicks
        else if (title.contains("EARTHQUAKE")) {
            handleEarthquakeClick(player, clickedItem);
        }
        // Handle meteorite GUI clicks
        else if (title.contains("METEORITE")) {
            handleMeteoriteClick(player, clickedItem);
        }
        // Handle volcano GUI clicks
        else if (title.contains("VOLCANO")) {
            handleVolcanoClick(player, clickedItem);
        }
    }
    
    private void handleMainMenuClick(Player player, ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        
        if (name.contains("EARTHQUAKE")) {
            earthquakeGUI.open(player);
        } else if (name.contains("METEORITE")) {
            meteoriteGUI.open(player);
        } else if (name.contains("VOLCANO")) {
            volcanoGUI.open(player);
        } else if (name.contains("STOP ALL")) {
            plugin.getEarthquakeDisaster().stop();
            plugin.getMeteoriteDisaster().stop();
            plugin.getVolcanoDisaster().stop();
            player.sendMessage(ChatColor.GREEN + "All disasters stopped!");
            player.closeInventory();
        }
    }
    
    private void handleEarthquakeClick(Player player, ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        
        if (name.contains("Level")) {
            try {
                String[] parts = name.split(" ");
                int level = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
                plugin.getEarthquakeDisaster().start(player.getLocation(), level);
                player.sendMessage(ChatColor.GREEN + "Earthquake level " + level + " started!");
            } catch (NumberFormatException ignored) {}
        } else if (name.contains("START")) {
            plugin.getEarthquakeDisaster().start(player.getLocation(), 4);
            player.sendMessage(ChatColor.GREEN + "Earthquake started!");
        } else if (name.contains("STOP")) {
            plugin.getEarthquakeDisaster().stop();
            player.sendMessage(ChatColor.RED + "Earthquake stopped!");
        } else if (name.contains("BACK")) {
            mainGUI.openMainMenu(player);
        }
    }
    
    private void handleMeteoriteClick(Player player, ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        
        if (name.contains("LAUNCH")) {
            plugin.getMeteoriteDisaster().start(player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Meteorite launched!");
        } else if (name.contains("STOP")) {
            plugin.getMeteoriteDisaster().stop();
            player.sendMessage(ChatColor.RED + "Meteorite stopped!");
        } else if (name.contains("BACK")) {
            mainGUI.openMainMenu(player);
        }
    }
    
    private void handleVolcanoClick(Player player, ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        
        if (name.contains("ERUPT")) {
            plugin.getVolcanoDisaster().start(player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Volcano eruption started!");
        } else if (name.contains("STOP")) {
            plugin.getVolcanoDisaster().stop();
            player.sendMessage(ChatColor.RED + "Volcano stopped!");
        } else if (name.contains("BACK")) {
            mainGUI.openMainMenu(player);
        }
    }
    
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        String title = event.getView().getTitle();
        if (title.contains("Disaster Control") || 
            title.contains("EARTHQUAKE") || 
            title.contains("METEORITE") || 
            title.contains("VOLCANO")) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Cleanup any player-specific data if needed
    }
}
