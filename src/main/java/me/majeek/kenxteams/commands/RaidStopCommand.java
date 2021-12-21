package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class RaidStopCommand extends SubCommand {
    public RaidStopCommand() {
        super(new String[]{ "raid", "stop" }, false, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();

        if(TeamHelper.isInTeam(uuid)) {
            String team = TeamHelper.getTeam(uuid);

            if(TeamHelper.isTeamLeader(uuid)) {
                if(KenxTeams.getInstance().getRaidManager().isInRaid(team)) {
                    String defending = KenxTeams.getInstance().getRaidManager().getRaids().get(KenxTeams.getInstance().getRaidManager().getRaidIndex(team)).getValue();

                    KenxTeams.getInstance().getRaidManager().stopRaid(team, defending);

                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(defending + ".raid", new Timestamp(Timestamp.from(Instant.now()).getTime() + (60000L * KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("raid-cooldown"))).toString());
                    KenxTeams.getInstance().getTeamDataConfig().saveConfig();

                    String message = KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString("raid.stopped").replace("{attacker}", team).replace("{defender}", defending);
                    KenxTeams.getInstance().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                } else {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "raid.not-in-raid", team);
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-leader", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-in-team");
        }
    }
}
