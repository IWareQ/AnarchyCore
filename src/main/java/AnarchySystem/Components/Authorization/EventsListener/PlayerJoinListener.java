package AnarchySystem.Components.Authorization.EventsListener;

import AnarchySystem.Components.Authorization.AuthorizationAPI;
import AnarchySystem.Utils.Utils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    public static Long startPlayerTime;

    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!AuthorizationAPI.isRegister(player.getName())) {
            AuthorizationAPI.registerPlayer(player.getName(), player.getAddress(), Utils.getDate());
            Server.getInstance().getLogger().info("§l§7(§6Система§7) §fИгрок §6" + player.getName() + " §fне зарегистрирован§7! §fРегистрируем§7!");
        }
        player.sendMessage("§l§6• §r§fДобро пожаловать на §3DEATH §fMC §7(§cАнархия§7)\n§l§6• §r§fМы в §9ВК §7- §fvk§7.§fcom§7/§6deathmc§7.§6club §l§6| §r§fНаш сайт §7- §6death§7-§6mc§7.§6online");
        startPlayerTime = System.currentTimeMillis();
        player.setCheckMovement(false);
        event.setJoinMessage("");
    }
}