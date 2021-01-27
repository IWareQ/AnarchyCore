package AnarchySystem.Components.AntiCheat.EventsListener;

import AnarchySystem.Components.AdminSystem.BanSystem.BanSystemAPI;
import AnarchySystem.Components.AntiCheat.AntiCheatAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    @EventHandler()
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (AntiCheatAPI.checkToolBox(player)) {
            player.close("", "§r§l§fНа нашем сервер запрещены §6Читы§7!\n§r§l§fВы заблокированы на 1 час за попытку входа с читами");
            BanSystemAPI.addBan(player.getName(), "Попытка зайти с читами", "AntiCheat", 3600);
        }
    }
}