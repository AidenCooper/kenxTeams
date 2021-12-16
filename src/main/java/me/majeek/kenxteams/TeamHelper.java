package me.majeek.kenxteams;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.*;

public class TeamHelper {
    public static void claimChunk(String team, ImmutableList<Integer> playerChunk) {
        List<ImmutableList<Integer>> chunks = TeamHelper.getChunkList(team);
        chunks.add(playerChunk);

        List<String> formatted = Lists.newArrayList();
        for(ImmutableList<Integer> chunk : chunks) {
            formatted.add(String.valueOf(chunk.get(0)) + ':' + chunk.get(1));
        }

        KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".claims", formatted);
        KenxTeams.getInstance().getTeamDataConfig().saveConfig();
    }

    public static void unclaimChunk(ImmutableList<Integer> chunk) {
        for(String team : TeamHelper.getTeamList()) {
            List<ImmutableList<Integer>> chunks = TeamHelper.getChunkList(team);

            for(int i = 0; i < chunks.size(); i++) {
                if(chunks.get(i).get(0).equals(chunk.get(0)) && chunks.get(i).get(1).equals(chunk.get(1))) {
                    chunks.remove(i);

                    List<String> formatted = Lists.newArrayList();
                    for(ImmutableList<Integer> temp : chunks) {
                        formatted.add(String.valueOf(temp.get(0)) + ':' + temp.get(1));
                    }

                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".claims", formatted);
                    KenxTeams.getInstance().getTeamDataConfig().saveConfig();

                    return;
                }
            }
        }
    }

    public static List<ImmutableList<Integer>> getChunkList(String team) {
        List<String> unformatted = KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".claims");
        List<ImmutableList<Integer>> formatted = Lists.newArrayList();

        for(String chunk : unformatted) {
            String[] separated = chunk.split(":");

            formatted.add(ImmutableList.copyOf(Arrays.asList(Integer.parseInt(separated[0]), Integer.parseInt(separated[1]))));
        }

        return formatted;
    }

    public static String getTeam(UUID uuid) {
        String team = KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid.toString() + ".team");

        return team.equals("") ? null : team;
    }

    public static String getTeamFromChunk(ImmutableList<Integer> chunk) {
        for(String team : TeamHelper.getTeamList()) {
            for(ImmutableList<Integer> location : TeamHelper.getChunkList(team)) {
                if(location.get(0).equals(chunk.get(0)) && location.get(1).equals(chunk.get(1))) {
                    return team;
                }
            }
        }

        return null;
    }

    public static UUID getTeamLeader(String team) {
        return UUID.fromString(KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getString(team + ".leader"));
    }

    public static List<UUID> getTeamMembers(String team) {
        List<UUID> members = Lists.newArrayList();
        for(String uuid : KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".members")) {
            members.add(UUID.fromString(uuid));
        }

        return members;
    }

    public static Set<String> getTeamList() {
        return KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getValues(false).keySet();
    }

    public static boolean isTeam(String team) {
        for(String name : KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getValues(true).keySet()) {
            if(name.equalsIgnoreCase(team)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isTeamLeader(UUID uuid) {
        return TeamHelper.isInTeam(uuid) && KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getString(TeamHelper.getTeam(uuid) + ".leader").equals(uuid.toString());
    }

    public static boolean isTeamLeader(UUID uuid, String team) {
        if(TeamHelper.isInTeam(uuid)) {
            for (String name : TeamHelper.getTeamList()) {
                if (name.equalsIgnoreCase(team)) {
                    if(KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(team + ".leader").equals(uuid.toString())) {
                        return true;
                    }

                    break;
                }
            }
        }

        return false;
    }

    public static boolean isInTeam(UUID uuid) {
        return TeamHelper.getTeam(uuid) != null;
    }

    public static boolean isInTeam(UUID uuid, String team) {
        if(TeamHelper.isInTeam(uuid)) {
            for (String name : TeamHelper.getTeamList()) {
                if (name.equalsIgnoreCase(team)) {
                    if(KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(team + ".leader").equals(uuid.toString())) {
                        return true;
                    } else if(KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getStringList(team + ".members").contains(uuid.toString())) {
                        return true;
                    }

                    break;
                }
            }
        }

        return false;
    }
}
