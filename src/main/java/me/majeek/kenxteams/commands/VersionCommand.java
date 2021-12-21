package me.majeek.kenxteams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class VersionCommand extends SubCommand {
    public VersionCommand() {
        super(new String[]{ "version" }, true, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &eAuthor &7- &aMajeek"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &eFiverr &7- &ahttps://fiverr.com/majeek_"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &eGithub &7- &ahttps://github.com/AidenCooper"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &eVersion &7- &a1.1"));
    }
}
