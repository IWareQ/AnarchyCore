package Anarchy.Module.Commands;

import FormAPI.Forms.Elements.CustomForm;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class KickCommand extends Command {
	
	public KickCommand() {
		super("kick", "Выгнать Игрока с Сервера");
		setPermission("Command.Check");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		String playerName = player.getName();
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.Kick")) {
			return false;
		}
		if (args.length != 1) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6kick §7(§3игрок§7)");
			return true;
		}
		Player kickPlayer = Server.getInstance().getPlayer(args[0]);
		if (kickPlayer == null) {
			player.sendMessage("§l§6| §r§fИгрок §6" + args[0] + " §7- §3Оффлайн");
			return true;
		}
		new CustomForm("§l§fВыгнать Игрока §3" + kickPlayer.getName()).addLabel("Текст").addInput("Причина").addToggle("Отправить сообщение о кике в общий чат сервера §7•").send(player, (target,form,data)->{
			if (data == null) return;
			kickPlayer.close("", "§l§6| §r§fВас кикнули с сервера§7! §l§6| §r§fПричина §7- §3" + data.get(1).toString());
			if ((boolean)data.get(2)) {
				Server.getInstance().broadcastMessage("§l§6| §r§fАдминистратор §3" + playerName + " §fвыгнал Игрока §6" + kickPlayer + " §fс сервера§7, §fпо причине §7- §3" + data.get(1).toString());
			}
		});
		return false;
	}
}