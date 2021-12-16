package me.majeek.kenxteams.managers;

import com.google.common.collect.Sets;
import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.commands.SubCommand;
import me.majeek.kenxteams.commands.VersionCommand;
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

    public SubCommand getCommand(String[] name) {
        for(SubCommand subCommand : this.subCommands) {
            if(Arrays.equals(subCommand.getName(), name)) {
                return subCommand;
            }
        }

        return null;
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

    public void sendMessage(CommandSender sender, String path, String... info) {
        String[] separatedPath = path.split("\\.");
        String raw = KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString(path);

        String formatted = raw.replace("{prefix}", KenxTeams.getInstance().getMessagesConfig().getConfiguration().getString("prefix"));

        switch (path) {
            case "claims.claim-item":
                formatted = formatted.replace("{count}", info[3]);
            case "claim.already-claimed":
            case "claim.claimed":
            case "claim.owns":
            case "unclaim.unclaimed":
                formatted = formatted.replace("{x}", info[1]);
                formatted = formatted.replace("{z}", info[2]);
            case "claim.over-limit":
            case "claim.not-leader":
            case "claims.claims":
            case "claims.not-in-team":
            case "claims.not-leader":
            case "create.created":
            case "create.exists":
            case "delete.deleted":
            case "delete.not-leader":
            case "unclaim.no-claims":
            case "unclaim.not-leader":
                formatted = formatted.replace("{team}", info[0]);
                break;
            case "create.over-character-limit":
                formatted = formatted.replace("{limit}", info[0]);
                break;
        }

        formatted = ChatColor.translateAlternateColorCodes('&', formatted);

        if(formatted.length() != 0) {
            sender.sendMessage(formatted);
        }
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final boolean isPlayer = commandSender instanceof Player;

        if(strings.length == 0) {
            for(SubCommand subCommand : this.subCommands) {
                if(subCommand instanceof VersionCommand) {
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
                                this.sendMessage(commandSender, "error.no-arguments");
                                return false;
                            }
                        } else {
                            this.sendMessage(commandSender, "error.no-permission");
                            return false;
                        }
                    } else {
                        this.sendMessage(commandSender, "error.console-sender");
                        return false;
                    }
                }
            }

            this.sendMessage(commandSender, "error.invalid-command");

            return false;
        }

        return true;
    }
}
