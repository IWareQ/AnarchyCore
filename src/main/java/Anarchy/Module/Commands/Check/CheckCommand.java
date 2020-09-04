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
		super("check", "§l§fПросмотреть Информацию о Игроке");
		setPermission("Command.Check");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.Check")) {
			return false;
		}
		if (args.length != 1) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6check §7(§3игрок§7)");
			return true;
		}
		Player target = Server.getInstance().getPlayerExact(args[0]);
		String playerName = target.getName();
		PlayerSession playerSession = PlayerSessionManager.getPlayerSession(target);
		String device = String.valueOf(target.getLoginChainData().getDeviceOS()).replace("0", "Неизвестно").replace("1", "Android").replace("2", "iOS").replace("3", "MacOS").replace("4", "FireOS").replace("5", "GearVR").replace("6", "HoloLens").replace("10", "PS 4").replace("7", "Win 10").replace("8", "Win").replace("9", "Dedicated").replace("11", "Switch");
		new SimpleForm("Профиль Игрока", "§7• §fИмя Пользователя §7- §3" + playerName + "\n\n §7- §fОбщее Время на Сервере §7- §3" + new DecimalFormat("#.#").format((float)(playerSession.getInteger("Gametime") + playerSession.getSessionTime()) / 3600).replace("§7,", "§7.") + " §fч§7.\n §7- §fСостояние Счета §7- §6" + String.format("%.1f", playerSession.getDouble("Money")) + " \n §7- §fУ§7/§fС§7: §3" + String.format("%.1f", getKDR(target)) + " §7(§fУ: §3" + getKills(target) + "§7, §fС: §3" + getDeaths(target) + "§7)\n §7- §fГруппа §7- " + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")) + "\n\n §fУстройство §7- §f" + device + " §7(§a1§7.§a16§7.§a20§7)").addButton("§l§fИнвентарь").addButton("§l§fЭндер Сундук").addButton("§l§fОчистить Инвентарь").send(player, (targetPlayer,form,data)->{
			if (data == -1) return;
			if (data == 0) {
				InventoryChest inventoryChest = new InventoryChest("§3" + playerName + " §7- §fИнвентарь");
				inventoryChest.setContents(target.getInventory().getContents());
				FakeChestsAPI.openInventory(player, inventoryChest);
				return;
			}
			if (data == 1) {
				EnderChest enderChest = new EnderChest("§3" + playerName + " §7- §fЭндер Сундук");
				enderChest.setContents(target.getEnderChestInventory().getContents());
				FakeChestsAPI.openInventory(player, enderChest);
				return;
			}
			if (data == 2) {
				target.getInventory().clearAll();
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