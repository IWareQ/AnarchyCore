package Anarchy.Module.Commands.Spectate;

import java.util.Map;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Commands.Inventory.InventoryHandler;
import Anarchy.Module.Commands.Spectate.Utils.DoubleChest;
import Anarchy.Module.Commands.Spectate.Utils.SpectatePlayer;
import Anarchy.Module.Regions.RegionsAPI;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBarrel;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.transaction.data.UseItemData;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InventoryTransactionPacket;

public class SpectateEventsHandler implements Listener {

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
				case Item.REDSTONE_DUST: {
					if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && player.getGamemode() == 3) {
						SpectateAPI.removeSpectate(player);
					}
				}
				break;

				case Item.STICK: {
					if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && player.getGamemode() == 3) {
						int regionID = RegionsAPI.getRegionIDByLocation(player.getLocation());
						if (regionID != -1) {
							player.sendTip(RegionsAPI.BUSY_BY.replace("{PLAYER}", RegionsAPI.getRegionOwner(regionID)));
							player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
						} else {
							player.sendTip(RegionsAPI.FREE);
							player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
						}
					}
				}
				break;

				case Item.CHEST: {
					if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && player.getGamemode() == 3) {
						SpectatePlayer spectatePlayer = SpectateAPI.SPECTATE_PLAYERS.get(player.getName());
						InventoryHandler.checkInventory(spectatePlayer.getSpectateName(), player);
					}
				}
				break;

				case Item.SHULKER_BOX: {
					if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && player.getGamemode() == 3) {
						BlockEntity blockEntity = block.getLevel().getBlockEntity(blockVector3);
						if (blockEntity instanceof BlockEntityChest) {
							BlockEntityChest blockEntityChest = (BlockEntityChest) blockEntity;
							Map<Integer, Item> contents = blockEntityChest.getInventory().getContents();
							DoubleChest doubleChest = new DoubleChest("Просмотр содержимого");
							doubleChest.setContents(contents);
							FakeChestsAPI.openInventory(player, doubleChest);
						}
					}
				}
				break;
				
				default: {
					if (player.getGamemode() == 3) {
						BlockEntity blockEntity = block.getLevel().getBlockEntity(blockVector3);
						if (blockEntity instanceof BlockEntityChest) {
							BlockEntityChest blockEntityChest = (BlockEntityChest) blockEntity;
							Map<Integer, Item> contents = blockEntityChest.getInventory().getContents();
							DoubleChest doubleChest = new DoubleChest("Просмотр содержимого");
							doubleChest.setContents(contents);
							FakeChestsAPI.openInventory(player, doubleChest);
						} else if (blockEntity instanceof BlockEntityBarrel) {
							BlockEntityBarrel entityBarrel = (BlockEntityBarrel)blockEntity;
							Map<Integer, Item> contents = entityBarrel.getInventory().getContents();
							DoubleChest doubleChest = new DoubleChest("Просмотр содержимого");
							doubleChest.setContents(contents);
							FakeChestsAPI.openInventory(player, doubleChest);
						}
					}
				}
				break;
				}
			}
		}
	}

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName())) {
			SpectateAPI.removeSpectate(player);
		}
	}
}