package me.majeek.kenxteams.commands;

import com.google.common.collect.Maps;
import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;

import java.util.*;

public class TopCommand extends SubCommand {
    public TopCommand() {
        super(new String[]{ "top" }, true, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        HashMap<String, Integer> temp = Maps.newHashMap();
        for(String team : TeamHelper.getTeamList()) {
            temp.put(team, TeamHelper.getPoints(team));
        }

        List<Map.Entry<String, Integer>> sort = new LinkedList<>(temp.entrySet());
        sort.sort(Map.Entry.comparingByValue());
        HashMap<String, Integer> data = new LinkedHashMap<>();
        for(Map.Entry<String, Integer> element : sort) {
            data.put(element.getKey(), element.getValue());
        }

        int number = KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("pagination.top");
        int page = 1;
        if(args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "top.title", Integer.toString(page));
        List<String> keys = new ArrayList<>(data.keySet());
        List<Integer> values = new ArrayList<>(data.values());
        for(int i = (page - 1) * number; i >= 0 && i < ((page - 1) * number) + number && i < keys.size(); i++) {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "top.each-team", keys.get(i), Integer.toString(i + 1), Integer.toString(values.get(i)));
        }
    }
}
