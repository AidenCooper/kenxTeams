package me.majeek.kenxteams.listeners;

import me.majeek.kenxteams.KenxTeams;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class SquidSpawnerListener implements Listener {
    @EventHandler
    public void onSpawn(SpawnerSpawnEvent event) {
        if(event.getSpawner().getSpawnedType() == EntityType.SQUID && !KenxTeams.getInstance().getMainConfig().getConfiguration().getBoolean("squid-spawners-enabled")) {
            event.setCancelled(true);
        }
    }
}
