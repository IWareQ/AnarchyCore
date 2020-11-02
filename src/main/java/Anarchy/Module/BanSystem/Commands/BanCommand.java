
package Anarchy.Module.BanSystem.Commands;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import Anarchy.AnarchyMain;
import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.BanSystem.BanAPI;
import Anarchy.Module.Permissions.PermissionsAPI;
import FormAPI.Forms.Elements.CustomForm;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;

public class BanCommand extends Command {
	File dataFile = new File(AnarchyMain.datapath + "/bans.yml");
	Config BanList = new Config(dataFile, Config.YAML);

	public BanCommand() {
		super("bantest", "§r§l§fТестовый бан");
		this.setPermission("Command.Ban");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Ban")) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6| §r§fИспользование §7- /§6bantest §7(§3игрок§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6| §r§fИгрок §6" + args[0] + " §fне в сети§7, §fили уже забанен§7!");
				return true;
			}
			PlayerSession playerSession = PlayerSessionManager.getPlayerSession(target);
			List<String> timeBan = Arrays.asList("§62 §fДня", "§65 §fДней", "§610 §fДней", "§630 §fДней", "§6Навсегда");
			new CustomForm("§l§fБлокировка Игрок").addLabel("§l§6• §r§fИгрок§7: §6" + target.getName() + "\n§l§6• §r§fРанг§7: " + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")) + "\n").addInput("§l§6• §r§fПричина блокировки§7:").addDropDown("§l§6• §r§fВремя блокировки§7:", timeBan).send(player, (targetPlayer, form, data)-> {
				if (data == null) return;
				int secondsTime;
				if (data.get(2).equals("§62 §fДня")) {
					secondsTime = 172800;
				} else if (data.get(2).equals("§65 §fДней")) {
					secondsTime = 432000;
				} else if (data.get(2).equals("§610 §fДней")) {
					secondsTime = 864000;
				} else if (data.get(2).equals("§630 §fДней")) {
					secondsTime = 2592000;
				} else if (data.get(2).equals("§6Навсегда")) {
					secondsTime = -1;
				} else {
					secondsTime = 1;
				}
				player.sendMessage(AnarchyMain.PREFIX + "§fИгрок §6" + target.getName() + " §fбыл заблокирован§7!\n\n§l§6• §r§fПричина§7: §6" + (String)data.get(1) + "\n§l§6• §r§fЗабанен на§7: " + (String)data.get(2) + "\n§l§6• §r§fТех§7.§fинформация§7: §6" + secondsTime);
				BanAPI.banPlayer(player, target, (String)data.get(1), System.currentTimeMillis() / 1000 + secondsTime);
			});
		}
		return false;
	}
}