package Anarchy.Module.Commands.Check;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Manager.FakeChests.Inventory.DefaultChest;
import Anarchy.Module.Economy.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import ru.nukkitx.FormAPI;
import ru.nukkitx.forms.elements.SimpleForm;

public class CheckCommand extends Command {

	public CheckCommand() {
		super("check", "Проверка игрока");
		setPermission("Command.Check");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {
			new CommandParameter("player", CommandParamType.TARGET, false)
		});
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Check")) {
			return false;
		}

		if (strings.length != 1) {
			commandSender.sendMessage("§l§e| §fИспользование §7- §e/check<игрок>");
			return true;
		}

		Player infoPlayer = Server.getInstance().getPlayer(strings[0]);
		if (infoPlayer == null) {
			commandSender.sendMessage("§fИгрок §a" + strings[0] + " §7- §cОффлайн");
			return true;
		}

		String ip = infoPlayer.getAddress();
		String playerName = infoPlayer.getName();
		String uuid = infoPlayer.getUniqueId().toString();
		Player player = (Player) commandSender;
		String device = String.valueOf(infoPlayer.getLoginChainData().getDeviceOS()).replace("0", "Неизвестно").replace("1", "Android").replace("2", "iOS").replace("3", "MacOS").replace("4", "FireOS").replace("5", "GearVR").replace("6", "HoloLens").replace("10", "PS 4").replace("7", "Win 10").replace("8", "Win").replace("9", "Dedicated").replace("11", "Switch");
		new SimpleForm(playerName + " > Выберите Категорию", "§fБаланс §7- §e" + EconomyAPI.myMoney(infoPlayer) + "\n§fУстройство §7- §3" + device + "\n§fIP адрес §7- §c" + ip + "\n§fUUID §7- §f" + uuid)
			.addButton("Инвентарь")
			.addButton("Эндер Сундук")
			.send(player, (target, form, data) -> {
				if (data == -1) return;
				if (data == 0) {
					InventoryChest inventoryChest = new InventoryChest(playerName + " §7- §fИнвентарь");
					inventoryChest.setContents(infoPlayer.getInventory().getContents());
					FakeChestsAPI.openInventory(player, inventoryChest);
				}
				if (data == 1) {
					EnderChest enderChest = new EnderChest(playerName + " §7- §fЭндер Сундук");
					enderChest.setContents(infoPlayer.getEnderChestInventory().getContents());
					FakeChestsAPI.openInventory(player, enderChest);
				}
			});
		return false;
	}
}