package me.majeek.kenxteams;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.util.com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.libs.com.google.gson.internal.Pair;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TeamHelper {
    public static int getPoints(String team) {
        return KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getInt(team + ".points.total");
    }

    public static void setPoints(String team, int points) {
        int minimum = KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("points.minimum");
        int maximum = KenxTeams.getInstance().getMainConfig().getConfiguration().getInt("points.maximum");

        if(points > maximum) {
            KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".points.total", maximum);
        } else if(points < minimum) {
            KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".points.total", minimum);
        } else {
            KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".points.total", points);
        }

        KenxTeams.getInstance().getTeamDataConfig().saveConfig();
    }

    public static void updatePoints(String team) {
        List<String> sets = new ArrayList<>(KenxTeams.getInstance().getPointsConfig().getConfiguration().getValues(false).keySet());

        List<ImmutableList<Integer>> chunks = getChestChunks(team);
        List<ImmutableList<Integer>> locations = getChestLocations(team);
        List<String> worlds = getChestWorlds(team);

        int points = 0;
        for(int i = 0; i < chunks.size(); i++) {
            World world = Bukkit.getWorld(worlds.get(i));
            Block block = world.getBlockAt(new Location(world, locations.get(i).get(0), locations.get(i).get(1), locations.get(i).get(2)));
            ItemStack[] items;

            if(block.getType() == Material.CHEST) {
                items = ((Chest) block.getState()).getInventory().getContents();
            } else {
                continue;
            }

            List<HashMap<Material, Integer>> data = Lists.newArrayList();
            for(ItemStack item : items) {
                for (int j = 0; j < sets.size(); j++) {
                    if(data.size() <= j) {
                        data.add(Maps.newHashMap());
                    }

                    Map<String, Object> required = KenxTeams.getInstance().getPointsConfig().getConfiguration().getConfigurationSection(sets.get(j) + ".materials").getValues(false);
                    List<String> keys = new ArrayList<>(required.keySet());
                    for (int k = 0; k < keys.size(); k++) {
                        Material material = Material.valueOf(keys.get(k));

                        data.get(j).putIfAbsent(material, 0);

                        if (item != null && material == item.getType()) {
                            data.get(j).put(material, data.get(j).get(material) + item.getAmount());
                        }
                    }
                }
            }

            for(int j = 0; j < sets.size(); j++) {
                Map<String, Object> required = KenxTeams.getInstance().getPointsConfig().getConfiguration().getConfigurationSection(sets.get(j) + ".materials").getValues(false);
                List<String> keys = new ArrayList<>(required.keySet());

                List<Integer> total = Lists.newArrayList();
                for (int k = 0; k < keys.size(); k++) {
                    Material material = Material.valueOf(keys.get(k));
                    int amount = (int) required.get(keys.get(k));

                    total.add(data.get(j).get(material) / amount);
                }

                points += (KenxTeams.getInstance().getPointsConfig().getConfiguration().getInt(sets.get(j) + ".points") * Collections.min(total));
            }
        }

        setPoints(team, points + KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getInt(team + ".points.modifier"));
    }

    public static void addChest(String team, Location location) {
        String formatted = location.getChunk().getX() + ":" + location.getChunk().getZ() + ";" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ";" + location.getWorld().getName();

        List<String> chests = KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".chests");
        chests.add(formatted);

        KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".chests", chests);
        KenxTeams.getInstance().getTeamDataConfig().saveConfig();
    }

    public static void removeChest(String team, Location location) {
        String formatted = location.getChunk().getX() + ":" + location.getChunk().getZ() + ";" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ";" + location.getWorld().getName();

        List<String> chests = KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".chests");
        for(int i = 0; i < chests.size(); i++) {
            if(chests.get(i).equals(formatted)) {
                chests.remove(i);
                break;
            }
        }

        KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".chests", chests);
        KenxTeams.getInstance().getTeamDataConfig().saveConfig();
    }

    public static List<ImmutableList<Integer>> getChestChunks(String team) {
        List<String> unformatted = KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".chests");
        List<ImmutableList<Integer>> formatted = Lists.newArrayList();

        for(String chest : unformatted) {
            String[] separated = chest.split(";")[0].split(":");

            formatted.add(ImmutableList.copyOf(Arrays.asList(Integer.parseInt(separated[0]), Integer.parseInt(separated[1]))));
        }

        return formatted;
    }

    public static List<ImmutableList<Integer>> getChestLocations(String team) {
        List<String> unformatted = KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".chests");
        List<ImmutableList<Integer>> formatted = Lists.newArrayList();

        for(String chest : unformatted) {
            String[] separated = chest.split(";")[1].split(":");

            formatted.add(ImmutableList.copyOf(Arrays.asList(Integer.parseInt(separated[0]), Integer.parseInt(separated[1]), Integer.parseInt(separated[2]))));
        }

        return formatted;
    }

    public static List<String> getChestWorlds(String team) {
        List<String> unformatted = KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".chests");
        List<String> formatted = Lists.newArrayList();

        for(String chest : unformatted) {
            formatted.add(chest.split(";")[2]);
        }

        return formatted;
    }

    public static void claimChunk(String team, ImmutableList<Integer> playerChunk, String world) {
        List<String> formatted = KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".claims");
        formatted.add(String.valueOf(playerChunk.get(0)) + ':' + playerChunk.get(1) + ';' + world);

        KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".claims", formatted);
        KenxTeams.getInstance().getTeamDataConfig().saveConfig();
    }

    public static void unclaimChunk(ImmutableList<Integer> chunk, String world) {
        for(String team : TeamHelper.getTeamList()) {
            List<Pair<ImmutableList<Integer>, String>> data = TeamHelper.getChunkList(team);

            for(int i = 0; i < data.size(); i++) {
                if(data.get(i).first.get(0).equals(chunk.get(0)) && data.get(i).first.get(1).equals(chunk.get(1)) && data.get(i).second.equalsIgnoreCase(world)) {
                    List<ImmutableList<Integer>> chest = getChestChunks(team);
                    for(int j = 0; j < chest.size(); j++) {
                        ImmutableList<Integer> key = data.get(i).first;
                        ImmutableList<Integer> chestChunk = chest.get(j);

                        if(key.get(0).equals(chestChunk.get(0)) && key.get(1).equals(chestChunk.get(1)) && data.get(i).second.equalsIgnoreCase(getChestWorlds(team).get(j))) {
                            ImmutableList<Integer> location = getChestLocations(team).get(j);
                            removeChest(team, new Location(Bukkit.getWorld(world), location.get(0), location.get(1), location.get(2)));

                            if(getChestChunks(team).size() == 0) {
                                break;
                            } else {
                                j--;
                            }
                        }
                    }

                    data.remove(i);

                    List<String> formatted = Lists.newArrayList();
                    for(Pair<ImmutableList<Integer>, String> pair : data) {
                        formatted.add(String.valueOf(pair.first.get(0)) + ':' + pair.first.get(1) + ';' + pair.first);
                    }

                    KenxTeams.getInstance().getTeamDataConfig().getConfiguration().set(team + ".claims", formatted);
                    KenxTeams.getInstance().getTeamDataConfig().saveConfig();

                    updatePoints(team);

                    break;
                }
            }
        }
    }

    public static List<Pair<ImmutableList<Integer>, String>> getChunkList(String team) {
        List<String> unformatted = KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getStringList(team + ".claims");

        List<Pair<ImmutableList<Integer>, String>> data = Lists.newArrayList();

        for(String chunk : unformatted) {
            String[] chunkSeparated = chunk.split(";")[0].split(":");
            String world = chunk.split(";")[1];

            data.add(new Pair<>(ImmutableList.copyOf(Arrays.asList(Integer.parseInt(chunkSeparated[0]), Integer.parseInt(chunkSeparated[1]))), world));
        }

        return data;
    }

    public static String getTeam(UUID uuid) {
        String team = KenxTeams.getInstance().getPlayerDataConfig().getConfiguration().getString(uuid.toString() + ".team");

        return team.equals("") ? null : team;
    }

    public static String getTeamFromChunk(ImmutableList<Integer> chunk, String world) {
        for(String team : TeamHelper.getTeamList()) {
            for(Pair<ImmutableList<Integer>, String> pair : TeamHelper.getChunkList(team)) {
                if(pair.first.get(0).equals(chunk.get(0)) && pair.first.get(1).equals(chunk.get(1)) && pair.second.equalsIgnoreCase(world)) {
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
        for(String name : KenxTeams.getInstance().getTeamDataConfig().getConfiguration().getValues(false).keySet()) {
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