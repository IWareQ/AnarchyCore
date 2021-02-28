package ru.jl1mbo.AnarchyCore.GameHandler.ClanManager.Commands;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.GameHandler.ClanManager.ClanManager;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.CustomForm;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.ImageType;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.ModalForm;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EconomyAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class ClanCommand extends Command {

	public ClanCommand() {
		super("clan", "§rСистема кланов", "", new String[] {"c"});
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("string", new String[]{"help", "menu", "invite", "create", "invites"}), new CommandParameter("player", CommandParamType.TARGET, false)});
	}



	private void openMenuForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Меню клана");
		simpleForm.setContent("§l§6• §rНазвание§7: §6" + ClanManager.getPlayerClan(player.getName()) + "\n§l§6• §rУчастников§7: §6" + ClanManager.getClanMembers(
								  ClanManager.getPlayerClan(player.getName())).size() + "\n§l§6• §rВаша роль§7: §6" + ClanManager.getPlayerRole(player.getName()) +
							  "\n\n§fВыберите §6нужный §fВам пункт§7:");
		if (ClanManager.getPlayerRole(player.getName()).equals("Участник")) {
			simpleForm.addButton("Выйти с клана");
			simpleForm.send(player, (targetPlayer, targetForm, data) -> {
				if (data == -1) return;
				this.openLeaveClanForm(player, ClanManager.getPlayerClan(player.getName()));
			});
		} else if (ClanManager.getPlayerRole(player.getName()).equals("Старейшина")) {
			simpleForm.addButton("Список приглашений");
			simpleForm.addButton("Выйти с клана");
			simpleForm.send(player, (targetPlayer, targetForm, data) -> {
				if (data == -1) return;
				switch (data) {
				case 0: {
					this.openRequestsListForm(player, ClanManager.getPlayerClan(player.getName()));
				}
				break;

				case 1: {
					this.openLeaveClanForm(player, ClanManager.getPlayerClan(player.getName()));
				}
				break;
				}
			});
		} else if (ClanManager.getPlayerRole(player.getName()).equals("Соруководитель")) {
			simpleForm.addButton("Список приглашений");
			simpleForm.addButton("Участники");
			simpleForm.addButton("Выйти с клана");
			simpleForm.send(player, (targetPlayer, targetForm, data) -> {
				if (data == -1) return;
				switch (data) {
				case 0: {
					this.openRequestsListForm(player, ClanManager.getPlayerClan(player.getName()));
				}
				break;

				case 1: {
					this.openMembersForm(player, ClanManager.getPlayerClan(player.getName()));
				}
				break;

				case 2: {
					this.openLeaveClanForm(player, ClanManager.getPlayerClan(player.getName()));
				}
				break;
				}
			});
		} else {
			simpleForm.addButton("Список приглашений");
			simpleForm.addButton("Участники");
			simpleForm.addButton("Удалить клан");
			simpleForm.send(player, (targetPlayer, targetForm, data) -> {
				if (data == -1) return;
				switch (data) {
				case 0: {
					this.openRequestsListForm(player, ClanManager.getPlayerClan(player.getName()));
				}
				break;

				case 1: {
					this.openMembersForm(player, ClanManager.getPlayerClan(player.getName()));
				}
				break;

				case 2: {
					this.openDeleteClanForm(player, ClanManager.getPlayerClan(player.getName()));
				}
				break;
				}
			});
		}
	}

	private void openRequestsListForm(Player player, String clanName) {
		SimpleForm simpleForm = new SimpleForm("Список приглашений");
		simpleForm.setContent("Здесь Вы можете увидеть все отправившие приглашения игрокам§7, §fи взаимодействовать с ними§7.\n\n§fПриглашения§7:");
		List<String> clanRequests = ClanManager.getClanRequests(clanName);
		for (String playersName : clanRequests) {
			simpleForm.addButton("§6" + playersName + "\n§fНажмите§7, §fчтобы удалить");
		}
		simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
		simpleForm.send(player, (targetPlayer, form, data) -> {
			if (data == -1) return;
			if (clanRequests.size() == data) {
				this.openMenuForm(player);
				return;
			}
			ClanManager.removeRequestClan(clanRequests.get(data), ClanManager.getPlayerClan(player.getName()));
			player.sendMessage(ClanManager.PREFIX + "Заявка на приглашение игрока §6" + clanRequests.get(data) + " §fудалена§7!");
		});
	}

	private void openLeaveClanForm(Player player, String clanName) {
		ModalForm modalForm = new ModalForm("Выход с клана", "Вы действительно хотите §6покинуть §fклан§7?", "Да", "Нет");
		modalForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) return;
			if (data == 0) {
				player.sendMessage(ClanManager.PREFIX + "Вы успешно §6покинули §fклан!");
				ClanManager.leaveClan(player.getName(), clanName);
			}
		});
	}

	private void openDeleteClanForm(Player player, String clanName) {
		ModalForm modalForm = new ModalForm("Удаление клана", "Вы действительно хотите §6удалить §fсвой клан§7?", "Да", "Нет");
		modalForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) return;
			if (data == 0) {
				player.sendMessage(ClanManager.PREFIX + "Клан успешно §6удален!");
				ClanManager.deleteClan(clanName);
			}
		});
	}

	private void openMembersForm(Player player, String clanName) {
		SimpleForm simpleForm = new SimpleForm("Участники");
		simpleForm.setContent("Здесь Вы можете увидеть всех участников§7, §fи взаимодействовать с ними§7.\n\n§fУчастники§7:");
		List<String> clanMembers = ClanManager.getClanMembers(clanName);
		for (String memberName : clanMembers) {
			simpleForm.addButton(memberName + "\n§6" + ClanManager.getPlayerRole(memberName));
		}
		simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
		simpleForm.send(player, (targetPlayer, form, data) -> {
			if (data == -1) return;
			if (clanMembers.size() == data) {
				this.openMenuForm(player);
				return;
			}
			this.openEditPlayerForm(player, clanMembers.get(data));
		});
	}

	private void openEditPlayerForm(Player player, String targetName) {
		SimpleForm simpleForm = new SimpleForm("Участники");
		if (ClanManager.getPlayerRole(player.getName()).equals("Соруководитель") || ClanManager.getPlayerRole(player.getName()).equals("Глава")) {
			if (ClanManager.getPlayerRole(targetName).equals("Соруководитель")) {
				simpleForm.setContent("Имя: " +  targetName + "\nРоль: " + ClanManager.getPlayerRole(targetName));
				simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
				simpleForm.send(player, (targetPlayer, form, data) -> {
					if (data == -1) return;
					this.openMembersForm(player, ClanManager.getPlayerClan(player.getName()));
				});
			} else {
				simpleForm.setContent("Имя: " +  targetName + "\nРоль: " + ClanManager.getPlayerRole(targetName));
				simpleForm.addButton("Повысить");
				simpleForm.addButton("Выгнать");
				simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
				simpleForm.send(player, (targetPlayer, form, data) -> {
					if (data == -1) return;
					switch (data) {
					case 0: {
						if (ClanManager.getPlayerRole(targetName).equals("Участник")) {
							ClanManager.changeRolePlayer(targetName, "Старейшина");
							this.openEditPlayerForm(player, targetName);
						} else if (ClanManager.getPlayerRole(targetName).equals("Старейшина")) {
							ClanManager.changeRolePlayer(targetName, "Соруководитель");
							this.openEditPlayerForm(player, targetName);
						}
					}
					break;

					case 1: {
						ClanManager.leaveClan(targetName, ClanManager.getPlayerClan(targetName));
						player.sendMessage(ClanManager.PREFIX + "Игрок §6" + targetName + " §fбыл успешно кикнут§7!");
					}
					break;

					case 2: {
						this.openMembersForm(player, ClanManager.getPlayerClan(player.getName()));
					}
					break;
					}
				});
			}
		}
	}

	private void openCreateClanForm(Player player) {
		CustomForm customForm = new CustomForm("Создание клана");
		customForm.addLabel("Для §6создания клана §fВам потребуется §62000§7. §fУбедитесь§7, §fчто у Вас имеются §6все нужны §fкомпоненты§7!\n\n§fНиже §6введите §fназвание Клана§7.\n\n§l§6• §rОтсутсвие спец§7. §fсимволов\n§l§6• §rОт §64 §fдо §610 §fсимволов");
		customForm.addInput("Название клана", "ExtraNons");
		customForm.send(player, (targetPlayer, form, data) -> {
			if (data == null) return;
			if (EconomyAPI.myMoney(player.getName()) >= 2000) {
				String clanName = data.get(1).toString();
				if (clanName.length() > 10 || clanName.length() < 4) {
					player.sendMessage(ClanManager.PREFIX + "Название клана слишком §6длинное§7/§6короткое§7!");
					return;
				}
				if (!ClanManager.clanNameExists(clanName)) {
					player.sendMessage(ClanManager.PREFIX + "Клан с таким названием уже §6существует§7!");
					return;
				}
				ClanManager.createClan(player, clanName);
				this.openMenuForm(player);
			} else {
				player.sendMessage(ClanManager.PREFIX + "Вам не хватает монет для создания клана§7!");
			}
		});
	}

	private void openMyRequestsForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Заявки в Клан",
											   "Для §6принятия заявки §fпросто нажмите на нее.\n\nСписок §6заявок §fв клан§7:");
		List<String> requests = ClanManager.getPlayerRequests(player.getName());
		requests.forEach(clanName -> {
			simpleForm.addButton("§6" + clanName + "\n§fНажмите§7, §fчтобы принять");
		});
		simpleForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) return;
			ClanManager.acceptRequestClan(player, requests.get(data));
		});
	}

	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage("§l§6• §rИспользование §7- /§6c help");
				return true;
			}
			switch (args[0]) {
			case "create": {
				if (!ClanManager.playerIsInClan(player.getName())) {
					this.openCreateClanForm(player);
					return true;
				}
				this.openMenuForm(player);
			}
			break;

			case "invites": {
				if (!ClanManager.playerIsInClan(player.getName())) {
					this.openMyRequestsForm(player);
					return true;
				}
				player.sendMessage(ClanManager.PREFIX + "Вы уже §6состоите §fв клане§7!");
			}
			break;

			case "invite": {
				if (ClanManager.playerIsInClan(player.getName()) && !ClanManager.getPlayerRole(player.getName()).equals("Участник")) {
					if (args.length < 2) {
						player.sendMessage("§l§6• §rИспользование §7- /§6c invite §7(§6игрок§7)");
						return true;
					}
					Player target = Server.getInstance().getPlayer(Utils.implode(args, 1));
					if (target == null) {
						player.sendMessage("§l§6• §rИгрок §6" + Utils.implode(args, 1) + " §fне в сети§7!");
						return true;
					}
					player.sendMessage(ClanManager.PREFIX + "Приглашение в клан игроку §6" + target.getName() + " §fотправлено§7!");
					target.sendMessage(ClanManager.PREFIX + "Вам поступило §6новое приглашение §fв клан§7, §fдля принятия используйте §7/§6c invites");
					ClanManager.sendRequestsClan(target, ClanManager.getPlayerClan(player.getName()));
				} else {
					player.sendMessage(ClanManager.PREFIX + "Вы не являетесь §6Старейшеной §fили §6выше §fв клане§7!");
				}
			}
			break;

			default: {
				if (!ClanManager.playerIsInClan(player.getName())) {
					this.openCreateClanForm(player);
					return true;
				}
				this.openMenuForm(player);
			}
			break;

			}
		}
		return false;
	}
}