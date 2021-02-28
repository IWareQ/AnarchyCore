package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Commands;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.BlockProtectionAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks.DefaultBlockProtection;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Utils.SQLiteUtils;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.AuthorizationAPI;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.ImageType;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class RegionCommand extends Command {

	public RegionCommand() {
		super("region", "§rСистема регионов", "", new String[] {"rg"});
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("string", new String[]{"add", "del"}), new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	private void openRegionEditForm(Player player) {
		int regionId = BlockProtectionAPI.getRegionIDByPosition(player);
		SimpleForm simpleForm = new SimpleForm("Мои регионы");
		if (regionId == -1 || !BlockProtectionAPI.isRegionOwner(player.getName(), regionId)) {
			simpleForm.setContent("Вы должны находиться внутри Вашего региона§7!");
		}
		simpleForm.setContent("§l§6• §rID региона§7: §6" + regionId + "\n§l§6• §rКол§7-§fво участников§7: §6" + BlockProtectionAPI.getRegionMembers(
								  regionId).size() + "\n§l§6• §rДата создания§7: §6" + BlockProtectionAPI.getCreateRegionDate(regionId) +
							  "\n\n§6Выберите §6нужное §fВам действие§7, §fкоторое хотите применить к данному §6Региону§7:");
		simpleForm.addButton("Удалить Игрока");
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
		SimpleForm simpleForm = new SimpleForm("Удалить Участника", "Выберите §6участника§7, §fкоторого хотите §6удалить §fиз региона§7:");
		for (String removePlayer : BlockProtectionAPI.getRegionMembers(regionID)) {
			simpleForm.addButton("§6" + removePlayer + "\n§fНажмите чтобы удалить");
		}
		simpleForm.send(player, (targetPlayer, form, data) -> {
			if (data == -1) return;
			Player target = Server.getInstance().getPlayerExact(form.getResponse().getClickedButton().getText().replace("§6", "").split("\n")[0]);
			if (target != null) {
				target.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + player.getName() + " §fудалил Вас из своего региона§7!");
			}
			player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + form.getResponse().getClickedButton().getText().replace("§6", "").split("\n")[0] + " §fудален из региона§7! (" + Utils.getOnlineString(form.getResponse().getClickedButton().getText().replace("§6", "").split("\n")[0]) + "§7)");
			SQLiteUtils.query("DELETE FROM MEMBERS WHERE UPPER(Username) = '" + form.getResponse().getClickedButton().getText().replace("§6", "").split("\n")[0].toUpperCase() + "' AND Region_ID = '" + regionID + "';");
		});
	}

	private void openMyRegionsForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Мои регионы");
		simpleForm.setContent("Выберите один из регионов§7, §fс которым хотите §6взаимодействовать§7.\n\n§fСписок §6Ваших §fрегионов§7:");
		List<Integer> regionsData = SQLiteUtils.selectIntegerList("SELECT Region_ID FROM AREAS WHERE Username = '" + player.getName() + "';");
		if (regionsData == null || regionsData.isEmpty()) {
			simpleForm.addContent("\n\n§fВы не имеете регионов§7!");
		}
		for (int region_id : regionsData) {
			Integer mainX = SQLiteUtils.selectInteger("SELECT Main_X FROM AREAS WHERE (Region_ID = '" + region_id + "');");
			Integer mainY = SQLiteUtils.selectInteger("SELECT Main_Y FROM AREAS WHERE (Region_ID = '" + region_id + "');");
			Integer mainZ = SQLiteUtils.selectInteger("SELECT Main_Z FROM AREAS WHERE (Region_ID = '" + region_id + "');");
			DefaultBlockProtection defaultBlock = BlockProtectionAPI.getAllBlocks().get(WorldSystemAPI.getMap().getBlock(mainX, mainY, mainZ).getId());
			simpleForm.addButton(defaultBlock.getBlockName() + "\n§f" + mainX + "§7, §f" + mainY + "§7, §f" + mainZ, ImageType.PATH, defaultBlock.getBlockImage());
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
		simpleForm.setContent("Хочешь создать свой регион§7? §fНе проблема§7! §fМожешь следовать пунктам§7:\n\n§6• §fДобудь блок привата\n§6• §fПроверь§7, §fнет ли вблизи другого региона\n§6• §fПоставь блок для привата и будь уверен §7- §fтвою постройку не тронут§7!\n\n§fБлоки§7, §fкоторыми можно приватить§7, §fпоказаны на спавне§7.§7!");
		simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
		simpleForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) return;
			this.openMenuForm(player);
		});
	}

	private void openMenuForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Регионы");
		simpleForm.setContent("Вы находитесь в главном меню взаимодействия с регионами на §6Сервере§7!\n\n§fМаксимальное кол§7-§fво §6Регионов §fдля Вас§7: §6"
							  + PermissionAPI.getAllGroups().get(PermissionAPI.getGroup(player.getName())).getMaxRegions() + "\n\n§fВыберите §6нужный §fВам пункт§7:");
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
		simpleForm.setContent("Здесь Вы можете увидеть регионы§7, §fв которых Вы §6состоите§7.\n\n§fСписок регионов§7:");
		List<Integer> regionsData = SQLiteUtils.selectIntegerList("SELECT Region_ID FROM MEMBERS WHERE Username = '" + player.getName() + "';");
		if (regionsData == null || regionsData.isEmpty()) {
			simpleForm.addContent("\n\n§fВас §6не добавили §fни в §61 §fиз регионов§7!");
		}
		for (int region_id : regionsData) {
			String username = SQLiteUtils.selectString("SELECT Username FROM AREAS WHERE (Region_ID = '" + region_id + "');");
			Integer mainX = SQLiteUtils.selectInteger("SELECT Main_X FROM AREAS WHERE (Region_ID = '" + region_id + "');");
			Integer mainY = SQLiteUtils.selectInteger("SELECT Main_Y FROM AREAS WHERE (Region_ID = '" + region_id + "');");
			Integer mainZ = SQLiteUtils.selectInteger("SELECT Main_Z FROM AREAS WHERE (Region_ID = '" + region_id + "');");
			simpleForm.addButton("§6" + username + "\n§f" + mainX + "§7, §f" + mainY + "§7, §f" + mainZ);
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
					player.sendMessage("§l§6• §r§fИспользование §7- /§6rg add §7(§6игрок§7)");
					return true;
				}
				int regionID = BlockProtectionAPI.getRegionIDByPosition(player);
				if (regionID == -1 || !BlockProtectionAPI.isRegionOwner(player.getName(), regionID)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Вы должны находиться §6внутри своего §fрегиона§7!");
					return true;
				}
				String nickname = Utils.implode(args, 1);
				if (!AuthorizationAPI.isRegister(nickname)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + nickname + " §fне зарегистрирован§7!");
					return true;
				}
				if (player.getName().equals(nickname)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Нельзя §6добавить себя §fв свой регион§7!");
					return true;
				}
				if (SQLiteUtils.selectString("SELECT Username FROM MEMBERS WHERE UPPER(Username) = '" + player.getName().toUpperCase() + "' AND Region_ID = '" + regionID + "';") != null) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + nickname + " §fуже состоит в Вашем регионе§7!");
					return true;
				}
				player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + nickname + " §fбыл успешно добавлен в Ваш регион§7!");
				Player target = Server.getInstance().getPlayerExact(nickname);
				if (target != null) {
					target.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + player.getName() + " §fдобавил Вас в свой регион§7!");
				}
				SQLiteUtils.query("INSERT INTO MEMBERS (Region_ID, Username) VALUES ('" + regionID + "', '" + nickname + "');");
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
				String nickname = Utils.implode(args, 1);
				if (player.getName().equals(nickname)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Нельзя §6удалить себя §fс своего региона§7!");
					return true;
				}
				if (BlockProtectionAPI.isRegionMember(nickname, regionID)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + nickname + " §fне состоит в Вашем регионе§7!");
					return true;
				}
				player.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + nickname + " §fудален из региона§7! (" + Utils.getOnlineString(nickname) + "§7)");
				Player target = Server.getInstance().getPlayerExact(nickname);
				if (target != null) {
					target.sendMessage(BlockProtectionAPI.PREFIX + "Игрок §6" + player.getName() + " §fудалил Вас из своего региона§7!");
				}
				SQLiteUtils.query("DELETE FROM MEMBERS WHERE UPPER(Username) = '" + nickname.toUpperCase() + "' AND Region_ID = '" + regionID + "';");
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