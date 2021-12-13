package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CreateCommand extends SubCommand {
    public CreateCommand() {
        super(new String[]{ "create" }, false, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String uuid = ((Player) sender).getUniqueId().toString();

        if(KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid + ".team").equals("")) {
            boolean exists = false;
            for(String name : KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getValues(true).keySet()) {
                if(name.equalsIgnoreCase(args[0])) {
                    exists = true;
                    break;
                }
            }

            if(!exists) {
                int limit = KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("team-name-character-limit");

                if(args[0].length() <= limit) {
                    KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".team", args[0]);

                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(args[0] + ".leader", uuid);
                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(args[0] + ".members", new ArrayList<String>());
                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(args[0] + ".points", 0);

                    KenxTeams.getInstance().getPlayerDataConfig().saveConfig();
                    KenxTeams.getInstance().getTeamDataConfig().saveConfig();

                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "create.created", args[0]);
                } else {
                    KenxTeams.getInstance().getCommandManager().sendMessage(sender, "create.over-character-limit", Integer.toString(limit));
                }
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "create.exists", args[0]);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "create.in-team");
        }
    }
}
