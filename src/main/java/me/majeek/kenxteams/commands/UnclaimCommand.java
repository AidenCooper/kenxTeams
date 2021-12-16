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

public class UnclaimCommand extends SubCommand {
    public UnclaimCommand() {
        super(new String[]{ "unclaim" }, false, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();
        String team = TeamHelper.getTeam(uuid);

        if(team != null) {
            if(TeamHelper.isTeamLeader(uuid)) {
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

                        int count = 0;
                        Chunk chunk = ((Player) sender).getLocation().getChunk();
                        ImmutableList<Integer> location = ImmutableList.copyOf(Arrays.asList(chunk.getX(), chunk.getZ()));
                        for (int x = -radius; x <= radius; x++) {
                            for (int z = -radius; z <= radius; z++) {
                                ImmutableList<Integer> newChunk = ImmutableList.copyOf(Arrays.asList(location.get(0) + x, location.get(1) + z));

                                String result = TeamHelper.getTeamFromChunk(newChunk);
                                if(result != null && result.equalsIgnoreCase(team)) {
                                    count++;

                                    TeamHelper.unclaimChunk(newChunk);

                                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "unclaim.unclaimed", team, newChunk.get(0).toString(), newChunk.get(1).toString());
                                }
                            }
                        }

                        if(count == 0) {
                            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "unclaim.no-claims", team);
                        }
                    }
                }.runTaskAsynchronously(KenxTeams.getInstance());
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "unclaim.not-leader", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "unclaim.not-in-team");
        }
    }
}
