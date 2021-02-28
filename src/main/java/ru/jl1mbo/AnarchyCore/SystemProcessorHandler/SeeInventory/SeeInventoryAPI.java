package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.SeeInventory;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.SeeInventory.Commands.SeeInventoryCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.SeeInventory.EventsListener.InventoryTransactionListener;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.SeeInventory.Inventory.DoubleChest;

public class SeeInventoryAPI {
	public static Map<String, Player> INVENTORY_PLAYER = new HashMap<>();

	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new InventoryTransactionListener(), Main.getInstance());
		Server.getInstance().getCommandMap().register("", new SeeInventoryCommand());
	}

	public static void checkInventory(Player target, Player player) {
		INVENTORY_PLAYER.put(player.getName(), target);
		DoubleChest doubleChest = new DoubleChest("Просмотр Инвентаря");
		doubleChest.setContents(setItems(target.getInventory().getContents()));
		FakeInventoryAPI.openInventory(player, doubleChest);
	}

	public static Map<Integer, Item> setItems(Map<Integer, Item> items) {
		DoubleChest doubleChest = new DoubleChest("Просмотр Инвентаря");
		doubleChest.setContents(items);
		doubleChest.setItem(45, Item.get(
								Item.ENDER_EYE).setCustomName("§r§6Назад").setLore("\n§rНажмите§7, §fчтобы вернуться в главное\nменю просмотра Инвентаря§7."));
		doubleChest.setItem(49, Item.get(
								Item.ENDER_CHEST).setCustomName("§r§6Открыть Сундук Края").setLore("\n§rНажмите§7, §fчтобы открыть\n§6Эндер Сундук§7!"));
		doubleChest.setItem(53, Item.get(
								Item.BOOK).setCustomName("§r§6Справка").setLore("\n§rПока что здесь ничего нет§7,\n§fно я уже занимаюсь этим§7!"));
		return doubleChest.getContents();
	}
}