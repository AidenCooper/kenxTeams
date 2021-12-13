package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SubCommand {
    public HelpCommand() {
        super(new String[]{"help"}, true, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean permissionBased = KenxTeams.getInstance().getMainConfig().getConfiguration().getBoolean("permission-based-help-command");

        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "help.title");
        for(String key : KenxTeams.getInstance().getMessagesConfig().getConfiguration().getConfigurationSection("help").getValues(true).keySet()) {
            if(key.equals("title")) {
                continue;
            }

            String[] name = key.split("-");
            if(!permissionBased || sender.hasPermission(KenxTeams.getInstance().getCommandManager().getCommand(name).getPermission())) {
                KenxTeams.getInstance().getCommandManager().sendMessage(sender, "help." + key);
            }
        }
    }
}
