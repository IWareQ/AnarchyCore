package ru.jl1mbo.AnarchyCore.Modules.Cooldown;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.Utils.Cooldown;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

import java.util.LinkedList;
import java.util.List;

public class CooldownEventsListener implements Listener {

	@EventHandler()
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String command = event.getMessage().toLowerCase().substring(1);
		List<Cooldown> cooldownList = new LinkedList<>(CooldownAPI.getCooldowns());
		cooldownList.removeIf(cooldown -> !cooldown.getPlayer().equals(player));
		for (Cooldown cooldown : cooldownList) {
			if (cooldown.getCommand().contains(command)) {
				player.sendMessage("§l§7(§3Задержка§7) §rСледующее §6использование §fбудет доступно через §6" + Utils.getRemainingTime(cooldown.getTime()));
				event.setCancelled(true);
			}
		}
	}
}