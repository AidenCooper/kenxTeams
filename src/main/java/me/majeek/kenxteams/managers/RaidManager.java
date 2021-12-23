package me.majeek.kenxteams.managers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.clip.placeholderapi.PlaceholderAPI;
import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.TeamHelper;
import me.majeek.kenxteams.board.FastBoard;
import net.minecraft.util.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

public class RaidManager {
    private final HashMap<UUID, FastBoard> boards = Maps.newHashMap();
    private final List<Pair<String, String>> raids = Lists.newArrayList();
    private final List<Integer> countdowns = Lists.newArrayList();

    public RaidManager() {
        KenxTeams.getInstance().getServer().getScheduler().runTaskTimer(KenxTeams.getInstance(), () -> {
            for(FastBoard board : this.boards.values()) {
                int index = getRaidIndex(TeamHelper.getTeam(board.getPlayer().getUniqueId()));
                Pair<String, String> raid = raids.get(index);
                int countdown = countdowns.get(index);

                String title = KenxTeams.getInstance().getScoreboardConfig().getConfiguration().getString("title");
                List<String> lines = KenxTeams.getInstance().getScoreboardConfig().getConfiguration().getStringList("lines");

                if(KenxTeams.getInstance().isPlaceholderEnabled()) {
                    title = PlaceholderAPI.setPlaceholders(board.getPlayer(), title);
                }
                title = ChatColor.translateAlternateColorCodes('&', title);

                for(int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);

                    if(countdown == 0) {
                        line = line.replace("{countdown}", "&cFIGHT");
                    } else {
                        line = line.replace("{countdown}", Integer.toString(countdown));
                    }
                    line = line.replace("{attacker}", raid.getKey()).replace("{defender}", raid.getValue());
                    line = PlaceholderAPI.setPlaceholders(board.getPlayer(), line);
                    line = ChatColor.translateAlternateColorCodes('&', line);

                    lines.set(i, line);
                }

                board.updateTitle(title);
                board.updateLines(lines);
            }

            for(int i = 0; i < countdowns.size(); i++) {
                if(countdowns.get(i) > 0) {
                    countdowns.set(i, countdowns.get(i) - 1);
                }
            }
        }, 0, 20);
    }

    public void startRaid(String attacker, String defender) {
        Pair<String, String> pair = Pair.of(attacker, defender);
        raids.add(pair);
        countdowns.add(KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("timer.raid"));

        List<UUID> all = TeamHelper.getTeamMembers(attacker); all.addAll(TeamHelper.getTeamMembers(defender)); all.addAll(Arrays.asList(TeamHelper.getTeamLeader(attacker), TeamHelper.getTeamLeader(defender)));
        for(UUID member : all) {
            Player player = KenxTeams.getInstance().getServer().getPlayer(member);

            if(player !=  null) {
                addBoard(player);
            }
        }
    }

    public void stopRaid(String attacker, String defender) {
        int index = getRaidIndex(attacker);

        raids.remove(index);
        countdowns.remove(index);

        List<UUID> all = TeamHelper.getTeamMembers(attacker); all.addAll(TeamHelper.getTeamMembers(defender)); all.addAll(Arrays.asList(TeamHelper.getTeamLeader(attacker), TeamHelper.getTeamLeader(defender)));
        for(UUID member : all) {
            Player player = KenxTeams.getInstance().getServer().getPlayer(member);

            if(player !=  null) {
                removeBoard(player);
            }
        }
    }

    public boolean onCooldown(String team) {
        return Timestamp.from(Instant.now()).getTime() < Timestamp.valueOf(KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getString(team + ".raid")).getTime();
    }

    public int getRaidIndex(String team) {
        for(int i = 0; i < raids.size(); i++) {
            if(team.equals(raids.get(i).getKey()) || team.equals(raids.get(i).getValue())) {
                return i;
            }
        }

        return -1;
    }

    public boolean isInRaid(String team) {
        for(Pair<String, String> pair : raids) {
            if(team.equals(pair.getKey()) || team.equals(pair.getValue())) {
                return true;
            }
        }

        return false;
    }

    public List<Pair<String, String>> getRaids() {
        return this.raids;
    }

    public void addBoard(Player player) {
        FastBoard board = new FastBoard(player);
        this.boards.put(player.getUniqueId(), board);
    }

    public void removeBoard(Player player) {
        FastBoard board = this.boards.remove(player.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }
}
