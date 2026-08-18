package dev.disasterpanel.utils;

import dev.disasterpanel.DisasterPanel;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    
    private final DisasterPanel plugin;
    private FileConfiguration config;
    
    public ConfigManager(DisasterPanel plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
    
    public int getParticleDensity() {
        return config.getInt("settings.particle-density", 5);
    }
    
    public double getSoundMultiplier() {
        return config.getDouble("settings.sound-multiplier", 1.0);
    }
    
    public String getDefaultWorld() {
        return config.getString("settings.default-world", "world");
    }
    
    public int getEarthquakeRadius() {
        return config.getInt("earthquake.radius", 100);
    }
    
    public int getEarthquakeDurationPerLevel() {
        return config.getInt("earthquake.duration-per-level", 5);
    }
    
    public int getMeteoriteStartHeight() {
        return config.getInt("meteorite.start-height", 2000);
    }
    
    public int getMeteoriteImpactRadius() {
        return config.getInt("meteorite.impact-radius", 30);
    }
    
    public int getMeteoriteFireDuration() {
        return config.getInt("meteorite.fire-duration", 60);
    }
    
    public int getMeteoriteDangerDuration() {
        return config.getInt("meteorite.danger-duration", 60);
    }
    
    public int getVolcanoDefaultRise() {
        return config.getInt("volcano.default-rise", 20);
    }
    
    public int getVolcanoDefaultFall() {
        return config.getInt("volcano.default-fall", 10);
    }
    
    public int getVolcanoEruptionDelay() {
        return config.getInt("volcano.eruption-delay", 10);
    }
    
    public int getVolcanoReEruptionCooldown() {
        return config.getInt("volcano.re-eruption-cooldown", 2);
    }
}
