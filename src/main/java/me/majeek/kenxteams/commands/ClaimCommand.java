package me.majeek.kenxteams.commands;

import com.google.common.collect.ImmutableList;
import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.UUID;

public class ClaimCommand extends SubCommand {
    public ClaimCommand() {
        super(new String[]{ "claim" }, false, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();
        String team = TeamHelper.getTeam(uuid);

        if(team != null) {
            if (TeamHelper.isTeamLeader(uuid)) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        int radius = 0;

                        if(args.length > 0) {
                            try {
                                int rad = Integer.parseInt(args[0]);
                                if(rad < 0) {
                                    rad *= -1;
                                }

                                radius = rad;
                            } catch (NumberFormatException exception) {
                                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.invalid-arguments");
                                return;
                            }
                        }

                        String world = ((Player) sender).getWorld().getName();
                        Chunk chunk = ((Player) sender).getLocation().getChunk();
                        ImmutableList<Integer> location = ImmutableList.copyOf(Arrays.asList(chunk.getX(), chunk.getZ()));
                        for (int x = -radius; x <= radius; x++) {
                            for (int z = -radius; z <= radius; z++) {
                                ImmutableList<Integer> newChunk = ImmutableList.copyOf(Arrays.asList(location.get(0) + x, location.get(1) + z));

                                String result = TeamHelper.getTeamFromChunk(newChunk, world);
                                if(result == null) {
                                    if(KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("team-chunk-limit") >= TeamHelper.getChunkList(team).size()) {
                                        TeamHelper.claimChunk(team, newChunk, world);

                                        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claim.claimed", team, newChunk.get(0).toString(), newChunk.get(1).toString(), world);
                                    } else {
                                        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claim.over-limit", team);
                                        return;
                                    }
                                } else if (result.equalsIgnoreCase(TeamHelper.getTeam(uuid))) {
                                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claim.owns", team, newChunk.get(0).toString(), newChunk.get(1).toString(), world);
                                } else {
                                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claim.already-claimed", result, newChunk.get(0).toString(), newChunk.get(1).toString(), world);
                                }
                            }
                        }

                        TeamHelper.updatePoints(team);
                    }
                }.runTask(KenxTeams.getInstance());
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claim.not-leader", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claim.not-in-team");
        }
    }
}
