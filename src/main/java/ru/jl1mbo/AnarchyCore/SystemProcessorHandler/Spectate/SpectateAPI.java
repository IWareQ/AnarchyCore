package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate;

import java.io.IOException;
import java.nio.ByteOrder;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.GameRulesChangedPacket;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.Commands.SpectateCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.EventsListener.DataPacketReceiveListener;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.EventsListener.PlayerCommandPreprocessListener;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.EventsListener.PlayerJoinListener;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.Task.SpectateTask;

public class SpectateAPI {
	public static Config config;
	public static String PREFIX = "§l§7(§3Система§7) §r";

	public static void register() {
		config = new Config(Main.getInstance().getDataFolder() + "/AdminSystem/Spectate.yml", Config.YAML);
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new DataPacketReceiveListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerCommandPreprocessListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
		Server.getInstance().getCommandMap().register("", new SpectateCommand());
		Server.getInstance().getScheduler().scheduleRepeatingTask(new SpectateTask(), 20);
	}

	public static boolean isSpectate(String playerName) {
		return config.exists(playerName.toLowerCase());
	}

	public static void addSpectate(Player player, Player target) {
		CompoundTag namedTag = new CompoundTag();
		if (isSpectate(player.getName())) {
			player.sendMessage(PREFIX + "§fПереключение на §6" + target.getName() + "§7!");
			config.set(player.getName().toLowerCase() + ".Spectate", target.getName());
			player.teleport(target);
			enableCoordinate(player, false);
		} else if (target != null) {
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
			try {
				config.set(player.getName().toLowerCase() + ".Position.World", player.getLevel().getName());
				config.set(player.getName().toLowerCase() + ".Position.X", player.getFloorX());
				config.set(player.getName().toLowerCase() + ".Position.Y", player.getFloorY());
				config.set(player.getName().toLowerCase() + ".Position.Z", player.getFloorZ());
				config.set(player.getName().toLowerCase() + ".Spectate", target.getName());
				config.set(player.getName().toLowerCase() + ".Inventory", NBTIO.write(namedTag, ByteOrder.LITTLE_ENDIAN));
				config.save();
				config.reload();
				namedTag.remove("Inventory");
			} catch (IOException e) {
				e.printStackTrace();
			}
			player.setGamemode(3);
			player.teleport(target);
			enableCoordinate(player, false);
			player.addEffect(Effect.getEffect(Effect.NIGHT_VISION).setAmplifier(0).setDuration(99999999 * 20).setVisible(false));
			player.getInventory().clearAll();
			player.getInventory().setHeldItemIndex(0);
			player.getInventory().setItem(4, Item.get(
											  Item.CHEST).setCustomName("§r§fПросмотреть Инвентарь").setLore("§r§l§6• §r§fИспользуйте чтобы просмотреть §6Инвентарь §fнаблюдаемого§7!"));
			player.getInventory().setItem(5, Item.get(Item.MOB_SPAWNER).setCustomName("§r§fСкоро").setLore("§r§l§6• §r§fСкоро тут что-то будет"));
			player.getInventory().setItem(8, Item.get(Item.REDSTONE).setCustomName("§r§fЗавершить Наблюдение"));
			player.getInventory().setItem(6, Item.get(Item.CLOCK).setCustomName("§r§fПанель Администрирования"));
			player.sendMessage(PREFIX + "§fВы начали наблюдать за Игроком §6" + target.getName() +
							   "\n§l§6• §r§fДля окончания наблюдения используйте §6Редстоун§7!");
		}
	}

	public static void removeSpectate(Player player) {
		CompoundTag namedTag = null;
		if (isSpectate(player.getName())) {
			try {
				namedTag = NBTIO.read((byte[]) config.get(player.getName().toLowerCase() + ".Inventory"), ByteOrder.LITTLE_ENDIAN);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (namedTag.contains("Inventory") && namedTag.get("Inventory") instanceof ListTag) {
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
			}
			player.teleport(new Position(config.getInt(player.getName().toLowerCase() + ".Position.X") + 0.5, config.getInt(player.getName().toLowerCase() + ".Position.Y"),
										 config.getInt(player.getName().toLowerCase() + ".Position.Z") + 0.5, Server.getInstance().getLevelByName(config.getString(player.getName().toLowerCase() + ".Position.World"))));
			player.setGamemode(0);
			player.removeEffect(Effect.NIGHT_VISION);
			player.extinguish();
			enableCoordinate(player, true);
			player.sendMessage(PREFIX + "§fВы закончили наблюдение за игроком §6" + config.getString(player.getName().toLowerCase() + ".Spectate"));
			namedTag.remove("Inventory");
			config.remove(player.getName().toLowerCase());
			config.save();
			config.reload();
		}
	}

	private static void enableCoordinate(Player player, boolean value) {
		GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
		gameRulesChanged.gameRules = WorldSystemAPI.getTest().getGameRules();
		gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, value);
		player.dataPacket(gameRulesChanged);
	}
}