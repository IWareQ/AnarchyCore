package ru.jl1mbo.AnarchyCore.Modules.BlockProtection.Commands;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Location;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.ImageType;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Modules.Auth.AuthAPI;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.BlockProtectionAPI;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.Blocks.DefaultBlockProtection;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.Utils.SQLiteUtils;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class RegionCommand extends Command {

	public RegionCommand() {
		super("region", "§rСистема регионов", "", new String[] {"rg"});
		this.commandParameters.clear();
		this.commandParameters.put("region", new CommandParameter[] {CommandParameter.newEnum("action", new CommandEnum("RegionAction", "help", "add", "del")), CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	private void openRegionEditForm(Player player) {
		int regionId = BlockProtectionAPI.getRegionIDByPosition(player);
		SimpleForm simpleForm = new SimpleForm("Мои регионы");
		if (!BlockProtectionAPI.isRegionOwner(player.getName(), regionId)) {
			simpleForm.addContent("Вы должны находиться внутри Вашего региона§7!");
			simpleForm.send(player);
			return;
		}
		simpleForm.addContent("§l§6• §rID региона§7: §6" + regionId);
		simpleForm.addContent("\n§l§6• §rКол§7-§fво участников§7: §6" + BlockProtectionAPI.getRegionMembers(regionId).size());
		simpleForm.addContent("\n\n§6Выберите §6нужное §fВам действие§7, §fкоторое хотите применить к данному §6Региону§7:");
		simpleForm.addButton("Удалить участника");
		simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
		simpleForm.send(player, (targetPlayer, targetForm, data) -> {
			switch (data) {
			case 0: {
				this.openRemovePlayerForm(player, regionId);
				break;
			}

			case 1: {
				this.openMyRegionsForm(player);
				break;
			}

			}
		});
	}

	private void openRemovePlayerForm(Player player, Integer regionID) {
		SimpleForm simpleForm = new SimpleForm("Удалить Участника");
		simpleForm.addContent("Выберите §6участника§7, §fкоторого хотите §6удалить §fиз региона§7:");
		for (String playerName : BlockProtectionAPI.getRegionMembers(regionID)) {
			simpleForm.addButton("§6" + playerName + "\n§fНажмите§7, §fчтобы удалить§7!");
		}
		simpleForm.send(player, (targetPlayer, form, data) -> {
			if (data == -1) return;
			String targetName = form.getResponse().getClickedButton().getText().replace("§6", "").split("\n")[0];
			Player target = Server.getInstance().getPlayerExact(targetName);
			if (target != null) {
				target.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + player.getName() + " §fудалил Вас из своего региона§7!");
			}
			player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + targetName + " §fудален из региона§7!");
			SQLiteUtils.query("BlockProtection.db", "DELETE FROM `Members` WHERE UPPER (`Name`) = '" + targetName.toUpperCase() + "' AND (`RegionID`) = '" + regionID + "'");
		});
	}

	private void openMyRegionsForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Мои регионы");
		simpleForm.addContent("Выберите один из регионов§7, §fс которым хотите §6взаимодействовать§7.");
		simpleForm.addContent("\n\n§fСписок §6Ваших §fрегионов§7:");
		List<Integer> regionsData = SQLiteUtils.getIntegerList("BlockProtection.db", "SELECT `ID` FROM `Areas` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'");
		if (regionsData == null || regionsData.isEmpty()) {
			simpleForm.addContent("\n\n§fВы не имеете регионов§7!");
		}
		for (int region_id : regionsData) {
			Location location = BlockProtectionAPI.getRegionBlockLocation(region_id);
			DefaultBlockProtection defaultBlock = BlockProtectionAPI.getAllBlocks().get(WorldSystemAPI.getMap().getBlock(location).getId());
			simpleForm.addButton(defaultBlock.getBlockName() + "\n§f" + location.getFloorX() + "§7, §f" + location.getFloorY() + "§7, §f" + location.getFloorZ(), ImageType.PATH, defaultBlock.getBlockImage());
		}
		simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
		simpleForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) return;
			if (regionsData.size() == data) {
				this.openMenuForm(player);
				return;
			}
			this.openRegionEditForm(player);
		});
	}

	private void openGuidForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Гайд по регионам");
		simpleForm.addContent("Хочешь создать свой регион§7? §fНе проблема§7! §fМожешь следовать пунктам§7:");
		simpleForm.addContent("\n\n§6• §fДобудь блок привата");
		simpleForm.addContent("\n§6• §fПроверь§7, §fнет ли вблизи другого региона");
		simpleForm.addContent("\n§6• §fПоставь блок для привата и будь уверен §7- §fтвою постройку не тронут§7!");
		simpleForm.addContent("\n\n§fБлоки§7, §fкоторыми можно приватить§7, §fпоказаны на спавне§7!");
		simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
		simpleForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) return;
			this.openMenuForm(player);
		});
	}

	private void openMenuForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Регионы");
		simpleForm.addContent("Вы находитесь в главном меню взаимодействия с регионами на §6Сервере§7.");
		simpleForm.addContent("\n\n§fМаксимальное кол§7-§fво §6Регионов §fдля Вас§7: §6" + PermissionAPI.getPlayerGroup(player.getName()).getMaxRegions());
		simpleForm.addContent("\n\n§fВыберите §6нужный §fВам пункт§7:");
		simpleForm.addButton("Мои регионы", ImageType.PATH, "textures/ui/absorption_effect");
		simpleForm.addButton("Членство в регионах", ImageType.PATH, "textures/ui/dressing_room_skins");
		simpleForm.addButton("Гайд по регионам", ImageType.PATH, "textures/ui/how_to_play_button_default_light");
		simpleForm.send(player, (targetPlayer, targetForm, data) -> {
			switch (data) {
			case 0: {
				this.openMyRegionsForm(player);
				break;
			}

			case 1: {
				this.openMembershipInRegionsForm(player);
				break;
			}

			case 2: {
				this.openGuidForm(player);
				break;
			}

			}
		});
	}

	private void openMembershipInRegionsForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Членство в Регионах");
		simpleForm.addContent("Здесь Вы можете увидеть регионы§7, §fв которых Вы §6состоите§7.");
		simpleForm.addContent("\n\n§fСписок регионов§7:");
		List<Integer> regionsData = SQLiteUtils.getIntegerList("BlockProtection.db", "SELECT `RegionID` FROM `Members` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'");
		if (regionsData == null || regionsData.isEmpty()) {
			simpleForm.addContent("\n\n§fВас §6не добавили §fни в §61 §fиз регионов§7!");
		}
		for (int region_id : regionsData) {
			String rgOwner = BlockProtectionAPI.getRegionOwner(region_id);
		Location rgLocation = BlockProtectionAPI.getRegionBlockLocation(region_id);
			simpleForm.addButton("§6" + rgOwner + "\n§f" + rgLocation.getFloorX() + "§7, §f" + rgLocation.getFloorY() + "§7, §f" + rgLocation.getFloorZ());
		}
		simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
		simpleForm.send(player, (targetPlayer, form, data) -> {
			if (data == -1) return;
			if (regionsData.size() == data) {
				openMenuForm(player);
				return;
			}
			this.openMembershipInRegionsForm(player);
		});
	}

	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage("§l§6• §rИспользование §7- /§6rg help");
				return true;
			}
			switch (args[0]) {
			case "add": {
				if (args.length != 2) {
					player.sendMessage("§l§6• §rИспользование §7- /§6rg add §7(§6игрок§7)");
					return true;
				}
				int regionID = BlockProtectionAPI.getRegionIDByPosition(player.getPosition());
				if ((regionID == -1) || !(BlockProtectionAPI.isRegionOwner(player.getName(), regionID))) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Вы должны находиться §6внутри своего §fрегиона§7!");
					return true;
				}
				String targetName = Utils.implode(args, 1);
				if (!AuthAPI.isRegister(targetName)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + targetName + " §fне зарегистрирован§7!");
					return true;
				}
				if (player.getName().equalsIgnoreCase(targetName)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Нельзя §6добавить себя §fв свой регион§7!");
					return true;
				}
				if (BlockProtectionAPI.isRegionMember(targetName, regionID)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + targetName + " §fуже состоит в Вашем регионе§7!");
					return true;
				}
				player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + targetName + " §fбыл успешно добавлен в Ваш регион§7!");
				Player target = Server.getInstance().getPlayerExact(targetName);
				if (target != null) {
					target.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + player.getName() + " §fдобавил Вас в свой регион§7!");
				}
				SQLiteUtils.query("BlockProtection.db", "INSERT INTO `Members` (`Name`, `RegionID`) VALUES ('" + targetName + "', '" + regionID + "')");
			}
			break;

			case "del": {
				if (args.length != 2) {
					player.sendMessage("§l§6• §r§fИспользование §7- /§6rg del §7(§6игрок§7)");
					return true;
				}
				int regionID = BlockProtectionAPI.getRegionIDByPosition(player);
				if (regionID == -1 || !BlockProtectionAPI.isRegionOwner(player.getName(), regionID)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Вы должны находиться §6внутри своего §fрегиона§7!");
					return true;
				}
				String targetName = Utils.implode(args, 1);
				if (player.getName().equals(targetName)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Нельзя §6удалить себя §fс своего региона§7!");
					return true;
				}
				if (!BlockProtectionAPI.isRegionMember(targetName, regionID)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + targetName + " §fне состоит в Вашем регионе§7!");
					return true;
				}
				player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + targetName + " §fудален из региона§7!");
				Player target = Server.getInstance().getPlayerExact(targetName);
				if (target != null) {
					target.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + player.getName() + " §fудалил Вас из своего региона§7!");
				}
				SQLiteUtils.query("BlockProtection.db", "DELETE FROM `Members` WHERE UPPER (`Name`) = '" + targetName.toUpperCase() + "' AND (`RegionID`) = '" + regionID + "'");
			}
			break;

			default: {
				this.openMenuForm(player);
			}
			break;

			}
		}
		return false;
	}
}