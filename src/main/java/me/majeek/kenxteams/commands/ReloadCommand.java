package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class ReloadCommand implements SubCommand {
    @Override
    public String[] getName() {
        return new String[]{ "reload" };
    }

    @Override
    public String getPermission() {
        return "kenxteams.reload";
    }

    @Override
    public boolean allowConsole() {
        return true;
    }

    @Override
    public int requiredArgs() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        KenxTeams.getInstance().getMainConfig().reloadConfig();
        KenxTeams.getInstance().getMessagesConfig().reloadConfig();

        String content = Objects.requireNonNull(KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString("reload"));
        KenxTeams.getInstance().getCommandManager().sendMessage(sender, content);
    }
}
