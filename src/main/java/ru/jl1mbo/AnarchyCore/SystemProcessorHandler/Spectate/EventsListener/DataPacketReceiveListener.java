package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.EventsListener;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBarrel;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.blockentity.BlockEntityShulkerBox;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.transaction.data.UseItemData;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Commands.BanCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Commands.MuteCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.CheatCheackerAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.SeeInventory.SeeInventoryAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.SpectateAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.Inventory.DoubleChest;

public class DataPacketReceiveListener implements Listener  {
	private static Config config = SpectateAPI.config;

	@EventHandler()
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		DataPacket dataPacket = event.getPacket();
		Player player = event.getPlayer();
		if (dataPacket instanceof InventoryTransactionPacket) {
			InventoryTransactionPacket transactionPacket = (InventoryTransactionPacket) dataPacket;
			if (transactionPacket.transactionType == InventoryTransactionPacket.TYPE_USE_ITEM) {
				UseItemData useItemData = (UseItemData) transactionPacket.transactionData;
				Vector3 blockVector3 = new Vector3(useItemData.blockPos.getX(), useItemData.blockPos.getY(), useItemData.blockPos.getZ());
				Block block = player.getLevel().getBlock(blockVector3);
				Item item = player.getInventory().getItemInHand();
				switch (item.getId()) {
				case Item.REDSTONE_DUST:
					if (SpectateAPI.isSpectate(player.getName()) && player.getGamemode() == 3) {
						SpectateAPI.removeSpectate(player);
					}
					break;
				case Item.CHEST:
					if (SpectateAPI.isSpectate(player.getName()) && player.getGamemode() == 3) {
						Player target = Server.getInstance().getPlayerExact(config.getString(player.getName().toLowerCase() + ".Spectate"));
						SeeInventoryAPI.checkInventory(target.getName(), player);
					}
					break;
				case Item.CLOCK:
					if (SpectateAPI.isSpectate(player.getName()) && player.getGamemode() == 3) {
						Player target = Server.getInstance().getPlayerExact(config.getString(player.getName().toLowerCase() + ".Spectate"));
						SimpleForm simpleForm = new SimpleForm("§r§fПанель Администрирования");
						simpleForm.setContent("§l§6• §r§fИгрок§7: §6" + target.getName() + "\n§l§6• §r§fРанг§7: " + PermissionAPI.getAllGroups().get(PermissionAPI.getGroup(target.getName())).getGroupName() + "\n§l§6• §r§fУстройство§7: §6" + target.getLoginChainData().getDeviceModel() + "\n§l§6• §r§fЭффекты§7: §6" + target.getEffects() +
											  "\n\n§l§6• §r§fВыберите нужный пункт Меню§7:");
						simpleForm.addButton("§r§fБлокировка Аккаунта");
						simpleForm.addButton("§r§fБлокировка Чата");
						simpleForm.send(player, (targetPlayer, targetForm, data) -> {
							if (data == -1) return;
							switch (data) {
							case 0: {
								BanCommand.banPlayerForm(player, target.getName());
							}
							break;

							case 1: {
								MuteCommand.mutePlayerForm(player, target.getName());
							}
							break;

							}
						});
					}
					break;
				case Item.MOB_SPAWNER:
					if (SpectateAPI.isSpectate(player.getName()) && player.getGamemode() == 3) {
						Player target = Server.getInstance().getPlayerExact(config.getString(player.getName().toLowerCase() + ".Spectate"));
						CheatCheackerAPI.addCheatCheacker(player, target);
					}
					break;
				default:
					if (player.getGamemode() == 3) {
						BlockEntity blockEntity = block.getLevel().getBlockEntity(blockVector3);
						if (blockEntity instanceof BlockEntityChest) {
							BlockEntityChest blockEntityChest = (BlockEntityChest) blockEntity;
							Map<Integer, Item> contents = blockEntityChest.getInventory().getContents();
							DoubleChest doubleChest = new DoubleChest("Просмотр содержимого");
							doubleChest.setContents(contents);
							FakeInventoryAPI.openInventory(player, doubleChest);
						} else if (blockEntity instanceof BlockEntityBarrel) {
							BlockEntityBarrel entityBarrel = (BlockEntityBarrel) blockEntity;
							Map<Integer, Item> contents = entityBarrel.getInventory().getContents();
							DoubleChest doubleChest = new DoubleChest("Просмотр содержимого");
							doubleChest.setContents(contents);
							FakeInventoryAPI.openInventory(player, doubleChest);
						} else if (blockEntity instanceof BlockEntityShulkerBox) {
							BlockEntityShulkerBox entityShulkerBox = (BlockEntityShulkerBox) blockEntity;
							Map<Integer, Item> contents = entityShulkerBox.getInventory().getContents();
							DoubleChest doubleChest = new DoubleChest("Просмотр содержимого");
							doubleChest.setContents(contents);
							FakeInventoryAPI.openInventory(player, doubleChest);
						}
					}
					break;
				}
			}
		}
	}
}