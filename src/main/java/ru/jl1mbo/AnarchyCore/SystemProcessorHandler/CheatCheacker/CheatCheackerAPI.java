package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.ModalForm;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.Commands.CheatCheackCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.Commands.CheatCheackTutorialCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.EventsListener.EntityDamageListener;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.EventsListener.PlayerJumpListener;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.EventsListener.PlayerQuitListener;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class CheatCheackerAPI {
	private static int seconds = 420;
	public static String PREFIX = "§l§7(§3Проверка§7) §r";
	private static HashMap<String, Integer> CHEAT_CHEACKER = new HashMap<>();

	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new EntityDamageListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerJumpListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerQuitListener(), Main.getInstance());
		Server.getInstance().getCommandMap().register("", new CheatCheackCommand());
		Server.getInstance().getCommandMap().register("", new CheatCheackTutorialCommand());
	}

	public static boolean isCheatChecker(String playerName) {
		if (CHEAT_CHEACKER.get(playerName.toLowerCase()) != null) {
			return true;
		}
		return false;
	}

	public static void addCheatCheacker(Player player, Player target) {
		if (!isCheatChecker(target.getName())) {
			ModalForm modalForm = new ModalForm("§rПроверка", "§r§fВы уверены что хотите вызвать игрока §6" + target.getName() + " §rна проверку§7?",
												"§r§l§fДа", "§r§l§fНет");
			modalForm.send(player, (targetPlayer, targetForm, data)-> {
				if (data == -1) return;
				switch (data) {
				case 0: {
					int checkCode = Utils.rand(-10000, 10000);
					CHEAT_CHEACKER.put(target.getName().toLowerCase(), checkCode);
					Utils.sendMessageToChat("💂CheatCheacker\n\nИгрок: " + target.getName()+ "\nАдмин: " + player.getName() + "\nКод: " + checkCode, 2000000001);
					target.teleport(new Position(target.getFloorX() + 0.5, target.getFloorY(), target.getFloorZ() + 0.5));
					player.sendMessage(PREFIX + "§fИгрок §6" + target.getName() + " §fвызван на проверку§7, §fпроверочный код§7: §6" + checkCode);
					target.sendTitle("§l§6Проверка");
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(5, 2, 0), "§6Проверка на читы§7!",
												  "§fУ Вас есть §6" + seconds +
												  " §f секунд §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6"
												  + checkCode), target);
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(-5, 2, 0), "§6Проверка на читы§7!",
												  "§fУ Вас есть §6" + seconds +
												  " §f секунд §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6"
												  + checkCode), target);
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(0, 2, 5), "§6Проверка на читы§7!",
												  "§fУ Вас есть §6" + seconds +
												  " §f секунд §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6"
												  + checkCode), target);
					target.getLevel().addParticle(new FloatingTextParticle(target.getPosition().add(0, 2, -5), "§6Проверка на читы§7!",
												  "§fУ Вас есть §6" + seconds +
												  " §f секунд §fчтобы пройти проверку§7!\n\n§fВведите §7/§6cct §fчтобы узнать\nкак пройти проверку\n§fПроверочный код§7: §6"
												  + checkCode), target);
					target.getLevel().addSound(target, Sound.RANDOM_SCREENSHOT, 1.0F, 1.0F, target);
					target.setMovementSpeed(0);
					Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {

						@Override()
						public void onRun(int tick) {
							if (seconds != 0) {
								seconds--;
								target.sendTip("§fВремя§7: §6" + seconds + Utils.getSecond(seconds));
							} else {
								this.cancel();
								target.close("", "§l§6Время проверки вышло§7!\n§fПросим отправить все сделанные\n§fвами скриншоты§7, §fв группу §6ВК§7!");
								seconds = 420;
							}
						}
					}, 20);
				}
				break;

				case 1: {
					player.sendMessage(PREFIX + "§fПроверка отменена§7!");
				}
				break;

				}
			});
		} else {
			player.sendMessage(PREFIX + "§fИгрок §6" + target.getName() + " §fуже находится на проверке§7, §fпроверочный код§7: §6" + CHEAT_CHEACKER.get(
								   target.getName().toLowerCase()));
		}
	}

	public static void removeCheatCheacker(String playerName) {
		if (isCheatChecker(playerName)) {
			CHEAT_CHEACKER.remove(playerName.toLowerCase());
		}
	}
}