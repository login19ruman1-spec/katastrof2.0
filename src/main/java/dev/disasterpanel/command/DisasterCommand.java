package dev.disasterpanel.command;

import dev.disasterpanel.DisasterPanel;
import dev.disasterpanel.gui.DisasterGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisasterCommand implements CommandExecutor {
    
    private final DisasterPanel plugin;
    private final DisasterGUI disasterGUI;
    
    public DisasterCommand(DisasterPanel plugin) {
        this.plugin = plugin;
        this.disasterGUI = new DisasterGUI(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("disasterpanel.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }
        
        if (args.length > 0 && args[0].equalsIgnoreCase("stop")) {
            plugin.getEarthquakeDisaster().stop();
            plugin.getMeteoriteDisaster().stop();
            plugin.getVolcanoDisaster().stop();
            player.sendMessage(ChatColor.GREEN + "All disasters stopped!");
            return true;
        }
        
        disasterGUI.openMainMenu(player);
        return true;
    }
}
