package me.majeek.kenxteams.managers;

import com.google.common.collect.Sets;
import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.commands.HelpCommand;
import me.majeek.kenxteams.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class CommandManager implements CommandExecutor {
    private final Set<SubCommand> subCommands = Sets.newHashSet();

    public CommandManager(final String name, Set<SubCommand> subCommands) {
        this.subCommands.addAll(subCommands);

        Objects.requireNonNull(KenxTeams.getInstance().getCommand(name)).setExecutor(this);
    }

    public SubCommand getCommand(Class<?> instance) {
        for (SubCommand subCommand : this.subCommands) {
            if (subCommand.getClass().isAssignableFrom(instance)) {
                return subCommand;
            }
        }

        return null;
    }

    public Set<SubCommand> getSubCommands() {
        return this.subCommands;
    }

    public void sendMessage(CommandSender sender, String content) {
        String formatted = ChatColor.translateAlternateColorCodes('&', content.replace("{prefix}", KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString("prefix")));

        if(formatted.length() != 0) {
            sender.sendMessage(formatted);
        }
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final boolean isPlayer = commandSender instanceof Player;

        if(strings.length == 0) {
            for(SubCommand subCommand : this.subCommands) {
                if(subCommand instanceof HelpCommand) {
                    subCommand.execute(commandSender, strings);
                }
            }
        } else {
            for(SubCommand subCommand : this.subCommands) {
                if(Arrays.equals(Arrays.copyOfRange(strings, 0, subCommand.getName().length), subCommand.getName())) {
                    if(isPlayer || subCommand.allowConsole()) {
                        if(commandSender.hasPermission(subCommand.getPermission())) {
                            if(strings.length - subCommand.getName().length >= subCommand.requiredArgs()){
                                subCommand.execute(commandSender, Arrays.copyOfRange(strings, subCommand.getName().length, strings.length));
                                return true;
                            } else {
                                String content = Objects.requireNonNull(KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString("error.no-arguments"));
                                this.sendMessage(commandSender, content);
                                return false;
                            }
                        } else {
                            String content = Objects.requireNonNull(KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString("error.no-permission"));
                            this.sendMessage(commandSender, content);
                            return false;
                        }
                    } else {
                        String content = Objects.requireNonNull(KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString("error.console-sender"));
                        this.sendMessage(commandSender, content);
                        return false;
                    }
                }
            }

            String content = Objects.requireNonNull(KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString("error.invalid-command"));
            this.sendMessage(commandSender, content);
            return false;
        }

        return true;
    }
}
