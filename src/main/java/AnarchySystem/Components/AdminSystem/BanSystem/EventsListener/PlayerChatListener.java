package AnarchySystem.Components.AdminSystem.BanSystem.EventsListener;

import AnarchySystem.Components.AdminSystem.BanSystem.BanSystemAPI;
import AnarchySystem.Components.AdminSystem.BanSystem.Utils.MuteUtils;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler()
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        MuteUtils muteUtils = BanSystemAPI.getMuteUtils(player.getName());
        if (BanSystemAPI.IsMuted(player.getName())) {
            if (muteUtils.getTime() < System.currentTimeMillis() / 1000L) {
                BanSystemAPI.removeMute(player.getName());
            } else {
                event.setCancelled(true);
                player.sendMessage("§l§c• §r§fТебя замутили§7! §fАдминистратор §fзакрыл тебе доступ к чату на §6" + BanSystemAPI.getRemainingTime(muteUtils.getTime()) + " §fпо причине §6" + muteUtils.getReason() + "§7!\n§fНо не расстраивайся§7, §fвсё наладится§7!");
            }
        }
    }
}