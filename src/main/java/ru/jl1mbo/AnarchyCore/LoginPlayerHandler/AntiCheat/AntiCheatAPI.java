package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.LoginChainData;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.EventsListener.EntityDamageByEntityListener;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.EventsListener.PlayerJoinListener;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.EventsListener.PlayerLoginListener;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.EventsListener.PlayerMoveListener;

import java.util.HashMap;
import java.util.Map;

public class AntiCheatAPI {
	private static final Map<String, Integer> flyDetect = new HashMap<>();
	private static final Map<String, Integer> reachDetect = new HashMap<>();

	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerMoveListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerLoginListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityDamageByEntityListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
	}

	public static boolean checkToolBox(Player player) {
		LoginChainData clientData = player.getLoginChainData();
		if (clientData.getDeviceOS() == 1) {
			String[] modelSplit = clientData.getDeviceModel().split(" ");
			if (modelSplit.length != 0) {
				return !modelSplit[0].equals(modelSplit[0].toUpperCase());
			}
		}
		return false;
	}

	public static void checkReach(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		if (!(damager instanceof Player)) return;
		if (event instanceof EntityDamageByChildEntityEvent) return;
		if (((Player) damager).isCreative()) return;
		if (damager.distance(damaged) > getAllowedDistance((Player) damager)) {
			event.setCancelled(true);
			reachDetect.put(damager.getName(), reachDetect.get(damager.getName()) + 1);
			if (reachDetect.get(damager.getName()) > 5) {
				reachDetect.put(damager.getName(), 0);
				event.setCancelled(true);
				for (Player player : Server.getInstance().getOnlinePlayers().values()) {
					if (player.hasPermission("AdminChat")) {
						player.sendMessage("§fНовая жалоба§7! (§6AntiCheat§7)\n§fНарушитель§7: §6" + damager.getName() + "\n§fПричина§7: §6Reach");
					}
				}
			}
		}
	}

	public static void checkFly(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location oldLocation = event.getFrom();
		Location newLocation = event.getTo();
		if (!player.isCreative() && !player.isSpectator()) {
			if (oldLocation.getY() <= newLocation.getY()) {
				if (player.getInAirTicks() > 20) {
					int maxY = player.getLevel().getHighestBlockAt(newLocation.getFloorX(), newLocation.getFloorZ());
					if (newLocation.getY() - 5 > maxY) {
						flyDetect.put(player.getName(), flyDetect.get(player.getName()) + 1);
						if (flyDetect.get(player.getName()) > 5) {
							flyDetect.put(player.getName(), 0);
							for (Player players : Server.getInstance().getOnlinePlayers().values()) {
								if (players.hasPermission("AdminChat")) {
									players.sendMessage("§fНовая жалоба§7! (§6AntiCheat§7)\n§fНарушитель§7: §6" + player.getName() + "\n§fПричина§7: §6Fly");
								}
							}
						}
					}
				}
			}
		}
	}

	public static void addDetect(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		flyDetect.put(player.getName(), 0);
		reachDetect.put(player.getName(), 0);
	}

	public static double getAllowedDistance(Player damager) {
		double projected = damager.isOnGround() ? 4 : 6.2;
		return (damager.getPing() * 0.002) + projected;
	}
}