package AnarchySystem.Components.Permissions.EventsListener;

import AnarchySystem.Components.Permissions.PermissionAPI;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent event) {
        PermissionAPI.updatePermissions(event.getPlayer());
        PermissionAPI.updateTag(event.getPlayer());
    }
}