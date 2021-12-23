package me.majeek.kenxteams.commands;

import com.google.common.collect.ImmutableList;
import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import net.minecraft.util.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ClaimsCommand extends SubCommand {
    public ClaimsCommand() {
        super(new String[]{ "claims" }, false, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();
        String team = TeamHelper.getTeam(uuid);

        if(team != null) {
            if (TeamHelper.isTeamLeader(uuid)) {
                int number = KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("pagination.top");
                int page = 1;
                if(args.length > 0) {
                    try {
                        page = Integer.parseInt(args[0]);
                    } catch (NumberFormatException ignored) {}
                }

                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claims.title", Integer.toString(page));
                List<Pair<ImmutableList<Integer>, String>> chunks = TeamHelper.getChunkList(team);
                for(int i = (page - 1) * number; i >= 0 && i < ((page - 1) * number) + number && i < chunks.size(); i++) {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "claims.claim-item", team, chunks.get(i).getKey().get(0).toString(), chunks.get(i).getKey().get(1).toString(), chunks.get(i).getValue(), Integer.toString(i + 1));
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-leader", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "error.not-in-team");
        }
    }
}
