package Anarchy.Module.EventsHandler;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import Anarchy.AnarchyMain;
import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Utils.RandomUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockBurnEvent;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.block.LeavesDecayEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import nukkitcoders.mobplugin.entities.animal.Animal;
import nukkitcoders.mobplugin.entities.monster.Monster;

public class EventsHandler implements Listener {
	File dataFileDeaths = new File(AnarchyMain.datapath + "/Deaths.yml");
	File dataFileKills = new File(AnarchyMain.datapath + "/Kills.yml");
	Config configDeaths = new Config(dataFileDeaths, Config.YAML);
	Config configKills = new Config(dataFileKills, Config.YAML);
	public static int CHAT_RADIUS = 70;

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Block block = player.getLevel().getBlock(new Position((double)(int)Math.round(event.getPlayer().x - 0.5), (double)(int)Math.round(event.getPlayer().y - 1.0), (double)(int)Math.round(event.getPlayer().z - 0.5)));
		if (player.x < FunctionsAPI.BORDER[0] || player.x > FunctionsAPI.BORDER[1] || player.z < FunctionsAPI.BORDER[2] || player.z > FunctionsAPI.BORDER[3]) {
			player.sendTip("§c§l| §fВы пытаетесь §6выйти §fза границу мира§7! §c|§r");
			event.setCancelled(true);
		}
		if (player.getFloorY() <= 0 && player.getLevel() != Server.getInstance().getLevelByName("the_end")) {
			player.teleport(FunctionsAPI.SPAWN.getSafeSpawn(new Position(-7, 148, 93)));
			player.sendMessage("§l§c| §r§fВы упали за границу мира§7! §fЧтобы Вы не потеряли свои вещи§7, §fмы решили телепортировать Вас на спавн§7!");
		}
		if (player.getLevel().equals(FunctionsAPI.SPAWN) && block.getId() == 416) {
			player.sendMessage("§l§6• §r§fПортал временно не доступен§7, §fдля выхода со спавна используйте §7/§6rtp");
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		Location location = event.getTo();
		if (location.x < FunctionsAPI.BORDER[0] || location.x > FunctionsAPI.BORDER[1] || location.z < FunctionsAPI.BORDER[2] || location.z > FunctionsAPI.BORDER[3]) {
			player.sendTip("§l§c| §fВы пытаетесь §6телепортироваться §fза границу мира§7! §c|§r");
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		EntityDamageEvent cause = player.getLastDamageCause();
		if (cause instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent)cause).getDamager();
			if (damager instanceof Player && player != damager) {
				player.sendMessage("§c§l| §r§fВы были убиты Игроком §6" + damager.getName());
				event.setDeathMessage("§6§l| §r§fИгрок §6" + player.getName() + " §fпогиб от руки Игрока §6" + damager.getName());
				Double money = EconomyAPI.myMoney(player) * 20 / 100;
				if (money != 0) {
					player.sendMessage("§c§l| §r§fПри смерти Вы потеряли §6" + String.format("%.1f", money) + " §7(§f20§7%)");
					((Player)damager).sendMessage("§l§a| §r§fВо время убийства§7, §fВы украли §6" + String.format("%.1f", money) + " §fу Игрока §6" + player.getName());
					EconomyAPI.reduceMoney(player, money);
					EconomyAPI.addMoney((Player)damager, money);
					this.addKill((Player)damager, 1);
					this.addDeaths(player, 1);
				}
				return;
			}
		}
		event.setDeathMessage("§6§l| §r§fИгрок §6" + player.getName() + " §fпогиб");
		this.addDeaths(player, 1);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent cause = (EntityDamageByEntityEvent)entity.getLastDamageCause();
			if (cause.getDamager() instanceof Player) {
				Player player = (Player)cause.getDamager();
				double money = RandomUtils.rand(0.1, 2.0);
				if (entity instanceof Animal) {
					player.sendTip("§7+ §6" + String.format("%.1f", money) + "");
					EconomyAPI.addMoney(player, money);
				} else if (entity instanceof Monster) {
					player.sendTip("§7+ §6" + String.format("%.1f", money) + "");
					EconomyAPI.addMoney(player, money);
				}
			}
		}
	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		/*Item item = event.getItem();
		if (item.getCustomName().equals("§r§l§f๑ Сокровище ๑") && item.getId() == Item.DOUBLE_PLANT) {
			player.sendMessage("§l§6| §r§fСокровище успешно Активированно§7!");
			player.getInventory().removeItem(item);
		}*/
		if (player.getLevel().equals(FunctionsAPI.SPAWN) && !(player.hasPermission("Access.Admin"))) {
			player.sendTip("§l§fТерритория не доступна для взаимодействия§7!");
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(FunctionsAPI.SPAWN) && !(player.hasPermission("Access.Admin"))) {
			player.sendTip("§l§fТерритория не доступна для взаимодействия§7!");
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(FunctionsAPI.SPAWN) && !(player.hasPermission("Access.Admin"))) {
			player.sendTip("§l§fТерритория не доступна для взаимодействия§7!");
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayetBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(FunctionsAPI.SPAWN) && !(player.hasPermission("Access.Admin"))) {
			player.sendTip("§l§fТерритория не доступна для взаимодействия§7!");
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity.getLevel().equals(FunctionsAPI.SPAWN)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onWeatherChange(WeatherChangeEvent event) {
		Level level = event.getLevel();
		if (level.equals(FunctionsAPI.SPAWN)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockIgnite(BlockIgniteEvent event) {
		Block block = event.getBlock();
		if (block.getLevel().equals(FunctionsAPI.SPAWN)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBurn(BlockBurnEvent event) {
		Block block = event.getBlock();
		if (block.getLevel().equals(FunctionsAPI.SPAWN)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onLeavesDecay(LeavesDecayEvent event) {
		Block block = event.getBlock();
		if (block.getLevel().equals(FunctionsAPI.SPAWN)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Entity entity = event.getEntity();
		if (entity.getLevel().equals(FunctionsAPI.SPAWN)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(FunctionsAPI.SPAWN)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onItemFrameDropItem(ItemFrameDropItemEvent event) {
		BlockEntityItemFrame item = event.getItemFrame();
		if (item.getLevel().equals(FunctionsAPI.SPAWN)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String playerMessage = event.getMessage();
		PlayerSession playerSession = PlayerSessionManager.getPlayerSession(player.getName());
		String displayName = PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")) + " §f" + player.getName();
		if (String.valueOf(playerMessage.charAt(0)).equals("!")) {
			event.setFormat("§aⒼ " + displayName + " §8» §7" + playerMessage.substring(1).replaceAll("§", ""));
		} else if (String.valueOf(playerMessage.charAt(0)).equals("*")) {
			Set<CommandSender> players = new HashSet<>();
			for (Player playerChat : Server.getInstance().getOnlinePlayers().values()) {
				if (playerChat.hasPermission("AdminChat")) {
					players.add(playerChat);
				}
			}
			players.add(new ConsoleCommandSender());
			event.setFormat("§cA " + displayName + " §8» §f" + playerMessage.replaceAll("§", ""));
			event.setRecipients(players);
		} else {
			Set<CommandSender> players = new HashSet<>();
			for (Player playerChat : Server.getInstance().getOnlinePlayers().values()) {
				if (playerChat.level == player.level && playerChat.distance(new Location(player.getX(), player.getY(), player.getZ(), playerChat.getLevel())) <= CHAT_RADIUS) {
					players.add(playerChat);
				}
			}
			players.add(new ConsoleCommandSender());
			event.setFormat("§6Ⓛ " + displayName + " §8» §f" + playerMessage.replaceAll("§", ""));
			event.setRecipients(players);
		} 
	}

	public void addKill(Player player, int number) {
		this.configKills.set(player.getName(), this.getKills(player) + number);
		this.configKills.save();
	}

	public int getKills(Player player) {
		return this.configKills.getInt(player.getName(), 0);
	}

	public int getDeaths(Player player) {
		return this.configDeaths.getInt(player.getName(), 0);
	}

	public void addDeaths(Player player, int number) {
		this.configDeaths.set(player.getName(), this.getDeaths(player) + number);
		this.configDeaths.save();
	}
}