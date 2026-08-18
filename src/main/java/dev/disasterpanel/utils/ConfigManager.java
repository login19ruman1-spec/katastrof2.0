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

    // Основные настройки
    public int getMaxRadius() {
        return config.getInt("settings.max-radius", 200);
    }

    public int getMinRadius() {
        return config.getInt("settings.min-radius", 10);
    }

    public int getCheckInterval() {
        return config.getInt("settings.check-interval", 5);
    }

    // Землетрясение
    public int getMaxEarthquakeIntensity() {
        return config.getInt("earthquake.max-intensity", 10);
    }

    public int getEarthquakeDurationPerLevel() {
        return config.getInt("earthquake.duration-per-level", 6);
    }

    public int getEarthquakeRadius() {
        return config.getInt("earthquake.radius", 150);
    }

    public int getFissureLevel() {
        return config.getInt("earthquake.fissure-level", 5);
    }

    public int getMaxFissures() {
        return config.getInt("earthquake.max-fissures", 20);
    }

    // Метеорит - ИСПРАВЛЕНО
    public int getMeteoriteStartHeight() {
        return config.getInt("meteorite.start-height", 2000);
    }

    public int getMeteoriteCraterRadius() {
        return config.getInt("meteorite.crater-radius", 15);
    }

    public int getMeteoriteFireDuration() {
        return config.getInt("meteorite.fire-duration", 45);
    }

    public int getMeteoriteDangerDuration() {
        return config.getInt("meteorite.danger-duration", 30);
    }

    public double getMeteoriteExplosionPower() {
        return config.getDouble("meteorite.explosion-power", 4.0);
    }

    public int getMeteoriteMaxFallSpeed() {
        return config.getInt("meteorite.max-fall-speed", 35);
    }

    // Вулкан - ИСПРАВЛЕНО
    public int getVolcanoEruptionDelay() {
        return config.getInt("volcano.eruption-delay", 15);
    }

    public int getVolcanoLavaRiseHeight() {
        return config.getInt("volcano.lava-rise-height", 25);
    }

    public int getVolcanoLavaFallHeight() {
        return config.getInt("volcano.lava-fall-height", 10);
    }

    public int getVolcanoCoolDownTime() {
        return config.getInt("volcano.cool-down-time", 20);
    }

    public int getVolcanoReEruptionDelay() {
        return config.getInt("volcano.re-eruption-delay", 120);
    }

    public int getVolcanoRadius() {
        return config.getInt("volcano.radius", 5);
    }
}
