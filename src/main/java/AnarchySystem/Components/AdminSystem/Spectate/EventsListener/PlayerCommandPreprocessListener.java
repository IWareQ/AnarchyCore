package AnarchySystem.Components.AdminSystem.Spectate.EventsListener;

import AnarchySystem.Components.AdminSystem.Spectate.SpectateAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {

    @EventHandler()
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase().replaceAll(" ", "");
        if (SpectateAPI.isSpectate(player.getName())) {
            if (command.equals("/tpc")) {
                player.sendMessage(SpectateAPI.PREFIX + "§fЭта команда заблокированна в режиме слежки§7!");
            }
        }
    }
}