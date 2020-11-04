package Anarchy.Module.EventsHandler;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Module.BanSystem.BanSystemAPI;
import Anarchy.Module.BanSystem.Utils.MuteUtils;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Module.EventsHandler.Utils.Hopper;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Utils.RandomUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
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
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.Config;
import nukkitcoders.mobplugin.entities.animal.Animal;
import nukkitcoders.mobplugin.entities.block.BlockEntitySpawner;
import nukkitcoders.mobplugin.entities.monster.Monster;

public class EventsHandler implements Listener {
	File dataFileDeaths = new File(AnarchyMain.datapath + "/Deaths.yml");
	File dataFileKills = new File(AnarchyMain.datapath + "/Kills.yml");
	Config configDeaths = new Config(dataFileDeaths, Config.YAML);
	Config configKills = new Config(dataFileKills, Config.YAML);
	public static Map<Player, Long> COOLDOWN = new HashMap<>();
	public static int CHAT_RADIUS = 70;

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Block block = player.getLevel().getBlock(new Position((double)(int)Math.round(event.getPlayer().x - 0.5), (double)(int)Math.round(event.getPlayer().y - 1.0), (double)(int)Math.round(event.getPlayer().z - 0.5)));
		if (!player.hasPermission("Acces.Admin")) {
			if (player.x < FunctionsAPI.BORDER[0] || player.x > FunctionsAPI.BORDER[1] || player.z < FunctionsAPI.BORDER[2] || player.z > FunctionsAPI.BORDER[3]) {
				player.sendTip("§l§c• §fВы пытаетесь §6выйти §fза границу мира§7! §c•§r");
				event.setCancelled(true);
			}
		}
		if (player.getFloorY() <= -10 && player.getLevel() != Server.getInstance().getLevelByName("the_end")) {
			player.teleport(FunctionsAPI.SPAWN.getSafeSpawn(new Position(-8.5, 51, -3.5)));
			player.sendMessage("§l§a• §r§fВы упали за границу мира§7! §fЧтобы Вы не потеряли свои вещи§7, §fмы решили телепортировать Вас на спавн§7!\n§l§6• §r§fЗапомните§7, §fесли вы упадете в бездну в мире §6TheEnd§7, §fто Вас не спасут§7!");
		}
		if (player.getLevel().equals(FunctionsAPI.SPAWN) && block.getId() == 544) {
			player.teleport(FunctionsAPI.randomPos(new Position(0, 0, 0, FunctionsAPI.MAP)));
			player.sendMessage("§l§a| §r§fВас §6успешно §fтелепортировало на рандомное место§7.");
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		Location location = event.getTo();
		if (!player.hasPermission("Acces.Admin")) {
			if (location.x < FunctionsAPI.BORDER[0] || location.x > FunctionsAPI.BORDER[1] || location.z < FunctionsAPI.BORDER[2] || location.z > FunctionsAPI.BORDER[3]) {
				player.sendTip("§l§c| §fВы пытаетесь §6телепортироваться §fза границу мира§7! §c|§r");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		EntityDamageEvent cause = player.getLastDamageCause();
		if (cause instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent)cause).getDamager();
			if (damager instanceof Player && player != damager) {
				player.sendMessage("§l§c• §r§fВы были убиты Игроком §6" + damager.getName());
				event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fпогиб от руки Игрока §6" + damager.getName());
				Double money = EconomyAPI.myMoney(player) * 20 / 100;
				if (money > 5.0) {
					player.sendMessage("§l§c• §r§fПри смерти Вы потеряли §6" + String.format("%.1f", money) + " §7(§f20§7%)");
					((Player)damager).sendMessage("§l§6• §r§fВо время убийства§7, §fВы украли §6" + String.format("%.1f", money) + " §fу Игрока §6" + player.getName());
					EconomyAPI.reduceMoney(player, money);
					EconomyAPI.addMoney((Player)damager, money);
					this.addKill((Player)damager, 1);
					this.addDeaths(player, 1);
				}
				return;
			}
		}
		event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fпогиб");
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
		Item item = event.getItem();
		PlayerInventory playerInventory = player.getInventory();
		Block block = event.getBlock();
		if (item != null && item.getCustomName().equals("§r§l§f๑ Сокровище ๑") && item.getId() == Item.DOUBLE_PLANT) {
			player.sendMessage("§l§6• §r§fСокровище успешно Активированно§7!");
			player.getInventory().removeItem(item);
		}
		if (player.getLevel().equals(FunctionsAPI.SPAWN) && !(player.hasPermission("Access.Admin"))) {
			player.sendTip("§l§fТерритория не доступна для взаимодествия§7!");
			event.setCancelled(true);
		}
		if (item != null && item.getCustomName().equals("§r§fЗлодейская кирка") && item.getId() == Item.NETHERITE_PICKAXE) {
			if (block.getId() == Item.BEDROCK) {
				player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
				player.sendMessage("§l§6• §r§fБедрок был успешно сломан");
				playerInventory.setItemInHand(Item.get(Item.AIR));
				player.getLevel().setBlock(new Position(block.getX(), block.getY(), block.getZ()), Block.get(0));
			} else {
				player.sendTip("§l§fИспользуй только на §6Бедроке§7!");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Item item = event.getItem();
		if (player.getLevel().equals(FunctionsAPI.SPAWN) && !(player.hasPermission("Access.Admin"))) {
			player.sendTip("§l§fТерритория не доступна для взаимодействия§7!");
			event.setCancelled(true);
		}

		if (block.getId() == Block.MONSTER_SPAWNER) {
			if (item != null && item.isPickaxe() && item.getName().equals("§r§fКирка похитителя")) {
				BlockEntity be = block.getLevel().getBlockEntity(block);
				if (be instanceof BlockEntitySpawner) {
					int entity = ((BlockEntitySpawner)be).getSpawnEntityType();
					if (entity > 0) {
						item.setDamage(item.getDamage() + 20);
						event.setDrops(new Item[] {Item.get(52, 0, 1), Item.get(383, entity, 1)});
						return;
					}
				}
				item.setDamage(item.getDamage() + 20);
				event.setDrops(new Item[] {Item.get(52, 0, 1)});
			}
		}
		if (block.getId() == Block.DIAMOND_ORE) {
			int dropExp = block.getDropExp();
			player.addExperience(dropExp);
			event.setDropExp(0);
		} else if (block.getId() == Block.REDSTONE_ORE) {
			int dropExp = block.getDropExp();
			player.addExperience(dropExp);
			event.setDropExp(0);
		} else if (block.getId() == Block.COAL_ORE) {
			int dropExp = block.getDropExp();
			player.addExperience(dropExp);
			event.setDropExp(0);
		} else if (block.getId() == Block.EMERALD_ORE) {
			int dropExp = block.getDropExp();
			player.addExperience(dropExp);
			event.setDropExp(0);
		} else if (block.getId() == Block.LAPIS_ORE) {
			int dropExp = block.getDropExp();
			player.addExperience(dropExp);
			event.setDropExp(0);
		} else if (block.getId() == Block.QUARTZ_ORE) {
			int dropExp = block.getDropExp();
			player.addExperience(dropExp);
			event.setDropExp(0);
		} else if (block.getId() == Block.NETHER_GOLD_ORE) {
			int dropExp = block.getDropExp();
			player.addExperience(dropExp);
			event.setDropExp(0);
		} else if (block.getId() == Block.LIT_REDSTONE_ORE) {
			int dropExp = block.getDropExp();
			player.addExperience(dropExp);
			event.setDropExp(0);
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
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(FunctionsAPI.SPAWN) && !(player.hasPermission("Access.Admin"))) {
			player.sendTip("§l§fТерритория не доступна для взаимодействия§7!");
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (event instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
			if (entity.getNameTag().equals("§l§6Аукционер")) {
				AuctionAPI.AUCTION_PAGE.put((Player)damager, 0);
				AuctionAPI.showAuction((Player)damager, true);
			}
			if (entity.getNameTag().equalsIgnoreCase("§l§6Барыга")) {
				Hopper hopper = new Hopper("§l§6Барыга");
				Item netheritePickaxe = Item.get(Item.NETHERITE_PICKAXE, 0, 1).setCustomName("§r§fЗлодейская кирка").setLore("§l§6• §r§fХотели сломать §6Бедрок §fкоторый мешается?\n§r§fЭта кирка поможет Вам с этим§7!\n\n§r§fЦена§7: §630000");
				Item goldPickaxe = Item.get(Item.WOODEN_PICKAXE, 0, 1).setCustomName("§r§fКирка похитителя").setLore("§l§6• §r§fНе правильно поставили §6Спавнер?\n§r§fХотели бы переставить§7? §fНе беда§7!\n§r§fЭта кирка поможет Вам с этим§7!\n\n§r§fЦена§7: §620000");
				hopper.addItem(netheritePickaxe);
				hopper.addItem(goldPickaxe);
				FakeChestsAPI.openInventory((Player)damager, hopper);
			}
		}
		if (entity.getLevel().equals(FunctionsAPI.SPAWN)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction)action;
				if (slotChange.getInventory() instanceof Hopper) {
					Player player = event.getTransaction().getSource();
					Item sourceItem = action.getSourceItem();
					event.setCancelled(true);
					switch (sourceItem.getName()) {
					case "§r§fЗлодейская кирка": {
						if (EconomyAPI.myMoney(player) < 30000) {
							player.sendMessage("§l§7(§3Барыга§7) §r§fНедостаточно монет§7, §fдля совершения покупки§7!");
							player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
							return;
						}
						PlayerInventory playerInventory = player.getInventory();
						if (playerInventory.canAddItem(sourceItem)) {
							playerInventory.addItem(sourceItem.setLore(""));
							player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
							player.sendMessage("§l§7(§3Барыга§7) §r§7«§6Злодейская кирка§7» §fуспешно куплена за §630000§7!");
							EconomyAPI.reduceMoney(player, 30000.0);
						}
					}
					break;

					case "§r§fКирка похитителя": {
						if (EconomyAPI.myMoney(player) < 20000) {
							player.sendMessage("§l§7(§3Барыга§7) §r§fНедостаточно монет§7, §fдля совершения покупки§7!");
							player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
							return;
						}
						PlayerInventory playerInventory = player.getInventory();
						if (playerInventory.canAddItem(sourceItem)) {
							playerInventory.addItem(sourceItem.setLore(""));
							player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
							player.sendMessage("§l§7(§3Барыга§7) §r§7«§6Кирка похитителя§7» §fуспешно куплена за §620000§7!");
							EconomyAPI.reduceMoney(player, 20000.0);
						}
					}
					break;

					}
				}
			}
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
		Long cooldownTime = COOLDOWN.get(player);
		Long nowTime = System.currentTimeMillis() / 1000;
		if (cooldownTime != null && cooldownTime > nowTime) {
			player.sendMessage("§l§6• §r§fВы слишком часто пишите в чат§7, §fпожалуйста§7, §fподождите несколько секунд§7!");
			event.setCancelled(true);
			return;
		}
		MuteUtils muteUtils = BanSystemAPI.getMute(player.getName());
		if (BanSystemAPI.playerIsMuted(player.getName())) {
			if (muteUtils.getTime() < System.currentTimeMillis() / 1000L) {
				BanSystemAPI.unMutePlayer(player.getName());
				event.setCancelled(true);
				return;
			} else {
				player.sendMessage("§l§c• §r§fТебя замутили§7! §fАдминистратор §6" + muteUtils.getBanner() + " §fзакрыл тебе доступ к чату на §6" + ((muteUtils.getTime() - System.currentTimeMillis() / 1000L) / 60 % 60) + " §fмин§7. §6" + ((muteUtils.getTime() - System.currentTimeMillis() / 1000L) % 60) + " §fсек§7. §fпо причине §6" + muteUtils.getReason() + "§7!\n§fНо не расстраивайся§7, §fвсё наладится§7!");
				event.setCancelled(true);
			}
		}
		if (String.valueOf(playerMessage.charAt(0)).equals("#")) {
			Set<CommandSender> adminPlayer = new HashSet<>();
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				if (players.hasPermission("AdminChat")) {
					adminPlayer.add(players);
				}
			}
			adminPlayer.add(new ConsoleCommandSender());
			event.setFormat("§7(§cA§7) " + displayName + " §8» §6" + playerMessage.substring(1).replaceAll("§", ""));
			event.setRecipients(adminPlayer);
			COOLDOWN.put(player, nowTime + 2);
		} else if (String.valueOf(playerMessage.charAt(0)).equals("!")) {
			event.setFormat("§7(§aG§7) " + displayName + " §8» §7" + playerMessage.substring(1).replaceAll("§", ""));
			COOLDOWN.put(player, nowTime + 2);
		} else {
			Set<CommandSender> players = new HashSet<>();
			for (Player playerChat : Server.getInstance().getOnlinePlayers().values()) {
				if (playerChat.level == player.level && playerChat.distance(new Location(player.getX(), player.getY(), player.getZ(), playerChat.getLevel())) <= CHAT_RADIUS) {
					players.add(playerChat);
				}
			}
			players.add(new ConsoleCommandSender());
			event.setFormat("§7(§6L§7) " + displayName + " §8» §f" + playerMessage.replaceAll("§", ""));
			event.setRecipients(players);
			COOLDOWN.put(player, nowTime + 2);
		}
	}

	private void addKill(Player player, int number) {
		this.configKills.set(player.getName(), this.getKills(player) + number);
		this.configKills.save();
	}

	private int getKills(Player player) {
		return this.configKills.getInt(player.getName(), 0);
	}

	private int getDeaths(Player player) {
		return this.configDeaths.getInt(player.getName(), 0);
	}

	private void addDeaths(Player player, int number) {
		this.configDeaths.set(player.getName(), this.getDeaths(player) + number);
		this.configDeaths.save();
	}
}