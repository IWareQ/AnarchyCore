package ru.jl1mbo.AnarchyCore.Task;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

import java.util.ArrayList;

public class BroadcastTask extends Task {
	private static final ArrayList<String> BROADCAST = new ArrayList<>();
	private static final String PREFIX = "§l§1| §r";
	private static int minutes = 10;
	private static int MESSAGES_SIZE = 0;

	public static void register() {
		BROADCAST.add("Во §9ВКонтакте §fу нас есть беседа\n§l§1| §r§fЗаходи и находи новых друзей§7.\n§l§1| §rЧат во §9ВКонтакте §7-  §fvk§7.§fcc§7/§3avEklQ");
		BROADCAST.add("Будь в курсе всех последних новостей проекта§7!\n§l§1| §r§fПодпишись на нашу группу во §9ВКонтакте\n§l§1| §r§fНаше сообщество §9ВКонтакте §7- §fvk§7.§fcom§7/§3death§7.§3club");
		BROADCAST.add("Обязательно посетите Наш сайт §7- §3death§7-§3mc§7.§3online");
		BROADCAST.add("Для проверки границ территорий используйте обычную палку\n§l§1| §r§fДля создания региона§7, §fпоставьте блок привата\n§l§1| §r§fВсе блоки привата показанны на спавне§7!");
		BROADCAST.add("Не пропустите важные новости из нашей группы §9ВКонтакте §7- §fvk§7.§fcom§7/§3death§7.§3club");
		BROADCAST.add("При совершении покупок на нашем сайте\n§l§1| §r§fВы помогаете в развитии Сервера§7!\n§l§1| §r§fСайт §7- §3death§7-§3mc§7.§3online");
		BROADCAST.add("На сервере присутствует Аукцион §7- §6/auc");
	}

	private static String getBroadcastMessage() {
		if (MESSAGES_SIZE >= BROADCAST.size()) {
			MESSAGES_SIZE = 0;
		}
		String broadcastText = BROADCAST.get(MESSAGES_SIZE);
		MESSAGES_SIZE++;
		return PREFIX + broadcastText;
	}

	@Override()
	public void onRun(int tick) {
		if (minutes != 0) {
			minutes--;
		} else {
			Server.getInstance().broadcastMessage(getBroadcastMessage());
			minutes = 10;
		}
	}
}