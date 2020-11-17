package Anarchy.Module.BanSystem.Commands;

import java.util.Arrays;
import java.util.List;

import Anarchy.AnarchyMain;
import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.BanSystem.BanSystemAPI;
import Anarchy.Module.Permissions.PermissionsAPI;
import FormAPI.Forms.Elements.CustomForm;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class BanCommand extends Command {

	public BanCommand() {
		super("ban", "§r§fБлокировка аккаунта");
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
				player.sendMessage("§l§6• §r§fИспользование §7- /§6ban §7(§3игрок§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6• §r§fИгрок §6" + args[0] + " §fне в сети§7!");
				return true;
			}
			PlayerSession playerSession = PlayerSessionManager.getPlayerSession(target);
			List<String> timeBan = Arrays.asList("§62 §fДня", "§65 §fДней", "§610 §fДней", "§630 §fДней", "§6Навсегда");
			CustomForm customForm = new CustomForm("§l§fБлокировка аккаунта");
			customForm.addLabel("§l§6• §r§fИгрок§7: §6" + target.getName() + "\n§l§6• §r§fРанг§7: " + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")) + "\n");
			customForm.addInput("§l§6• §r§fПричина блокировки аккаунта§7:");
			customForm.addDropDown("§l§6• §r§fВремя блокировки аккаунта§7:", timeBan);
			customForm.send(player,(targetPlayer, targetForm, data)-> {
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
				if (BanSystemAPI.playerIsBanned(target.getName())) {
					player.sendMessage(AnarchyMain.PREFIX + "§fИгрок §6" + target.getName() + " §fуже заблокирован§7!");
				}
				if ((String)data.get(1) == null) {
					player.sendMessage(AnarchyMain.PREFIX + "§fПричина блокировки не может быть пустой§7!");
				}
				player.sendMessage(AnarchyMain.PREFIX + "§fИгрок §6" + target.getName() + " §fбыл заблокирован§7!\n§l§6• §r§fПричина§7: §6" + (String)data.get(1) + "\n§l§6• §r§fПереод§7: " + (String)data.get(2));
				AnarchyMain.sendMessageToChat("🔒Блокировка аккаунта\n\nИгрок: " + target.getName() + "\nАдминистратор: " + player.getName() + "\nПричина: " + (String)data.get(1) + "\nПериод: " + (String)data.get(2), 2000000001);
				BanSystemAPI.banPlayer(target.getName(), (String)data.get(1), player.getName(), secondsTime);
			});
		}
		return false;
	}
}