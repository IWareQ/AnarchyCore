package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker;

import java.util.Arrays;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.ModalForm;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.Commands.CheatCheackCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.Commands.CheatCheackTutorialCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.EventsListener.EventsListenerCheatCheacker;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class CheatCheackerAPI {
	private static int seconds = 420;
	public static String PREFIX = "§l§7(§3Проверка§7) §r";
	private static HashMap<String, Integer> CHEAT_CHEACKER = new HashMap<>();

	public static void register() {
		EventsListenerCheatCheacker.register();
		Server.getInstance().getCommandMap().registerAll("", Arrays.asList(new CheatCheackCommand(), new CheatCheackTutorialCommand()));
	}

	public static boolean isCheatChecker(String playerName) {
		if (CHEAT_CHEACKER.get(playerName.toLowerCase()) != null) {
			return true;
		}
		return false;
	}

	public static void addCheatCheacker(Player player, Player target) {
		if (!isCheatChecker(target.getName())) {
			ModalForm modalForm = new ModalForm("Проверка", "Вы уверены что хотите вызвать игрока §6" + target.getName() + " §fна проверку§7?",
												"Да", "Нет");
			modalForm.send(player, (targetPlayer, targetForm, data)-> {
				if (data == -1) return;
				switch (data) {
				case 0: {
					int checkCode = Utils.rand(0, 10000);
					CHEAT_CHEACKER.put(target.getName().toLowerCase(), checkCode);
					Utils.sendMessageToChat("💂CheatCheacker\n\nИгрок: " + target.getName() + "\nАдмин: " + player.getName() + "\nКод: " + checkCode);
					player.sendMessage(PREFIX + "Игрок §6" + target.getName() + " §fвызван на проверку§7, §fпроверочный код§7: §6" + checkCode);
					target.sendTitle("§l§6Проверка");
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(5, 2, 0), "§6Проверка на читы§7!",
												  "§fУ Вас есть §6" + seconds +
												  " §fсекунд §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6"
												  + checkCode), target);
					Entity.createEntity("CowNPC", target.getPosition().add(5, 1, 0)).spawnToAll();
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(-5, 2, 0), "§6Проверка на читы§7!",
												  "§fУ Вас есть §6" + seconds +
												  " §fсекунд §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6"
												  + checkCode), target);
					Entity.createEntity("CowNPC", target.getPosition().add(-5, 1, 0)).spawnToAll();
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(0, 2, 5), "§6Проверка на читы§7!",
												  "§fУ Вас есть §6" + seconds +
												  " §fсекунд §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6"
												  + checkCode), target);
					Entity.createEntity("CowNPC", target.getPosition().add(0, 1, 5)).spawnToAll();
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(0, 2, -5), "§6Проверка на читы§7!",
												  "§fУ Вас есть §6" + seconds +
												  " §fсекунд §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6"
												  + checkCode), target);
					Entity.createEntity("CowNPC", target.getPosition().add(0, 1, -5)).spawnToAll();
					target.getLevel().addSound(target, Sound.RANDOM_SCREENSHOT, 1.0F, 1.0F, target);
					Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {

						@Override()
						public void onRun(int tick) {
							if (seconds != 0) {
								seconds--;
								if (isCheatChecker(target.getName())) {
									target.sendTip("Время§7: §6" + seconds + Utils.getSecond(seconds));
									target.setImmobile(true);
								}
							} else {
								this.cancel();
								if (isCheatChecker(target.getName())) {
									BanSystemAPI.addBan(target.getName(), "время проверки вышло", "CheatCheacker", 30 * 86400);
								}
								seconds = 420;
							}
						}
					}, 20);
				}
				break;

				case 1: {
					player.sendMessage(PREFIX + "Проверка §6отменена§7!");
				}
				break;

				}
			});
		} else {
			player.sendMessage(PREFIX + "Игрок §6" + target.getName() + " §fуже находится на проверке§7, §fпроверочный код§7: §6" + CHEAT_CHEACKER.get(
								   target.getName().toLowerCase()));
		}
	}

	public static void removeCheatCheacker(String playerName) {
		if (isCheatChecker(playerName)) {
			CHEAT_CHEACKER.remove(playerName.toLowerCase());
		}
	}
}