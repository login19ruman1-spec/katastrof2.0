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
    private int intensity = 1;
    private BukkitTask task;
    private Location center;
    private int radius = 50;
    private final Set<Location> fissures = new HashSet<>();

    public EarthquakeDisaster(DisasterPanel plugin) {
        this.plugin = plugin;
    }

    public void start(Location center, int intensity) {
        if (active) return;
        
        this.center = center;
        this.intensity = Math.min(intensity, 7);
        this.radius = plugin.getConfigManager().getEarthquakeRadius();
        this.active = true;
        this.fissures.clear();

        int duration = plugin.getConfigManager().getEarthquakeDurationPerLevel() * intensity;

        task = new BukkitRunnable() {
            int ticks = 0;
            int maxTicks = duration * 20;

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

                // Shake players within radius
                for (Player player : world.getPlayers()) {
                    if (player.getLocation().distance(center) <= radius) {
                        float shakeStrength = intensity / 7.0f;
                        player.setVelocity(player.getVelocity().add(
                                new org.bukkit.util.Vector(
                                        (Math.random() - 0.5) * shakeStrength * 0.5,
                                        Math.random() * shakeStrength * 0.3,
                                        (Math.random() - 0.5) * shakeStrength * 0.5
                                )
                        ));
                        
                        // Play sound based on intensity
                        if (ticks % 10 == 0) {
                            float pitch = 1.0f - (intensity / 14.0f);
                            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f + (intensity / 14.0f), pitch);
                        }
                    }
                }

                // Create fissures for intensity 5-7
                int fissureLevel = 5;
                if (intensity >= fissureLevel && ticks % 20 == 0) {
                    int fissureCount = intensity - fissureLevel + 1;
                    for (int i = 0; i < fissureCount; i++) {
                        Location fissure = center.clone().add(
                                (Math.random() - 0.5) * radius * 1.5,
                                0,
                                (Math.random() - 0.5) * radius * 1.5
                        );
                        fissure.setY(world.getHighestBlockYAt(fissure));
                        fissures.add(fissure);
                        world.spawnParticle(Particle.CRIT, fissure, 20, 0.5, 0.5, 0.5, 0.1);
                        world.spawnParticle(Particle.FALLING_DUST, fissure, 30, 1, 0.5, 1, 0);
                    }
                }

                // Particle effects
                if (ticks % 5 == 0) {
                    for (int i = 0; i < intensity * 2; i++) {
                        double angle = Math.random() * 2 * Math.PI;
                        double dist = Math.random() * radius;
                        double x = center.getX() + Math.cos(angle) * dist;
                        double z = center.getZ() + Math.sin(angle) * dist;
                        Location loc = new Location(world, x, center.getY() + 1, z);
                        world.spawnParticle(Particle.CLOUD, loc, 1, 0, 0.3, 0, 0);
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
        plugin.getLogger().info("Earthquake stopped");
    }

    public boolean isActive() {
        return active;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = Math.min(intensity, 7);
    }
}
