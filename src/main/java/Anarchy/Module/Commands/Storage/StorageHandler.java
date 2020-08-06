package Anarchy.Module.Commands.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Commands.Storage.Utils.StorageChest;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;

public class StorageHandler extends Command implements Listener {
	
	public StorageHandler() {
		super("storage", "Хранилище купленных Предметов"); //Для выдачи предмета в Хранилище - /donate ID:META:COUNT USERNAME
		new File(AnarchyMain.datapath + "/StorageItems/").mkdirs();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length == 0) {
			Player player = (Player)commandSender;
			File dataFile = new File(AnarchyMain.datapath + "/StorageItems/" + player.getName().toLowerCase() + ".yml");
			if (!dataFile.exists()) {
				player.sendMessage("§l§c| §r§fСписок активных покупок §3пуст§7!");
				return false;
			}
			StorageChest storageChest = new StorageChest("§l§3Хранилище купленных Предметов", dataFile);
			Config config = new Config(dataFile, Config.YAML);
			for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
				ArrayList<Object> itemData = (ArrayList<Object>)entry.getValue();
				Item item = Item.get(Integer.parseInt((String)itemData.get(1).toString()), Integer.parseInt((String)itemData.get(2).toString()), Integer.parseInt((String)itemData.get(3).toString()));
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.putString("DATE", (String)itemData.get(0));
				item.setNamedTag(compoundTag);
				if (storageChest.canAddItem(item)) {
					storageChest.addItem(item.setLore("§r§fДата покупки §7- §3" + itemData.get(0)));
				}
			}
			FakeChestsAPI.openInventory(player, storageChest);
		} else if (strings.length >= 2 && !(commandSender instanceof Player)) {
			String[] split = strings[0].split(":");
			String playerName = StringUtils.implode(strings, 1).toLowerCase();
			commandSender.sendMessage("§l§e| §r§fИгрок §e" + playerName + " §fполучил §3" + split[0] + "§f:§3" + split[1] + " §7(§fx§3" + split[2] + "§7)");
			Config config = new Config(AnarchyMain.datapath + "/StorageItems/" + playerName + ".yml", Config.YAML);
			LinkedHashMap objectMap = (LinkedHashMap)config.getAll();
			objectMap.put(UUID.randomUUID().toString(), new Object[]{(String)StringUtils.getDate().toString(), Integer.parseInt((String)split[0]), Integer.parseInt((String)split[1]), Integer.parseInt((String)split[2])});
			config.setAll(objectMap);
			config.save();
		}
		return false;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction && ((SlotChangeAction)action).getInventory() instanceof StorageChest) {
				Item sourceItem = action.getSourceItem();
				Player player = event.getTransaction().getSource();
				if (sourceItem.hasCompoundTag() && sourceItem.getNamedTag().getString("DATE") != null) {
					sourceItem.clearCustomBlockData();
					player.level.addSound(player, Sound.RANDOM_ORB, 1, 1, player);
				} else {
					player.level.addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
					event.setCancelled();
				}
				return;
			}
		}
	}
}