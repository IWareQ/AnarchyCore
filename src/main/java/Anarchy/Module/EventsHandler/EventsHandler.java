package Anarchy.Module.EventsHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Module.Commands.Home.HomeCommand;
import Anarchy.Module.Commands.Home.SetHomeCommand;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerEatFoodEvent;
import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ChangeDimensionPacket;
import cn.nukkit.network.protocol.PlayStatusPacket;
import nukkitcoders.mobplugin.entities.animal.Animal;
import nukkitcoders.mobplugin.entities.animal.swimming.Squid;
import nukkitcoders.mobplugin.entities.monster.Monster;

public class EventsHandler implements Listener {
	public static int CHAT_RADIUS = 70;

	public static void changeDimension(Player player, int dimension) {
		ChangeDimensionPacket cdp = new ChangeDimensionPacket();
		cdp.dimension = dimension;
		cdp.x = (float) player.x;
		cdp.y = (float) player.y;
		cdp.z = (float) player.z;
		cdp.respawn = true;
		player.dataPacket(cdp);

		PlayStatusPacket psp = new PlayStatusPacket();
		psp.status = 3;
		player.dataPacket(psp);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.x < FunctionsAPI.BORDER[0] || player.x > FunctionsAPI.BORDER[1] || player.z < FunctionsAPI.BORDER[2] || player.z > FunctionsAPI.BORDER[3]) {
			player.sendPopup("§c§l| §fВы пытаетесь §cвыйти §fза границу мира! §c§l|");
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		Location location = event.getTo();
		if (location.x < FunctionsAPI.BORDER[0] || location.x > FunctionsAPI.BORDER[1] || location.z < FunctionsAPI.BORDER[2] || location.z > FunctionsAPI.BORDER[3]) {
			player.sendPopup("§l§c| §fВы пытаетесь §cтелепортироваться §fза границу мира! §c|");
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		Block block = event.getBlock();
		if (SetHomeCommand.HOME_SET.containsKey(player)) {
			if (block != null && block.getId() == Item.BED_BLOCK) {
				if (block.getLevel().getBlock(block.add(0, 1, 0)).getId() != 0) {
					player.sendMessage(HomeCommand.PREFIX + "§fРасчистите место над кроватью!");
					event.setCancelled();
					return;
				}

				player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
				player.sendMessage(HomeCommand.PREFIX + "§fВы установили новую точку дома!");
				SQLiteUtils.query("Homes.db", "INSERT INTO `HOMES` (`Home_Name`, `Username`, `X`, `Y`, `Z`, `LEVEL`) VALUES ('" + SetHomeCommand.HOME_SET.get(player) + "', '" + playerName + "', " + block.getFloorX() + ", " + block.getFloorY() + ", " + block.getFloorZ() + ", '" + player.getLevel().getName() + "');");
				SetHomeCommand.HOME_SET.remove(player);
				event.setCancelled();
			} else {
				player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
				player.sendMessage(HomeCommand.PREFIX + "§fВы отменили установку точки дома!");
				SetHomeCommand.HOME_SET.remove(player);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		EntityDamageEvent cause = player.getLastDamageCause();
		if (cause instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
			if (damager instanceof Player) {
				player.sendMessage("§c§l| §r§fВы были убиты Игроком §c" + damager.getName());
				event.setDeathMessage("§e§l| §r§fИгрока §a" + player.getName() + " §fубил Игрок §c" + damager.getName());
				int money = EconomyAPI.myMoney(player) * 20 / 100;
				if (money != 0) {
					player.sendMessage("§c§l| §r§fПри смерти Вы потеряли §e" + money + " §7(§f20%§7)");
					((Player) damager).sendMessage("§a§l| §r§fВо время убийства, Вы украли §e" + money + " §fу Игрока §a" + player.getName());
					EconomyAPI.reduceMoney(player, money);
					EconomyAPI.addMoney((Player) damager, money);
				}
				return;
			}
		}
		event.setDeathMessage("§e§l| §r§fИгрок §a" + player.getName() + " §fпогиб");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		EntityDamageEvent cause = entity.getLastDamageCause();
		if (cause instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
			if (damager instanceof Player) {
				if (entity instanceof Animal) {
					if (entity instanceof Squid) {
						((Player) damager).sendTitle("", "+ §23");
						EconomyAPI.addMoney(((Player) damager), 3);
					} else {
						((Player) damager).sendTitle("", "+ §21");
						EconomyAPI.addMoney(((Player) damager), 1);
					}
				} else if (entity instanceof Monster) {
					((Player) damager).sendTitle("", "+ §22");
					EconomyAPI.addMoney(((Player) damager), 2);
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerEatFood(PlayerEatFoodEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel() == FunctionsAPI.WORLD2) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerFoodLevelChange(PlayerFoodLevelChangeEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel() == FunctionsAPI.WORLD2) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMove2(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		final Block block = player.getLevel().getBlock(new Vector3((double)(int) Math.round(event.getPlayer().x - 0.5), (double)(int) Math.round(event.getPlayer().y - 1.0), (double)(int) Math.round(event.getPlayer().z - 0.5)));
		if (player.getLevel() == FunctionsAPI.WORLD2) {
			if (block.getId() == 241) {
				switch (block.getId()) {
				case 241:
					{
						Level level = Server.getInstance().getLevelByName("world");
						Vector3 teleportPosition = new Vector3(ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[0], FunctionsAPI.RANDOM_TP[1]), 256, ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[2], FunctionsAPI.RANDOM_TP[3]));
						player.teleport(level.getSafeSpawn(teleportPosition));
						player.sendMessage("§l§a| §r§fВы телепортированы в случайное место");
						return;
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String playerMessage = event.getMessage();

		if (String.valueOf(playerMessage.charAt(0)).equals("!")) {
			event.setFormat("§aⒼ " + player.getDisplayName() + " §8» §f" + playerMessage.substring(1).replaceAll("§", ""));
		} else {
			Set<CommandSender> players = new HashSet <>();
			for (Player reciver: Server.getInstance().getOnlinePlayers().values()) {
				if (reciver.level == player.level && reciver.distance(new Location(player.getX(), player.getY(), player.getZ(), reciver.getLevel())) <= CHAT_RADIUS) {
					players.add(reciver);
				}
			}

			players.add(new ConsoleCommandSender());
			event.setFormat("§7Ⓛ " + player.getDisplayName() + " §8» §f" + playerMessage.replaceAll("§", ""));
			event.setRecipients(players);
		}
	}
}