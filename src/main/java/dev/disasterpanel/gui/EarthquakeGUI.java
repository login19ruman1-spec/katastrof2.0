package dev.disasterpanel.disaster;

import dev.disasterpanel.DisasterPanel;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class EarthquakeDisaster {

    private final DisasterPanel plugin;
    private boolean active = false;
    private int intensity = 0;
    private BukkitTask task;
    private Location center;
    private final Set<Location> fissures = new HashSet<>();

    public EarthquakeDisaster(DisasterPanel plugin) {
        this.plugin = plugin;
    }

    public void start(Location center, int intensity) {
        if (active) return;
        
        this.center = center.clone();
        this.intensity = Math.min(intensity, plugin.getConfigManager().getMaxEarthquakeIntensity());
        this.active = true;
        this.fissures.clear();

        int radius = plugin.getConfigManager().getEarthquakeRadius();
        int duration = plugin.getConfigManager().getEarthquakeDurationPerLevel() * intensity;
        int fissureLevel = plugin.getConfigManager().getFissureLevel();

        plugin.getLogger().info("§eНачалось землетрясение! Уровень: " + intensity);

        task = new BukkitRunnable() {
            int ticks = 0;
            int maxTicks = duration * 20;
            int fissureCount = 0;
            int maxFissures = plugin.getConfigManager().getMaxFissures();

            @Override
            public void run() {
                if (ticks >= maxTicks || !active) {
                    stop();
