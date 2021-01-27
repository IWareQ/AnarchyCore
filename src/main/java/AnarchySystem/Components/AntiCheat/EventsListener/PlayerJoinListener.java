package AnarchySystem.Components.AntiCheat.EventsListener;

import AnarchySystem.Components.AntiCheat.AntiCheatAPI;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent event) {
        AntiCheatAPI.addDetect(event);
    }
}