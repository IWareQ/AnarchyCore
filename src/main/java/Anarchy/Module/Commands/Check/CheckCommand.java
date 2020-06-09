package Anarchy.Module.Commands.Check;

import java.text.DecimalFormat;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.Permissions.PermissionsAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
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
			commandSender.sendMessage("§l§e| §fИспользование §7- §e/check <игрок>");
			return true;
		}

		Player infoPlayer = Server.getInstance().getPlayer(strings[0]);
		if (infoPlayer == null) {
			commandSender.sendMessage("§fИгрок §a" + strings[0] + " §7- §fне в сети");
			return true;
		}

		int kills = kdr.Main.plugin.getKills(infoPlayer);
		int deaths = kdr.Main.plugin.getDeaths(infoPlayer);
		double kd = kdr.Main.plugin.getKDR(infoPlayer);
		// String ip = infoPlayer.getAddress();
		String playerName = infoPlayer.getName();
		PlayerSession playerSession = PlayerSessionManager.getPlayerSession(infoPlayer);
		// String uuid = infoPlayer.getUniqueId().toString();
		Player player = (Player) commandSender;
		String device = String.valueOf(infoPlayer.getLoginChainData().getDeviceOS()).replace("0", "Неизвестно").replace("1", "Android").replace("2", "iOS").replace("3", "MacOS").replace("4", "FireOS").replace("5", "GearVR").replace("6", "HoloLens").replace("10", "PS 4").replace("7", "Win 10").replace("8", "Win").replace("9", "Dedicated").replace("11", "Switch");
		new SimpleForm("Профиль Игрока", "§7• §fИмя Пользователя: §c" + playerName + "\n\n §7- §fОбщее Время на Сервере: §3" + new DecimalFormat("#.#").format((float)(playerSession.getInteger("Gametime") + playerSession.getSessionTime()) / 3600).replace("§f,", "§f.") + " §fч.\n §7- §fСостояние Счета: §e" + playerSession.getInteger("Money") + " \n §7- §fУ§7/§fС: §5" + kd + " §7(§fУ: §4" + kills + "§7, §fС: §4" + deaths + "§7)\n §7- §fГруппа: " + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")) + "\n\n §fУстройство: " + device + " §7(§a1.14.60§7)").addButton("Инвентарь").addButton("Эндер Сундук").send(player, (target, form, data) -> {
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