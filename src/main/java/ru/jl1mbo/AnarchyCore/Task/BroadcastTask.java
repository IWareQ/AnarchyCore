package ru.jl1mbo.AnarchyCore.Task;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

import java.util.ArrayList;

public class BroadcastTask extends Task {

	private static final ArrayList<String> BROADCAST = new ArrayList<>();
	private int minutes = 7;
	private int MESSAGES_SIZE = 0;

	public static void register() {
		BROADCAST.add("Будь в курсе последних новостей проекта§7!\n§l§6 §rПодпишись на нашу группу §9ВКонтакте\n§l§6 §rСсылка на группу§7: §fvk§7.§fcom§7/§3death§fmc§7.§fclub");
		BROADCAST.add("Не усложняй жизнь §6себе §fи §6другим!§7 §fНашел нарушителя§7? §fИспользуй §7/§6report§7!");
		BROADCAST.add("Хочешь §6выделиться §fиз толпы§7? §fСтать §6круче§7?\n§l§6 §rТогда успей приобрести донат по §6шокирующим §fскидкам§7! §fСайт§7: §3death§7-§fmc§7.§fru");
		BROADCAST.add("Не знаешь как приватить§7? §fИнформацию по регионами можно найти на §6спавне§7, §fлибо же введя команду §7/§6rg help");
		BROADCAST.add("Обязательно посетите Наш сайт§7: §3death§7-§fmc§7.§fru");
		BROADCAST.add("Зови друзей и получай бонусы§7!\n§l§6 §rСсылка на группу§7: §fvk§7.§fcom§7/§3death§fmc§7.§fclub");
		BROADCAST.add("На сервере присутствует §6Аукцион§7! §fИспользуй §7/§6auc");
	}

	private String getBroadcastMessage() {
		if (MESSAGES_SIZE >= BROADCAST.size()) {
			MESSAGES_SIZE = 0;
		}
		String broadcastText = BROADCAST.get(MESSAGES_SIZE);
		MESSAGES_SIZE++;
		return "\n" + broadcastText + "\n";
	}

	@Override()
	public void onRun(int tick) {
		if (this.minutes != 0) {
			this.minutes--;
		} else {
			Server.getInstance().broadcastMessage(this.getBroadcastMessage());
			this.minutes = 7;
		}
	}
}