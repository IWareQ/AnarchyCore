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

public class MuteCommand extends Command {

	public MuteCommand() {
		super("mute", "§r§fБлокировка чата");
		this.setPermission("Command.Mute");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Mute")) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6• §r§fИспользование §7- /§6mute §7(§3игрок§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6• §r§fИгрок §6" + args[0] + " §fне в сети§7!");
				return true;
			}
			PlayerSession playerSession = PlayerSessionManager.getPlayerSession(target);
			List<String> timeMute = Arrays.asList("§61 §fМинута", "§65 §fМинут", "§610 §fМинут", "§615 §fМинут", "§620 §fМинут", "§625 §fМинут", "§630 §fМинут", "§635 §fМинут", "§640 §fМинут", "§645 §fМинут", "§650 §fМинут", "§655 §fМинут", "§61 §fЧас", "§6Тест время");
			CustomForm customForm = new CustomForm("§l§fБлокировка чата");
			customForm.addLabel("§l§6• §r§fИгрок§7: §6" + target.getName() + "\n§l§6• §r§fРанг§7: " + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")) + "\n");
			customForm.addInput("§l§6• §r§fПричина блокировки чата§7:");
			customForm.addDropDown("§l§6• §r§fВремя блокировки чата§7:", timeMute);
			customForm.send(player, (targetPlayer, targetForm, data)-> {
				if (data == null) return;
				int secondsTime;
				if (data.get(2).equals("§61 §fМинута")) {
					secondsTime = 60;
				} else if (data.get(2).equals("§65 §fМинут")) {
					secondsTime = 300;
				} else if (data.get(2).equals("§610 §fМинут")) {
					secondsTime = 600;
				} else if (data.get(2).equals("§615 §fМинут")) {
					secondsTime = 900;
				} else if (data.get(2).equals("§620 §fМинут")) {
					secondsTime = 1200;
				} else if (data.get(2).equals("§625 §fМинут")) {
					secondsTime = 1500;
				} else if (data.get(2).equals("§630 §fМинут")) {
					secondsTime = 1800;
				} else if (data.get(2).equals("§635 §fМинут")) {
					secondsTime = 2100;
				} else if (data.get(2).equals("§640 §fМинут")) {
					secondsTime = 2400;
				} else if (data.get(2).equals("§645 §fМинут")) {
					secondsTime = 2700;
				} else if (data.get(2).equals("§650 §fМинут")) {
					secondsTime = 3000;
				} else if (data.get(2).equals("§655 §fМинут")) {
					secondsTime = 3300;
				} else if (data.get(2).equals("§61 §fЧас")) {
					secondsTime = 3600;
				} else {
					secondsTime = 100;
				}
				if (BanSystemAPI.playerIsMuted(target.getName())) {
					player.sendMessage(AnarchyMain.PREFIX + "§fЧат Игрока §6" + target.getName() + " §fуже заблокирован§7!");
				}
				if ((String)data.get(1) == null) {
					player.sendMessage(AnarchyMain.PREFIX + "§fПричина блокировки чата не может быть пустой§7!");
				}
				player.sendMessage(AnarchyMain.PREFIX + "§fЧат Игрока §6" + target.getName() + " §fбыл заблокирован§7!\n\n§l§6• §r§fПричина§7: §6" + (String)data.get(1) + "\n§l§6• §r§fПериод§7: " + (String)data.get(2));
				BanSystemAPI.mutePlayer(target.getName(), (String)data.get(1), player.getName(), secondsTime);
			});
		}
		return false;
	}
}