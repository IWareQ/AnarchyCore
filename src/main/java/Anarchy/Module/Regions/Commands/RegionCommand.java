package Anarchy.Module.Regions.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Regions.RegionsAPI;
import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import FormAPI.Forms.Elements.CustomForm;
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
		super("region", "§r§fСистема регионов", "", new String[] {"rg"});
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("string", new String[]{"add", "del", "my", "me", "list", "how", "help"}), new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (args.length == 0) {
				player.sendMessage("§l§6• §r§fИспользование §7- §6/rg help");
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
				if (regionID == -1 || !RegionsAPI.isRegionOwner(player.getName(), regionID)) {
					player.sendMessage(RegionsAPI.PREFIX + "§fВы должны находиться внутри Вашего региона§7!");
					player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
					return true;
				}
				String target = StringUtils.implode(args, 1);
				if (!AuthAPI.isRegistered(target)) {
					player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + target + " §fни разу не заходил на сервер§7!");
					return true;
				}
				if (SQLiteUtils.selectString("SELECT Username FROM MEMBERS WHERE UPPER(Username) = \'" + player.getName().toUpperCase() + "\' AND Region_ID = \'" + regionID + "\';") != null) {
					player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + target + " §fуже состоит в Вашем регионе§7!");
					player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
					return true;
				}
				player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + target + " §fбыл успешно добавлен в Ваш регион§7!");
				SQLiteUtils.query("INSERT INTO MEMBERS (Region_ID, Username) VALUES (\'" + regionID + "\', \'" + target + "\');");
			}
			break;

			case "del": {
				if (args.length != 2) {
					player.sendMessage("§l§6| §r§fИспользование §7- §6/rg del §7(§3игрок§7)");
					player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
					return true;
				}
				int regionID = RegionsAPI.getRegionIDByPosition(player);
				if (regionID == -1 || !RegionsAPI.isRegionOwner(player.getName(), regionID)) {
					player.sendMessage(RegionsAPI.PREFIX + "§fВы должны находиться внутри Вашего региона§7!");
					player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
					return true;
				}
				String targetName = StringUtils.implode(args, 1);
				if (!RegionsAPI.isRegionMember(targetName, regionID)) {
					player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + targetName + " §fне состоит в Вашем регионе§7!");
					player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
					return true;
				}
				Player target = Server.getInstance().getPlayerExact(targetName);
				if (target != null) {
					target.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fудалил Вас из своего региона§7!");
				}
				player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + targetName + " §fудален из региона§7! (" + StringUtils.getOnlineString(targetName) + "§7)");
				SQLiteUtils.query("DELETE FROM MEMBERS WHERE UPPER(Username) = \'" + targetName.toUpperCase() + "\' AND Region_ID = \'" + regionID + "\';");
			}
			break;

			case "me": {
				List<Integer> regionsData = SQLiteUtils.selectIntegerList("SELECT Region_ID FROM MEMBERS WHERE Username = \'" + player.getName() + "\';");
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
				if (regionID == -1 || !RegionsAPI.isRegionOwner(player.getName(), regionID)) {
					player.sendMessage(RegionsAPI.PREFIX + "§fВы должны находиться внутри Вашего региона§7!");
					player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
					return true;
				}
				List<String> membersData = SQLiteUtils.selectStringList("SELECT Username FROM MEMBERS WHERE Region_ID = \'" + regionID + "\';");
				if (RegionsAPI.getRegionMembers(regionID) == null || RegionsAPI.getRegionMembers(regionID).isEmpty()) {
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
			break;

			default: {
				regionMenu(player);
			}
			break;
			}
		}
		return false;
	}

	private static void regionEdit(Player player) {
		SimpleForm simpleForm = new SimpleForm("§fУправление регионами");
		simpleForm.setContent("§fВыберите нужное Вам действие§7, §fкоторое хотите применить к данному §6Региону§7:");
		simpleForm.addButton("§fДобавить Игрока");
		simpleForm.addButton("§fУдалить Игрока");
		simpleForm.addButton("§fУдалить регион");
		simpleForm.addButton("§fНазад");
		simpleForm.send(player, (targetPlayer, targetForm, data)-> {
			if (data == -1) return;
			int regionID = RegionsAPI.getRegionIDByPosition(player);
			switch (data) {
			case 0: {
				addRegionPlayer(player, regionID);
				break;
			}

			case 1: {
				removeRegionPlayer(player, regionID);
				break;
			}

			case 2: {
				if (regionID != -1 && RegionsAPI.isRegionOwner(player.getName(), regionID)) {
					player.sendMessage(RegionsAPI.PREFIX + "§fРегион §7#§6" + regionID + " §fуспешно удален§7!");
					SQLiteUtils.query("DELETE FROM AREAS WHERE Region_ID = \'" + regionID + "\';");
					SQLiteUtils.query("DELETE FROM MEMBERS WHERE Region_ID = \'" + regionID + "\';");
				}
				break;
			}

			case 3: {
				regionMenu(player);
				break;
			}

			}
		});
	}

	private static void addRegionPlayer(Player player, Integer regionID) {
		CustomForm customForm = new CustomForm("§fУправление Участниками §7› §fДобавить Игрока");
		customForm.addLabel("§l§6Добавить Игрока\n\n§r§fВведите §6Nickname §fИгрока§7, §fкоторого хотите добавить в свой регион§7!");
		customForm.addInput("§fНапример§7: §6Steve");
		customForm.send(player, (targetPlayer, form, data)-> {
			if (data == null) return;
			String nickname = (String)data.get(1);
			if (!AuthAPI.isRegistered(nickname)) {
				player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + nickname + " §fни разу не заходил на сервер§7!");
				return;
			}
			Player target = Server.getInstance().getPlayerExact(nickname);
			if (player.equals(target)) {
				player.sendMessage(RegionsAPI.PREFIX + "§fНельзя добавить себя в свой регион§7!");
				return;
			}
			if (SQLiteUtils.selectString("SELECT Username FROM MEMBERS WHERE UPPER(Username) = \'" + player.getName().toUpperCase() + "\' AND Region_ID = \'" + regionID + "\';") != null) {
				player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + nickname + " §fуже состоит в Вашем регионе§7!");
				return;
			}
			player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + target.getName() + " §fбыл успешно добавлен в Ваш регион§7!");
			if (target != null) {
				target.sendMessage(RegionsAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fдобавил Вас в свой регион§7!");
			}
			SQLiteUtils.query("INSERT INTO MEMBERS (Region_ID, Username) VALUES (\'" + regionID + "\', \'" + target.getName() + "\');");
		});
	}

	private static void removeRegionPlayer(Player player, Integer regionID) {
		SimpleForm simpleForm = new SimpleForm("§fУдалить Участника");
		simpleForm.setContent("§fВыберите Участника региона§7, §fкоторого хотите удалить из региона§7:");
		for (String test : RegionsAPI.getRegionMembers(regionID)) {
			simpleForm.addButton(test);
		}
		simpleForm.send(player, (targetPlayer, form, data)-> {
			if (data == -1) return;
			player.sendMessage(form.getResponse().getClickedButton().getText());
		});
	}

	private static void regionControls(Player player) {
		SimpleForm simpleForm = new SimpleForm("§fУправление регионами");
		simpleForm.setContent("§fВыберите регион§7, §fкоторый хотите §6Отредактировать§7:");
		List<Integer> regionsData = SQLiteUtils.selectIntegerList("SELECT Region_ID FROM AREAS WHERE Username = \'" + player.getName() + "\';");
		if (regionsData == null || regionsData.isEmpty()) {
			simpleForm.addContent("\n\n§fВы не имеете регионов§7!");
		}
		for (int region_id : regionsData) {
			Map<String, String> regionInfo = RegionsAPI.getRegionInfo(region_id);
			simpleForm.addButton(regionInfo.get("Main_X") + "§7, §f" + regionInfo.get("Main_Y") + "§7, §f" + regionInfo.get("Main_Z"));
		}
		simpleForm.send(player, (targetPlayer, targetForm, data)-> {
			if (data == -1) return;
			regionEdit(player);
		});
	}

	private static void regionGuid(Player player) {
		SimpleForm simpleForm = new SimpleForm("§fГайд");
		simpleForm.setContent("§fХочешь создать свой регион§7? §fНе проблема§7! §fМожешь следовать пунктам§7:\n\n§6• §fДобудь блок привта\n§6• §fПроверь§7, §fнет ли вблизи другого региона\n§6• §fПоставь блок для привата и будь уверен §7- §fтвою постройку не тронут§7!\n\nБлоки§7, §fкоторыми можно приватить показаны на спавне§7!");
		simpleForm.addButton("§fНазад");
		simpleForm.send(player, (targetPlayer, targetForm, data)-> {
			if (data == -1) return;
			regionMenu(player);
		});
	}

	private static void regionMenu(Player player) {
		SimpleForm simpleForm = new SimpleForm("§fМеню Регионов");
		simpleForm.setContent("§fВы находитесь в главном меню взаимодействия с регионами на §6Сервере§7!\n\n§fВыберите нужный Вам пункт§7:");
		simpleForm.addButton("§fУправление регионами");
		simpleForm.addButton("§fЧленство в регионах");
		simpleForm.addButton("§fГайд");
		simpleForm.send(player, (targetPlayer, targetForm, data)-> {
			if (data == -1) return;
			switch (data) {
			case 0: {
				regionControls(player);
				break;
			}

			case 1: {
				//TODO
				break;
			}

			case 2: {
				regionGuid(player);
				break;
			}

			}
		});
	}
}