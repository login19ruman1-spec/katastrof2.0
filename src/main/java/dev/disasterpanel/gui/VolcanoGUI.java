package dev.disasterpanel.gui;

import dev.disasterpanel.DisasterPanel;
import dev.disasterpanel.disaster.VolcanoDisaster;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class VolcanoGUI {

    public static void open(Player player) {
        String title = DisasterPanel.getInstance().getConfig().getString("gui.volcano-title",
                "§8▸ §c🌋 §4Вулкан §c🌋 §8◂");
        
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', title));

        // Фон
        ItemStack bg = createGlass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, bg);
        }

        // Заголовок
        ItemStack header = createItem(
                Material.MAGMA_BLOCK,
                "§c🌋 §lУПРАВЛЕНИЕ ВУЛКАНОМ",
                "§7Полноценное извержение вулкана с лавой",
                "§7▸ Высота лавы: §e" + DisasterPanel.getInstance().getConfigManager().getVolcanoLavaRiseHeight() + " блоков",
                "§7▸ Задержка: §e" + DisasterPanel.getInstance().getConfigManager().getVolcanoEruptionDelay() + " сек",
                "§7§oНажмите «Извергнуть» для запуска"
        );
        gui.setItem(4, header);

        VolcanoDisaster vd = DisasterPanel.getInstance().getVolcano();

        // Статус
        ItemStack status = createItem(
                vd.isActive() ? Material.REDSTONE : Material.GRAY_DYE,
                "§b§l📊 СТАТУС ВУЛКАНА",
                "§7Состояние: " + (vd.isActive() ? "§cИЗВЕРГАЕТСЯ" : "§7ОЖИДАНИЕ"),
                vd.isActive() ? "§7Лава поднимается..." : "§7Нажмите «Извергнуть» для активации"
        );
        gui.setItem(19, status);

        // Фазы извержения
        ItemStack phase1 = createItem(
                Material.ORANGE_WOOL,
                "§6▸ ФАЗА 1: ПОДГОТОВКА",
                "§7Длительность: §e" + DisasterPanel.getInstance().getConfigManager().getVolcanoEruptionDelay() + " сек",
                "§7§oДым и небольшие толчки"
        );
        gui.setItem(28, phase1);

        ItemStack phase2 = createItem(
                Material.RED_WOOL,
                "§c▸ ФАЗА 2: ИЗВЕРЖЕНИЕ",
                "§7Подъём лавы на §e" + DisasterPanel.getInstance().getConfigManager().getVolcanoLavaRiseHeight() + " блоков",
                "§7§oФонтаны магмы и пепла"
        );
        gui.setItem(29, phase2);

        ItemStack phase3 = createItem(
                Material.BLACK_WOOL,
                "§8▸ ФАЗА 3: ОСТЫВАНИЕ",
                "§7Длительность: §e" + DisasterPanel.getInstance().getConfigManager().getVolcanoCoolDownTime() + " сек",
                "§7Лава превращается в §8обсидиан"
        );
        gui.setItem(30, phase3);

        ItemStack phase4 = createItem(
                Material.OBSIDIAN,
                "§5▸ ФАЗА 4: ПОВТОРНОЕ ИЗВЕРЖЕНИЕ",
                "§7Если разрушить обсидиан",
                "§7Новое извержение через §e" + DisasterPanel.getInstance().getConfigManager().getVolcanoReEruptionDelay() + " сек",
                "§c⚠ Осторожно! Механизм может сработать неожиданно"
        );
        gui.setItem(31, phase4);

        // Кнопки управления
        ItemStack erupt = createItem(
                Material.GREEN_CONCRETE,
                "§a🌋 ИЗВЕРГНУТЬ",
                "§7Запустить извержение вулкана"
        );
        gui.setItem(48, erupt);

        ItemStack stop = createItem(
                Material.RED_CONCRETE,
                "§c■ ОСТАНОВИТЬ",
                "§7Остановить извержение"
        );
        gui.setItem(49, stop);

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
