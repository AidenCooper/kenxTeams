package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.RequestData;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JoinCommand extends SubCommand {
    public JoinCommand() {
        super(new String[]{ "join" }, false, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();

        if(!TeamHelper.isInTeam(uuid)) {
            if(TeamHelper.getTeamList().contains(args[0])) {
                String team = args[0];
                Player leader = KenxTeams.getInstance().getServer().getPlayer(TeamHelper.getTeamLeader(team));

                if(leader != null) {
                    if(RequestData.joinTeam((Player) sender, team)) {
                        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "join.invited", team);
                    } else {
                        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "join.already-invited", team);
                    }
                } else {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "join.offline", KenxTeams.getInstance().getServer().getOfflinePlayer(TeamHelper.getTeamLeader(team)).getName());
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "join.not-team");
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "join.in-team");
        }
    }
}
