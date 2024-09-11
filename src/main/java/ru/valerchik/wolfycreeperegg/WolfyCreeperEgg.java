package ru.valerchik.wolfycreeperegg;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WolfyCreeperEgg extends JavaPlugin implements Listener {

    private double chargedCreeperExplosionRadius;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        loadConfigValues();
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        loadConfigValues();
    }

    private void loadConfigValues() {
        FileConfiguration config = getConfig();
        chargedCreeperExplosionRadius = config.getDouble("charged-creeper-explosion-radius", 6.0);
    }

    @EventHandler
    public void onCreeperSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();

            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
                creeper.setPowered(true);
            }
        }
    }

    @EventHandler
    public void onCreeperExplode(ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();
            if (creeper.isPowered()) {
                event.setRadius((float) chargedCreeperExplosionRadius);
            }
        }
    }
}
