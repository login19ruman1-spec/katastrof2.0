package dev.disasterpanel.disaster;

import dev.disasterpanel.DisasterPanel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MeteoriteDisaster {

    private final DisasterPanel plugin;
    private boolean active = false;
    private boolean impacted = false;
    private BukkitTask task;
    private Location impactLocation;
    private Location currentLocation;

    public MeteoriteDisaster(DisasterPanel plugin) {
        this.plugin = plugin;
    }

    public void start(Location target) {
        if (active) return;
        
        this.active = true;
        this.impacted = false;
        this.impactLocation = target.clone();

        int startHeight = plugin.getConfigManager().getMeteoriteStartHeight();
        this.currentLocation = target.clone().add(0, startHeight, 0);

        int size = plugin.getConfigManager().getMeteoriteImpactRadius();

        task = new BukkitRunnable() {
            int speed = 1;
            int ticks = 0;
            int height = startHeight;

            @Override
            public void run() {
                if (!active) {
                    stop();
                    return;
                }

                if (impacted) {
                    // Handle danger zone
                    int dangerDuration = plugin.getConfigManager().getMeteoriteDangerDuration();
                    if (ticks < dangerDuration * 20) {
                        World world = impactLocation.getWorld();
                        if (world == null) return;
                        
                        for (Player player : world.getPlayers()) {
                            if (player.getLocation().distance(impactLocation) < size * 2) {
                                player.damage(2.0);
                                player.setFireTicks(40);
                            }
                        }
                        ticks++;
                    } else {
                        stop();
                    }
                    return;
                }

                // Move meteorite down with acceleration
                speed = Math.min(speed + 1, 25);
                height -= speed;
                currentLocation.setY(currentLocation.getY() - speed);

                World world = currentLocation.getWorld();
                if (world == null) {
                    stop();
                    return;
                }

                // Sound effects based on height
                if (height < 1500 && height > 500) {
                    if (ticks % 10 == 0) {
                        world.playSound(currentLocation, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.5f, 1.0f);
                    }
                } else if (height <= 500 && height > 100) {
                    if (ticks % 5 == 0) {
                        world.playSound(currentLocation, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 0.5f);
                    }
                }

                // Particles for trail
                if (ticks % 2 == 0) {
                    world.spawnParticle(Particle.FLAME, currentLocation, 10, 1, 0.5, 1, 0.1);
                    world.spawnParticle(Particle.LAVA, currentLocation, 5, 0.5, 0.5, 0.5, 0.05);
                    world.spawnParticle(Particle.SMOKE, currentLocation, 5, 1, 0.5, 1, 0);
                }

                // Impact
                if (height <= 0) {
                    impact();
                    return;
                }

                ticks++;
            }

            private void impact() {
                impacted = true;
                ticks = 0;
                World world = impactLocation.getWorld();
                if (world == null) {
                    stop();
                    return;
                }

                int size = plugin.getConfigManager().getMeteoriteImpactRadius();

                // Create crater
                for (int x = -size; x <= size; x++) {
                    for (int z = -size; z <= size; z++) {
                        double distance = Math.sqrt(x*x + z*z);
                        if (distance <= size) {
                            int yOffset = (int)(size - distance);
                            for (int y = 0; y <= yOffset; y++) {
                                Location loc = impactLocation.clone().add(x, -y, z);
                                Block block = world.getBlockAt(loc);
                                if (y < yOffset - 1) {
                                    block.setType(Material.AIR);
                                } else if (y == yOffset) {
                                    block.setType(Material.COBBLESTONE);
                                }
                            }
                        }
                    }
                }

                // Fire in crater
                int fireDuration = plugin.getConfigManager().getMeteoriteFireDuration();
                for (int i = 0; i < size * 3; i++) {
                    Location fireLoc = impactLocation.clone().add(
                            (Math.random() - 0.5) * size * 2,
                            0.5,
                            (Math.random() - 0.5) * size * 2
                    );
                    if (world.getBlockAt(fireLoc).getType() == Material.AIR) {
                        world.getBlockAt(fireLoc).setType(Material.FIRE);
                    }
                }

                // Explosion
                world.createExplosion(impactLocation, size * 2, true, true);
                
                // Massive particles
                world.spawnParticle(Particle.EXPLOSION, impactLocation, 1, 0, 0, 0, 0);
                world.spawnParticle(Particle.CLOUD, impactLocation, 10, 2, 2, 2, 0);
                world.spawnParticle(Particle.FLAME, impactLocation, 50, 3, 3, 3, 0.1);
                
                world.playSound(impactLocation, Sound.ENTITY_GENERIC_EXPLODE, 3.0f, 0.5f);
                world.playSound(impactLocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.0f, 0.7f);

                // Start danger zone timer
                new BukkitRunnable() {
                    int dangerTicks = 0;
                    int maxDangerTicks = plugin.getConfigManager().getMeteoriteDangerDuration() * 20;
                    
                    @Override
                    public void run() {
                        if (dangerTicks >= maxDangerTicks || !active) {
                            this.cancel();
                            return;
                        }
                        
                        // Show danger zone particles - используем SPELL_MOB (работает в Purpur 1.21.4)
                        for (int i = 0; i < 10; i++) {
                            double angle = Math.random() * 2 * Math.PI;
                            double dist = Math.random() * size * 2;
                            Location loc = impactLocation.clone().add(
                                    Math.cos(angle) * dist,
                                    0.5 + Math.random(),
                                    Math.sin(angle) * dist
                            );
                            world.spawnParticle(Particle.SPELL_MOB, loc, 1, 0, 0, 0, 0);
                        }
                        
                        dangerTicks++;
                    }
                }.runTaskTimer(plugin, 0L, 1L);
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        active = false;
        impacted = false;
        plugin.getLogger().info("Meteorite stopped");
    }

    public boolean isActive() {
        return active;
    }

    public boolean isImpacted() {
        return impacted;
    }
}
