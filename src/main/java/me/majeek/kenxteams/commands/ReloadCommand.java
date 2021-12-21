package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {
    public ReloadCommand() {
        super(new String[]{ "reload" }, true, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        KenxTeams.getInstance().getMainConfig().reloadConfig();
        KenxTeams.getInstance().getMessagesConfig().reloadConfig();
        KenxTeams.getInstance().getPlayerDataConfig().reloadConfig();
        KenxTeams.getInstance().getPointsConfig().reloadConfig();
        KenxTeams.getInstance().getScoreboardConfig().reloadConfig();
        KenxTeams.getInstance().getTeamDataConfig().reloadConfig();

        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "reload");
    }
}
