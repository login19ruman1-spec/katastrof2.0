package dev.disasterpanel.listener;

import dev.disasterpanel.DisasterPanel;
import dev.disasterpanel.gui.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class DisasterListener implements Listener {
    
    private final DisasterPanel plugin;
    
    public DisasterListener(DisasterPanel plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.contains("Панель Управления") || 
            title.contains("Землетрясение") || 
            title.contains("Метеорит") || 
            title.contains("Вулкан")) {
            event.setCancelled(true);
        }
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        String name = clickedItem.getItemMeta().getDisplayName();
        
        // Главное меню
        if (title.contains("Панель Управления")) {
            handleMainMenuClick(player, name);
        }
        // Землетрясение
        else if (title.contains("Землетрясение")) {
            handleEarthquakeClick(player, name);
        }
        // Метеорит
        else if (title.contains("Метеорит")) {
            handleMeteoriteClick(player, name);
        }
        // Вулкан
        else if (title.contains("Вулкан")) {
            handleVolcanoClick(player, name);
        }
    }
    
    private void handleMainMenuClick(Player player, String name) {
        if (name.contains("ЗЕМЛЕТРЯСЕНИЕ")) {
            EarthquakeGUI.open(player);
        } else if (name.contains("МЕТЕОРИТ")) {
            MeteoriteGUI.open(player);
        } else if (name.contains("ВУЛКАН")) {
            VolcanoGUI.open(player);
        } else if (name.contains("Закрыть")) {
            player.closeInventory();
        }
    }
    
    private void handleEarthquakeClick(Player player, String name) {
        if (name.contains("Уровень")) {
            try {
                String levelStr = name.replaceAll("[^0-9]", "");
                int level = Integer.parseInt(levelStr);
                plugin.getEarthquake().start(player.getLocation(), level);
                player.sendMessage("§a⚡ Землетрясение уровня " + level + " начато!");
                player.closeInventory();
            } catch (NumberFormatException ignored) {}
        } else if (name.contains("ОСТАНОВИТЬ")) {
            plugin.getEarthquake().stop();
            player.sendMessage("§cЗемлетрясение остановлено!");
            player.closeInventory();
        } else if (name.contains("НАЗАД")) {
            MainGUI.open(player);
        }
    }
    
    private void handleMeteoriteClick(Player player, String name) {
        if (name.contains("ЗАПУСТИТЬ")) {
            plugin.getMeteorite().start(player.getLocation());
            player.sendMessage("§6☄ Метеорит запущен!");
            player.closeInventory();
        } else if (name.contains("ОСТАНОВИТЬ")) {
            plugin.getMeteorite().stop();
            player.sendMessage("§cМетеорит остановлен!");
            player.closeInventory();
        } else if (name.contains("НАЗАД")) {
            MainGUI.open(player);
        }
    }
    
    private void handleVolcanoClick(Player player, String name) {
        if (name.contains("ИЗВЕРГНУТЬ")) {
            plugin.getVolcano().start(player.getLocation());
            player.sendMessage("§c🌋 Вулкан начал извержение!");
            player.closeInventory();
        } else if (name.contains("ОСТАНОВИТЬ")) {
            plugin.getVolcano().stop();
            player.sendMessage("§cВулкан остановлен!");
            player.closeInventory();
        } else if (name.contains("НАЗАД")) {
            MainGUI.open(player);
        }
    }
    
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        String title = event.getView().getTitle();
        if (title.contains("Панель Управления") || 
            title.contains("Землетрясение") || 
            title.contains("Метеорит") || 
            title.contains("Вулкан")) {
            event.setCancelled(true);
        }
    }
}
