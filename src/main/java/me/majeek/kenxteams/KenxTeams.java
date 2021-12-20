package me.majeek.kenxteams;

import com.google.common.collect.Sets;
import me.majeek.kenxteams.commands.*;
import me.majeek.kenxteams.configs.ConfigFile;
import me.majeek.kenxteams.listeners.ChestDataListener;
import me.majeek.kenxteams.listeners.LogExploitListener;
import me.majeek.kenxteams.listeners.PlayerDataListener;
import me.majeek.kenxteams.listeners.SquidSpawnerListener;
import me.majeek.kenxteams.managers.CommandManager;
import me.majeek.kenxteams.managers.EventManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

public final class KenxTeams extends JavaPlugin {
    private static KenxTeams instance;

    private ConfigFile mainConfig = null;
    private ConfigFile messagesConfig = null;
    private ConfigFile playerDataConfig = null;
    private ConfigFile pointsConfig = null;
    private ConfigFile teamDataConfig = null;

    private CommandManager commandManager = null;
    private EventManager eventManager = null;

    @Override
    public void onEnable() {
        instance = this;

        this.mainConfig = new ConfigFile("config").createFile(true).loadConfig();
        this.messagesConfig = new ConfigFile("messages").createFile(true).loadConfig();
        this.playerDataConfig = new ConfigFile("player_data").createFile(false).loadConfig();
        this.pointsConfig = new ConfigFile("points").createFile(true).loadConfig();
        this.teamDataConfig = new ConfigFile("team_data").createFile(false).loadConfig();

        Set<SubCommand> commands = Sets.newHashSet(
                new ClaimCommand(),
                new ClaimsCommand(),
                new CreateCommand(),
                new DeleteCommand(),
                new HelpCommand(),
                new LeaveCommand(),
                new PointsAddCommand(),
                new PointsRemoveCommand(),
                new ReloadCommand(),
                new UnclaimCommand(),
                new VersionCommand()
        );
        Set<Listener> listeners = Sets.newHashSet(
                new ChestDataListener(),
                new LogExploitListener(),
                new PlayerDataListener(),
                new SquidSpawnerListener()
        );

        this.commandManager = new CommandManager("kenxteams", commands);
        this.eventManager = new EventManager(listeners);
    }

    @Override
    public void onDisable() {
        long now = Timestamp.from(Instant.now()).getTime();

        for(Player player : getServer().getOnlinePlayers()) {
            String uuid = player.getUniqueId().toString();

            long milliseconds = now - Timestamp.valueOf(KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid + ".lastLogin")).getTime();
            int minutes = (((int) milliseconds / 1000) % 3600) / 60;

            KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(uuid + ".playtime", minutes + KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getInt(uuid + ".playtime"));
        }

        KenxTeams.getInstance().getPlayerDataConfig().saveConfig();
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

    public ConfigFile getPlayerDataConfig() {
        return this.playerDataConfig;
    }

    public ConfigFile getPointsConfig() {
        return this.pointsConfig;
    }

    public ConfigFile getTeamDataConfig() {
        return this.teamDataConfig;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }
}

// Team invite, join, leave, promote, and chat system
// Raid and scoreboard system