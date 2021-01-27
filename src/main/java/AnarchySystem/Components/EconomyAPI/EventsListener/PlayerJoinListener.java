package AnarchySystem.Components.EconomyAPI.EventsListener;

import AnarchySystem.Components.EconomyAPI.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!EconomyAPI.isRegister(player.getName())) {
            EconomyAPI.registerPlayer(player.getName());
        }
    }
}