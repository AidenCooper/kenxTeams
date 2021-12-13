package me.majeek.kenxteams.commands;

import org.bukkit.command.CommandSender;

public class SubCommand {
    private final String[] name;
    private final boolean allowConsole;
    private final int args;

    public SubCommand(String[] name, boolean allowConsole, int args) {
        this.name = name;
        this.allowConsole = allowConsole;
        this.args = args;
    }

    public String[] getName() {
        return this.name;
    }

    public String getPermission() {
        StringBuilder builder = new StringBuilder("kenxteams");
        for(String string : this.name) {
            builder.append(".").append(string);
        }
        return builder.toString();
    }

    public boolean allowConsole() {
        return this.allowConsole;
    }

    public int requiredArgs() {
        return this.args;
    }

    public void execute(CommandSender sender, String[] args) {

    }
}
