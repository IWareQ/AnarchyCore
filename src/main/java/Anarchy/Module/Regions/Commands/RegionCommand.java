package Anarchy.Module.Regions.Commands;

import java.util.ArrayList;
import java.util.Map;

import Anarchy.Module.Regions.RegionsAPI;
import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import FormAPI.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Sound;

public class RegionCommand extends Command {

	public RegionCommand() {
		super("rg", "Система регионов");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("string", new String[]{"add", "del", "my", "me", "list", "how", "help"}), new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		String playerName = player.getName();
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (args.length == 0) {
			player.sendMessage("§l§6| §r§fИспользование §7- §6/rg help");
			player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
			return true;
		}
		switch (args[0]) {
		case "add": {
			if (args.length != 2) {
				player.sendMessage("§l§6| §r§fИспользование §7- §6/rg add §7(§3игрок§7)");
				player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
				return true;
			}
			int regionID = RegionsAPI.getRegionIDByPosition(player);
			if (regionID == -1 || !RegionsAPI.isRegionOwner(playerName, regionID)) {
				player.sendMessage(RegionsAPI.PREFIX + "§fВы должны находиться внутри Вашего региона§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			Player addPlayer = Server.getInstance().getPlayer(args[1]);
			if (addPlayer == null) {
				player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + args[1] + " §fне в сети§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			if (player == addPlayer) {
				player.sendMessage(RegionsAPI.PREFIX + "§fНельзя добавить себя в свой регион§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			if (SQLiteUtils.selectString("Regions.db", "SELECT `Username` FROM `MEMBERS` WHERE UPPER(`Username`) = \'" + playerName.toUpperCase() + "\' AND `Region_ID` = \'" + regionID + "\';") != null) {
				player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + addPlayer.getName() + " §fуже состоит в Вашем регионе§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §3" + addPlayer.getName() + " §fдобавлен в Ваш регион§7!");
			addPlayer.sendMessage(RegionsAPI.PREFIX + "§fИгрок §3" + player.getName() + " §fдобавил Вас в свой регион§7!");
			SQLiteUtils.query("Regions.db", "INSERT INTO `MEMBERS` (`Region_ID`, `Username`) VALUES (\'" + regionID + "\', \'" + addPlayer.getName() + "\');");
		}
		break;

		case "del": {
			if (args.length != 2) {
				player.sendMessage("§l§6| §r§fИспользование §7- §6/rg del §7(§3игрок§7)");
				player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
				return true;
			}
			int regionID = RegionsAPI.getRegionIDByPosition(player);
			if (regionID == -1 || !RegionsAPI.isRegionOwner(playerName, regionID)) {
				player.sendMessage(RegionsAPI.PREFIX + "§fВы должны находиться внутри Вашего региона§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			String delName = StringUtils.implode(args, 1);
			if (!RegionsAPI.isRegionMember(delName, regionID)) {
				player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + delName + " §fне состоит в Вашем регионе§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			Player delPlayer = Server.getInstance().getPlayerExact(delName);
			if (delPlayer != null) {
				delPlayer.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fудалил Вас из своего региона§7!");
			}
			player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + delName + " §fудален из региона§7! (" + StringUtils.getOnlineString(delName) + "§7)");
			SQLiteUtils.query("Regions.db", "DELETE FROM `MEMBERS` WHERE UPPER(`Username`) = \'" + delName.toUpperCase() + "\' AND `Region_ID` = \'" + regionID + "\';");
		}
		break;

		case "my": {
			ArrayList<Integer> regionsData = SQLiteUtils.selectIntegerList("Regions.db", "SELECT `Region_ID` FROM `AREAS` WHERE `Username` = \'" + playerName + "\';");
			if (regionsData == null || regionsData.isEmpty()) {
				player.sendMessage(RegionsAPI.PREFIX + "§fВы не имеете регионов§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			StringBuilder stringBuilder = new StringBuilder();
			for (int region_id : regionsData) {
				Map<String, String> regionInfo = RegionsAPI.getRegionInfo(region_id);
				stringBuilder.append("\n §7→ §fРегион §7(§f").append(regionInfo.get("Main_X")).append("§7, §f").append(regionInfo.get("Main_Y")).append("§7, §f").append(regionInfo.get("Main_Z")).append("§7)");
			}
			player.sendMessage(RegionsAPI.PREFIX + "§fВаши регионы §7- " + stringBuilder.toString());
		}
		break;

		case "me": {
			ArrayList<Integer> regionsData = SQLiteUtils.selectIntegerList("Regions.db", "SELECT `Region_ID` FROM `MEMBERS` WHERE `Username` = \'" + playerName + "\';");
			if (regionsData == null || regionsData.isEmpty()) {
				player.sendMessage(RegionsAPI.PREFIX + "§fВас не добавили ни в §31 §fиз регионов§7!");
				return true;
			}
			StringBuilder stringBuilder = new StringBuilder();
			for (int region_id : regionsData) {
				Map<String, String> regionInfo = RegionsAPI.getRegionInfo(region_id);
				stringBuilder.append("\n §7→ §fРегион Игрока §3").append(regionInfo.get("Username")).append(" §7(§f").append(regionInfo.get("Main_X")).append("§7, §f").append(regionInfo.get("Main_Y")).append("§7, §f").append(regionInfo.get("Main_Z")).append("§7)");
			}
			player.sendMessage(RegionsAPI.PREFIX + "§fРегионы§7, §fв которых Вы состоите §7- " + stringBuilder.toString());
		}
		break;

		case "list": {
			int regionID = RegionsAPI.getRegionIDByPosition(player);
			if (regionID == -1 || !RegionsAPI.isRegionOwner(playerName, regionID)) {
				player.sendMessage(RegionsAPI.PREFIX + "§fВы должны находиться внутри Вашего региона§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			ArrayList<String> membersData = SQLiteUtils.selectList("Regions.db", "SELECT `Username` FROM `MEMBERS` WHERE `Region_ID` = \'" + regionID + "\';");
			if (membersData == null || membersData.isEmpty()) {
				player.sendMessage(RegionsAPI.PREFIX + "§fВ Вашем регионе нет участников§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			StringBuilder regionMembers = new StringBuilder();
			for (String memberName : membersData) {
				regionMembers.append("§7, §f").append(memberName);
			}
			player.sendMessage(RegionsAPI.PREFIX + "§fУчастники региона §3" + args[1] + " §7- " + regionMembers.toString().substring(6));
		}

		case "how": {
			new SimpleForm("Информация о Регионах",
						   "Хочешь создать свой регион? Не ��роблема! Можешь следовать пунктам:\n\n§l§6| §r§fДобудь блок привта\n§6§l| §r§fПроверь, нет ли вблизи другого региона\n§l§6| §r§fПоставь блок для привата и будь уверене §7- §fтвою постройку не тронут!\n\nБлоки, которыми можно приватить:\n\n§l§6| §r§fЖелезный блок §7(§fприватит 3 × 3§7)\n§l§6| §r§fАлмазный блок §7(§fприватит 6 × 6§7)\n§l§6| §r§fИзумрудный блок §7(§fприватит 10 × 10§7)").send(
							   player);
		}
		break;

		default:
			new SimpleForm("§3Регионы", "  ").addButton("§fИнформация о Регионах").addButton("§fМои Регионы").addButton("§fСписок регионов§7, §fгде Вас добавили").send(player, (target, form, data)-> {
				if (data == -1) return;
				if (data == 0) {
					new SimpleForm("Информация о Регионах", "Хочешь создать свой регион? Не проблема! Можешь следовать пунктам:\n\n§l§6| §r§fДобудь блок привата\n§6§l| §r§fПроверь, нет ли вблизи другого региона\n§l§6| §r§fПоставь блок для привата и будь уверене §7- §fтвою постройку не тронут!\n\nБлоки, которыми можно приватить:\n\n§l§6| §r§fЖелезный блок §7(§fприватит 3 × 3§7)\n§l§6| §r§fАлмазный блок §7(§fприватит 6 × 6§7)\n§l§6| §r§fИзумрудный блок §7(§fприватит 10 × 10§7)").send(player);
				}
				if (data == 1) {
					ArrayList<Integer> regionsData = SQLiteUtils.selectIntegerList("Regions.db", "SELECT `Region_ID` FROM `AREAS` WHERE `Username` = \'" + playerName + "\';");
					if (regionsData == null || regionsData.isEmpty()) {
						player.sendMessage(RegionsAPI.PREFIX + "§fВы не имеете регионов§7!");
						player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
						return;
					}
					StringBuilder stringBuilder = new StringBuilder();
					for (int region_id : regionsData) {
						Map<String, String> regionInfo = RegionsAPI.getRegionInfo(region_id);
						stringBuilder.append("\n §7→ §fРегион §7(§f").append(regionInfo.get("Main_X")).append("§7, §f").append(regionInfo.get("Main_Y")).append("§7, §f").append(regionInfo.get("Main_Z")).append("§7)");
					}
					new SimpleForm("§fРегионы§7, §fв которых Вы состоите", "§fВаши регионы" + stringBuilder.toString()).send(player);
				}
				if (data == 2) {
					ArrayList<Integer> regionsData = SQLiteUtils.selectIntegerList("Regions.db", "SELECT `Region_ID` FROM `MEMBERS` WHERE `Username` = \'" + playerName + "\';");
					if (regionsData == null || regionsData.isEmpty()) {
						player.sendMessage(RegionsAPI.PREFIX + "§fВас не добавили ни в §61 §fиз регионов§7!");
						return;
					}
					StringBuilder stringBuilder = new StringBuilder();
					for (int region_id : regionsData) {
						Map<String, String> regionInfo = RegionsAPI.getRegionInfo(region_id);
						stringBuilder.append("\n §7→ §fРегион Игрока §6").append(regionInfo.get("Username")).append(" §7(§f").append(regionInfo.get("Main_X")).append("§7, §f").append(regionInfo.get("Main_Y")).append("§7, §f").append(regionInfo.get("Main_Z")).append("§7)");
					}
					new SimpleForm("§fРегионы§7, §fв которых Вы состоите", "§fВы состоите в " + stringBuilder.toString()).send(player);
				}
			});

		}
		return false;
	}
}