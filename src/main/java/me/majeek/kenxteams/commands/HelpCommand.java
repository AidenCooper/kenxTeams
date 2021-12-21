package me.majeek.kenxteams.commands;

import com.google.common.collect.Lists;
import me.majeek.kenxteams.KenxTeams;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends SubCommand {
    public HelpCommand() {
        super(new String[]{"help"}, true, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        int number = KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("pagination.help");
        int page = 1;
        if(args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        List<String> commands = new ArrayList<>(KenxTeams.getInstance().getMessagesConfig().getConfiguration().getConfigurationSection("help").getValues(false).keySet());
        List<String> filtered = Lists.newArrayList();
        boolean permissionBased = KenxTeams.getInstance().getMainConfig().getConfiguration().getBoolean("permission-based-help-command");

        for(String command : commands) {
            if(command.equals("title")) {
                continue;
            }

            String[] name = command.split("-");
            if(permissionBased && !sender.hasPermission(KenxTeams.getInstance().getCommandManager().getCommand(name).getPermission())) {
                continue;
            }

            filtered.add(command);
        }

        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "help.title", Integer.toString(page));
        for(int i = (page - 1) * number; i >= 0 && i < ((page - 1) * number) + number && i < filtered.size(); i++) {
            KenxTeams.getInstance().getCommandManager().sendMessage(sender, "help." + filtered.get(i));
        }
    }
}
