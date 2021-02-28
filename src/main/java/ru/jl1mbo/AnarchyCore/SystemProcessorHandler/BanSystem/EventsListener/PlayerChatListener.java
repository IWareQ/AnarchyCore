package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Utils.MuteUtils;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

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
				player.sendMessage("§l§c• §r§fТебя замутили§7! §fАдминистратор §fзакрыл тебе доступ к чату на §6" + Utils.getRemainingTime(
									   muteUtils.getTime()) + " §fпо причине §6" + muteUtils.getReason() + "§7!\n§fНо не расстраивайся§7, §fвсё наладится§7!");
			}
		}
		if (BanSystemAPI.IsBanned(player.getName())) {
			player.sendMessage("§l§6• §rАккаунт §6заблокирован§7!");
			event.setCancelled(true);
		}
	}
}