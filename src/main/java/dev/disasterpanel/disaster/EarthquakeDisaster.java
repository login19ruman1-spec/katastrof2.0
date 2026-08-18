package dev.disasterpanel.disaster;

import dev.disasterpanel.DisasterPanel;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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
        int maxFissures = plugin.getConfigManager().getMaxFissures();

        plugin.getLogger().info("§eНачалось землетрясение! Уровень: " + intensity);

        task = new BukkitRunnable() {
            int ticks = 0;
            int maxTicks = duration * 20;
            int fissureCount = 0;

            @Override
            public void run() {
                if (ticks >= maxTicks || !active) {
                    stop();
                    return;
                }

                World world = center.getWorld();
                if (world == null) {
                    stop();
                    return;
                }

                // Тряска игроков в радиусе
                for (Player player : world.getPlayers()) {
                    if (player.getLocation().distance(center) <= radius) {
                        float shakeStrength = Math.min(intensity / 7.0f, 1.5f);
                        double randomX = (Math.random() - 0.5) * shakeStrength * 0.8;
                        double randomZ = (Math.random() - 0.5) * shakeStrength * 0.8;
                        double randomY = Math.random() * shakeStrength * 0.4;
                        player.setVelocity(player.getVelocity().add(new org.bukkit.util.Vector(randomX, randomY, randomZ)));
                        
                        if (ticks % 15 == 0) {
                            float pitch = Math.max(0.3f, 1.0f - (intensity / 12.0f));
                            float volume = Math.min(0.5f + (intensity / 20.0f), 2.0f);
                            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, volume, pitch);
                        }
                    }
                }

                // Создание разломов (уровень 5+)
                if (intensity >= fissureLevel && fissureCount < maxFissures && ticks % 20 == 0) {
                    int fissuresToCreate = Math.min(intensity - fissureLevel + 1, maxFissures - fissureCount);
                    for (int i = 0; i < fissuresToCreate; i++) {
                        double angle = Math.random() * 2 * Math.PI;
                        double dist = Math.random() * radius * 0.8;
                        Location fissure = center.clone().add(
                                Math.cos(angle) * dist,
                                0,
                                Math.sin(angle) * dist
                        );
                        fissure.setY(world.getHighestBlockYAt(fissure) + 0.5);
                        fissures.add(fissure);
                        fissureCount++;
                        
                        world.spawnParticle(Particle.CRIT, fissure, 15, 0.5, 0.3, 0.5, 0.1);
                        world.spawnParticle(Particle.SMOKE, fissure, 10, 0.5, 0.3, 0.5, 0.05);
                        world.playSound(fissure, Sound.BLOCK_STONE_BREAK, 0.8f, 0.7f);
                    }
                }

                if (ticks % 3 == 0) {
                    for (int i = 0; i < Math.min(intensity * 2, 15); i++) {
                        double angle = Math.random() * 2 * Math.PI;
                        double dist = Math.random() * radius;
                        double x = center.getX() + Math.cos(angle) * dist;
                        double z = center.getZ() + Math.sin(angle) * dist;
                        Location loc = new Location(world, x, center.getY() + 0.5 + Math.random(), z);
                        world.spawnParticle(Particle.CLOUD, loc, 1, 0.2, 0.1, 0.2, 0.01);
                    }
                }

                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        active = false;
        fissures.clear();
        plugin.getLogger().info("§cЗемлетрясение остановлено");
    }

    public boolean isActive() {
        return active;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = Math.min(intensity, plugin.getConfigManager().getMaxEarthquakeIntensity());
    }
}
