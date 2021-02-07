package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Utils.BanUtils;

public class PlayerPreLoginListener implements Listener {

    @EventHandler()
    public void onPlayerPreLogin(PlayerPreLoginEvent event) {
        Player player = event.getPlayer();
        BanUtils banUtils = BanSystemAPI.getBanUtils(player.getName());
        if (BanSystemAPI.IsBanned(player.getName())) {
            if (banUtils.getTime() < System.currentTimeMillis() / 1000L) {
                BanSystemAPI.removeBan(player.getName());
            } else {
                player.close("", "§l§fУвы§7, §fно Вас §6временно §fзаблокировали§7!\n§fПричина блокировки§7: §6" + banUtils.getReason() + "\n§fРазбан через§7: §6" + BanSystemAPI.getRemainingTime(banUtils.getTime()));
            }
        }
    }
}