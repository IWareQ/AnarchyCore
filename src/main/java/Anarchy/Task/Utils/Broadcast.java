package Anarchy.Task.Utils;

import java.util.ArrayList;

public class Broadcast {
	public static String PREFIX = "\u00a7l\u00a71| \u00a7r";
	private static ArrayList<String> BROADCAST = new ArrayList<>();
	private static int TIMER = 0;
	
	public static void register() {
		BROADCAST.add("\u00a7f\u0412\u043e \u00a79\u0412\u041a\u043e\u043d\u0442\u0430\u043a\u0442\u0435 \u00a7f\u0443 \u043d\u0430\u0441 \u0435\u0441\u0442\u044c \u0431\u0435\u0441\u0435\u0434\u0430\n\u00a7l\u00a71| \u00a7r\u00a7f\u0417\u0430\u0445\u043e\u0434\u0438 \u0438 \u043d\u0430\u0445\u043e\u0434\u0438 \u043d\u043e\u0432\u044b\u0445 \u0434\u0440\u0443\u0437\u0435\u0439\u00a77.\n\u00a7l\u00a71| \u00a7r\u0427\u0430\u0442 \u0432\u043e \u00a79\u0412\u041a\u043e\u043d\u0442\u0430\u043a\u0442\u0435 \u00a77-  \u00a7fvk\u00a77.\u00a7fcc\u00a77/\u00a73avEklQ");
		BROADCAST.add("\u00a7f\u0411\u0443\u0434\u044c \u0432 \u043a\u0443\u0440\u0441\u0435 \u0432\u0441\u0435\u0445 \u043f\u043e\u0441\u043b\u0435\u0434\u043d\u0438\u0445 \u043d\u043e\u0432\u043e\u0441\u0442\u0435\u0439 \u043f\u0440\u043e\u0435\u043a\u0442\u0430\u00a77!\n\u00a7l\u00a71| \u00a7r\u00a7f\u041f\u043e\u0434\u043f\u0438\u0448\u0438\u0441\u044c \u043d\u0430 \u043d\u0430\u0448\u0443 \u0433\u0440\u0443\u043f\u043f\u0443 \u0432\u043e \u00a79\u0412\u041a\u043e\u043d\u0442\u0430\u043a\u0442\u0435\n\u00a7l\u00a71| \u00a7r\u00a7f\u041d\u0430\u0448\u0435 \u0441\u043e\u043e\u0431\u0449\u0435\u0441\u0442\u0432\u043e \u00a79\u0412\u041a\u043e\u043d\u0442\u0430\u043a\u0442\u0435 \u00a77- \u00a7fvk\u00a77.\u00a7fcom\u00a77/\u00a73death\u00a7fanarchy");
		BROADCAST.add("\u00a7f\u041e\u0431\u044f\u0437\u0430\u0442\u0435\u043b\u044c\u043d\u043e \u043f\u043e\u0441\u0435\u0442\u0438\u0442\u0435 \u041d\u0430\u0448 \u0441\u0430\u0439\u0442 \u00a77- \u00a73death\u00a77-\u00a73mc\u00a77.\u00a73online");
		BROADCAST.add("\u00a7f\u0414\u043b\u044f \u043f\u0440\u043e\u0432\u0435\u0440\u043a\u0438 \u0433\u0440\u0430\u043d\u0438\u0446 \u0442\u0435\u0440\u0440\u0438\u0442\u043e\u0440\u0438\u0439 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 \u043e\u0431\u044b\u0447\u043d\u0443\u044e \u043f\u0430\u043b\u043a\u0443\n\u00a7l\u00a71| \u00a7r\u00a7f\u0414\u043b\u044f \u0441\u043e\u0437\u0434\u0430\u043d\u0438\u044f \u0440\u0435\u0433\u0438\u043e\u043d\u0430\u00a77, \u00a7f\u043f\u043e\u0441\u0442\u0430\u0432\u044c\u0442\u0435 \u0431\u043b\u043e\u043a \u043f\u0440\u0438\u0432\u0430\u0442\u0430\n\u00a7l\u00a71| \u00a7r\u00a7f\u0412\u0441\u0435 \u0431\u043b\u043e\u043a\u0438 \u043f\u0440\u0438\u0432\u0430\u0442\u0430 \u043f\u043e\u043a\u0430\u0437\u0430\u043d\u043d\u044b \u043d\u0430 \u0441\u043f\u0430\u0432\u043d\u0435\u00a77!");
		BROADCAST.add("\u00a7f\u041d\u0435 \u043f\u0440\u043e\u043f\u0443\u0441\u0442\u0438\u0442\u0435 \u0432\u0430\u0436\u043d\u044b\u0435 \u043d\u043e\u0432\u043e\u0441\u0442\u0438 \u0438\u0437 \u043d\u0430\u0448\u0435\u0439 \u0433\u0440\u0443\u043f\u043f\u044b \u00a79\u0412\u041a\u043e\u043d\u0442\u0430\u043a\u0442\u0435 \u00a77- \u00a7fvk\u00a77.\u00a7fcom\u00a77/\u00a73death\u00a7fanarchy");
		BROADCAST.add("\u00a7f\u041f\u0440\u0438 \u0441\u043e\u0432\u0435\u0440\u0448\u0435\u043d\u0438\u0438 \u043f\u043e\u043a\u0443\u043f\u043e\u043a \u043d\u0430 \u043d\u0430\u0448\u0435\u043c \u0441\u0430\u0439\u0442\u0435\n\u00a7l\u00a71| \u00a7r\u00a7f\u0412\u044b \u043f\u043e\u043c\u043e\u0433\u0430\u0435\u0442\u0435 \u0432 \u0440\u0430\u0437\u0432\u0438\u0442\u0438\u0438 \u0421\u0435\u0440\u0432\u0435\u0440\u0430\u00a77!\n\u00a7l\u00a71| \u00a7r\u00a7f\u0421\u0430\u0439\u0442 \u00a77- \u00a73death\u00a77-\u00a73mc\u00a77.\u00a73online");
		BROADCAST.add("\u00a7f\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 \u00a76/bar\u00a7f, \u0434\u043b\u044f \u0443\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044f \u0445\u043e\u0442\u0431\u0430\u0440\u043e\u043c");
		BROADCAST.add("\u00a7f\u041d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0435 \u043f\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u0443\u0435\u0442 \u0410\u0443\u043a\u0446\u0438\u043e\u043d \u00a78- \u00a76/auc");
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