package Anarchy.Module.BanSystem;

import java.io.File;

import Anarchy.AnarchyMain;
import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.utils.Config;

public class BanAPI {


	public static void banPlayer(Player player, Player target, String reason, Long banTime) {
		File dataFile = new File(AnarchyMain.datapath + "/bans.yml");
		Config config = new Config(dataFile, Config.YAML);
		if (!config.exists(target.getName())) {
			target.close("", "§l§fУвы§7, §fно Вас §6временно §fзаблокировали§7!\n§fВас заблокировал§7: §6" + player.getName() + "\n§fПричина блокировки§7: §6" + reason + "\n§fРазбан через§7: §6" + banTime);
			config.set(target.getName(), new Object[] {player.getName(), reason, banTime});
			config.save();
		} else {
			player.sendMessage(AnarchyMain.PREFIX + "§fИгрок §6" + target.getName() + " §fуже заблокирован§7!");
		}
	}
	public static void unbanPlayer(Player player, Player target, String reason, Long banTime) {
		SQLiteUtils.query("BanList.db", "INSERT INTO BANPLAYERS (Username, Banned, Reason, banTime) VALUES ('" + target.getName() + "', '" + player.getName() + "', '" + reason + "', '" + banTime + "');");
	}
}