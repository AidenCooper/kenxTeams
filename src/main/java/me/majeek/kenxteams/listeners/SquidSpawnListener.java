package me.majeek.kenxteams.listeners;

import me.majeek.kenxteams.KenxTeams;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SquidSpawnListener implements Listener {
    @EventHandler
    public void onSquidSpawn(CreatureSpawnEvent event) {
        if(event.getEntityType() == EntityType.SQUID && !KenxTeams.getInstance().getMainConfig().getConfiguration().getBoolean("squid-spawn-enabled")) {
            event.getEntity().remove();
        }
    }
}
