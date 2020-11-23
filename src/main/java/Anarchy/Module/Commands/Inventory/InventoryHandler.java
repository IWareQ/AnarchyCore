package Anarchy.Module.Commands.Inventory;

import Anarchy.Module.Commands.Inventory.Utils.DoubleChest;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Utils.StringUtils;
import FakeInventoryAPI.FakeInventoryAPI;
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
import cn.nukkit.level.Sound;

public class InventoryHandler extends Command implements Listener {

	public InventoryHandler() {
		super("inv", "§r§fПросмотр инвентаря");
		this.setPermission("Command.Inventory");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Inventory")) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6• §r§fИспользование §7- /§6inv §7(§3игрок§7)");
				return true;
			}
			String nickname = StringUtils.implode(args, 0);
			checkInventory(nickname, player);
		}
		return false;
	}

	public static void checkInventory(String checked, Player player) {
		Player target = Server.getInstance().getPlayer(checked);
		SimpleForm simpleForm = new SimpleForm("§l§6" + checked + " §7› §fВыберите Инвентарь");
		simpleForm.setContent("§l§6• §r§fЗдоровье§7: §6" + String.format("%.0f", target.getHealth()) + "§7/§6" + target.getMaxHealth() + "\n§l§6• §r§fУровень§7: §6" + target.getExperienceLevel() + " §fур§7.\n§l§6• §r§fБаланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(target.getName())) + "");
		simpleForm.addButton("§r§fИнвентарь", ImageType.PATH, "textures/ui/inventory_icon");
		simpleForm.addButton("§r§fЭндер Сундук", ImageType.PATH, "textures/ui/icon_blackfriday");
		simpleForm.send(player, (targetPlayer, form, data)-> {
			if (data == -1) return;
			if (data == 0) {
				DoubleChest doubleChest = new DoubleChest("§r§6" + target.getName() + " §7- §fИнвентарь");
				doubleChest.setContents(target.getInventory().getContents());
				FakeInventoryAPI.openInventory(player, doubleChest);
			}
			if (data == 1) {
				DoubleChest doubleChest = new DoubleChest("§r§6" + target.getName() + " §7- §fЭндер Сундук");
				doubleChest.setContents(target.getEnderChestInventory().getContents());
				FakeInventoryAPI.openInventory(player, doubleChest);
			}
		});
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction)action;
				if (slotChange.getInventory() instanceof DoubleChest) {
					Player player = event.getTransaction().getSource();
					player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
					event.setCancelled(true);
				}
			}
		}
	}
}