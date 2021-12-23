package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.RequestData;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DeclineCommand extends SubCommand {
    public DeclineCommand() {
        super(new String[]{ "decline" }, false, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();

        if(TeamHelper.isInTeam(uuid)) {
            if(TeamHelper.isTeamLeader(uuid)) {
                Player target = KenxTeams.getInstance().getServer().getPlayer(args[0]);

                if(target != null) {
                    if(TeamHelper.isInTeam(target.getUniqueId())) {
                        if (RequestData.declinePlayer(target, TeamHelper.getTeam(uuid))) {
                            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "decline.declined-join", TeamHelper.getTeam(uuid), target.getName());
                        } else {
                            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "decline.no-join-request", target.getName());
                        }
                    } else {
                        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "decline.in-team", target.getName());
                    }
                } else {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "decline.offline");
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-leader", TeamHelper.getTeam(uuid));
            }
        } else {
            String team = args[0];

            if(TeamHelper.getTeamList().contains(team)) {
                if(RequestData.declineTeam((Player) sender, team)) {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "decline.declined-invite", team);
                } else {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "decline.no-invite-request", team);
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "decline.not-team", team);
            }
        }
    }
}
