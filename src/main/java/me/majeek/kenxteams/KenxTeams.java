package me.majeek.kenxteams;

import com.google.common.collect.Sets;
import me.majeek.kenxteams.commands.HelpCommand;
import me.majeek.kenxteams.commands.ReloadCommand;
import me.majeek.kenxteams.commands.SubCommand;
import me.majeek.kenxteams.configs.ConfigFile;
import me.majeek.kenxteams.managers.CommandManager;
import me.majeek.kenxteams.managers.EventManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public final class KenxTeams extends JavaPlugin {
    private static KenxTeams instance;

    private ConfigFile mainConfig = null;
    private ConfigFile messagesConfig = null;

    private CommandManager commandManager = null;
    private EventManager eventManager = null;

    @Override
    public void onEnable() {
        instance = this;

        this.mainConfig = new ConfigFile("config").createFile(true).loadConfig();
        this.messagesConfig = new ConfigFile("messages").createFile(true).loadConfig();

        Set<SubCommand> commands = Sets.newHashSet(
                new HelpCommand(),
                new ReloadCommand()
        );
        Set<Listener> listeners = Sets.newHashSet(

        );

        this.commandManager = new CommandManager("kenxteams", commands);
        this.eventManager = new EventManager(listeners);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static KenxTeams getInstance() {
        return instance;
    }

    public ConfigFile getMainConfig() {
        return this.mainConfig;
    }

    public ConfigFile getMessagesConfig() {
        return this.messagesConfig;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }
}
