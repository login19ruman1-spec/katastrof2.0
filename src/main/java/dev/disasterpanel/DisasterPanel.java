package dev.disasterpanel;

import dev.disasterpanel.command.DisasterCommand;
import dev.disasterpanel.disaster.EarthquakeDisaster;
import dev.disasterpanel.disaster.MeteoriteDisaster;
import dev.disasterpanel.disaster.VolcanoDisaster;
import dev.disasterpanel.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DisasterPanel extends JavaPlugin {

    private static DisasterPanel instance;
    private ConfigManager configManager;
    private EarthquakeDisaster earthquake;
    private MeteoriteDisaster meteorite;
    private VolcanoDisaster volcano;

    @Override
    public void onEnable() {
        instance = this;
        
        // Загружаем конфиг
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        
        // Инициализируем катастрофы
        earthquake = new EarthquakeDisaster(this);
        meteorite = new MeteoriteDisaster(this);
        volcano = new VolcanoDisaster(this);
        
        // Регистрируем команду
        getCommand("disaster").setExecutor(new DisasterCommand(this));
        
        getLogger().info("§a=================================");
        getLogger().info("§a  DisasterPanel v2.0 загружен!");
        getLogger().info("§a  Система катастроф активирована");
        getLogger().info("§a=================================");
    }

    @Override
    public void onDisable() {
        // Останавливаем все катастрофы
        if (earthquake != null) earthquake.stop();
        if (meteorite != null) meteorite.stop();
        if (volcano != null) volcano.stop();
        
        getLogger().info("§cDisasterPanel выключен");
    }

    public static DisasterPanel getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EarthquakeDisaster getEarthquake() {
        return earthquake;
    }

    public MeteoriteDisaster getMeteorite() {
        return meteorite;
    }

    public VolcanoDisaster getVolcano() {
        return volcano;
    }
}
