package me.majeek.kenxteams.commands;

import com.google.common.collect.ImmutableList;
import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ClaimsCommand extends SubCommand {
    public ClaimsCommand() {
        super(new String[]{ "claims" }, false, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // Show all claims
        UUID uuid = ((Player) sender).getUniqueId();
        String team = TeamHelper.getTeam(uuid);

        if(team != null) {
            if (TeamHelper.isTeamLeader(uuid)) {
                List<ImmutableList<Integer>> chunks = TeamHelper.getChunkList(team);
                for(int i = 0; i < chunks.size(); i++) {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claims.claim-item", team, chunks.get(i).get(0).toString(), chunks.get(i).get(1).toString(), Integer.toString(i));
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claims.not-leader", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claims.not-in-team");
        }
    }
}
