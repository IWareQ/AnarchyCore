package Anarchy.Module.Commands.Inventory;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Economy.EconomyAPI;
import FormAPI.Forms.Elements.ImageType;
import FormAPI.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;

public class InventoryCommand extends Command implements Listener {

	public InventoryCommand() {
		super("inventory", "§l§fПосмотреть инвентарь Игрока", "", new String[] {"inv"});
		this.setPermission("Command.Inventory");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		Player player = (Player)sender;
		if (!player.hasPermission("Command.Inventory")) {
			return false;
		}
		if (args.length != 1) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6inv §7(§3игрок§7)");
			return true;
		}
		Player target = Server.getInstance().getPlayer(args[0]);
		new SimpleForm("§l§6" + target.getName() + " §7> §fВыберите Инвентарь", "§l§6• §r§fЗдоровье§7: §6" + String.format("%.0f", target.getHealth()) + "§7/§6" + target.getMaxHealth() + "\n§l§6• §r§fУровень§7: §6" + target.getExperienceLevel() + " §fур§7.\n§l§6• §r§fБаланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(target)) + "").addButton("§l§fИнвентарь", ImageType.PATH, "textures/ui/inventory_icon").addButton("§l§fЭндер Сундук", ImageType.PATH, "textures/ui/icon_blackfriday").addButton("§l§fХранилище Предметов", ImageType.PATH, "textures/ui/invite_hover").send(player, (targetPlayer, form, data)-> {
			if (data == -1) return;
			if (data == 0) {
				CheckInventoryChest checkInventoryChest = new CheckInventoryChest("§l§6" + target.getName() + " §7- §fИнвентарь");
				checkInventoryChest.setContents(target.getInventory().getContents());
				FakeChestsAPI.openInventory(player, checkInventoryChest);
				return;
			}
			if (data == 1) {
				CheckEnderChest checkEnderChest = new CheckEnderChest("§l§6" + target.getName() + " §7- §fЭндер Сундук");
				checkEnderChest.setContents(target.getEnderChestInventory().getContents());
				FakeChestsAPI.openInventory(player, checkEnderChest);
				return;
			}
			if (data == 2) {
				player.sendMessage("Скоро");
			}
		});
		return false;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction && ((SlotChangeAction)action).getInventory() instanceof CheckInventoryChest && ((SlotChangeAction)action).getInventory() instanceof CheckEnderChest) {
				Item sourceItem = action.getSourceItem();
				Player player = event.getTransaction().getSource();
				if (!sourceItem.hasCompoundTag()) {
					player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
					event.setCancelled(true);
				}
				return;
			}
		}
	}
}