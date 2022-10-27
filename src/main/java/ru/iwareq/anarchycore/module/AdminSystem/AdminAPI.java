package ru.iwareq.anarchycore.module.AdminSystem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.GameRulesChangedPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.Task;
import ru.contentforge.formconstructor.form.ModalForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.handler.SimpleFormHandler;
import ru.iwareq.anarchycore.manager.FakeInventory.FakeInventoryAPI;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.BanCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.MuteCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.UnBanCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.UnMuteCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Inventory.AdminChest;
import ru.iwareq.anarchycore.module.Permissions.PermissionAPI;
import ru.iwareq.anarchycore.util.NbtConverter;
import ru.iwareq.anarchycore.util.Utils;

import java.util.HashMap;
import java.util.List;

public class AdminAPI {

	private static final HashMap<String, Integer> CHEAT_CHECK = new HashMap<>();
	private static final AdminDB DB = new AdminDB();
	public static String PREFIX = "§l§7(§3Система§7) §r";
	private static int seconds = 420;

	public static void sendSearchPlayerForm(Player player, String playerName) {
		List<String> players = Utils.getPlayersList(playerName);
		SimpleForm simpleForm = new SimpleForm("Панель Администрирования");
		simpleForm.addContent("Выберите §6игрока§7, §fс которым хотите §6взаимодействовать§7!");
		SimpleFormHandler handler = (p, button) -> {
			sendAdminPanelForm(player, players.get(button.index));
		};
		for (String playerList : players) {
			simpleForm.addButton("§6" + playerList + "\n§fНажмите§7, §fчтобы перейти§7!", handler);
		}
		simpleForm.send(player);
	}

	public static void sendAdminPanelForm(Player player, String targetName) {
		Player target = Server.getInstance().getPlayerExact(targetName);
		SimpleForm simpleForm = new SimpleForm("Панель Администрирования");
		simpleForm.addContent("§l§6• §rИгрок§7: §6" + targetName);
		simpleForm.addContent("\n§l§6• §rРанг§7: " + PermissionAPI.getPlayerGroup(targetName).getGroupName());
		if (target != null) {
			simpleForm.addContent("\n§l§6• §rУстройство§7: §6" + target.getLoginChainData().getDeviceModel());
		}
		simpleForm.addContent("\n\nВыберите §6нужный пункт §fМеню§7:");
		if (isBanned(targetName) && player.hasPermission("Command.UnBan")) {
			simpleForm.addButton("Разблокировать аккаунт", (p, button) -> {
				UnBanCommand.openUnBanPlayerForm(player, targetName);
			});
		} else {
			simpleForm.addButton("Заблокировать аккаунт", (p, button) -> {
				BanCommand.openBanPlayerForm(player, targetName);
			});
		}
		if (isMuted(targetName)) {
			simpleForm.addButton("Разблокировать чат", (p, button) -> {
				UnMuteCommand.openUnMutePlayerForm(player, targetName);
			});
		} else {
			simpleForm.addButton("Заблокировать чат", (p, button) -> {
				MuteCommand.openMutePlayerForm(player, targetName);
			});
		}
		simpleForm.addButton("Просмотреть инвентарь", (p, button) -> {
			openCheckInventoryChest(player, targetName);
		});
		simpleForm.send(player);
	}


	private static void enableCoordinate(Player player, boolean value) {
		GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
		gameRulesChanged.gameRules = WorldSystemAPI.Test.getGameRules();
		gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, value);
		player.dataPacket(gameRulesChanged);
	}

	public static boolean isCheatCheck(String playerName) {
		return CHEAT_CHECK.containsKey(playerName.toLowerCase());
	}

	public static void addCheatCheacker(Player player, Player target) {
		if (!isCheatCheck(target.getName())) {
			ModalForm modalForm = new ModalForm("Проверка", "Вы уверены что хотите вызвать игрока §6" + target.getName() + " §fна проверку§7?", "Да", "Нет");
			modalForm.send(player, (p, yes) -> {
				if (yes) {
					int checkCode = Utils.rand(0, 10000);
					CHEAT_CHECK.put(target.getName(), checkCode);
					Utils.sendMessageToChat("💂CheatCheacker\n\nИгрок: " + target.getName() + "\nАдмин: " + player.getName() + "\nКод: " + checkCode);
					player.sendMessage(PREFIX + "Игрок §6" + target.getName() + " §fвызван на проверку§7!");
					target.sendTitle("§l§6Проверка");
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(3, 2, 0), "§6Проверка на читы§7!", "§fУ Вас есть §610 §fминут §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6" + checkCode), target);
					Entity.createEntity("CowNPC", target.getPosition().add(3, 0, 0)).spawnToAll();
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(-3, 2, 0), "§6Проверка на читы§7!", "§fУ Вас есть §610 §fминут §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6" + checkCode), target);
					Entity.createEntity("CowNPC", target.getPosition().add(-3, 0, 0)).spawnToAll();
					target.getLevel().addParticle(new FloatingTextParticle(player.getPosition().add(0, 2, 3), "§6Проверка на читы§7!", "§fУ Вас есть §610 §fминут §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6" + checkCode), target);
					Entity.createEntity("CowNPC", target.getPosition().add(0, 0, 3)).spawnToAll();
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(0, 2, -3), "§6Проверка на читы§7!", "§fУ Вас есть §610 §fминут §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6" + checkCode), target);
					Entity.createEntity("CowNPC", target.getPosition().add(0, 0, -3)).spawnToAll();
					target.getLevel().addSound(target, Sound.RANDOM_SCREENSHOT, 1.0F, 1.0F, target);
					Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {

						@Override()
						public void onRun(int tick) {
							if (seconds != 0) {
								seconds--;
								if (isCheatCheck(target.getName())) {
									target.sendTip("Время§7: §6" + Utils.getSecond(seconds));
									target.setImmobile(true);
								}
							} else {
								this.cancel();
								if (isCheatCheck(target.getName())) {
									addBan(target.getName(), "время проверки вышло", "CheatCheacker", 30 * 86400);
								}
								seconds = 420;
							}
						}
					}, 20);
				} else {
					player.sendMessage(PREFIX + "Проверка §6отменена§7!");
				}
			});
		} else {
			player.sendMessage(PREFIX + "Игрок §6" + target.getName() + " §fуже находится на проверке§7, §fпроверочный код§7: §6" + CHEAT_CHECK.get(target.getName()));
		}
	}

	public static void removeCheatCheacker(String playerName) {
		if (isCheatCheck(playerName)) {
			CHEAT_CHECK.remove(playerName.toLowerCase());
		}
	}

	public static void openCheckInventoryChest(Player player, String targetName) {
		AdminChest doubleChest = new AdminChest("Просмотр Инвентаря");
		Player target = Server.getInstance().getPlayer(targetName);
		if (target == null) {
			CompoundTag namedTag = Server.getInstance().getOfflinePlayerData(targetName);
			if (namedTag != null && namedTag.contains("Inventory") && namedTag.get("Inventory") instanceof ListTag) {
				ListTag<CompoundTag> inventoryList = namedTag.getList("Inventory", CompoundTag.class);
				for (CompoundTag item : inventoryList.getAll()) {
					int slot = item.getByte("Slot");
					if (slot >= 0 && slot < 9) {
						inventoryList.remove(item);
					} else if (slot >= 100 && slot < 104) {
						doubleChest.setItem(player.getInventory().getSize() + slot - 100, NBTIO.getItemHelper(item));
					} else if (slot == -106) {
						doubleChest.setItem(0, NBTIO.getItemHelper(item));
					}
				}
			} else {
				player.sendMessage(PREFIX + "Аккаунт игрока §6" + targetName + " §fне найден§7!");
				return;
			}
		} else {
			doubleChest.setContents(target.getInventory().getContents());
		}
		doubleChest.setItem(45, Item.get(Item.ENDER_EYE).setNamedTag(new CompoundTag().putString("Target", targetName)).setCustomName("§r§6Назад").setLore("\n§rНажмите§7, §fчтобы вернуться в главное\nменю просмотра Инвентаря§7."));
		doubleChest.setItem(49, Item.get(Item.ENDER_CHEST).setNamedTag(new CompoundTag().putString("Target", targetName)).setCustomName("§r§6Открыть Сундук Края").setLore("\n§rНажмите§7, §fчтобы открыть\n§6Эндер Сундук§7!"));
		doubleChest.setItem(53, Item.get(Item.BOOK).setCustomName("§r§6Справка").setLore("\n§rПока что здесь ничего нет§7,\n§fно я уже занимаюсь этим§7!"));
		FakeInventoryAPI.openInventory(player, doubleChest);
	}

	public static void openCheckEnderChest(Player player, String targetName) {
		AdminChest doubleChest = new AdminChest("Просмотр Инвентаря");
		Player target = Server.getInstance().getPlayer(targetName);
		if (target == null) {
			CompoundTag namedTag = Server.getInstance().getOfflinePlayerData(targetName);
			if (namedTag != null && namedTag.contains("EnderItems") && namedTag.get("EnderItems") instanceof ListTag) {
				ListTag<CompoundTag> inventoryList = namedTag.getList("EnderItems", CompoundTag.class);
				for (CompoundTag item : inventoryList.getAll()) {
					doubleChest.setItem(item.getByte("Slot"), NBTIO.getItemHelper(item));
				}
			} else {
				player.sendMessage(PREFIX + "Аккаунт игрока §6" + targetName + " §fне найден§7!");
				return;
			}
		} else {
			doubleChest.setContents(target.getEnderChestInventory().getContents());
		}
		doubleChest.setItem(45, Item.get(Item.ENDER_EYE).setNamedTag(new CompoundTag().putString("Target", targetName)).setCustomName("§r§6Назад").setLore("\n§rНажмите§7, §fчтобы вернуться в главное\nменю просмотра Инвентаря§7."));
		doubleChest.setItem(49, Item.get(Item.ENDER_CHEST).setNamedTag(new CompoundTag().putString("Target", targetName)).setCustomName("§r§6Открыть Сундук Края").setLore("\n§rНажмите§7, §fчтобы открыть\n§6Эндер Сундук§7!"));
		doubleChest.setItem(53, Item.get(Item.BOOK).setCustomName("§r§6Справка").setLore("\n§rПока что здесь ничего нет§7,\n§fно я уже занимаюсь этим§7!"));
		FakeInventoryAPI.openInventory(player, doubleChest);
	}

	public static boolean isBanned(String playerName) {
		return DB.isBanned(playerName);
	}

	public static boolean isMuted(String playerName) {
		return DB.isMuted(playerName);
	}

	public static void addBan(String playerName, String bannerName, String reason, Integer time) {
		long endBan = System.currentTimeMillis() / 1000L + time;
		Utils.sendMessageToChat("🔒Блокировка аккаунта\n\nИгрок: " + playerName + "\nАдмин: " + bannerName + "\nПричина: " + reason + "\nПериод: " + Utils.getRemainingTime(endBan).replaceAll("§[0-9]", "").replaceAll("§[a-zA-Z]", ""));
		DB.ban(playerName, reason, endBan);
		Player target = Server.getInstance().getPlayerExact(playerName);
		if (target != null) {
			target.sendTitle("§l§6Аккаунт", "§l§6заблокирован", 0, 60, 0);
			target.setImmobile(true);
		}
	}

	public static void addMute(String playerName, String bannerName, String reason, Integer time) {
		long endMute = System.currentTimeMillis() / 1000L + time;
		Utils.sendMessageToChat("🙊Блокировка чата\n\nИгрок: " + playerName + "\nАдмин: " + bannerName + "\nПричина: " + reason + "\nПериод: " + Utils.getRemainingTime(endMute).replaceAll("§[0-9]", "").replaceAll("§[a-zA-Z]", ""));
		DB.mute(playerName, reason, endMute);
		Player target = Server.getInstance().getPlayerExact(playerName);
		if (target != null) {
			target.sendMessage("§l§6• §rТебя замутили§7! §fАдминистратор закрыл тебе доступ к чату на §6" + Utils.getRemainingTime(endMute) + " §fпо причине §6" + reason + "§7!\n§fНо не расстраивайся§7, §fвсё наладится§7!");
		}
	}

	public static void removeBan(String playerName, String unbannerName, String reason) {
		DB.unban(playerName);
		if (!reason.equals("buy")) {
			Utils.sendMessageToChat("🔓Разблокировка аккаунта\n\nИгрок: " + playerName + "\nАдмин: " + unbannerName + "\nПричина: " + reason);
		}
	}

	public static void removeMute(String playerName, String unbannerName, String reason) {
		DB.unmute(playerName);
		if (!reason.equals("buy")) {
			Utils.sendMessageToChat("🙉Разблокировка чата\n\nИгрок: " + playerName + "\nАдмин: " + unbannerName + "\nПричина: " + reason);
		}
	}

	public static long getBanTime(String playerName) {
		return DB.getBanTime(playerName);
	}

	public static String getBanReason(String playerName) {
		return DB.getBanReason(playerName);
	}

	public static long getMuteTime(String playerName) {
		return DB.getMuteTime(playerName);
	}

	public static String getMuteReason(String playerName) {
		return DB.getMuteReason(playerName);
	}

	public static boolean isSpectate(String playerName) {
		return false;
		// return MySQLUtils.getInteger("SELECT `ID` FROM `Spectates` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'") != -1;
	}

	public static void addSpectate(Player player, Player target) {
		CompoundTag namedTag = new CompoundTag();
		if (isSpectate(player.getName())) {
			player.sendMessage(PREFIX + "Переключение на §6" + target.getName() + "§7!");
			DB.spectateTargetUpdate(player.getName(), target.getName());
			player.teleport(new Position(target.getFloorX() + 0.5, target.getFloorY(), target.getFloorZ() + 0.5, target.getLevel()));
			enableCoordinate(player, false);
		} else {
			ListTag<CompoundTag> inventoryTag = null;
			if (player.getInventory() != null) {
				inventoryTag = new ListTag<>("Inventory");
				namedTag.putList(inventoryTag);
				for (int slot = 0; slot < 9; ++slot) {
					inventoryTag.add(new CompoundTag().putByte("Count", 0).putShort("Damage", 0).putByte("Slot", slot).putByte("TrueSlot", -1).putShort("id", 0));
				}
				int slotCount = Player.SURVIVAL_SLOTS + 9;
				for (int slot = 9; slot < slotCount; ++slot) {
					Item item = player.getInventory().getItem(slot - 9);
					inventoryTag.add(NBTIO.putItemHelper(item, slot));
				}
				for (int slot = 100; slot < 104; ++slot) {
					Item item = player.getInventory().getItem(player.getInventory().getSize() + slot - 100);
					if (item != null && item.getId() != Item.AIR) {
						inventoryTag.add(NBTIO.putItemHelper(item, slot));
					}
				}
			}
			if (player.getOffhandInventory() != null) {
				Item item = player.getOffhandInventory().getItem(0);
				if (item.getId() != Item.AIR) {
					if (inventoryTag == null) {
						inventoryTag = new ListTag<>("Inventory");
						namedTag.putList(inventoryTag);
					}
					inventoryTag.add(NBTIO.putItemHelper(item, -106));
				}
			}

			DB.spectate(player.getName(), target.getName(), player.getLevel().getName(), player.getFloorX(),
					player.getFloorY(), player.getFloorZ(), NbtConverter.toHex(namedTag));
			namedTag.remove("Inventory");
			player.setGamemode(3);
			player.teleport(target);
			enableCoordinate(player, false);
			player.addEffect(Effect.getEffect(Effect.NIGHT_VISION).setAmplifier(Integer.MAX_VALUE).setDuration(Integer.MAX_VALUE).setVisible(false));
			player.getInventory().clearAll();
			player.getInventory().setHeldItemIndex(0);
			player.getInventory().setItem(4, Item.get(Item.CHEST).setCustomName("§r§6Просмотреть Инвентарь"));
			player.getInventory().setItem(5, Item.get(Item.MONSTER_SPAWNER).setCustomName("§r§6Вызвать на проверку"));
			player.getInventory().setItem(8, Item.get(Item.REDSTONE).setCustomName("§r§6Завершить Наблюдение"));
			player.getInventory().setItem(6, Item.get(Item.CLOCK).setCustomName("§r§6Панель Администрирования"));
			player.sendMessage(PREFIX + "Вы начали §6наблюдать §fза игроком §6" + target.getName());
		}
	}

	public static String getSpectateTarget(String playerName) {
		return DB.getSpectateTarget(playerName);
	}

	public static void removeSpectate(Player player) {
		String playerName = player.getName();
		CompoundTag namedTag = NbtConverter.toNbt(DB.getSpectateNbtHex(playerName));
		if (namedTag != null && namedTag.contains("Inventory") && namedTag.get("Inventory") instanceof ListTag) {
			ListTag<CompoundTag> inventoryList = namedTag.getList("Inventory", CompoundTag.class);
			for (CompoundTag item : inventoryList.getAll()) {
				int slot = item.getByte("Slot");
				if (slot >= 0 && slot < 9) {
					inventoryList.remove(item);
				} else if (slot >= 100 && slot < 104) {
					player.getInventory().setItem(player.getInventory().getSize() + slot - 100, NBTIO.getItemHelper(item));
				} else if (slot == -106) {
					player.getOffhandInventory().setItem(0, NBTIO.getItemHelper(item));
				} else {
					player.getInventory().setItem(slot - 9, NBTIO.getItemHelper(item));
				}
			}
			player.teleport(DB.getSpectateStartPosition(playerName));
			player.extinguish();
			player.setGamemode(0);
			player.removeEffect(Effect.NIGHT_VISION);
			enableCoordinate(player, true);
			player.sendMessage(PREFIX + "Вы §6закончили §fнаблюдение за игроком §6" + AdminAPI.getSpectateTarget(playerName));
			namedTag.remove("Inventory");
			DB.removeSpectate(playerName);
		}
	}
}