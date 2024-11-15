package ru.valerchik.wolfycreeperegg;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class CreeperExplosion implements Listener {

    private final WolfyCreeperEgg plugin;

    public CreeperExplosion(WolfyCreeperEgg plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();
            FileConfiguration config = plugin.getConfig();

            boolean defaultCreeperExplode = config.getBoolean("defaultCreeperExplode", false);

            if (!creeper.isPowered() && !defaultCreeperExplode) {
                event.blockList().clear();
            }
        }
    }
}