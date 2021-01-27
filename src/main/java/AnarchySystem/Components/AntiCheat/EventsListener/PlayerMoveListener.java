package AnarchySystem.Components.AntiCheat.EventsListener;

import AnarchySystem.Components.AntiCheat.AntiCheatAPI;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler()
    public void onPlayerMove(PlayerMoveEvent event) {
        AntiCheatAPI.checkFly(event);
    }
}