package dev.disasterpanel.gui;

import dev.disasterpanel.DisasterPanel;
import dev.disasterpanel.disaster.EarthquakeDisaster;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EarthquakeGUI {

    public static void open(Player player) {
        String title = DisasterPanel.getInstance().getConfig().getString("gui.earthquake-title",
                "§8▸ §c⚡ §4Землетрясение §c⚡ §8◂");
        
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', title));

        // Фон
        ItemStack bg = createGlass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, bg);
        }

        // Заголовок
        ItemStack header = createItem(
                Material.CRACKED_STONE_BRICKS,
                "§c⚡ §lУПРАВЛЕНИЕ ЗЕМЛЕТРЯСЕНИЕМ",
                "§7Выберите интенсивность от §e1 §7до §c10",
                "§7§oЧем выше уровень, тем сильнее разрушения"
        );
        gui.setItem(4, header);

        EarthquakeDisaster eq = DisasterPanel.getInstance().getEarthquake();
        int currentLevel = eq.isActive() ? eq.getIntensity() : 0;

        // Слайдер уровней (1-10)
        for (int level = 1; level <= 10; level++) {
            Material mat;
            String color;
            List<String> lore = new ArrayList<>();
            
            if (level <= 4) {
                mat = Material.GREEN_WOOL;
                color = "§a";
                lore.add("§7Тип: §aЛёгкие толчки");
                lore.add("§7Визуальные эффекты: §aМинимальные");
            } else if (level <= 7) {
                mat = Material.YELLOW_WOOL;
                color = "§e";
                lore.add("§7Тип: §eСредней силы");
                lore.add("§7Визуальные эффекты: §eТрещины на земле");
            } else {
                mat = Material.RED_WOOL;
                color = "§c";
                lore.add("§7Тип: §cКатастрофический");
                lore.add("§7Визуальные эффекты: §cМножественные разломы");
            }

            if (level >= 5) {
                lore.add("§7Разломы: §c" + (level - 4) + " шт");
            }
            
            lore.add("§7Радиус: §e" + (level * 15) + " блоков");
            lore.add("§7Длительность: §e" + (level * 5) + " сек");
            
            if (level == currentLevel && eq.isActive()) {
                lore.add("§a✦ АКТИВНО СЕЙЧАС");
            }

            ItemStack levelItem = createItem(mat, color + "§lУровень " + level, lore.toArray(new String[0]));
            gui.setItem(9 + level - 1, levelItem);
        }

        // Информация о текущем состоянии
        ItemStack info = createItem(
                Material.BOOK,
                "§b§lℹ ИНФОРМАЦИЯ",
                "§7Статус: " + (eq.isActive() ? "§aАКТИВЕН" : "§cНЕАКТИВЕН"),
                eq.isActive() ? "§7Текущий уровень: §c" + eq.getIntensity() : "§7Катастрофа не активна"
        );
        gui.setItem(49, info);

        // Кнопка "Остановить"
        ItemStack stop = createItem(
                Material.RED_CONCRETE,
                "§c■ ОСТАНОВИТЬ",
                "§7Немедленно остановить землетрясение"
        );
        gui.setItem(48, stop);

        // Кнопка "Назад"
        ItemStack back = createItem(
                Material.OAK_DOOR,
                "§6◄ НАЗАД",
                "§7Вернуться в главное меню"
        );
        gui.setItem(50, back);

        player.openInventory(gui);
    }

    private static ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(Arrays.stream(lore)
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .toList());
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack createGlass(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }
}
