package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PointsAddCommand extends SubCommand {
    public PointsAddCommand() {
        super(new String[]{ "points", "add" }, false, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        int points;

        try {
            points = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.invalid-arguments");
            return;
        }

        if(!TeamHelper.isInTeam(player.getUniqueId())) {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-in-team");
            return;
        }

        String team = TeamHelper.getTeam(player.getUniqueId());

        if(TeamHelper.isTeamLeader(player.getUniqueId())) {
            KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".points.modifier", KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getInt(team + ".points.modifier") + points);
            KenxTeams.getInstance().getTeamDataConfig().saveConfig();

            TeamHelper.updatePoints(team);

            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "points.added", team, Integer.toString(points));
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-leader", team);
        }
    }
}
