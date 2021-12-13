package me.majeek.kenxteams.listeners;

import me.majeek.kenxteams.KenxTeams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Timestamp;
import java.time.Instant;

public class PlayerDataListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        String uuid = event.getPlayer().getUniqueId().toString();

        String old = KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid + ".name");
        if(old != null) {
            if(!old.equals(name)) {
                KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".name", name);
            }
        } else {
            KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".name", name);
        }

        if(KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid + ".playtime") == null) {
            KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".playtime", 0);
        }

        if(KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid + ".team") == null) {
            KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".team", "");
        }

        KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".lastLogin", Timestamp.from(Instant.now()).toString());

        KenxTeams.getInstance().getPlayerDataConfig().saveConfig();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();

        long milliseconds = Timestamp.from(Instant.now()).getTime() - Timestamp.valueOf(KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid + ".lastLogin")).getTime();
        int minutes = (((int) milliseconds / 1000) % 3600) / 60;

        KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".playtime", minutes + KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getInt(uuid + ".playtime"));
        KenxTeams.getInstance().getPlayerDataConfig().saveConfig();

        System.out.println("PLAYER EVENT");
    }
}
