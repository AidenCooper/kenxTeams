package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PointsTotalCommand extends SubCommand {
    public PointsTotalCommand() {
        super(new String[]{ "points", "total" }, false, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(!TeamHelper.isInTeam(player.getUniqueId())) {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-in-team");
            return;
        }

        String team = TeamHelper.getTeam(player.getUniqueId());

        if(TeamHelper.isTeamLeader(player.getUniqueId())) {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "points.total", team, KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getString(team + ".points.total"));
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-leader", team);
        }
    }
}
