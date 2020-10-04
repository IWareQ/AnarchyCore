package Anarchy.Module.Commands.Storage;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Commands.Storage.Utils.StorageChest;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;

public class StorageHandler extends Command implements Listener {

	public StorageHandler() {
		super("storage", "§r§l§fХранилище купленных Предметов");
		this.commandParameters.clear();
		new File(AnarchyMain.datapath + "/StorageItems/").mkdirs();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args.length == 0) {
			Player player = (Player)sender;
			File dataFile = new File(AnarchyMain.datapath + "/StorageItems/" + player.getName().toLowerCase() + ".yml");
			if (!dataFile.exists()) {
				player.sendMessage("§l§c| §r§fВ хранилище §6пусто §7:§fc§7!");
				return false;
			}
			StorageChest storageChest = new StorageChest("§l§fХранилище Предметов", dataFile);
			Config config = new Config(dataFile, Config.YAML);
			for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
				ArrayList<Object> itemData = (ArrayList<Object>)entry.getValue();
				Item item = Item.get((int)itemData.get(0), (int)itemData.get(1), (int)itemData.get(2));
				CompoundTag compoundTag = null;
				if (itemData.size() > 3) {
					try {
						compoundTag = NBTIO.read((byte[])itemData.get(3), ByteOrder.LITTLE_ENDIAN);
					} catch (IOException e) {
						Server.getInstance().getLogger().alert("StorageHandler: " + e);
						AnarchyMain.sendMessageToChat("StorageHandler.java\nСмотрите Server.log", 2000000004);
					}
				}
				if (compoundTag == null) {
					compoundTag = new CompoundTag();
				}
				compoundTag.putString("UUID", entry.getKey());
				item.setNamedTag(compoundTag);
				storageChest.addItem(item);
			}
			FakeChestsAPI.openInventory(player, storageChest);
		} else if (args.length >= 2 && !(sender instanceof Player)) {
			String[] split = args[0].split(":");
			String playerName = StringUtils.implode(args, 1).toLowerCase();
			Item item = Item.get(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
			try {
				sender.sendMessage(AnarchyMain.PREFIX + "§fИгрок §6" + playerName + " §fполучил §6" + item.getName() + " §fв колличестве §6" + split[2] + " §fшт§7!");
				Config config = new Config(AnarchyMain.datapath + "/StorageItems/" + playerName + ".yml", Config.YAML);
				config.set(UUID.randomUUID().toString(), item.hasCompoundTag() ? new Object[] {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), NBTIO.write(item.getNamedTag(), ByteOrder.LITTLE_ENDIAN)} :
						   new Object[] {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])});
				config.save();
			} catch (IOException e) {
				Server.getInstance().getLogger().alert("StorageHandler: " + e);
				AnarchyMain.sendMessageToChat("StorageHandler.java\nСмотрите Server.log", 2000000004);
			}
		}
		return false;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction)action;
				if (slotChange.getInventory() instanceof StorageChest) {
					event.setCancelled(true);
					Item sourceItem = action.getSourceItem();
					Player player = event.getTransaction().getSource();
					CompoundTag compoundTag = sourceItem.getNamedTag();
					if (compoundTag != null && compoundTag.getString("UUID") != null) {
						PlayerInventory playerInventory = player.getInventory();
						StorageChest storageChest = (StorageChest)slotChange.getInventory();
						if (playerInventory.canAddItem(sourceItem)) {
							storageChest.removeItem(sourceItem);
							compoundTag.remove("UUID");
							playerInventory.addItem(sourceItem.clearCustomName().setNamedTag(compoundTag));
							player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
							player.sendMessage("§l§6• §r§fПредмет с Хранилища успешно взят§7!");
							AnarchyMain.sendMessage("Игрок " + player.getName() + " забрал " + sourceItem.getName() + " с Хранилища", 391567987);
						}
					} else {
						player.level.addSound(player, Sound.NOTE_BASS, 1, 1, player);
						event.setCancelled(true);
					}
					return;
				}
			}
		}
	}
}