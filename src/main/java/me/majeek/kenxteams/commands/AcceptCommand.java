package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.RequestData;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AcceptCommand extends SubCommand {
    public AcceptCommand() {
        super(new String[]{ "accept" }, false, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();

        if(TeamHelper.isInTeam(uuid)) {
            if(TeamHelper.isTeamLeader(uuid)) {
                Player target = KenxTeams.getInstance().getServer().getPlayer(args[0]);

                if(target != null) {
                    if(!TeamHelper.isInTeam(target.getUniqueId())) {
                        if (RequestData.acceptPlayer(target, TeamHelper.getTeam(uuid))) {
                            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "accept.accepted-join", TeamHelper.getTeam(uuid), target.getName());
                        } else {
                            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "accept.no-join-request", target.getName());
                        }
                    } else {
                        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "accept.in-team", target.getName());
                    }
                } else {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "accept.offline");
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-leader");
            }
        } else {
            String team = args[0];

            if(TeamHelper.getTeamList().contains(team)) {
                if(RequestData.acceptTeam((Player) sender, team)) {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "accept.accepted-invite", team);
                } else {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "accept.no-invite-request", team);
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "accept.not-team", team);
            }
        }
    }
}
