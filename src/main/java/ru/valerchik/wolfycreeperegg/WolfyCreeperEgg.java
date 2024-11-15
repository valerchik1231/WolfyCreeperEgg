package ru.valerchik.wolfycreeperegg;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class WolfyCreeperEgg extends JavaPlugin implements Listener {

    private double chargedCreeperExplosionRadius;
    private double spawnChance;
    private Random random = new Random();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new CreeperExplosion(this), this);
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
        spawnChance = config.getDouble("spawn-chance", 100.0);
    }

    @EventHandler
    public void onCreeperSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();

            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
                if (random.nextDouble() < (spawnChance / 100.0)) {
                    creeper.setPowered(true);
                }
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("wolfycreeperegg")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                FileConfiguration config = getConfig();
                String reloadMessage = config.getString("reload-message");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessage));
                return true;
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("setchance")) {
                try {
                    double newChance = Double.parseDouble(args[1]);
                    if (newChance < 0 || newChance > 100) {
                        FileConfiguration config = getConfig();
                        String valueMessage = config.getString("value-message");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', valueMessage));
                        return false;
                    }
                    getConfig().set("spawn-chance", newChance);
                    saveConfig();
                    spawnChance = newChance;
                    sender.sendMessage("Шанс спавна заряженного крипера установлен на " + newChance + "%.");
                } catch (NumberFormatException e) {
                    FileConfiguration config = getConfig();
                    String noFormatValue = config.getString("no-format-value");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noFormatValue));
                    return false;
                }
                return true;
            }
        }
        return false;
    }
}
