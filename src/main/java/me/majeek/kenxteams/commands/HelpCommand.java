package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.Objects;

public class HelpCommand implements SubCommand {
    @Override
    public String[] getName() {
        return new String[]{ "help" };
    }

    @Override
    public String getPermission() {
        return "kenxteams.help";
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
        Map<String, Object> content = Objects.requireNonNull(KenxTeams.getInstance().getMessagesConfig().getConfiguration().getConfigurationSection("help")).getValues(true);

        for(Object item : content.values()){
            if(item instanceof String) {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, (String) item);
            }
        }
    }
}
