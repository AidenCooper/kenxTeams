package me.majeek.kenxteams;

import me.majeek.kenxteams.commands.*;
import me.majeek.kenxteams.configs.ConfigFile;
import me.majeek.kenxteams.listeners.*;
import me.majeek.kenxteams.managers.RaidManager;
import me.majeek.kenxteams.managers.CommandManager;
import me.majeek.kenxteams.managers.EventManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class KenxTeams extends JavaPlugin {
    private static KenxTeams instance;

    private boolean placeholderEnabled;

    private ConfigFile mainConfig = null;
    private ConfigFile messagesConfig = null;
    private ConfigFile playerDataConfig = null;
    private ConfigFile pointsConfig = null;
    private ConfigFile scoreboardConfig = null;
    private ConfigFile teamDataConfig = null;

    private CommandManager commandManager = null;
    private EventManager eventManager = null;
    private RaidManager raidManager = null;

    @Override
    public void onEnable() {
        instance = this;

        this.placeholderEnabled = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

        this.mainConfig = new ConfigFile("config").createFile(true).loadConfig();
        this.messagesConfig = new ConfigFile("messages").createFile(true).loadConfig();
        this.playerDataConfig = new ConfigFile("player_data").createFile(false).loadConfig();
        this.pointsConfig = new ConfigFile("points").createFile(true).loadConfig();
        this.scoreboardConfig = new ConfigFile("scoreboard").createFile(true).loadConfig();
        this.teamDataConfig = new ConfigFile("team_data").createFile(false).loadConfig();

        Set<SubCommand> commands = new HashSet<>(Arrays.asList(
                new AcceptCommand(),
                new ChatCommand(),
                new ClaimCommand(),
                new ClaimsCommand(),
                new CreateCommand(),
                new DeclineCommand(),
                new DeleteCommand(),
                new HelpCommand(),
                new InviteCommand(),
                new JoinCommand(),
                new LeaveCommand(),
                new PointsAddCommand(),
                new PointsModifierCommand(),
                new PointsRemoveCommand(),
                new PointsTotalCommand(),
                new PromoteCommand(),
                new RaidStartCommand(),
                new RaidStopCommand(),
                new ReloadCommand(),
                new TopCommand(),
                new UnclaimCommand(),
                new VersionCommand()
        ));
        Set<Listener> listeners = new HashSet<>(Arrays.asList(
                new ChatListener(),
                new ChestDataListener(),
                new LogExploitListener(),
                new PlayerDataListener(),
                new RaidListener(),
                new SquidSpawnListener()
        ));

        this.commandManager = new CommandManager("kenxteams", commands);
        this.eventManager = new EventManager(listeners);
        this.raidManager = new RaidManager();
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

    public boolean isPlaceholderEnabled() {
        return this.placeholderEnabled;
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

    public ConfigFile getScoreboardConfig() {
        return this.scoreboardConfig;
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

    public RaidManager getRaidManager() {
        return this.raidManager;
    }
}

// Team invite, join, leave, promote, and chat system
// Raid and scoreboard system