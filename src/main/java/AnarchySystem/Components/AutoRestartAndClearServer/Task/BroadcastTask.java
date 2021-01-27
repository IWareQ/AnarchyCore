package AnarchySystem.Components.AutoRestartAndClearServer.Task;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

import java.util.ArrayList;

public class BroadcastTask extends Task {
    private static final ArrayList<String> BROADCAST = new ArrayList<>();
    private static final String PREFIX = "§l§1| §r";
    private static int TIMER = 5;
    private static int COUNT = 0;

    public static void register() {
        BROADCAST.add("§fВо §9ВКонтакте §fу нас есть беседа\n§l§1| §r§fЗаходи и находи новых друзей§7.\n§l§1| §rЧат во §9ВКонтакте §7-  §fvk§7.§fcc§7/§3avEklQ");
        BROADCAST.add("§fБудь в курсе всех последних новостей проекта§7!\n§l§1| §r§fПодпишись на нашу группу во §9ВКонтакте\n§l§1| §r§fНаше сообщество §9ВКонтакте §7- §fvk§7.§fcom§7/§3death§7.§3club");
        BROADCAST.add("§fОбязательно посетите Наш сайт §7- §3death§7-§3mc§7.§3online");
        BROADCAST.add("§fДля проверки границ территорий используйте обычную палку\n§l§1| §r§fДля создания региона§7, §fпоставьте блок привата\n§l§1| §r§fВсе блоки привата показанны на спавне§7!");
        BROADCAST.add("§fНе пропустите важные новости из нашей группы §9ВКонтакте §7- §fvk§7.§fcom§7/§3death§7.§3club");
        BROADCAST.add("§fПри совершении покупок на нашем сайте\n§l§1| §r§fВы помогаете в развитии Сервера§7!\n§l§1| §r§fСайт §7- §3death§7-§3mc§7.§3online");
        BROADCAST.add("§fИспользуйте §6/bar§f, для управления хотбаром");
        BROADCAST.add("§fНа сервере присутствует Аукцион §8- §6/auc");
    }

    public static String getBroadcast() {
        if (COUNT >= BROADCAST.size()) {
            COUNT = 0;
        }
        String broadcastText = BROADCAST.get(COUNT);
        COUNT++;
        return PREFIX + broadcastText;
    }

    @Override()
    public void onRun(int currentTick) {
        if (TIMER == 0) {
            TIMER = 5;
            Server.getInstance().broadcastMessage(getBroadcast());
        }
        TIMER--;
    }
}