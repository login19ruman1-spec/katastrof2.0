package dev.disasterpanel.gui;

import dev.disasterpanel.DisasterPanel;
import dev.disasterpanel.disaster.MeteoriteDisaster;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MeteoriteGUI {

    public static void open(Player player) {
        String title = DisasterPanel.getInstance().getConfig().getString("gui.meteorite-title",
                "§8▸ §c☄ §4Метеорит §c☄ §8◂");
        
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', title));

        // Фон
        ItemStack bg = createGlass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, bg);
        }

        // Заголовок
        ItemStack header = createItem(
                Material.FIRE_CHARGE,
                "§6☄ §lУПРАВЛЕНИЕ МЕТЕОРИТОМ",
                "§7Реалистичное падение метеорита с высоты §e2000",
                "§7▸ Кратер §e" + DisasterPanel.getInstance().getConfigManager().getMeteoriteCraterRadius() + " блоков",
                "§7▸ Опасная зона §c" + DisasterPanel.getInstance().getConfigManager().getMeteoriteDangerDuration() + " сек",
                "§7§oНажмите кнопку для запуска"
        );
        gui.setItem(4, header);

        MeteoriteDisaster md = DisasterPanel.getInstance().getMeteorite();

        // Статус метеорита
        String statusColor = md.isActive() ? "§a" : "§c";
        String statusText = md.isActive() ? "В ПУТИ" : "ОЖИДАНИЕ";
        if (md.isImpacted()) {
            statusColor = "§4";
            statusText = "УДАР!";
        }

        ItemStack status = createItem(
                md.isActive() ? Material.REDSTONE : Material.GRAY_DYE,
                "§b§l📊 СТАТУС",
                "§7Состояние: " + statusColor + statusText,
                md.isActive() ? "§7Метеорит летит к земле..." : "§7Нажмите «Запустить» для активации"
        );
        gui.setItem(19, status);

        // Траектория (визуализация высоты)
        String[] heights = {"2000", "1500", "1000", "500", "0"};
        String[] heightLabels = {"§aВход в атмосферу", "§eНачало свечения", "§6Ускорение", "§cКритическая точка", "§4УДАР!"};
        
        for (int i = 0; i < heights.length; i++) {
            Material mat = i < 2 ? Material.GREEN_WOOL : (i < 4 ? Material.YELLOW_WOOL : Material.RED_WOOL);
            String color = i < 2 ? "§a" : (i < 4 ? "§e" : "§c");
            
            ItemStack height = createItem(
                    mat,
                    color + "▸ Высота: " + heights[i],
                    "§7" + heightLabels[i],
                    i == heights.length - 1 ? "§c⚠ Зона удара" : "§7Скорость: " + (i + 1) * 5 + " бл/сек"
            );
            gui.setItem(28 + i, height);
        }

        // Действие при ударе
        ItemStack impact = createItem(
                Material.TNT,
                "§c§l🔥 ПОСЛЕДСТВИЯ УДАРА",
                "§7▸ Взрыв: §e" + DisasterPanel.getInstance().getConfigManager().getMeteoriteExplosionPower(),
                "§7▸ Огонь: §e" + DisasterPanel.getInstance().getConfigManager().getMeteoriteFireDuration() + " сек",
                "§7▸ Опасная зона: §c" + DisasterPanel.getInstance().getConfigManager().getMeteoriteDangerDuration() + " сек"
        );
        gui.setItem(37, impact);

        // Кнопки управления
        ItemStack launch = createItem(
                Material.GREEN_CONCRETE,
                "§a☄ ЗАПУСТИТЬ",
                "§7Запустить метеорит в выбранную точку"
        );
        gui.setItem(48, launch);

        ItemStack stop = createItem(
                Material.RED_CONCRETE,
                "§c■ ОСТАНОВИТЬ",
                "§7Остановить падение метеорита"
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
