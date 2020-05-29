package Anarchy.Module.Regions.Commands;

import Anarchy.Module.Regions.RegionsAPI;
import Anarchy.Module.Auction.Utils.Form.SimpleTradeForm;
import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.form.element.ElementButton;
import ru.nukkitx.FormAPI;
import ru.nukkitx.forms.elements.SimpleForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class RegionCommand extends Command {
	public RegionCommand() {
		super("rg", "Система регионов");
	}

	public boolean execute(CommandSender commandSender, String s, String[] args) {
		if (args.length == 0) {
			commandSender.sendMessage("§l§e| §fИспользование §7- §e/rg help");
			return true;
		}

		Player player = (Player) commandSender;
		String playerName = player.getName();

		switch (args[0]) {
			case "add":
				{
					if (args.length != 2) {
						player.sendMessage("§l§e| §fИспользование §7- §e/rg add<игрок>");
						return true;
					}

					int regionID = RegionsAPI.getRegionIDByPosition(player);
					if (regionID == -1 || !RegionsAPI.isRegionOwner(playerName, regionID)) {
						player.sendMessage(RegionsAPI.PREFIX + "§fВы должны находиться внутри Вашего региона!");
						return true;
					}

					Player addPlayer = Server.getInstance().getPlayer(args[1]);
					if (addPlayer == null) {
						player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §a" + args[1] + " §7- §cОффлайн");
						return true;
					}

					if (player == addPlayer) {
						player.sendMessage(RegionsAPI.PREFIX + "§fНельзя добавить себя в свой регион!");
						return true;
					}

					if (SQLiteUtils.selectString("Regions.db", "SELECT `Username` FROM `MEMBERS` WHERE UPPER(`Username`) = '" + playerName.toUpperCase() + "' AND `Region_ID` = '" + regionID + "';") != null) {
						player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §a" + addPlayer.getName() + " §fуже состоит в Вашем регионе!");
						return true;
					}

					player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §a" + addPlayer.getName() + " §fдобавлен в Ваш регион!");
					addPlayer.sendMessage(RegionsAPI.PREFIX + "§fИгрок §a" + player.getName() + " §fдобавил Вас в свой регион!");

					SQLiteUtils.query("Regions.db", "INSERT INTO `MEMBERS` (`Region_ID`, `Username`) VALUES ('" + regionID + "', '" + addPlayer.getName() + "');");
				}
				break;

			case "del":
				{
					if (args.length != 2) {
						player.sendMessage("§l§e| §fИспользование §7- §e/rg del<игрок>");
						return true;
					}

					int regionID = RegionsAPI.getRegionIDByPosition(player);
					if (regionID == -1 || !RegionsAPI.isRegionOwner(playerName, regionID)) {
						player.sendMessage(RegionsAPI.PREFIX + "§fВы должны находиться внутри Вашего региона!");
						return true;
					}

					String delName = StringUtils.implode(args, 1);
					if (!RegionsAPI.isRegionMember(delName, regionID)) {
						player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §a" + delName + " §fне состоит в Вашем регионе!");
						return true;
					}

					Player delPlayer = Server.getInstance().getPlayerExact(delName);
					if (delPlayer != null) {
						delPlayer.sendMessage(RegionsAPI.PREFIX + "§fИгрок §a" + player.getName() + " §fудалил Вас из своего региона!");
					}

					player.sendMessage(RegionsAPI.PREFIX + "§fИгрок §a" + delName + " §fудален из региона! §7(" + StringUtils.getOnlineString(delName) + "§7)");
					SQLiteUtils.query("Regions.db", "DELETE FROM `MEMBERS` WHERE UPPER(`Username`) = '" + delName.toUpperCase() + "' AND `Region_ID` = '" + regionID + "';");
				}
				break;

			case "my":
				{
					ArrayList<Integer> regionsData = SQLiteUtils.selectIntegerList("Regions.db", "SELECT `Region_ID` FROM `AREAS` WHERE `Username` = '" + playerName + "';");
					if (regionsData == null || regionsData.isEmpty()) {
						player.sendMessage(RegionsAPI.PREFIX + "§fВы не имеете регионов!");
						return true;
					}

					StringBuilder stringBuilder = new StringBuilder();
					for (int region_id: regionsData) {
						Map<String, String> regionInfo = RegionsAPI.getRegionInfo(region_id);
						stringBuilder.append("\n §7→ §fРегион §7(§e").append(regionInfo.get("Main_X")).append(", ").append(regionInfo.get("Main_Y")).append(", ").append(regionInfo.get("Main_Z")).append("§7)");
					}

					player.sendMessage(RegionsAPI.PREFIX + "§fВаши регионы:" + stringBuilder.toString());
				}
				break;

			case "me":
				{
					ArrayList<Integer> regionsData = SQLiteUtils.selectIntegerList("Regions.db", "SELECT `Region_ID` FROM `MEMBERS` WHERE `Username` = '" + playerName + "';");
					if (regionsData == null || regionsData.isEmpty()) {
						player.sendMessage(RegionsAPI.PREFIX + "§fВас не добавили ни в §c1 §fиз регионов!");
						return true;
					}

					StringBuilder stringBuilder = new StringBuilder();
					for (int region_id: regionsData) {
						Map<String, String> regionInfo = RegionsAPI.getRegionInfo(region_id);
						stringBuilder.append("\n §7→ §fРегион Игрока §a").append(regionInfo.get("Username")).append(" §7(§e").append(regionInfo.get("Main_X")).append(", ").append(regionInfo.get("Main_Y")).append(", ").append(regionInfo.get("Main_Z")).append("§7)");
					}

					player.sendMessage(RegionsAPI.PREFIX + "§fРегионы, в которых Вы состоите:" + stringBuilder.toString());
				}
				break;

			case "list":
				{
					int regionID = RegionsAPI.getRegionIDByPosition(player);
					if (regionID == -1 || !RegionsAPI.isRegionOwner(playerName, regionID)) {
						player.sendMessage(RegionsAPI.PREFIX + "§fВы должны находиться внутри Вашего региона!");
						return true;
					}

					ArrayList<String> membersData = SQLiteUtils.selectList("Regions.db", "SELECT `Username` FROM `MEMBERS` WHERE `Region_ID` = '" + regionID + "';");
					if (membersData == null || membersData.isEmpty()) {
						player.sendMessage(RegionsAPI.PREFIX + "§fВ Вашем регионе нет участников!");
						return true;
					}

					StringBuilder regionMembers = new StringBuilder();
					for (String memberName: membersData) {
						regionMembers.append("§7, §f").append(memberName);
					}

					player.sendMessage(RegionsAPI.PREFIX + "§fУчастники региона §c" + args[1] + "§f:" + regionMembers.toString().substring(6));
				}

			case "how":
				{
					String border = StringUtils.getFormBorder();
					player.showFormWindow(new FormWindowSimple("Регион > Туториал", "Хочешь создать свой регион? Не проблема! Можешь следовать пунктам:\n\n§l§e| §r§fДобудь блок привта\n§e§l| §r§fПроверь, нет ли вблизи другого региона\n§l§e| §r§fПоставь блок для привата и будь уверене §7- §fтвою постройку не тронут!\n\nБлоки, которыми можно приватить:\n\n§l§e| §r§fЖелезный блок §7(§fприватит 3 × 3§7)\n§l§e| §r§fАлмазный блок §7(§fприватит 6 × 6§7)\n§l§e| §r§fИзумрудный блок §7(§fприватит 10 × 10§7)"));
				}
				break;

			case "help":
				new SimpleForm("Регионы > выберите категорию", "Текст")
					.addButton("Информация о Регионах")
					.addButton("Ваши Регионы")
					.addButton("Список регионов, где Вас добавили")
					.send(player, (target, form, data) -> {
						if (data == -1) return;
						if (data == 0) {
							new SimpleForm("Информация о Регионах", "Хочешь создать свой регион? Не проблема! Можешь следовать пунктам:\n\n§l§e| §r§fДобудь блок привта\n§e§l| §r§fПроверь, нет ли вблизи другого региона\n§l§e| §r§fПоставь блок для привата и будь уверене §7- §fтвою постройку не тронут!\n\nБлоки, которыми можно приватить:\n\n§l§e| §r§fЖелезный блок §7(§fприватит 3 × 3§7)\n§l§e| §r§fАлмазный блок §7(§fприватит 6 × 6§7)\n§l§e| §r§fИзумрудный блок §7(§fприватит 10 × 10§7)")
								.send(player);
						}
						if (data == 1) {
							/* ArrayList<Integer> regionsData = SQLiteUtils.selectIntegerList("Regions.db", "SELECT `Region_ID` FROM `AREAS` WHERE `Username` = '" + playerName + "';");
							if (regionsData == null || regionsData.isEmpty()) {
								player.showFormWindow(new FormWindowSimple("Регион > Мои регионы", "Вы не имеете регионов!"));
								return;
							}

							StringBuilder stringBuilder = new StringBuilder();
							for (int region_id: regionsData) {
								Map<String, String> regionInfo = RegionsAPI.getRegionInfo(region_id);
								stringBuilder.append("\n §7→ §fРегион §7(§e").append(regionInfo.get("Main_X")).append(", ").append(regionInfo.get("Main_Y")).append(", ").append(regionInfo.get("Main_Z")).append("§7)");
							}

							new SimpleForm("Мои Регионы", "Ваши регионы: " + stringBuilder.toString())
								.send(player, (target, form, data) -> {
									if (data == -1) return;
								}); */
						}
						if (data == 2) {
							// TODO
						}
					});
		}
		return false;
	}
}