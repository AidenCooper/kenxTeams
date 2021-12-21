package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PromoteCommand extends SubCommand {
    public PromoteCommand() {
        super(new String[]{ "promote" }, false, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();

        if(TeamHelper.isInTeam(uuid)) {
            String team = TeamHelper.getTeam(uuid);

            if(TeamHelper.isTeamLeader(uuid)) {
                Player target = KenxTeams.getInstance().getServer().getPlayer(args[0]);

                List<UUID> members = TeamHelper.getTeamMembers(team);
                if(target != null && members.contains(target.getUniqueId()) && target.getUniqueId() != uuid) {
                    for(int i = 0; i < members.size(); i++) {
                        if(members.get(i).equals(target.getUniqueId())) {
                            members.remove(i);
                            members.add(uuid);
                            break;
                        }
                    }

                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".leader", target.getUniqueId());
                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".members", members.stream().map(UUID::toString).collect(Collectors.toList()));

                    KenxTeams.getInstance().getTeamDataConfig().saveConfig();

                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "promote.transferred", team, target.getName(), "Leader");
                } else {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "promote.not-member", team);
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-leader", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-in-team");
        }
    }
}
