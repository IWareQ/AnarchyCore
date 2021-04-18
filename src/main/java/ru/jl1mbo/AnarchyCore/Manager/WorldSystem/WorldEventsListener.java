package ru.jl1mbo.AnarchyCore.Manager.WorldSystem;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockBurnEvent;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.block.LeavesDecayEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;

public class WorldEventsListener implements Listener {
	
	@EventHandler()
	public void onWeatherChange(WeatherChangeEvent event) {
		if (event.getLevel().equals(WorldSystemAPI.Spawn)) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(WorldSystemAPI.Spawn) && player.getGamemode() != 1) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onBlockBurn(BlockBurnEvent event) {
		Block block = event.getBlock();
		if (block.getLevel().equals(WorldSystemAPI.Spawn)) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onBlockIgnite(BlockIgniteEvent event) {
		Block block = event.getBlock();
		if (block.getLevel().equals(WorldSystemAPI.Spawn)) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(WorldSystemAPI.Spawn) && player.getGamemode() != 1) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity.getLevel().equals(WorldSystemAPI.Spawn)) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onItemFrameDropItem(ItemFrameDropItemEvent event) {
		BlockEntityItemFrame itemFrame = event.getItemFrame();
		if (itemFrame.getLevel().equals(WorldSystemAPI.Spawn)) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onLeavesDecay(LeavesDecayEvent event) {
		Block block = event.getBlock();
		if (block.getLevel().equals(WorldSystemAPI.Spawn)) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(WorldSystemAPI.Spawn) && player.getGamemode() != 1) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(WorldSystemAPI.Spawn) && player.getGamemode() != 1) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onPlayerFoodLevelChange(PlayerFoodLevelChangeEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(WorldSystemAPI.Spawn)) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Entity entity = event.getEntity();
		if (entity.getLevel().equals(WorldSystemAPI.Spawn)) {
			event.setCancelled(true);
		}
	}
}