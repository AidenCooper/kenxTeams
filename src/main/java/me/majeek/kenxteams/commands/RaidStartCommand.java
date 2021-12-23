package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RaidStartCommand extends SubCommand {
    public RaidStartCommand() {
        super(new String[]{ "raid", "start" }, false, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();

        if(TeamHelper.isInTeam(uuid)) {
            String team = TeamHelper.getTeam(uuid);

            if(TeamHelper.isTeamLeader(uuid)) {
                if(!KenxTeams.getInstance().getRaidManager().isInRaid(team)) {
                    if(TeamHelper.isTeam(args[0]) && !args[0].equalsIgnoreCase(team)) {
                        if(!KenxTeams.getInstance().getRaidManager().onCooldown(args[0])) {
                            KenxTeams.getInstance().getRaidManager().startRaid(team, args[0]);

                            String message = KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString("raid.started").replace("{attacker}", team).replace("{defender}", args[0]);
                            KenxTeams.getInstance().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                        } else {
                            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "raid.on-cooldown", args[0]);
                        }
                    } else {
                        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "raid.not-team");
                    }
                } else {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "raid.in-raid", team);
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-leader", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-in-team");
        }
    }
}
