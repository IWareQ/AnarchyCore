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
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;

public class PlayerChatListener implements Listener {
    private static final Map<Player, Long> COOLDOWN = new HashMap<>();
    private static final int CHAT_RADIUS = 70;

    @EventHandler()
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String playerMessage = event.getMessage();
        String displayName = PermissionAPI.GROUPS.get(PermissionAPI.getGroup(player.getName())) + player.getName();
        Long cooldownTime = COOLDOWN.get(player);
        long nowTime = System.currentTimeMillis() / 1000;
        if (cooldownTime != null && cooldownTime > nowTime) {
            player.sendMessage("§l§6• §r§fВы слишком часто пишите в чат§7, §fпожалуйста§7, §fподождите несколько секунд§7!");
            event.setCancelled(true);
            return;
        }
        if (String.valueOf(playerMessage.charAt(0)).equals("#")) {
            Set<CommandSender> adminPlayer = new HashSet<>();
            for (Player players : Server.getInstance().getOnlinePlayers().values()) {
                if (players.hasPermission("AdminChat")) {
                    adminPlayer.add(players);
                }
            }
            adminPlayer.add(new ConsoleCommandSender());
            event.setFormat("§7(§cA§7) " + displayName + " §8» §6" + playerMessage.substring(1).replaceAll("§", ""));
            event.setRecipients(adminPlayer);
            COOLDOWN.put(player, nowTime + 2);
        } else if (String.valueOf(playerMessage.charAt(0)).equals("!")) {
            event.setFormat("§7(§aG§7) " + displayName + " §8» §7" + playerMessage.substring(1).replaceAll("§", ""));
            COOLDOWN.put(player, nowTime + 2);
        } else {
            Set<CommandSender> players = new HashSet<>();
            for (Player playerChat : Server.getInstance().getOnlinePlayers().values()) {
                if (playerChat.getLevel() == player.getLevel() && playerChat.distance(new Location(player.getX(), player.getY(), player.getZ(), playerChat.getLevel())) <= CHAT_RADIUS) {
                    players.add(playerChat);
                }
            }
            players.add(new ConsoleCommandSender());
            event.setFormat("§7(§6L§7) " + displayName + " §8» §f" + playerMessage.replaceAll("§", ""));
            event.setRecipients(players);
            COOLDOWN.put(player, nowTime + 2);
        }
    }
}