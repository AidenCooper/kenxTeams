package me.majeek.kenxteams.commands;

import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChatCommand extends SubCommand {
    public ChatCommand() {
        super(new String[]{ "chat" }, false, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();

        String chat;
        String current = KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid + ".chat");
        if(current.equalsIgnoreCase("global")) {
            String team = TeamHelper.getTeam(uuid);
            if(team == null) {
                chat = "Global";
            } else {
                chat = team;
            }
        } else {
            chat = "Global";
        }

        KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".chat", chat);
        KenxTeams.getInstance().getPlayerDataConfig().saveConfig();

        KenxTeams.getInstance().getCommandManager().sendMessage(sender, "chat.switched", chat);
    }
}
