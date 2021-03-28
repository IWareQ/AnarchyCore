package ru.jl1mbo.AnarchyCore.Modules.EventsListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.item.EntityEndCrystal;
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
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.utils.Config;
import nukkitcoders.mobplugin.entities.animal.Animal;
import nukkitcoders.mobplugin.entities.monster.Monster;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Modules.Clans.ClanAPI;
import ru.jl1mbo.AnarchyCore.Modules.CustomRecipes.Utils.CustomItemID;
import ru.jl1mbo.AnarchyCore.Modules.Economy.EconomyAPI;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class EventsListener implements Listener {
	private static Integer[] BORDER = new Integer[] {-2000, 2000, -2000, 2000};
	private static final Map<Player, Long> COOLDOWN = new HashMap<>();
	private static final int CHAT_RADIUS = 70;

	@EventHandler()
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		String displayName = (ClanAPI.playerIsInClan(player.getName()) ? "§3" + ClanAPI.getClanName(ClanAPI.getPlayerClanId(player.getName())) + " " : "") + PermissionAPI.getPlayerGroup(
								 player.getName()).getGroupName() + " " + player.getName();
		Long cooldownTime = COOLDOWN.get(player);
		long nowTime = System.currentTimeMillis() / 1000;
		if (cooldownTime != null && cooldownTime > nowTime) {
			player.sendMessage("§l§6• §rВы слишком §6часто пишите §fв чат§7, §fпожалуйста§7, §fподождите §6несколько §fсекунд§7!");
			event.setCancelled(true);
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
				if (players.getLevel() == player.getLevel() && players.distance(new Position(player.getFloorX(), player.getFloorY(), player.getFloorZ(), players.getLevel())) <= CHAT_RADIUS) {
					playerChat.add(players);
				}
			}
			playerChat.add(new ConsoleCommandSender());
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
		if (player.getLevel().equals(WorldSystemAPI.getSpawn()) && !(player.hasPermission("Development"))) {
			event.setCancelled(true);
		}
		if (player.getLevel().equals(WorldSystemAPI.getMap()) || player.getLevel().equals(WorldSystemAPI.getSpawn())) {
			if (block.getId() == Block.BED_BLOCK) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler()
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent cause = (EntityDamageByEntityEvent)entity.getLastDamageCause();
			if (cause.getDamager() instanceof Player) {
				Player player = (Player)cause.getDamager();
				double money = Utils.rand(1.0, 3.0);
				if ((entity instanceof Animal) || (entity instanceof Monster)) {
					player.sendTip("§7+ §6" + String.format("%.1f", money) + "");
					EconomyAPI.addMoney(player.getName(), money);
				}
			}
		}
	}

	@EventHandler()
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Block block = player.getLevel().getBlock(new Position(player.getFloorX(), player.getFloorY() - 1, player.getFloorZ()));
		if (!(player.hasPermission("Development"))) {
			if ((player.getFloorX() < BORDER[0]) || (player.getFloorX() > BORDER[1]) || (player.getFloorZ() < BORDER[2]) || (player.getFloorZ() > BORDER[3])) {
				player.sendTip("Вы пытаетесь §6выйти §fза границу мира");
				event.setCancelled(true);
			}
		}
		if ((player.getFloorY() <= -15) && (player.getLevel() != WorldSystemAPI.getTheEnd())) {
			player.teleport(WorldSystemAPI.getSpawn().getSafeSpawn());
			player.sendMessage("§l§6• §rВы упали за границу мира§7. §fЧтобы Вы не потеряли свои вещи§7, §fмы решили телепортировать Вас на спавн§7!");
		}
		if (player.getLevel().equals(WorldSystemAPI.getSpawn()) && (block.getId() == Block.END_PORTAL)) {
			WorldSystemAPI.randomPosition(WorldSystemAPI.getMap(), (pos)-> {
				player.teleport(pos.setLevel(WorldSystemAPI.getMap()));
				player.sendTitle("Телепортация§7...");
			});
		}
	}

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		if (event.getDamager() instanceof Player) {
			Player player = (Player)event.getDamager();
			PlayerInventory playerInventory = player.getInventory();
			if ((playerInventory.getItemInHand() != null) && (playerInventory.getItemInHand().getId() == CustomItemID.GOLDEN_MONEY) && entity.getLevel().equals(WorldSystemAPI.getSpawn())
					&& (entity instanceof EntityEndCrystal)) {
				if (Utils.rand(0, 100) < 15) {
					if (playerInventory.canAddItem(getRandomItems())) {
						Server.getInstance().getOnlinePlayers().values().forEach((players)-> {
							entity.getLevel().addParticleEffect(new Vector3f(entity.getFloorX(), entity.getFloorY(), entity.getFloorZ()).add(0.5F, 1.0F, 0.5F), "minecraft:altar_win", -1L, entity.getLevel().getDimension(), players);
						});
						playerInventory.removeItem(Item.get(CustomItemID.GOLDEN_MONEY));
						playerInventory.addItem(getRandomItems());
						player.sendMessage("§l§7(§3Алтарь§7) §rСпасибо за §6монетку§7, §fдержи §6награду§7!");
					} else {
						Server.getInstance().getOnlinePlayers().values().forEach((players)-> {
							entity.getLevel().addParticleEffect(new Vector3f(entity.getFloorX(), entity.getFloorY(), entity.getFloorZ()).add(0.5F, 1.0F, 0.5F), "minecraft:altar_win", -1L, entity.getLevel().getDimension(), players);
						});
						player.sendMessage("§l§7(§3Алтарь§7) §rВам повезло§7, §fно Ваш инвентарь был переполнен и из§7-§fза этого Ваша награда была удалена§7!");
						playerInventory.removeItem(Item.get(CustomItemID.GOLDEN_MONEY));
					}
				} else {
					player.sendMessage("§l§7(§3Алтарь§7) §rТебе §6не повезло§7, §fне растраивайся§7, §fв следующий раз повезет§7!");
					playerInventory.removeItem(Item.get(CustomItemID.GOLDEN_MONEY));
				}
				event.setCancelled(true);
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
				Entity damager = ((EntityDamageByEntityEvent)cause).getDamager();
				if (damager instanceof Player) {
					player.sendMessage("§l§6• §rВы были убиты Игроком §6" + damager.getName());
					event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fпогиб от руки Игрока §6" + damager.getName());
					this.addDeath(player);
					this.addKill(damager.getName());
					Double money = EconomyAPI.myMoney(player.getName()) * 20 / 100;
					if (money > 1.0) {
						player.sendMessage("§l§6• §rПри смерти Вы потеряли §6" + String.format("%.1f", money) + " §7(§f20§7%)");
						((Player)damager).sendMessage("§l§6• §rВо время убийства§7, §fВы украли §6" + String.format("%.1f", money) + " §fу Игрока §6" + player.getName());
						EconomyAPI.reduceMoney(player.getName(), money);
						EconomyAPI.addMoney(((Player)damager).getName(), money);
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
				Entity damager = ((EntityDamageByEntityEvent)cause).getDamager();
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
			event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fсгорел");
			this.addDeath(player);
			break;

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
				if (((EntityDamageByBlockEvent)cause).getDamager().getId() == Block.CACTUS) {
					event.setDeathMessage("§l§6• §rИгрок §6" + player.getName() + " §fумер от кактуса");
					this.addDeath(player);
				}
			}
			break;

		case BLOCK_EXPLOSION:

		case ENTITY_EXPLOSION:
			if (cause instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent)cause).getDamager();
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
		Config config = new Config(Main.getInstance().getDataFolder() + "/kills.yml", Config.YAML);
		config.set(playerName, this.getKills(playerName) + 1);
		config.save();
		config.reload();
	}

	private int getKills(String playerName) {
		Config config = new Config(Main.getInstance().getDataFolder() + "/kills.yml", Config.YAML);
		return config.get(playerName, 0);
	}

	private void addDeath(Player player) {
		Config config = new Config(Main.getInstance().getDataFolder() + "/deaths.yml", Config.YAML);
		config.set(player.getName(), this.getDeaths(player) + 1);
		config.save();
		config.reload();
	}

	private int getDeaths(Player player) {
		Config config = new Config(Main.getInstance().getDataFolder() + "/deaths.yml", Config.YAML);
		return config.get(player.getName(), 0);
	}

	private static Item getRandomItems() {
		Integer[] items = new Integer[] {Item.END_CRYSTAL, Item.GOLDEN_PICKAXE, Item.TOTEM, Item.DRAGON_EGG, Item.GHAST_TEAR, Item.DIAMOND, Item.SPONGE, Item.GOLD_INGOT, Item.IRON_INGOT, Item.TNT, Item.GOLDEN_APPLE};
		int itemId = items[new Random().nextInt(11)];
		Item item;
		if (itemId == Item.GOLDEN_PICKAXE || itemId == Item.TOTEM || itemId == Item.DRAGON_EGG) {
			item = Item.get(itemId, 0, 1);
		} else {
			item = Item.get(itemId, 0, Utils.rand(0, 10));
		}
		return item;
	}
}