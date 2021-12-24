package me.majeek.kenxteams.listeners;

import com.google.common.collect.ImmutableList;
import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;

public class MoveIntoClaim implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Chunk from = event.getFrom().getChunk();
        Chunk to = event.getTo().getChunk();

        if(from.getX() != to.getX() || from.getZ() != to.getZ() || !from.getWorld().getName().equalsIgnoreCase(to.getWorld().getName())) {
            String fromTeam = TeamHelper.getTeamFromChunk(ImmutableList.copyOf(Arrays.asList(from.getX(), from.getZ())), from.getWorld().getName());
            String toTeam = TeamHelper.getTeamFromChunk(ImmutableList.copyOf(Arrays.asList(to.getX(), to.getZ())), to.getWorld().getName());

            String message;

            if((fromTeam != null && toTeam != null) && !fromTeam.equalsIgnoreCase(toTeam)) {
                message = KenxTeams.getInstance().getMainConfig().getConfiguration().getString("walk-into-area.claimed-to-claimed").replace("{from}", fromTeam).replace("{to}", toTeam);
            } else if(fromTeam != null && toTeam == null) {
                message = KenxTeams.getInstance().getMainConfig().getConfiguration().getString("walk-into-area.claimed-to-none").replace("{from}", fromTeam);
            } else if(fromTeam == null && toTeam != null) {
                message = KenxTeams.getInstance().getMainConfig().getConfiguration().getString("walk-into-area.none-to-claimed").replace("{to}", toTeam);
            } else {
                return;
            }

            message = message.replace("{prefix}", KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString("prefix"));
            message = ChatColor.translateAlternateColorCodes('&', message);

            if(!message.equalsIgnoreCase("")) {
                event.getPlayer().sendMessage(message);
            }
        }
    }
}
