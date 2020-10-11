
package Anarchy.Module.BanSystem.Commands;

import java.util.Arrays;
import java.util.List;

import Anarchy.AnarchyMain;
import Anarchy.Module.BanSystem.BanAPI;
import FormAPI.Forms.Elements.CustomForm;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class BanCommand extends Command {

	public BanCommand() {
		super("bantest", "Тест бан");
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
			List<String> days = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");
			List<String> hours = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24");
			List<String> minutes = Arrays.asList("0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60");
			new CustomForm("§l§fБлокировка Игрок §6" + target.getName()).addStepSlider("§l§fДней§6", days).addStepSlider("§l§fЧасов§6", hours).addStepSlider("§l§fМинут§6", minutes).addInput("§l§fПричина§7:", "Причину писать в числах, 1.1, 1.3 и т.д").send(player, (targetPlayer, form, data)-> {
				if (data == null) return;
				player.sendMessage(AnarchyMain.PREFIX + "§fИгрок §6" + target.getName() + " §fбыл успешно заблокирован§7!");
				String reason = (String)data.get(3);
				Long nowTime = System.currentTimeMillis() / 1000L;
				int day = (Integer.parseInt((String)data.get(0)) * 86400);
				int hour = (Integer.parseInt((String)data.get(1)) * 3600);
				int min;
				if(Integer.parseInt((String)data.get(2)) > 1){
					min = (Integer.parseInt((String)data.get(2)) * 60);
				} else {
					min = 60;
				}
				long banTime = nowTime + day + hour + min; 
				BanAPI.banPlayer(player, target, reason, banTime);
			});
		}
		return false;
	}
}