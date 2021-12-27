package me.majeek.kenxteams.listeners;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.UUID;

public class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(!KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(event.getPlayer().getUniqueId() + ".chat").equalsIgnoreCase("global")) {
            String format = KenxTeams.getInstance().getMainConfig().getConfiguration().getString("chat.team-format");
            String rank = TeamHelper.isTeamLeader(event.getPlayer().getUniqueId()) ? "Leader" : "Member";
            String name = event.getPlayer().getName();
            String message = event.getMessage();

            String formatted = format.replace("{rank}", rank).replace("{name}", name).replace("{message}", message);
            formatted = ChatColor.translateAlternateColorCodes('&', formatted);

            event.setCancelled(true);

            List<UUID> total = TeamHelper.getTeamMembers(TeamHelper.getTeam(event.getPlayer().getUniqueId())); total.add(TeamHelper.getTeamLeader(TeamHelper.getTeam(event.getPlayer().getUniqueId())));
            for(UUID member : total) {
                Player player = KenxTeams.getInstance().getServer().getPlayer(member);

                if(player != null) {
                    player.sendMessage(formatted);
                }
            }
        } else {
            String team = TeamHelper.getTeam(event.getPlayer().getUniqueId());

            if(team == null) {
                team = KenxTeams.getInstance().getMainConfig().getConfiguration().getString("chat.no-team");
            }

            event.setFormat(ChatColor.translateAlternateColorCodes('&', event.getFormat().replace("{team}", team)));
        }
    }
}
