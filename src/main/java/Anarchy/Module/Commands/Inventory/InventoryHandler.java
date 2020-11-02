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
		super("inventory", "\u00a7l\u00a7f\u041f\u043e\u0441\u043c\u043e\u0442\u0440\u0435\u0442\u044c \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c \u0418\u0433\u0440\u043e\u043a\u0430", "", new String[]{"inv"});
		this.setPermission("Command.Inventory");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Inventory")) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("\u00a7l\u00a76| \u00a7r\u00a7f\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u00a77- /\u00a76inv \u00a77(\u00a73\u0438\u0433\u0440\u043e\u043a\u00a77)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("\u00a7l\u00a76\u2022 \u00a7r\u00a7f\u0418\u0433\u0440\u043e\u043a \u00a76" + args[0] + " \u00a7f\u043d\u0435 \u0432 \u0441\u0435\u0442\u0438\u00a77!");
				return true;
			}
			checkInventory(target.getName(), player);
		}
		return false;
	}
	
	public static void checkInventory(String checked, Player player) {
		Player target = Server.getInstance().getPlayer(checked);
		new SimpleForm("\u00a7l\u00a76" + target.getName() + " \u00a77> \u00a7f\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0418\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c", "\u00a7l\u00a76\u2022 \u00a7r\u00a7f\u0417\u0434\u043e\u0440\u043e\u0432\u044c\u0435\u00a77: \u00a76" + String.format("%.0f", target.getHealth()) + "\u00a77/\u00a76" + target.getMaxHealth() + "\n\u00a7l\u00a76\u2022 \u00a7r\u00a7f\u0423\u0440\u043e\u0432\u0435\u043d\u044c\u00a77: \u00a76" + target.getExperienceLevel() + " \u00a7f\u0443\u0440\u00a77.\n\u00a7l\u00a76\u2022 \u00a7r\u00a7f\u0411\u0430\u043b\u0430\u043d\u0441\u00a77: \u00a76" + String.format("%.1f", EconomyAPI.myMoney(target)) + "\ue102").addButton("\u00a7l\u00a7f\u0418\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c", ImageType.PATH, "textures/ui/inventory_icon").addButton("\u00a7l\u00a7f\u042d\u043d\u0434\u0435\u0440 \u0421\u0443\u043d\u0434\u0443\u043a", ImageType.PATH, "textures/ui/icon_blackfriday").addButton("\u00a7l\u00a7f\u0425\u0440\u0430\u043d\u0438\u043b\u0438\u0449\u0435 \u041f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432", ImageType.PATH, "textures/ui/invite_hover").send(player, (targetPlayer,form,data)->{
			if (data == -1) return;
			if (data == 0) {
				InventoryChest InventoryChest = new InventoryChest("\u00a7l\u00a76" + target.getName() + " \u00a77- \u00a7f\u0418\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c");
				InventoryChest.setContents(target.getInventory().getContents());
				FakeChestsAPI.openInventory(player, InventoryChest);
			}
			if (data == 1) {
				InventoryEnderChest inventoryEnderChest = new InventoryEnderChest("\u00a7l\u00a76" + target.getName() + " \u00a77- \u00a7f\u042d\u043d\u0434\u0435\u0440 \u0421\u0443\u043d\u0434\u0443\u043a");
				inventoryEnderChest.setContents(target.getEnderChestInventory().getContents());
				FakeChestsAPI.openInventory(player, inventoryEnderChest);
			}
			if (data == 2) {
				player.sendMessage("\u0421\u043a\u043e\u0440\u043e");
			}
		});
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