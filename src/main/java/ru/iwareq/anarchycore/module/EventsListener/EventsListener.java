package ru.iwareq.anarchycore.module.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import nukkitcoders.mobplugin.entities.animal.Animal;
import nukkitcoders.mobplugin.entities.monster.Monster;
import ru.iwareq.anarchycore.AnarchyCore;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Clans.ClanAPI;
import ru.iwareq.anarchycore.module.Commands.HideGlobalChatCommand;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;
import ru.iwareq.anarchycore.module.Economy.EconomyAPI;
import ru.iwareq.anarchycore.module.Permissions.PermissionAPI;
import ru.iwareq.anarchycore.util.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

public class EventsListener implements Listener {

	private final Map<Player, Long> COOLDOWN = new HashMap<>();
	private final Integer[] BORDER = new Integer[]{-2000, 2000, -2000, 2000};

	private static Item getRandomItems() {
		Integer[] items = new Integer[]{
				Item.END_CRYSTAL, Item.GOLDEN_PICKAXE, Item.TOTEM, Item.DRAGON_EGG, Item.GHAST_TEAR, Item.DIAMOND,
				Item.SPONGE, Item.GOLD_INGOT, Item.IRON_INGOT, Item.TNT, Item.GOLDEN_APPLE
		};
		int itemId = items[new Random().nextInt(11)];
		Item item;
		if (itemId == Item.GOLDEN_PICKAXE || itemId == Item.TOTEM || itemId == Item.DRAGON_EGG) {
			item = Item.get(itemId, 0, 1);
		} else {
			item = Item.get(itemId, 0, Utils.rand(0, 10));
		}
		return item;
	}

	public static int getKills(String playerName) {
		Config config = new Config(AnarchyCore.getInstance().getDataFolder() + "/kills.yml", Config.YAML);
		return config.get(playerName, 0);
	}

	public static int getDeaths(Player player) {
		Config config = new Config(AnarchyCore.getInstance().getDataFolder() + "/deaths.yml", Config.YAML);
		return config.get(player.getName(), 0);
	}

	@EventHandler()
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		String displayName = (ClanAPI.playerIsInClan(player.getName()) ? "§3" + ClanAPI.getClanName(ClanAPI.getPlayerClanId(player.getName())) + " " : "") + PermissionAPI.getPlayerGroup(player.getName()).getGroupName() + " " + player.getName();
		Long cooldownTime = COOLDOWN.get(player);
		long nowTime = System.currentTimeMillis() / 1000;
		if (cooldownTime != null && cooldownTime > nowTime) {
			player.sendMessage("§l§6• §rВы слишком §6часто пишите §fв чат§7, §fпожалуйста§7, §fподождите §6несколько §fсекунд§7!");
			event.setCancelled(true);
			return;
		}

		if (Pattern.compile("[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)").matcher(message).matches()) {
			event.setCancelled();
			return;
		}

		if (String.valueOf(message.charAt(0)).equals("#") && PermissionAPI.getPlayerGroup(player.getName()).isAdmin()) {
			Set<CommandSender> adminChat = new HashSet<>();
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				if (PermissionAPI.getPlayerGroup(players.getName()).isAdmin()) {
					adminChat.add(players);
				}
			}
			adminChat.add(new ConsoleCommandSender());
			event.setFormat("§7(§cA§7) " + displayName + " §8» §6" + message.substring(1).replaceAll("§", ""));
			event.setRecipients(adminChat);
		} else if (String.valueOf(message.charAt(0)).equals("!")) {
			Set<CommandSender> globalChat = new HashSet<>();
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				if (!HideGlobalChatCommand.PLAYERS.contains(players.getName())) {
					globalChat.add(players);
				}
			}

			globalChat.add(new ConsoleCommandSender());
			event.setRecipients(globalChat);

			if (!Utils.validText(message.substring(1).replaceAll("§", "").replaceAll(" ", ""))) {
				event.setFormat("§7(§aG§7) " + displayName + " §8» §7" + message.substring(1).replaceAll("§", ""));
				COOLDOWN.put(player, nowTime + 2);
			} else {
				player.sendMessage("§l§6• §rНе матерись§7!");
				event.setCancelled(true);
			}
		} else {
			Set<CommandSender> playerChat = new HashSet<>();
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				int chatRadius = 70;
				if (players.getLevel() == player.getLevel() && players.distance(new Position(player.getFloorX(), player.getFloorY(), player.getFloorZ(), players.getLevel())) <= chatRadius) {
					playerChat.add(players);
				}
			}
			playerChat.add(new ConsoleCommandSender());

			if (playerChat.size() == 2) {
				player.sendTitle("Никто §cне увидел §rсообшение",
						"Вы использовали локальный чат\nПоставьте перед сообщением знак !\nчтобы написать в глобальный чат");
			}
			if (!Utils.validText(message.replaceAll("§", "").replaceAll(" ", ""))) {
				event.setFormat("§7(§6L§7) " + displayName + " §8» §f" + message.replaceAll("§", ""));
				event.setRecipients(playerChat);

				COOLDOWN.put(player, nowTime + 2);
			} else {
				player.sendMessage("§l§6• §rНе матерись§7!");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (player.getLevel().equals(WorldSystemAPI.Spawn) && player.getGamemode() != Player.CREATIVE) {
			event.setCancelled(true);
		}
		if (player.getLevel().equals(WorldSystemAPI.Map) || player.getLevel().equals(WorldSystemAPI.Spawn)) {
			if (block.getId() == Block.BED_BLOCK) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler()
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent cause = (EntityDamageByEntityEvent) entity.getLastDamageCause();
			if (cause.getDamager() instanceof Player) {
				Player player = (Player) cause.getDamager();
				double money = Utils.rand(0.5D, 3D);
				if ((entity instanceof Animal) || (entity instanceof Monster)) {
					player.sendTip("§7+ §6" + EconomyAPI.format(money) + "");
					EconomyAPI.addMoney(player.getName(), money);
				}
			}
		}
	}

	@EventHandler()
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(WorldSystemAPI.Spawn)) {
			Block block = player.getLevel().getBlock(player.getPosition());
			if (block.getId() == BlockID.NETHER_PORTAL) {
				WorldSystemAPI.findRandomPositionAndTp(WorldSystemAPI.Map, pos -> {
					player.teleport(pos);
					player.sendTitle("Телепортация§7...");
				});
			}
		}

		if (player.getGamemode() < Player.CREATIVE) {
			if ((player.getFloorX() < BORDER[0]) || (player.getFloorX() > BORDER[1]) || (player.getFloorZ() < BORDER[2]) || (player.getFloorZ() > BORDER[3])) {
				player.sendTip("Вы пытаетесь §6выйти §fза границу мира");
				player.dismountEntity(player.getRiding());
				event.setCancelled(true);
			}
		}

		if ((player.getFloorY() <= -15) && (player.getLevel() != WorldSystemAPI.TheEnd)) {
			player.teleport(WorldSystemAPI.Spawn.getSafeSpawn());
			player.sendMessage("§l§6• §rВы упали за границу мира§7. §fЧтобы Вы не потеряли свои вещи§7, §fмы решили телепортировать Вас на спавн§7!");
		}

		if (CooldownAPI.canTask(player)) {
			Location from = event.getFrom();
			Location to = event.getTo();

			// player.sendMessage("From length: " + from.length());
			// player.sendMessage("To length: " + to.length());
			player.sendMessage("XYZ different: X: " + (from.getX() - to.getX()) + " Y: " + (from.getY() - to.getY()) + " Z: " + (from.getZ() - to.getZ()));
			if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
				CooldownAPI.removeTask(player);
				player.sendMessage("Выполнение телепортации отменено");
			}
		}
	}

	@EventHandler()
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		EntityDamageEvent cause = player.getLastDamageCause();
		switch (cause == null ? DamageCause.CUSTOM : cause.getCause()) {
			case ENTITY_ATTACK:
				if (cause instanceof EntityDamageByEntityEvent) {
					Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
					if (damager instanceof Player) {
						player.sendMessage("§l§6• §rВы были убиты Игроком §6" + damager.getName());
						event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fпогиб от руки Игрока §6" + damager.getName());
						this.addDeath(player);
						this.addKill(damager.getName());
						double money = AuthAPI.getMoney(player.getName()) * 20 / 100;
						if (money > 1) {
							player.sendMessage("§l§6• §rПри смерти Вы потеряли §6" + EconomyAPI.format(money) + " §7(§f20§7%)");
							((Player) damager).sendMessage("§l§6• §rВо время убийства§7, §fВы украли §6" + EconomyAPI.format(money) + " §fу Игрока §6" + player.getName());
							EconomyAPI.reduceMoney(player.getName(), money);
							EconomyAPI.addMoney(damager.getName(), money);
						}
						break;
					} else if (damager instanceof EntityLiving) {
						event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fпогиб от моба");
						this.addDeath(player);
						break;
					} else {
						event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fпогиб");
						this.addDeath(player);
					}
				}
				break;

			case PROJECTILE:
				if (cause instanceof EntityDamageByEntityEvent) {
					Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
					if (damager instanceof Player) {
						event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fбыл застрелен игроком §6" + damager.getName());
						this.addDeath(player);
						this.addKill(damager.getName());
					} else if (damager instanceof EntityLiving) {
						event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fбыл застрелен");
						this.addDeath(player);
						break;
					} else {
						event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fпогиб");
						this.addDeath(player);
					}
				}
				break;

			case VOID:
				event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fупал в бездну");
				this.addDeath(player);
				break;

			case FALL:
				if (cause.getFinalDamage() > 2) {
					event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fразбился");
					this.addDeath(player);
					break;
				}
				event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fразбился");
				this.addDeath(player);
				break;

			case SUFFOCATION:
				event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fзадохнулся");
				this.addDeath(player);
				break;

			case LAVA:
				Block block = player.getLevel().getBlock(new Vector3(player.getX(), player.getY() - 1, player.getZ()));
				if (block.getId() == Block.MAGMA) {
					event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fсгорел");
					this.addDeath(player);
					break;
				}
				event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fсгорел");
				this.addDeath(player);
				break;

			case FIRE:
			case FIRE_TICK:
				event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fсгорел");
				this.addDeath(player);
				break;

			case DROWNING:
				event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fутонул");
				this.addDeath(player);
				break;

			case CONTACT:
				if (cause instanceof EntityDamageByBlockEvent) {
					if (((EntityDamageByBlockEvent) cause).getDamager().getId() == Block.CACTUS) {
						event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fумер от кактуса");
						this.addDeath(player);
					}
				}
				break;

			case BLOCK_EXPLOSION:
			case ENTITY_EXPLOSION:
				if (cause instanceof EntityDamageByEntityEvent) {
					Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
					if (damager instanceof Player) {
						event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fбыл взорван игроком §6" + damager.getName());
						this.addDeath(player);
						this.addKill(damager.getName());
					} else if (damager instanceof EntityLiving) {
						event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fбыл взорван");
						this.addDeath(player);
						break;
					} else {
						event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fбыл взорван");
						this.addDeath(player);
					}
				} else {
					event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fбыл взорван");
					this.addDeath(player);
				}
				break;

			case MAGIC:
				event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fумер от магии");
				this.addDeath(player);
				break;

			case HUNGER:
				event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fумер от голода");
				this.addDeath(player);
				break;

			default:
				event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fпогиб");
				this.addDeath(player);
				break;

		}
	}

	private void addKill(String playerName) {
		Config config = new Config(AnarchyCore.getInstance().getDataFolder() + "/kills.yml", Config.YAML);
		config.set(playerName, getKills(playerName) + 1);
		config.save();
		config.reload();
	}

	private void addDeath(Player player) {
		Config config = new Config(AnarchyCore.getInstance().getDataFolder() + "/deaths.yml", Config.YAML);
		config.set(player.getName(), getDeaths(player) + 1);
		config.save();
		config.reload();
	}
}