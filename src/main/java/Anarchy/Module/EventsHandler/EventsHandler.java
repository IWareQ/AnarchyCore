package Anarchy.Module.EventsHandler;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import Anarchy.AnarchyMain;
import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Module.Economy.EconomyAPI;
import MobPlugin.Entities.Animal.Animal;
import MobPlugin.Entities.Animal.Swimming.Squid;
import MobPlugin.Entities.Monster.Monster;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;

public class EventsHandler implements Listener {
	File dataFile = new File(AnarchyMain.datapath + "/KDR.yml");
	Config config = new Config(dataFile, Config.YAML);
	public static int CHAT_RADIUS = 70;
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Block block = player.getLevel().getBlock(new Vector3((double)(int)Math.round(event.getPlayer().x - 0.5), (double)(int)Math.round(event.getPlayer().y - 1.0), (double)(int)Math.round(event.getPlayer().z - 0.5)));
		if (player.x < FunctionsAPI.BORDER[0] || player.x > FunctionsAPI.BORDER[1] || player.z < FunctionsAPI.BORDER[2] || player.z > FunctionsAPI.BORDER[3]) {
			player.sendTip("§c§l| §fВы пытаетесь §6выйти §fза границу мира§7! §c|§r");
			event.setCancelled(true);
		}
		if (player.getFloorY() <= 0) {
			Level level = Server.getInstance().getLevelByName("world2");
			player.teleport(level.getSafeSpawn());
			player.sendMessage("§l§3| §r§fВы упали§7, §fно вас спас бог :)");
		}
		if (player.getLevel().equals(FunctionsAPI.WORLD2)) {
			if (block.getId() == 416) {
				Level level = Server.getInstance().getLevelByName("world");
				Vector3 teleportPosition = new Vector3(ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[0], FunctionsAPI.RANDOM_TP[1]), 68, ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[2], FunctionsAPI.RANDOM_TP[3]));
				player.teleport(level.getSafeSpawn(teleportPosition));
				level.loadChunk(ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[0], FunctionsAPI.RANDOM_TP[1]) >> 4, ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[2], FunctionsAPI.RANDOM_TP[3]) >> 4);
				player.sendMessage("§l§a| §r§fВас §6успешно §fтелепортировало на рандомное место§7.");
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		Location location = event.getTo();
		if (location.x < FunctionsAPI.BORDER[0] || location.x > FunctionsAPI.BORDER[1] || location.z < FunctionsAPI.BORDER[2] || location.z > FunctionsAPI.BORDER[3]) {
			player.sendTip("§l§c| §fВы пытаетесь §3телепортироваться §fза границу мира§7! §c|§r");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		EntityDamageEvent cause = player.getLastDamageCause();
		if (cause instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent)cause).getDamager();
			if (damager instanceof Player) {
				player.sendMessage("§c§l| §r§fВы были убиты Игроком §e" + damager.getName());
				event.setDeathMessage("§e§l| §r§fИгрок §3" + player.getName() + " §fпогиб от руки Игрока §e" + damager.getName());
				int money = EconomyAPI.myMoney(player) * 20 / 100;
				if (money != 0) {
					player.sendMessage("§c§l| §r§fПри смерти Вы потеряли §e" + money + " §7(§f20%§7)");
					((Player)damager).sendMessage("§a§l| §r§fВо время убийства§7, §fВы украли §e" + money + " §fу Игрока §e" + player.getName());
					EconomyAPI.reduceMoney(player, money);
					EconomyAPI.addMoney((Player)damager, money);
					addKill((Player)damager);
					addDeaths(player);
				}
				return;
			}
		}
		event.setDeathMessage("§e§l| §r§fИгрок §e" + player.getName() + " §fпогиб");
		addDeaths(player);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		EntityDamageEvent cause = entity.getLastDamageCause();
		if (cause instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent)cause).getDamager();
			if (damager instanceof Player) {
				if (entity instanceof Animal) {
					if (entity instanceof Squid) {
						((Player)damager).sendTip("§7+ §e3");
						EconomyAPI.addMoney(((Player)damager), 3);
					} else {
						((Player)damager).sendTip("§7+ §e1");
						EconomyAPI.addMoney(((Player)damager), 1);
					}
				} else if (entity instanceof Monster) {
					((Player)damager).sendTip("§7+ §e2");
					EconomyAPI.addMoney(((Player)damager), 2);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity().getLevel().equals(FunctionsAPI.WORLD2)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(FunctionsAPI.WORLD2) && !(player.hasPermission("Access.Admin"))) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(FunctionsAPI.WORLD2) && !(player.hasPermission("Access.Admin"))) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayetBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(FunctionsAPI.WORLD2) && !(player.hasPermission("Access.Admin"))) {
			event.setCancelled(true);
		}
	}
	
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(FunctionsAPI.WORLD2) && !(player.hasPermission("Access.Admin"))) {
			player.sendMessage("§l§e| §r§fКоманды §3заблокированны§7, §fпереместитесь в игровую зону§7!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String playerMessage = event.getMessage();
		if (String.valueOf(playerMessage.charAt(0)).equals("!")) {
			event.setFormat("§aⒼ " + player.getDisplayName() + " §8» §7" + playerMessage.substring(1).replaceAll("§", ""));
		} else {
			Set<CommandSender> players = new HashSet<>();
			for (Player all : Server.getInstance().getOnlinePlayers().values()) {
				if (all.level == player.level && all.distance(new Location(player.getX(), player.getY(), player.getZ(), all.getLevel())) <= CHAT_RADIUS) {
					players.add(all);
				}
			}
			players.add(new ConsoleCommandSender());
			event.setFormat("§6Ⓛ " + player.getDisplayName() + " §8» §7" + playerMessage.replaceAll("§", ""));
			event.setRecipients(players);
		}
	}
	
	public void addKill(Player player) {
		config.set("Kills." + player.getName(), getKills(player) + 1);
		config.save();
	}
	
	public int getKills(Player player) {
		return config.getInt("Kills." + player.getName(), 0);
	}
	
	public int getDeaths(Player player) {
		return config.getInt("Deaths." + player.getName(), 0);
	}
	
	public void addDeaths(Player player) {
		config.set("Deaths." + player.getName(), getDeaths(player) + 1);
		config.save();
	}
}