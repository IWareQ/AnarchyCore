package Anarchy.Module.Commands.Check;

import java.io.File;
import java.text.DecimalFormat;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.Permissions.PermissionsAPI;
import FormAPI.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.Listener;
import cn.nukkit.utils.Config;

public class CheckCommand extends Command implements Listener {
	File dataFile = new File(AnarchyMain.datapath + "/KDR.yml");
	Config config = new Config(dataFile, Config.YAML);
	
	public CheckCommand() {
		super("check", "Просмотреть Информацию о Игроке");
		setPermission("Command.Check");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.Check")) {
			return false;
		}
		if (args.length != 1) {
			player.sendMessage("§l§e| §r§fИспользование §7- §e/check §7(§3игрок§7)");
			return true;
		}
		Player infoPlayer = Server.getInstance().getPlayer(args[0]);
		if (infoPlayer == null) {
			player.sendMessage("§fИгрок §e" + args[0] + " §7- §3Оффлайн");
			return true;
		}
		String playerName = infoPlayer.getName();
		PlayerSession playerSession = PlayerSessionManager.getPlayerSession(infoPlayer);
		String device = String.valueOf(infoPlayer.getLoginChainData().getDeviceOS()).replace("0", "Неизвестно").replace("1", "Android").replace("2", "iOS").replace("3", "MacOS").replace("4", "FireOS").replace("5", "GearVR").replace("6", "HoloLens").replace("10", "PS 4").replace("7", "Win 10").replace("8", "Win").replace("9", "Dedicated").replace("11", "Switch");
		new SimpleForm("Профиль Игрока", "§7• §fИмя Пользователя §7- §e" + playerName + "\n\n §7- §fОбщее Время на Сервере §7- §3" + new DecimalFormat("#.#").format((float)(playerSession.getInteger("Gametime") + playerSession.getSessionTime()) / 3600).replace("§7,", "§7.") + " §fч§7.\n §7- §fСостояние Счета §7- §e" + playerSession.getInteger("Money") + " \n §7- §fУ§7/§fС: §5" + getKDR(infoPlayer)+ " §7(§fУ: §4" + getKills(infoPlayer) + "§7, §fС: §4" + getDeaths(infoPlayer) + "§7)\n §7- §fГруппа §7- " + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")) + "\n\n §fУстройство §7- §f" + device + " §7(§a1§7.§a14§7.§a60§7)\n §fКлиент ID §7- §f").addButton("Инвентарь").addButton("Эндер Сундук").addButton("Очистить Инвентарь").send(player, (target,form,data)->{
			if (data == -1) return;
			if (data == 0) {
				InventoryChest inventoryChest = new InventoryChest("§3" + playerName + " §7- §fИнвентарь");
				inventoryChest.setContents(infoPlayer.getInventory().getContents());
				FakeChestsAPI.openInventory(player, inventoryChest);
				return;
			}
			if (data == 1) {
				EnderChest enderChest = new EnderChest("§3" + playerName + " §7- §fЭндер Сундук");
				enderChest.setContents(infoPlayer.getEnderChestInventory().getContents());
				FakeChestsAPI.openInventory(player, enderChest);
				return;
			}
			if (data == 2) {
				infoPlayer.getInventory().clearAll();
			}
		});
		return false;
	}
	
	public int getKills(Player player) {
		return config.getInt("Kills." + player.getName(), 0);
	}
	
	public int getDeaths(Player player) {
		return config.getInt("Deaths." + player.getName(), 0);
	}
	
	public double getKDR(Player player) {
		return (double)getKills(player) / getDeaths(player);
	}
}