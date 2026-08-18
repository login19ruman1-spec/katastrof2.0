package dev.disasterpanel.command;

import dev.disasterpanel.DisasterPanel;
import dev.disasterpanel.gui.MainGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisasterCommand implements CommandExecutor {

    private final DisasterPanel plugin;

    public DisasterCommand(DisasterPanel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cЭта команда доступна только игрокам!");
            return true;
        }

        if (!player.hasPermission("disasterpanel.admin")) {
            player.sendMessage("§cУ вас нет прав для использования этой команды!");
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("стоп")) {
                plugin.getEarthquake().stop();
                plugin.getMeteorite().stop();
                plugin.getVolcano().stop();
                player.sendMessage("§a✅ Все катастрофы остановлены!");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("перезагрузить")) {
                plugin.getConfigManager().reloadConfig();
                player.sendMessage("§a✅ Конфигурация перезагружена!");
                return true;
            }
        }

        // Открываем главное меню
        MainGUI.open(player);
        return true;
    }
}
