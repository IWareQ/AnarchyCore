package Anarchy.Task.Utils;

import java.util.ArrayList;

public class Broadcast {
	public static String PREFIX = "§l§e|| §r";
	private static ArrayList<String> BROADCAST = new ArrayList <>();
	private static int TIMER = 0;

	public static void register() {
		BROADCAST.add("§fНаша группа §9ВКонтакте §7- §fvk.com/§6hall§fmines");
		BROADCAST.add("§fДля телепортации используйте §e/tpa");
		BROADCAST.add("§fОбязательно посетите Наш сайт §7- §6hall§fmines.ru");
		BROADCAST.add("§fДля проверки границ территорий используйте обычную палку");
		BROADCAST.add("§fНе пропустите важные новости из нашей группы §9ВКонтакте §7- §fvk.com/§6hall§fmines");
		BROADCAST.add("§fПри совершении покупок на нашем сайте, Вы помогаете в развитии Сервера!");
		BROADCAST.add("§fИспользуйте §e/bar§f, для управления хотбаром");
		BROADCAST.add("§fНа сервере присутствует Аукцион §8- §e/auc");
	}

	public static String getBroadcast() {
		if (TIMER >= BROADCAST.size()) {
			TIMER = 0;
		}

		String broadcastText = BROADCAST.get(TIMER);
		TIMER++;
		return PREFIX + broadcastText;
	}
}