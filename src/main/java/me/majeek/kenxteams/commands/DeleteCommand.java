package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DeleteCommand extends SubCommand {
    public DeleteCommand() {
        super(new String[]{ "delete" }, false, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();

        if(TeamHelper.isInTeam(uuid)) {
            String team = TeamHelper.getTeam(uuid);

            if(TeamHelper.isTeamLeader(uuid)) {
                TeamHelper.getTeamMembers(team).forEach(member -> KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(member + ".team", ""));
                KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".team", "");

                KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team, null);

                KenxTeams.getInstance().getPlayerDataConfig().saveConfig();
                KenxTeams.getInstance().getTeamDataConfig().saveConfig();

                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "delete.deleted", team);
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "delete.not-leader", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "delete.not-in-team");
        }
    }
}
