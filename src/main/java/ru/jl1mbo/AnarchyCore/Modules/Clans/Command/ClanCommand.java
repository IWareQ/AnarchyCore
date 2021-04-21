package ru.jl1mbo.AnarchyCore.Modules.Clans.Command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.CustomForm;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.ImageType;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.ModalForm;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;
import ru.jl1mbo.AnarchyCore.Modules.Clans.ClanAPI;
import ru.jl1mbo.AnarchyCore.Modules.Economy.EconomyAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

import java.util.List;

public class ClanCommand extends Command {

	public ClanCommand() {
		super("clan", "§rСистема кланов", "", new String[]{"c"});
		this.commandParameters.clear();
		this.commandParameters.put("clan", new CommandParameter[]{CommandParameter.newEnum("action", new CommandEnum("ClanAction", "help", "menu", "invite", "create", "invites")), CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	private void openMenuForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Меню клана");
		simpleForm.addContent("§l§6• §rНазвание§7: §6" + ClanAPI.getClanName(ClanAPI.getPlayerClanId(player.getName())));
		simpleForm.addContent("\n§l§6• §rУчастников§7: §6" + ClanAPI.getClanMembers(ClanAPI.getPlayerClanId(player.getName())).size());
		simpleForm.addContent("\n§l§6• §rВаша роль§7: §6" + ClanAPI.getPlayerRole(player.getName()));
		simpleForm.addContent("\n\n§fВыберите §6нужный §fВам пункт§7:");
		switch (ClanAPI.getPlayerRole(player.getName())) {
			case "Участник":
				simpleForm.addButton("Выйти с клана");
				simpleForm.send(player, (targetPlayer, targetForm, data) -> {
					if (data == -1) {
						return;
					}
					this.openLeaveClanForm(player);
				});
				break;
			case "Старейшина":
				simpleForm.addButton("Список приглашений");
				simpleForm.addButton("Выйти с клана");
				simpleForm.send(player, (targetPlayer, targetForm, data) -> {
					if (data == -1) {
						return;
					}
					switch (data) {
						case 0: {
							this.openRequestsListForm(player, ClanAPI.getPlayerClanId(player.getName()));
						}
						break;

						case 1: {
							this.openLeaveClanForm(player);
						}
						break;
					}
				});
				break;
			case "Соруководитель":
				simpleForm.addButton("Список приглашений");
				simpleForm.addButton("Участники");
				simpleForm.addButton("Выйти с клана");
				simpleForm.send(player, (targetPlayer, targetForm, data) -> {
					if (data == -1) {
						return;
					}
					switch (data) {
						case 0: {
							this.openRequestsListForm(player, ClanAPI.getPlayerClanId(player.getName()));
						}
						break;

						case 1: {
							this.openMembersForm(player, ClanAPI.getPlayerClanId(player.getName()));
						}
						break;

						case 2: {
							this.openLeaveClanForm(player);
						}
						break;
					}
				});
				break;
			default:
				simpleForm.addButton("Список приглашений");
				simpleForm.addButton("Участники");
				simpleForm.addButton("Удалить клан");
				simpleForm.send(player, (targetPlayer, targetForm, data) -> {
					if (data == -1) {
						return;
					}
					switch (data) {
						case 0: {
							this.openRequestsListForm(player, ClanAPI.getPlayerClanId(player.getName()));
						}
						break;

						case 1: {
							this.openMembersForm(player, ClanAPI.getPlayerClanId(player.getName()));
						}
						break;

						case 2: {
							this.openDeleteClanForm(player, ClanAPI.getPlayerClanId(player.getName()));
						}
						break;
					}
				});
				break;
		}
	}

	private void openRequestsListForm(Player player, Integer clanId) {
		SimpleForm simpleForm = new SimpleForm("Список приглашений");
		simpleForm.addContent("Здесь Вы можете увидеть все отправившие приглашения игрокам§7, §fи взаимодействовать с ними§7.");
		simpleForm.addContent("\n\n§fПриглашения§7:");
		List<String> clanRequests = ClanAPI.getClanRequests(clanId);
		for (String playersName : clanRequests) {
			simpleForm.addButton("§6" + playersName + "\n§fНажмите§7, §fчтобы удалить§7!");
		}
		simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
		simpleForm.send(player, (targetPlayer, form, data) -> {
			if (data == -1) {
				return;
			}
			if (clanRequests.size() == data) {
				this.openMenuForm(player);
				return;
			}
			ClanAPI.removeRequestClan(clanRequests.get(data), ClanAPI.getPlayerClanId(player.getName()));
			player.sendMessage(ClanAPI.PREFIX + "Заявка на приглашение игрока §6" + clanRequests.get(data) + " §fудалена§7!");
		});
	}

	private void openLeaveClanForm(Player player) {
		ModalForm modalForm = new ModalForm("Выход с клана");
		modalForm.setContent("Вы действительно хотите §6покинуть §fклан§7?");
		modalForm.setButton1("Да");
		modalForm.setButton2("Нет");
		modalForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) {
				return;
			}
			if (data == 0) {
				player.sendMessage(ClanAPI.PREFIX + "Вы успешно §6покинули §fклан!");
				ClanAPI.leaveClan(player.getName());
			}
		});
	}

	private void openDeleteClanForm(Player player, Integer clanId) {
		ModalForm modalForm = new ModalForm("Удаление клана");
		modalForm.setContent("Вы действительно хотите §6удалить §fсвой клан§7?");
		modalForm.setButton1("Да");
		modalForm.setButton2("Нет");
		modalForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) {
				return;
			}
			if (data == 0) {
				player.sendMessage(ClanAPI.PREFIX + "Клан успешно §6удален!");
				ClanAPI.deleteClan(clanId);
			}
		});
	}

	private void openMembersForm(Player player, Integer clanId) {
		SimpleForm simpleForm = new SimpleForm("Участники");
		simpleForm.addContent("Здесь Вы можете увидеть всех участников§7, §fи взаимодействовать с ними§7.");
		simpleForm.addContent("\n\n§fУчастники§7:");
		List<String> clanMembers = ClanAPI.getClanMembers(clanId);
		for (String memberName : clanMembers) {
			simpleForm.addButton(memberName + "\n§6" + ClanAPI.getPlayerRole(memberName));
		}
		simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
		simpleForm.send(player, (targetPlayer, form, data) -> {
			if (data == -1) {
				return;
			}
			if (clanMembers.size() == data) {
				this.openMenuForm(player);
				return;
			}
			this.openEditPlayerForm(player, clanMembers.get(data));
		});
	}

	private void openEditPlayerForm(Player player, String targetName) {
		SimpleForm simpleForm = new SimpleForm("Профиль §6" + targetName);
		if (ClanAPI.getPlayerRole(player.getName()).equals("Глава") || ClanAPI.getPlayerRole(player.getName()).equals("Соруководитель")) {
			simpleForm.addContent("§l§6• §rЗвание§7: §6" + ClanAPI.getPlayerRole(targetName));
			switch (ClanAPI.getPlayerRole(targetName)) {
				case "Соруководитель":
					simpleForm.addButton("Понизить");
					simpleForm.addButton("Исключить");
					simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
					simpleForm.send(player, (targetPlayer, form, data) -> {
						if (data == -1) {
							return;
						}
						switch (data) {
							case 0: {
								ClanAPI.changePlayerRole(targetName, "Старейшина");
								this.openEditPlayerForm(player, targetName);
							}
							break;

							case 1: {
								ClanAPI.leaveClan(targetName);
								player.sendMessage(ClanAPI.PREFIX + "Игрок §6" + targetName + " §fбыл успешно исключен§7!");
							}
							break;

							case 2: {
								this.openMembersForm(player, ClanAPI.getPlayerClanId(player.getName()));
							}
							break;
						}
					});
					break;
				case "Старейшина":
					simpleForm.addButton("Повысить");
					simpleForm.addButton("Понизить");
					simpleForm.addButton("Исключить");
					simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
					simpleForm.send(player, (targetPlayer, form, data) -> {
						if (data == -1) {
							return;
						}
						switch (data) {
							case 0: {
								ClanAPI.changePlayerRole(targetName, "Соруководитель");
								this.openEditPlayerForm(player, targetName);
							}
							break;

							case 1: {
								ClanAPI.changePlayerRole(targetName, "Участник");
								this.openEditPlayerForm(player, targetName);
							}
							break;

							case 2: {
								ClanAPI.leaveClan(targetName);
								player.sendMessage(ClanAPI.PREFIX + "Игрок §6" + targetName + " §fбыл успешно исключен§7!");
							}
							break;

							case 3: {
								this.openMembersForm(player, ClanAPI.getPlayerClanId(player.getName()));
							}
							break;
						}
					});
					break;
				case "Участник":
					simpleForm.addButton("Повысить");
					simpleForm.addButton("Исключить");
					simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
					simpleForm.send(player, (targetPlayer, form, data) -> {
						if (data == -1) {
							return;
						}
						switch (data) {
							case 0: {
								ClanAPI.changePlayerRole(targetName, "Старейшина");
								this.openEditPlayerForm(player, targetName);
							}
							break;

							case 2: {
								ClanAPI.leaveClan(targetName);
								player.sendMessage(ClanAPI.PREFIX + "Игрок §6" + targetName + " §fбыл успешно исключен§7!");
							}
							break;

							case 3: {
								this.openMembersForm(player, ClanAPI.getPlayerClanId(player.getName()));
							}
							break;
						}
					});
					break;
				default:
					simpleForm.addButton("Назад", ImageType.PATH, "textures/ui/back_button_default");
					simpleForm.send(player, (targetPlayer, form, data) -> {
						if (data == -1) {
							return;
						}
						this.openMembersForm(player, ClanAPI.getPlayerClanId(player.getName()));
					});
					break;
			}
		}
	}

	private void openCreateClanForm(Player player) {
		CustomForm customForm = new CustomForm("Создание клана");
		customForm.addLabel("Для §6создания клана §fВам потребуется §62000§7. §fУбедитесь§7, §fчто у Вас имеются §6все нужны §fкомпоненты§7!");
		customForm.addLabel("\n§fНиже §6введите §fназвание Клана§7.");
		customForm.addLabel("\n§l§6• §rОтсутсвие спец§7. §fсимволов");
		customForm.addLabel("§l§6• §rОт §64 §fдо §610 §fсимволов");
		customForm.addInput("Название клана", "название клана");
		customForm.send(player, (targetPlayer, form, data) -> {
			if (data == null) {
				return;
			}
			if (EconomyAPI.myMoney(player.getName()) >= 2000) {
				String clanName = data.get(4).toString();
				if (clanName.length() > 10 || clanName.length() < 4) {
					player.sendMessage(ClanAPI.PREFIX + "Название клана слишком §6длинное§7/§6короткое§7!");
					return;
				}
				if (!Utils.isValidString(clanName)) {
					player.sendMessage(ClanAPI.PREFIX + "В название клана есть §6недопустимые §fсимволы§7!");
					return;
				}
				if (!ClanAPI.clanNameExists(clanName)) {
					player.sendMessage(ClanAPI.PREFIX + "Клан с таким названием уже §6существует§7!");
					return;
				}
				EconomyAPI.reduceMoney(player.getName(), 2000.0);
				ClanAPI.createClan(player, clanName);
				this.openMenuForm(player);
			} else {
				player.sendMessage(ClanAPI.PREFIX + "Вам не хватает монет для создания клана§7!");
			}
		});
	}

	private void openMyRequestsForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Заявки в Клан");
		simpleForm.addContent("Для §6принятия заявки §fпросто нажмите на нее§7.");
		simpleForm.addContent("\n\nСписок §6заявок §fв клан§7:");
		List<Integer> requests = ClanAPI.getPlayerRequests(player.getName());
		for (Integer clanId : requests) {
			simpleForm.addButton("§6" + ClanAPI.getClanName(clanId) + "\n§fНажмите§7, §fчтобы принять§7!");
		}
		simpleForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) {
				return;
			}
			ClanAPI.acceptRequestsClan(player, requests.get(data));
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
					if (!ClanAPI.playerIsInClan(player.getName())) {
						this.openCreateClanForm(player);
						return true;
					}
					this.openMenuForm(player);
				}
				break;

				case "invites": {
					if (!ClanAPI.playerIsInClan(player.getName())) {
						this.openMyRequestsForm(player);
						return true;
					}
					player.sendMessage(ClanAPI.PREFIX + "Вы уже §6состоите §fв клане§7!");
				}
				break;

				case "invite": {
					if (ClanAPI.playerIsInClan(player.getName()) && !ClanAPI.getPlayerRole(player.getName()).equals("Участник")) {
						if (args.length < 2) {
							player.sendMessage("§l§6• §rИспользование §7- /§6c invite §7(§6игрок§7)");
							return true;
						}
						Player target = Server.getInstance().getPlayer(Utils.implode(args, 1));
						if (target == null) {
							player.sendMessage("§l§6• §rИгрок §6" + Utils.implode(args, 1) + " §fне в сети§7!");
							return true;
						}
						player.sendMessage(ClanAPI.PREFIX + "Приглашение в клан игроку §6" + target.getName() + " §fотправлено§7!");
						target.sendMessage(ClanAPI.PREFIX + "Вам поступило §6новое приглашение §fв клан§7, §fдля принятия используйте §7/§6c invites");
						ClanAPI.sendRequestsClan(target, ClanAPI.getPlayerClanId(player.getName()));
					} else {
						player.sendMessage(ClanAPI.PREFIX + "Вы не являетесь §6Старейшеной §fили §6выше §fв клане§7!");
					}
				}
				break;

				default: {
					player.sendMessage("§l§6• §rИспользование §7- /§6clan help");
				}
				break;

			}
		}
		return false;
	}
}