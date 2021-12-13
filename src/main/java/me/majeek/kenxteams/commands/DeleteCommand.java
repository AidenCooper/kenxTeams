package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand extends SubCommand {
    public DeleteCommand() {
        super(new String[]{ "delete" }, false, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String uuid = ((Player) sender).getUniqueId().toString();

        if(!KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid + ".team").equals("")) {
            String team = KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid + ".team");

            if(KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getString(team + ".leader").equals(uuid)) {
                KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".team", "");
                KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team, null);

                KenxTeams.getInstance().getPlayerDataConfig().saveConfig();
                KenxTeams.getInstance().getTeamDataConfig().saveConfig();

                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "delete.deleted", team);
            } else {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "delete.not-leader", team);
            }
        } else {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "delete.not-in-team");
        }
    }
}
