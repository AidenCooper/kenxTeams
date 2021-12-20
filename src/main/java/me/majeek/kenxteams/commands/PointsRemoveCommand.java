package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;

public class PointsRemoveCommand extends SubCommand {
    public PointsRemoveCommand() {
        super(new String[]{ "points", "remove" }, true, 2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String team = args[0];
        int points;

        try {
            points = Integer.parseInt(args[1]);

            if(!TeamHelper.isTeam(team)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.invalid-arguments");
            return;
        }

        TeamHelper.setPoints(team, TeamHelper.getPoints(team) - points);
        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "points.added", team, Integer.toString(points));
    }
}
