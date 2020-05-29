package Anarchy.Module.Auth;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Task.HotbarTask;
import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class AuthEventsHandler implements Listener {
	private static Map<Integer, Integer> START_MONEY = new HashMap<>();

	public static void register() {
		START_MONEY.put(1, 150);
		START_MONEY.put(2, 200);
		START_MONEY.put(3, 250);
		START_MONEY.put(4, 300);
		START_MONEY.put(5, 350);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		String upperCase = playerName.toUpperCase();
		String ip = player.getAddress();
		String date = StringUtils.getDate();

		Integer accountID = SQLiteUtils.selectInteger("Users.db", "SELECT `Account_ID` FROM `USERS` WHERE UPPER(`Username`) = '" + upperCase + "';");
		if (accountID == -1) {
			SQLiteUtils.query("Users.db", "INSERT INTO `USERS` (`Username`) VALUES ('" + playerName + "');");
			SQLiteUtils.query("Auth.db", "INSERT INTO `AUTH` (`Username`, `IP_Reg`, `Date_Reg`) VALUES ('" + playerName + "', '" + ip + "', '" + date + "');");
		}

		player.sendMessage("§l§e| §r§fДобро пожаловать на §6Hall§fMine Project §7(§cАнархия§7)\n §fМы в §9ВК §7- §fvk.com/§6hall§fmines §l§e| §r§fНаш сайт §7- §6hall§fmines.ru");
		PlayerSessionManager.startPlayerSession(player);
		if (!player.hasPlayedBefore()) {
			Integer money = START_MONEY.get(PermissionsAPI.getGroup(player));
			if (money != null) {
				player.sendMessage(EconomyAPI.PREFIX + "§fДля быстрого старта Вам выдано §e" + money + " §7(" + PermissionsAPI.GROUPS.get(PermissionsAPI.getGroup(player)) + "§7)");
				EconomyAPI.addMoney(player, money);
			}
		}

		if (PlayerSessionManager.SCOREBOARD.contains(player.getName())) {
			HotbarTask.showScoreboard(player);
		}

		PermissionsAPI.updateTag(player);
		PermissionsAPI.updatePermissions(player);
		player.setCheckMovement(false);
		SQLiteUtils.query("Auth.db", "UPDATE `AUTH` SET `IP_Last` = '" + ip + "', `Date_Last` = '" + date + "' WHERE UPPER(`Username`) = '" + upperCase + "';");
		event.setJoinMessage("");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (PlayerSessionManager.hasPlayerSession(player)) {
			PlayerSessionManager.stopPlayerSession(player);
		}
		event.setQuitMessage("");
	}
}