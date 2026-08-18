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

public class MainGUI {

    private static final int SIZE = 27;

    public static void open(Player player) {
        String title = DisasterPanel.getInstance().getConfig().getString("gui.main-title", 
                "§8▸ §c✧ §4Панель Управления §c✧ §8◂");
        
        Inventory gui = Bukkit.createInventory(null, SIZE, ChatColor.translateAlternateColorCodes('&', title));

        // Фон
        ItemStack bg = createItem(Material.BLACK_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < SIZE; i++) {
            if (i != 11 && i != 13 && i != 15 && i != 22 && i != 26) {
                gui.setItem(i, bg);
            }
        }

        // Декоративные линии
        ItemStack line = createItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 9; i < 18; i++) {
            if (i != 11 && i != 13 && i != 15) {
                gui.setItem(i, line);
            }
        }

        // 🌍 Землетрясение
        ItemStack earthquake = createItem(
                Material.GOLDEN_PICKAXE,
                "§c⚡ §lЗЕМЛЕТРЯСЕНИЕ",
                "§7Уровни: §e1-10",
                "§7▸ Сила: §c" + getEarthquakeStatus(),
                "§7▸ Радиус: §e" + DisasterPanel.getInstance().getConfigManager().getEarthquakeRadius() + " блоков",
                "§7▸ §8Нажмите для настройки"
        );
        gui.setItem(11, earthquake);

        // ☄ Метеорит
        ItemStack meteorite = createItem(
                Material.FIRE_CHARGE,
                "§6☄ §lМЕТЕОРИТ",
                "§7▸ Высота: §e2000 блоков",
                "§7▸ Кратер: §e" + DisasterPanel.getInstance().getConfigManager().getMeteoriteCraterRadius() + " блоков",
                "§7▸ Опасная зона: §c" + DisasterPanel.getInstance().getConfigManager().getMeteoriteDangerDuration() + " сек",
                "§7▸ §8Нажмите для настройки"
        );
        gui.setItem(13, meteorite);

        // 🌋 Вулкан
        ItemStack volcano = createItem(
                Material.MAGMA_BLOCK,
                "§c🌋 §lВУЛКАН",
                "§7▸ Высота лавы: §e" + DisasterPanel.getInstance().getConfigManager().getVolcanoLavaRiseHeight() + " блоков",
                "§7▸ Задержка: §e" + DisasterPanel.getInstance().getConfigManager().getVolcanoEruptionDelay() + " сек",
                "§7▸ §8Нажмите для настройки"
        );
        gui.setItem(15, volcano);

        // Статус
        ItemStack status = createItem(
                Material.COMPASS,
                "§b§l📊 СТАТУС",
                "§7Землетрясение: " + getStatus(DisasterPanel.getInstance().getEarthquake().isActive()),
                "§7Метеорит: " + getStatus(DisasterPanel.getInstance().getMeteorite().isActive()),
                "§7Вулкан: " + getStatus(DisasterPanel.getInstance().getVolcano().isActive())
        );
        gui.setItem(22, status);

        // Кнопка выхода
        ItemStack close = createItem(
                Material.BARRIER,
                "§c✖ Закрыть",
                "§7Закрыть панель управления"
        );
        gui.setItem(26, close);

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

    private static String getStatus(boolean active) {
        return active ? "§a● АКТИВНА" : "§c● НЕАКТИВНА";
    }

    private static String getEarthquakeStatus() {
        EarthquakeDisaster eq = DisasterPanel.getInstance().getEarthquake();
        return eq.isActive() ? "§cУровень " + eq.getIntensity() : "§7Ожидание";
    }
}
