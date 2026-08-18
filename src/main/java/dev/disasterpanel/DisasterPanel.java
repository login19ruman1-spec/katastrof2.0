package dev.disasterpanel;

import dev.disasterpanel.command.DisasterCommand;
import dev.disasterpanel.disaster.EarthquakeDisaster;
import dev.disasterpanel.disaster.MeteoriteDisaster;
import dev.disasterpanel.disaster.VolcanoDisaster;
import dev.disasterpanel.listener.DisasterListener;
import dev.disasterpanel.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DisasterPanel extends JavaPlugin {
    
    private static DisasterPanel instance;
    private ConfigManager configManager;
    private EarthquakeDisaster earthquakeDisaster;
    private MeteoriteDisaster meteoriteDisaster;
    private VolcanoDisaster volcanoDisaster;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Initialize managers
        configManager = new ConfigManager(this);
        
        // Initialize disasters
        earthquakeDisaster = new EarthquakeDisaster(this);
        meteoriteDisaster = new MeteoriteDisaster(this);
        volcanoDisaster = new VolcanoDisaster(this);
        
        // Register commands
        getCommand("disaster").setExecutor(new DisasterCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new DisasterListener(this), this);
        
        getLogger().info("DisasterPanel v1.0.0 enabled!");
    }
    
    @Override
    public void onDisable() {
        // Cleanup any running disasters
        if (earthquakeDisaster != null) earthquakeDisaster.stop();
        if (meteoriteDisaster != null) meteoriteDisaster.stop();
        if (volcanoDisaster != null) volcanoDisaster.stop();
        
        getLogger().info("DisasterPanel disabled!");
    }
    
    public static DisasterPanel getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public EarthquakeDisaster getEarthquakeDisaster() {
        return earthquakeDisaster;
    }
    
    public MeteoriteDisaster getMeteoriteDisaster() {
        return meteoriteDisaster;
    }
    
    public VolcanoDisaster getVolcanoDisaster() {
        return volcanoDisaster;
    }
}
