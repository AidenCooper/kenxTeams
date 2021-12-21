package me.majeek.kenxteams;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RequestData {
    private static final HashMap<String, List<UUID>> inviteList = Maps.newHashMap();
    private static final HashMap<String, List<UUID>> joinList = Maps.newHashMap();

    public static boolean acceptPlayer(Player player, String team) {
        if(joinList.get(team) != null && joinList.get(team).contains(player.getUniqueId())) {
            List<UUID> uuids = joinList.get(team);
            uuids.remove(player.getUniqueId());
            joinList.put(team, uuids);

            List<UUID> members = TeamHelper.getTeamMembers(team);
            members.add(player.getUniqueId());

            KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(player.getUniqueId() + ".team", team);
            KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".members", members.stream().map(UUID::toString).collect(Collectors.toList()));

            KenxTeams.getInstance().getPlayerDataConfig().saveConfig();
            KenxTeams.getInstance().getTeamDataConfig().saveConfig();

            return true;
        }

        return false;
    }

    public static boolean acceptTeam(Player player, String team) {
        if(inviteList.get(team) != null && inviteList.get(team).contains(player.getUniqueId())) {
            List<UUID> uuids = inviteList.get(team);
            uuids.remove(player.getUniqueId());
            inviteList.put(team, uuids);

            List<UUID> members = TeamHelper.getTeamMembers(team);
            members.add(player.getUniqueId());

            KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().set(player.getUniqueId() + ".team", team);
            KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".members", members.stream().map(UUID::toString).collect(Collectors.toList()));

            KenxTeams.getInstance().getPlayerDataConfig().saveConfig();
            KenxTeams.getInstance().getTeamDataConfig().saveConfig();

            return true;
        }

        return false;
    }

    public static boolean declinePlayer(Player player, String team) {
        if(joinList.get(team) != null && joinList.get(team).contains(player.getUniqueId())) {
            List<UUID> uuids = joinList.get(team);
            uuids.remove(player.getUniqueId());
            joinList.put(team, uuids);

            return true;
        }

        return false;
    }

    public static boolean declineTeam(Player player, String team) {
        if(inviteList.get(team) != null && inviteList.get(team).contains(player.getUniqueId())) {
            List<UUID> uuids = inviteList.get(team);
            uuids.remove(player.getUniqueId());
            inviteList.put(team, uuids);

            return true;
        }

        return false;
    }

    public static boolean invitePlayer(Player player, String team) {
        if(inviteList.get(team) == null || (inviteList.get(team) != null && !inviteList.get(team).contains(player.getUniqueId()))) {
            List<UUID> uuids = Lists.newArrayList();

            if(inviteList.get(team) != null) {
                uuids = inviteList.get(team);
            }

            uuids.add(player.getUniqueId());
            inviteList.put(team, uuids);

            int seconds = KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("timer.invite");
            KenxTeams.getInstance().getCommandManager().sendMessage(player, "invite.player-invited", team, Integer.toString(seconds));
            KenxTeams.getInstance().getServer().getScheduler().runTaskLater(KenxTeams.getInstance(), () -> {
                if(inviteList.get(team).contains(player.getUniqueId())) {
                    List<UUID> uuids1 = inviteList.get(team);
                    uuids1.remove(player.getUniqueId());
                    inviteList.put(team, uuids1);

                    if(player.isOnline()) {
                        KenxTeams.getInstance().getCommandManager().sendMessage(player, "invite.player-expired", team);
                    }
                }
            }, seconds * 20L);

            return true;
        }

        return false;
    }

    public static boolean joinTeam(Player player, String team) {
        if(joinList.get(team) == null || (joinList.get(team) != null && !joinList.get(team).contains(player.getUniqueId()))) {
            Player leader = KenxTeams.getInstance().getServer().getPlayer(TeamHelper.getTeamLeader(team));

            List<UUID> uuids = Lists.newArrayList();

            if(joinList.get(team) != null) {
                uuids = joinList.get(team);
            }

            uuids.add(player.getUniqueId());
            joinList.put(team, uuids);

            int seconds = KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("timer.join");
            KenxTeams.getInstance().getCommandManager().sendMessage(leader, "join.leader-invited", player.getName(), Integer.toString(seconds));
            KenxTeams.getInstance().getServer().getScheduler().runTaskLater(KenxTeams.getInstance(), () -> {
                if(joinList.get(team).contains(player.getUniqueId())) {
                    List<UUID> uuids1 = joinList.get(team);
                    uuids1.remove(player.getUniqueId());
                    joinList.put(team, uuids1);

                    if(leader.isOnline()) {
                        KenxTeams.getInstance().getCommandManager().sendMessage(leader, "join.leader-expired", player.getName());
                    }
                }
            }, seconds * 20L);

            return true;
        }

        return false;
    }
}
