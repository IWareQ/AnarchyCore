package Anarchy.Module.Commands.Spectate;

import java.util.Map;

import Anarchy.Module.Commands.Inventory.InventoryHandler;
import Anarchy.Module.Commands.Spectate.Utils.DoubleChest;
import Anarchy.Module.Commands.Spectate.Utils.SpectatePlayer;
import BanSystemAPI.BanSystem.Commands.BanCommand;
import BanSystemAPI.BanSystem.Commands.MuteCommand;
import BlockProtectionAPI.BlockProtection.BlockProtectionAPI;
import FakeInventoryAPI.FakeInventoryAPI;
import FormAPI.Forms.Elements.SimpleForm;
import PermissionsAPI.Permissions.PermissionsAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
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
			InventoryTransactionPacket transactionPacket = (InventoryTransactionPacket)dataPacket;
			if (transactionPacket.transactionType == InventoryTransactionPacket.TYPE_USE_ITEM) {
				UseItemData useItemData = (UseItemData)transactionPacket.transactionData;
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
						int regionID = BlockProtectionAPI.getRegionIDByLocation(player.getLocation());
						if (regionID != -1) {
							player.sendTip(BlockProtectionAPI.BUSY_BY.replace("{PLAYER}", BlockProtectionAPI.getRegionOwner(regionID)));
							player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
						} else {
							player.sendTip(BlockProtectionAPI.FREE);
							player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
						}
					}
				}
				break;

				case Item.CHEST: {
					if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && player.getGamemode() == 3) {
						SpectatePlayer spectatePlayer = SpectateAPI.SPECTATE_PLAYERS.get(player.getName());
						Player target = Server.getInstance().getPlayer(spectatePlayer.getName());
						InventoryHandler.checkInventory(target.getName(), player);
					}
				}
				break;

				case Item.SHULKER_BOX: {
					if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && player.getGamemode() == 3) {
						BlockEntity blockEntity = block.getLevel().getBlockEntity(blockVector3);
						if (blockEntity instanceof BlockEntityChest) {
							BlockEntityChest blockEntityChest = (BlockEntityChest)blockEntity;
							Map<Integer, Item> contents = blockEntityChest.getInventory().getContents();
							DoubleChest doubleChest = new DoubleChest("Просмотр содержимого");
							doubleChest.setContents(contents);
							FakeInventoryAPI.openInventory(player, doubleChest);
						} else if (blockEntity instanceof BlockEntityBarrel) {
							BlockEntityBarrel entityBarrel = (BlockEntityBarrel)blockEntity;
							Map<Integer, Item> contents = entityBarrel.getInventory().getContents();
							DoubleChest doubleChest = new DoubleChest("Просмотр содержимого");
							doubleChest.setContents(contents);
							FakeInventoryAPI.openInventory(player, doubleChest);
						}
					}
				}
				break;

				case Item.CLOCK: {
					if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && player.getGamemode() == 3) {
						SpectatePlayer spectatePlayer = SpectateAPI.SPECTATE_PLAYERS.get(player.getName());
						Player target = Server.getInstance().getPlayer(spectatePlayer.getName());
						String brand = player.getLoginChainData().getDeviceModel().split("\\s+")[0];
						String cheat;
						if (!brand.equals(brand.toUpperCase())) {
							cheat = "да";
						} else {
							cheat = "нет";
						}
						SimpleForm simpleForm = new SimpleForm("§r§fПанель Администрирования");
						simpleForm.setContent("§l§6• §r§fИгрок§7: §6" + target.getName() + "\n§l§6• §r§fРанг§7: " + PermissionsAPI.GROUPS.get(PermissionsAPI.getGroup(target.getName())) + "\n§l§6• §r§fУстройство§7: §6" + target.getLoginChainData().getDeviceModel() + "\n§l§6• §r§fШанс стороннего ПО§7: §6" + cheat + "\n§l§6• §r§fЭффекты§7: §6" + target.getEffects() + "\n\n§l§6• §r§fВыберите нужный пункт Меню§7:");
						simpleForm.addButton("§r§fБлокировка Аккаунта");
						simpleForm.addButton("§r§fБлокировка Чата");
						simpleForm.send(player, (targetPlayer, targetForm, data)-> {
							if (data == -1) return;
							switch (data) {
							case 0: {
								BanCommand.banPlayerForm(player, target);
							}
							break;

							case 1: {
								MuteCommand.mutePlayerForm(player, target);
							}
							break;

							}
						});
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