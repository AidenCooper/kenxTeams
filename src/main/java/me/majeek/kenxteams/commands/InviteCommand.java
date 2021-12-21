package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.RequestData;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InviteCommand extends SubCommand {
    public InviteCommand() {
        super(new String[]{ "invite" }, false, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();

        if(TeamHelper.isInTeam(uuid)) {
            String team = TeamHelper.getTeam(uuid);

            if(TeamHelper.isTeamLeader(uuid)) {
                Player target = KenxTeams.getInstance().getServer().getPlayer(args[0]);

                if(target != null) {
                    if(!TeamHelper.isInTeam(target.getUniqueId())) {
                        if(RequestData.invitePlayer(target, team)) {
                            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "invite.invited", team, target.getName());
                        } else {
                            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "invite.already-invited", team, target.getName());
                        }
                    } else {
                        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "invite.in-team", target.getName());
                    }
                } else {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "invite.offline", args[0]);
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-leader", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-in-team");
        }
    }
}
