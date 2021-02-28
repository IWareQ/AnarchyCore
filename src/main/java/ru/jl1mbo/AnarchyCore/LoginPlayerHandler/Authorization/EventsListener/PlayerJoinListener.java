package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import ru.jl1mbo.AnarchyCore.GameHandler.ClanManager.ClanManager;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.AuthorizationAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!AuthorizationAPI.isRegister(player.getName())) {
			AuthorizationAPI.registerPlayer(player.getName(), player.getAddress(), Utils.getDate());
			Server.getInstance().getLogger().info("§l§7(§6Система§7) Игрок §6" + player.getName() + " §fне зарегистрирован§7! §fРегистрируем§7!");
		}
		if (!ClanManager.isRegister(player.getName())) {
			ClanManager.registerPlayer(player);
		}
		if (player.getSkin().getSkinData().height > 256 || player.getSkin().getSkinData().width > 256) {
			Server.getInstance().getLogger().alert("У игрока " + player.getName() + " слишком большой скин");
		}
		player.sendMessage("§l§6• §rДобро пожаловать на §3DEATH §fMC §7(§cАнархия§7)\n§l§6• §rМы в §9ВК §7- §fvk§7.§fcom§7/§6deathmc§7.§6club §l§6| §r§fНаш сайт §7- §6death§7-§6mc§7.§6online");
		AuthorizationAPI.playerTime.put(player.getName().toLowerCase(), System.currentTimeMillis() / 1000L);
		player.setCheckMovement(false);
		event.setJoinMessage("");
	}
}