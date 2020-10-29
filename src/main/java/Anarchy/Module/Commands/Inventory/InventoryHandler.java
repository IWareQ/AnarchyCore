package Anarchy.Module.Commands.Inventory;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Commands.Inventory.Utils.InventoryEnderChest;
import Anarchy.Module.Commands.Inventory.Utils.InventoryChest;
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
import cn.nukkit.level.Sound;

public class InventoryHandler extends Command implements Listener {

	public InventoryHandler() {
		super("inventory", "§l§fПосмотреть инвентарь Игрока", "", new String[] {"inv"});
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
				player.sendMessage("§l§6| §r§fИспользование §7- /§6inv §7(§3игрок§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6• §r§fИгрок §6" + args[0] + " §fне в сети§7!");
				return true;
			}
			new SimpleForm("§l§6" + target.getName() + " §7> §fВыберите Инвентарь", "§l§6• §r§fЗдоровье§7: §6" + String.format("%.0f", target.getHealth()) + "§7/§6" + target.getMaxHealth() + "\n§l§6• §r§fУровень§7: §6" + target.getExperienceLevel() + " §fур§7.\n§l§6• §r§fБаланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(target)) + "").addButton("§l§fИнвентарь", ImageType.PATH, "textures/ui/inventory_icon").addButton("§l§fЭндер Сундук", ImageType.PATH, "textures/ui/icon_blackfriday").addButton("§l§fХранилище Предметов", ImageType.PATH, "textures/ui/invite_hover").send(player, (targetPlayer, form, data)-> {
				if (data == -1) return;
				if (data == 0) {
					InventoryChest InventoryChest = new InventoryChest("§l§6" + target.getName() + " §7- §fИнвентарь");
					InventoryChest.setContents(target.getInventory().getContents());
					FakeChestsAPI.openInventory(player, InventoryChest);
				}
				if (data == 1) {
					InventoryEnderChest inventoryEnderChest = new InventoryEnderChest("§l§6" + target.getName() + " §7- §fЭндер Сундук");
					inventoryEnderChest.setContents(target.getEnderChestInventory().getContents());
					FakeChestsAPI.openInventory(player, inventoryEnderChest);
				}
				if (data == 2) {
					player.sendMessage("Скоро");
				}
			});
		}
		return false;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction)action;
				if (slotChange.getInventory() instanceof InventoryChest || slotChange.getInventory() instanceof InventoryEnderChest) {
					Player player = event.getTransaction().getSource();
					player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
					event.setCancelled(true);
				}
			}
		}
	}
}