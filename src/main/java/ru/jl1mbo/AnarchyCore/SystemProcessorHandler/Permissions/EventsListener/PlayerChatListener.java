package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.EventsListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.level.Location;
import ru.jl1mbo.AnarchyCore.GameHandler.ClanManager.ClanManager;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;

public class PlayerChatListener implements Listener {
	private static final Map<Player, Long> COOLDOWN = new HashMap<>();
	private static final int CHAT_RADIUS = 70;

	@EventHandler()
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String playerMessage = event.getMessage();
		String displayName = (ClanManager.playerIsInClan(player.getName()) ? "§3" + ClanManager.getPlayerClan(player.getName()) + " " : "") + PermissionAPI.getAllGroups().get(PermissionAPI.getGroup(player.getName())).getGroupName() + " " + player.getName();
		Long cooldownTime = COOLDOWN.get(player);
		long nowTime = System.currentTimeMillis() / 1000;
		if (cooldownTime != null && cooldownTime > nowTime) {
			player.sendMessage("§l§6• §rВы слишком §6часто пишите §fв чат§7, §fпожалуйста§7, §fподождите несколько секунд§7!");
			event.setCancelled(true);
			return;
		}
		if (String.valueOf(playerMessage.charAt(0)).equals("#") && player.hasPermission("AdminChat")) {
			Set<CommandSender> adminChat = new HashSet<>();
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				if (players.hasPermission("AdminChat")) {
					adminChat.add(players);
				}
			}
			adminChat.add(new ConsoleCommandSender());
			event.setFormat("§7(§cA§7) " + displayName + " §8» §6" + playerMessage.substring(1).replaceAll("§", ""));
			event.setRecipients(adminChat);
		} else if (String.valueOf(playerMessage.charAt(0)).equals("!")) {
			event.setFormat("§7(§aG§7) " + displayName + " §8» §7" + playerMessage.substring(1).replaceAll("§", ""));
			COOLDOWN.put(player, nowTime + 2);
		} else {
			Set<CommandSender> playerChat = new HashSet<>();
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				if (players.getLevel() == player.getLevel() && players.distance(new Location(player.getX(), player.getY(), player.getZ(), players.getLevel())) <= CHAT_RADIUS) {
					playerChat.add(players);
				}
			}
			playerChat.add(new ConsoleCommandSender());
			event.setFormat("§7(§6L§7) " + displayName + " §8» §f" + playerMessage.replaceAll("§", ""));
			event.setRecipients(playerChat);
			COOLDOWN.put(player, nowTime + 2);
		}
	}
}