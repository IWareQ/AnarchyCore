package Anarchy.Module.Commands.Donate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Commands.Donate.Utils.DonateChest;
import Anarchy.Utils.StringUtils;
import FormAPI.Forms.Elements.SimpleForm;
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
	
	public static String getDate() {
		return new SimpleDateFormat("dd:MM:yyyy").format(Calendar.getInstance().getTime());
	}
	
	public DonateHandler() {
		super("donate", "Описание Донат Услуг");
		new File(AnarchyMain.datapath + "/DonateItems/").mkdirs();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length == 0) {
			Player player = (Player)commandSender;
			new SimpleForm("Описание Донат Услуг", " ").addButton("§l§6Вип").addButton("§l§aПремиум").addButton("§l§dEnigma").addButton("§l§cHydra").send(player, (target,form,data)->{
				if (data == -1) return;
				if (data == 0) {
					new SimpleForm("§fОписание §7- §l§6Вип", "Префикс §7- (§6Вип§7)\n§7• §6/food §7- §f восстановить голод\n§7• §fКоличество приватов §7- §63").send(player);
				}
				if (data == 1) {
					new SimpleForm("§fОписание §7- §l§aПремиум", "Префикс §7- (§aПремиум§7)\n§7• §6/food §7- §f восстановить голод\n§7• §6/heal §7- §f восстановить здоровье\n§7• §6/repair §7- §f починить предмет в руке\n§7• §fКоличество приватов §7- §63").send(player);
				}
				if (data == 2) {
					new SimpleForm("§fОписание §7- §l§dEnigma", "Префикс §7- (§dEnigma§7)\n§7• §6/food §7- §f восстановить голод\n§7• §6/heal §7- §f восстановить здоровье\n§7• §6/repair §7- §f починить предмет в руке\n§7• §6/day §7- §f сменить §9Ночь §fна §eДень\n§7• §6/night §7- §f сменить §eДень §fна §9Ночь\n§7• §fКоличество приватов §7- §63\n§7• §fКоличество точек дома §7- §61").send(player);
				}
				if (data == 3) {
					new SimpleForm("\u00a7f\u041e\u043f\u0438\u0441\u0430\u043d\u0438\u0435 \u00a77- \u00a7l\u00a7cHydra", "\u041f\u0440\u0435\u0444\u0438\u043a\u0441 \u00a77- (\u00a7cHydra\u00a77)\n \u00a77\u2022 \u00a76/food \u00a77- \u00a7f \u0432\u043e\u0441\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u0442\u044c \u0433\u043e\u043b\u043e\u0434\n \u00a77\u2022 \u00a76/heal \u00a77- \u00a7f \u0432\u043e\u0441\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u0442\u044c \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435\n \u00a77\u2022 \u00a76/repair \u00a77- \u00a7f \u043f\u043e\u0447\u0438\u043d\u0438\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442 \u0432 \u0440\u0443\u043a\u0435\n \u00a77\u2022 \u00a76/day \u00a77- \u00a7f \u0441\u043c\u0435\u043d\u0438\u0442\u044c \u00a79\u041d\u043e\u0447\u044c \u00a7f\u043d\u0430 \u00a7e\u0414\u0435\u043d\u044c\n \u00a77\u2022 \u00a76/night \u00a77- \u00a7f \u0441\u043c\u0435\u043d\u0438\u0442\u044c \u00a7e\u0414\u0435\u043d\u044c \u00a7f\u043d\u0430 \u00a79\u041d\u043e\u0447\u044c\n \u00a77\u2022 \u00a76/near \u00a77- \u00a7f \u0443\u0437\u043d\u0430\u0442\u044c \u043a\u0442\u043e \u0440\u044f\u0434\u043e\u043c \u0441 \u0442\u043e\u0431\u043e\u0439 \u0432 \u0440\u0430\u0434\u0438\u0443\u0441\u0435 \u00a7e70 \u00a7f\u0431\u043b\u043e\u043a\u043e\u0432\n \u00a77\u2022 \u00a7f\u041a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u043f\u0440\u0438\u0432\u0430\u0442\u043e\u0432 \u00a77- \u00a764\n \u00a77\u2022 \u00a7f\u041a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u0442\u043e\u0447\u0435\u043a \u0434\u043e\u043c\u0430 \u00a77- \u00a761").send(player);
				}
			});
		}
		if (strings.length == 1 && strings[0].equals("test")) {
			Player player = (Player)commandSender;
			File dataFile = new File(AnarchyMain.datapath + "/DonateItems/" + player.getName().toLowerCase() + ".yml");
			if (!dataFile.exists()) {
				player.sendMessage("§l§c| §r§fСписок активных покупок §6пуст§7!");
				return false;
			}
			DonateChest donateChest = new DonateChest("Купленные Предметы", dataFile);
			Config config = new Config(dataFile, Config.YAML);
			for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
				ArrayList<Object> itemData = (ArrayList<Object>)entry.getValue();
				Item item = Item.get(Integer.parseInt((String)itemData.get(1).toString()), Integer.parseInt((String)itemData.get(2).toString()), Integer.parseInt((String)itemData.get(3).toString()));
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.putString("DATE", (String)itemData.get(0));
				item.setNamedTag(compoundTag);
				if (donateChest.canAddItem(item)) {
					donateChest.addItem(item.setLore("§r§fДата покупки §7- §6" + itemData.get(0)));
				}
			}
			FakeChestsAPI.openInventory(player, donateChest);
		} else if (strings.length >= 2 && !(commandSender instanceof Player)) {
			String[] split = strings[0].split(":");
			String playerName = StringUtils.implode(strings, 1).toLowerCase();
			commandSender.sendMessage("§l§e| §r§fИгрок §e" + playerName + " §fполучил §6" + split[0] + "§f:§6" + split[1] + " §7(§fx§6" + split[2] + "§7)");
			Config config = new Config(AnarchyMain.datapath + "/DonateItems/" + playerName + ".yml", Config.YAML);
			LinkedHashMap objectMap = (LinkedHashMap)config.getAll();
			objectMap.put(UUID.randomUUID().toString(), new Object[]{(String)getDate().toString(), Integer.parseInt((String)split[0]), Integer.parseInt((String)split[1]), Integer.parseInt((String)split[2])});
			config.setAll(objectMap);
			config.save();
		}
		return false;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction && ((SlotChangeAction)action).getInventory() instanceof DonateChest) {
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