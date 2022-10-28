package ru.iwareq.anarchycore.module.title.cases;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockEnderChest;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Auth.AuthEventsListener;
import ru.iwareq.anarchycore.module.title.TitleAPI;
import ru.iwareq.anarchycore.module.title.Titles;
import ru.iwareq.anarchycore.module.title.manager.TitleManager;
import ru.iwareq.anarchycore.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CasesListener implements Listener {

	public static final Location CURRENT_CASE_LOCATION = new Location(0, 60, 0, WorldSystemAPI.Spawn);

	private final Map<Player, Location> lastPosition = new HashMap<>();
	private final Map<Location, Titles> titles = new HashMap<>();

	private final List<FloatingTextParticle> removedParticles = new ArrayList<>();

	private TimeoutCaseTask task;

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();

		if (block.getId() == Block.ENDER_CHEST && lastPosition.containsKey(player) && titles.containsKey(block.getLocation())) {
			Titles title = titles.get(block.getLocation());
			TitleManager manager = TitleAPI.getManager(player);
			manager.addTitle(title);

			player.sendMessage("Вы получили титул: " + title.getName());
			AuthAPI.setCases(player.getName(), AuthAPI.getCases(player.getName()) - 1);
			this.stopOpenCase(player);
			event.setCancelled();
			return;
		}

		if (block.getId() == Block.ENDER_CHEST && lastPosition.containsKey(player) && !titles.containsKey(block.getLocation())) {
			event.setCancelled();
			return;
		}

		if (AuthAPI.getCases(player.getName()) > 0) {
			if (block.getId() == Block.ENDER_CHEST && block.getLocation().equals(CURRENT_CASE_LOCATION)) {
				TitleManager manager = TitleAPI.getManager(player);
				if (Titles.getAllCount() != manager.getAllCount()) {
					this.startOpenCase(player);
					event.setCancelled();
				} else {
					player.sendMessage("Ты и так получил все титулы");
					event.setCancelled();
				}
			}
		} else {
			player.sendMessage("У тебя недостаточно кейсов");
			event.setCancelled();
		}
	}

	public void startOpenCase(Player player) {
		lastPosition.put(player, player.getLocation());

		this.removeText(player);

		Level level = CURRENT_CASE_LOCATION.level;

		level.setBlock(CURRENT_CASE_LOCATION, new BlockAir());
		player.teleport(CURRENT_CASE_LOCATION.add(0.5D, 0, 0.5D));

		level.setBlock(CURRENT_CASE_LOCATION.add(2), new BlockEnderChest());
		level.setBlock(CURRENT_CASE_LOCATION.add(-2), new BlockEnderChest());

		level.setBlock(CURRENT_CASE_LOCATION.add(0, 0, 2), new BlockEnderChest());
		level.setBlock(CURRENT_CASE_LOCATION.add(0, 0, -2), new BlockEnderChest());

		this.spawnRandomTitles(player);

		player.setImmobile(true);

		Server.getInstance().getScheduler().scheduleRepeatingTask(task = new TimeoutCaseTask(this, player), 20);
	}

	public void stopOpenCase(Player player) {
		Level level = CURRENT_CASE_LOCATION.level;

		removedParticles.forEach(particle -> {
			RemoveEntityPacket pk = new RemoveEntityPacket();
			pk.eid = particle.getEntityId();

			player.dataPacket(pk);
		});

		titles.clear();

		level.setBlock(CURRENT_CASE_LOCATION, new BlockEnderChest());
		player.teleport(lastPosition.get(player));
		lastPosition.remove(player);

		level.setBlock(CURRENT_CASE_LOCATION.add(2), new BlockAir());
		level.setBlock(CURRENT_CASE_LOCATION.add(-2), new BlockAir());

		level.setBlock(CURRENT_CASE_LOCATION.add(0, 0, 2), new BlockAir());
		level.setBlock(CURRENT_CASE_LOCATION.add(0, 0, -2), new BlockAir());

		player.setImmobile(false);

		this.createText(player);

		task.cancel();
	}

	private void spawnRandomTitles(Player player) {
		TitleManager manager = TitleAPI.getManager(player);
		Set<Titles> lockedTitles = manager.getLockedTitles();

		List<Titles> random = new ArrayList<>();
		Titles[] titles = lockedTitles.toArray(new Titles[0]);
		for (int i = 0; i < Math.min(4, titles.length); i++) {
			Titles title = titles[Utils.rand(0, lockedTitles.size() - 1)];
			random.add(title);
			lockedTitles.remove(title);
		}

		this.setRandomTitle(CURRENT_CASE_LOCATION.add(2), player, random.get(0));

		if (random.size() > 1) {
			this.setRandomTitle(CURRENT_CASE_LOCATION.add(-2), player, random.get(1));
		}

		if (random.size() > 2) {
			this.setRandomTitle(CURRENT_CASE_LOCATION.add(0, 0, 2), player, random.get(2));
		}

		if (random.size() > 3) {
			this.setRandomTitle(CURRENT_CASE_LOCATION.add(0, 0, -2), player, random.get(3));
		}
	}

	private void setRandomTitle(Location location, Player player, Titles title) {
		FloatingTextParticle particle = new FloatingTextParticle(location.add(0.5D, 1, 0.5D), title.getName());
		location.level.addParticle(particle, player);
		removedParticles.add(particle);
		titles.put(location, title);
	}

	private void createText(Player player) {
		if (AuthEventsListener.titlesMap.containsKey(player)) {
			FloatingTextParticle particle = AuthEventsListener.titlesMap.get(player);
			particle.setText("В наличии §6" + AuthAPI.getCases(player.getName()) + " §fнажмите чтобы открыть");

			AuthEventsListener.titlesMap.put(player, particle);
			WorldSystemAPI.Spawn.addParticle(
					particle,
					player);
		}
	}

	private void removeText(Player player) {
		if (AuthEventsListener.titlesMap.containsKey(player)) {
			FloatingTextParticle particle = AuthEventsListener.titlesMap.get(player);
			RemoveEntityPacket pk = new RemoveEntityPacket();
			pk.eid = particle.getEntityId();

			player.dataPacket(pk);
		}
	}
}
