package me.majeek.kenxteams.listeners;

import me.majeek.kenxteams.KenxTeams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RaidListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        KenxTeams.getInstance().getRaidManager().removeBoard(event.getPlayer());
    }
}
