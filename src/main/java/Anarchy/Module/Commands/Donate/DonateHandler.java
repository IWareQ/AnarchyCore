package Anarchy.Module.Commands.Donate;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Commands.Donate.Utils.DonateChest;
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

public class DonateHandler extends Command implements Listener {
	public DonateHandler() {
		super("donate", "Описание донат услуг");
		new File(AnarchyMain.datapath + "/DonateItems/").mkdirs();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length == 1 && strings[0].equals("my")) {
			Player player = (Player) commandSender;
			File dataFile = new File(AnarchyMain.datapath + "/DonateItems/" + player.getName().toLowerCase() + ".yml");
			if (!dataFile.exists()) {
				player.sendMessage("§l§c| §r§fУ Вас §cнет §fактивный покупок!");
				return false;
			}

			DonateChest donateChest = new DonateChest("Купленные Предметы", dataFile);
			Config config = new Config(dataFile, Config.YAML);
			for (Map.Entry <String, Object> entry: config.getAll().entrySet()) {
				ArrayList<Object> itemData = (ArrayList<Object>) entry.getValue();
				Item item = Item.get((int) itemData.get(0), (int) itemData.get(1), (int) itemData.get(2));
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.putString("DATE", entry.getKey());
				item.setNamedTag(compoundTag);
				if (donateChest.canAddItem(item)) {
					donateChest.addItem(item.setLore("§r§fДата покупки: §c" + entry.getKey()));
				}
			}
			FakeChestsAPI.openInventory(player, donateChest);
		} else if (strings.length >= 2) {
			String[] split = strings[0].split(":");
			String playerName = StringUtils.implode(strings, 1).toLowerCase();
			commandSender.sendMessage("Игрок " + playerName + " получил " + split[0] + ":" + split[1] + " (x" + split[2] + ")");
			Config config = new Config(AnarchyMain.datapath + "/DonateItems/" + playerName + ".yml", Config.YAML);
			LinkedHashMap objectMap = (LinkedHashMap) config.getAll();
			objectMap.put(UUID.randomUUID().toString(), new Object[] {
				StringUtils.getDate(),
				Integer.parseInt(split[0]),
				Integer.parseInt(split[1]),
				Integer.parseInt(split[2])
			});
			config.setAll(objectMap);
			config.save();
		}
		return false;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		for (InventoryAction action: event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction && ((SlotChangeAction) action).getInventory() instanceof DonateChest) {
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