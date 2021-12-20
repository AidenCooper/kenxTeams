package me.majeek.kenxteams.listeners;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import me.majeek.kenxteams.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class ChestDataListener implements Listener {
    private final HashMap<UUID, ImmutableList<Integer>> toUpdate = Maps.newHashMap();

    @EventHandler
    public void onChestPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();

        if(block.getType() == Material.CHEST) {
            String team = TeamHelper.getTeamFromChunk(ImmutableList.copyOf(Arrays.asList(block.getChunk().getX(), block.getChunk().getZ())), block.getWorld().getName());

            if(team != null && !TeamHelper.getChestLocations(team).contains(ImmutableList.copyOf(Arrays.asList(block.getX(), block.getY(), block.getZ())))) {
                TeamHelper.addChest(team, block.getLocation());
            }
        }
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if(block.getType() == Material.CHEST) {
            String team = TeamHelper.getTeamFromChunk(ImmutableList.copyOf(Arrays.asList(block.getChunk().getX(), block.getChunk().getZ())), block.getWorld().getName());

            if(team != null && TeamHelper.getChestLocations(team).contains(ImmutableList.copyOf(Arrays.asList(block.getX(), block.getY(), block.getZ())))) {
                TeamHelper.removeChest(team, block.getLocation());
                TeamHelper.updatePoints(team);
            }
        }
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.CHEST) {
            String team = TeamHelper.getTeamFromChunk(ImmutableList.copyOf(Arrays.asList(block.getChunk().getX(), block.getChunk().getZ())), block.getWorld().getName());
            ImmutableList<Integer> location = ImmutableList.copyOf(Arrays.asList(block.getX(), block.getY(), block.getZ()));

            if(team != null) {
                toUpdate.put(event.getPlayer().getUniqueId(), location);
            }
        }
    }

    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
        if(toUpdate.containsKey(event.getPlayer().getUniqueId())) {
            ImmutableList<Integer> location = toUpdate.get(event.getPlayer().getUniqueId());
            Location temp = new Location(event.getPlayer().getWorld(), location.get(0), location.get(1), location.get(2));
            String team = TeamHelper.getTeamFromChunk(ImmutableList.copyOf(Arrays.asList(temp.getChunk().getX(), temp.getChunk().getZ())), temp.getWorld().getName());

            if(!TeamHelper.getChestLocations(team).contains(location)) {
                TeamHelper.addChest(team, temp);
            }

            TeamHelper.updatePoints(team);

            toUpdate.remove(event.getPlayer().getUniqueId());
        }
    }
}
