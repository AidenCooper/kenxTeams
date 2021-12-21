package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class LeaveCommand extends SubCommand {
    public LeaveCommand() {
        super(new String[]{ "leave" }, false, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();

        if(TeamHelper.isInTeam(uuid)) {
            String team = TeamHelper.getTeam(uuid);

            if(TeamHelper.isTeamLeader(uuid)) {
                List<UUID> members = TeamHelper.getTeamMembers(team);
                if(members.size() == 0) {
                    KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".team", "");
                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team, null);

                    KenxTeams.getInstance().getPlayerDataConfig().saveConfig();
                    KenxTeams.getInstance().getTeamDataConfig().saveConfig();

                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "leave.disbanded", team);
                } else {
                    UUID random = members.get((int) (Math.random() * (members.size()+1)));

                    KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".team", "");

                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".leader", random.toString());
                    List<String> uuids = KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".members");
                    for(int i = 0; i < uuids.size(); i++) {
                        if(uuids.get(i).equalsIgnoreCase(random.toString())) {
                            uuids.remove(i);
                            break;
                        }
                    }
                    for(int i = 0; i < uuids.size(); i++) {
                        if(uuids.get(i).equalsIgnoreCase(uuid.toString())) {
                            uuids.remove(i);
                            break;
                        }
                    }
                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".members", uuids);

                    KenxTeams.getInstance().getPlayerDataConfig().saveConfig();
                    KenxTeams.getInstance().getTeamDataConfig().saveConfig();

                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "leave.transfer", team, random.toString());
                }
            } else {
                List<String> uuids = KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".members");
                for(int i = 0; i < uuids.size(); i++) {
                    if(uuids.get(i).equalsIgnoreCase(uuid.toString())) {
                        uuids.remove(i);
                        break;
                    }
                }

                KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".team", "");
                KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".members", uuids);

                KenxTeams.getInstance().getPlayerDataConfig().saveConfig();
                KenxTeams.getInstance().getTeamDataConfig().saveConfig();

                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "leave.leave", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-in-team");
        }
    }
}
