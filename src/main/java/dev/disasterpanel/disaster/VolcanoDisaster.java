package dev.disasterpanel.disaster;

import dev.disasterpanel.DisasterPanel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class VolcanoDisaster {

    private final DisasterPanel plugin;
    private boolean active = false;
    private BukkitTask task;
    private Location center;
    private int lavaLevel = 0;
    private int maxLavaLevel;
    private boolean cooled = false;

    public VolcanoDisaster(DisasterPanel plugin) {
        this.plugin = plugin;
    }

    public void start(Location center) {
        if (active) return;
        
        this.active = true;
        this.center = center.clone();
        this.maxLavaLevel = plugin.getConfigManager().getVolcanoDefaultRise();
        this.lavaLevel = 0;
        this.cooled = false;

        int delay = plugin.getConfigManager().getVolcanoEruptionDelay();

        task = new BukkitRunnable() {
            int ticks = 0;
            boolean eruptionStarted = false;

            @Override
            public void run() {
                if (!active) {
                    stop();
                    return;
                }

                World world = center.getWorld();
                if (world == null) {
                    stop();
                    return;
                }

                // Phase 1: Pre-eruption (waiting)
                if (ticks < delay * 20) {
                    // Smoke particles before eruption
                    if (ticks % 5 == 0) {
                        for (int i = 0; i < 5; i++) {
                            Location smokeLoc = center.clone().add(
                                    (Math.random() - 0.5) * 4,
                                    2 + Math.random() * 3,
                                    (Math.random() - 0.5) * 4
                            );
                            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, smokeLoc, 1, 0.3, 0.5, 0.3, 0.02);
                            world.spawnParticle(Particle.FLAME, smokeLoc, 1, 0.2, 0.2, 0.2, 0);
                        }
                    }
                    ticks++;
                    return;
                }

                // Phase 2: Eruption - Lava rising
                if (lavaLevel < maxLavaLevel) {
                    double riseSpeed = 0.5;
                    lavaLevel += riseSpeed;
                    
                    // Create lava columns
                    int radius = 3;
                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (Math.abs(x) + Math.abs(z) <= radius) {
                                Location loc = center.clone().add(x, lavaLevel, z);
                                Block block = world.getBlockAt(loc);
                                if (block.getType() == Material.AIR || block.getType() == Material.WATER) {
                                    block.setType(Material.LAVA);
                                }
                            }
                        }
                    }
                    
                    // Eruption particles
                    if (ticks % 3 == 0) {
                        for (int i = 0; i < 15; i++) {
                            double angle = Math.random() * 2 * Math.PI;
                            double dist = Math.random() * 4;
                            double height = 3 + Math.random() * 10;
                            Location loc = center.clone().add(
                                    Math.cos(angle) * dist,
                                    lavaLevel + 1 + Math.random() * height,
                                    Math.sin(angle) * dist
                            );
                            world.spawnParticle(Particle.LAVA, loc, 1, 0.5, 0.5, 0.5, 0.05);
                            world.spawnParticle(Particle.FLAME, loc, 2, 0.3, 0.3, 0.3, 0.02);
                            world.spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0.5, 0.5, 0.5, 0);
                        }
                        
                        world.playSound(center, Sound.BLOCK_LAVA_POP, 1.0f, 0.7f + (float)Math.random() * 0.3f);
                        world.playSound(center, Sound.BLOCK_FIRE_AMBIENT, 0.5f, 0.8f);
                    }
                }
                
                // Phase 3: Cooling - Convert lava to obsidian
                else if (!cooled) {
                    cooled = true;
                    int radius = 3;
                    
                    for (int y = 0; y <= maxLavaLevel; y += 2) {
                        for (int x = -radius; x <= radius; x++) {
                            for (int z = -radius; z <= radius; z++) {
                                if (Math.abs(x) + Math.abs(z) <= radius) {
                                    Location loc = center.clone().add(x, y, z);
                                    Block block = world.getBlockAt(loc);
                                    if (block.getType() == Material.LAVA) {
                                        block.setType(Material.OBSIDIAN);
                                        world.spawnParticle(Particle.SMOKE_LARGE, loc, 3, 0.3, 0.3, 0.3, 0);
                                        world.playSound(loc, Sound.BLOCK_LAVA_EXTINGUISH, 0.5f, 0.8f);
                                    }
                                }
                            }
                        }
                    }
                    
                    // Schedule re-eruption check
                    int coolDown = 30;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!active) return;
                            
                            // Check if obsidian is broken
                            boolean broken = false;
                            int radius = 3;
                            for (int y = 0; y <= maxLavaLevel; y += 2) {
                                for (int x = -radius; x <= radius; x++) {
                                    for (int z = -radius; z <= radius; z++) {
                                        if (Math.abs(x) + Math.abs(z) <= radius) {
                                            Location loc = center.clone().add(x, y, z);
                                            Block block = world.getBlockAt(loc);
                                            if (block.getType() != Material.OBSIDIAN && 
                                                block.getType() != Material.LAVA &&
                                                block.getType() != Material.AIR) {
                                                broken = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (broken) break;
                                }
                                if (broken) break;
                            }
                            
                            if (broken) {
                                // Re-eruption after 2 minutes
                                int reDelay = 120;
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (active) {
                                            // Reset and restart eruption
                                            lavaLevel = 0;
                                            cooled = false;
                                            ticks = delay * 20;
                                            world.playSound(center, Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f, 0.5f);
                                        }
                                    }
                                }.runTaskLater(plugin, reDelay * 20L);
                            }
                        }
                    }.runTaskLater(plugin, coolDown * 20L);
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
        
        // Clean up lava and obsidian blocks
        if (center != null) {
            World world = center.getWorld();
            if (world != null) {
                int radius = 4;
                for (int y = 0; y <= maxLavaLevel + 10; y++) {
                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (Math.abs(x) + Math.abs(z) <= radius) {
                                Location loc = center.clone().add(x, y, z);
                                Block block = world.getBlockAt(loc);
                                if (block.getType() == Material.LAVA || 
                                    block.getType() == Material.OBSIDIAN ||
                                    block.getType() == Material.FIRE) {
                                    block.setType(Material.AIR);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        plugin.getLogger().info("Volcano stopped");
    }

    public boolean isActive() {
        return active;
    }
}
