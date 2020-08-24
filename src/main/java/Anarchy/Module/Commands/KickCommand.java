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
		Player kickPlayer = Server.getInstance().getPlayer(args[0]);
		String kickName = kickPlayer.getName();
		String playerName = player.getName();
		if (!(player instanceof Player)) {
			player.sendMessage("§fЭту команду можно использовать только в игре");
			return false;
		}
		if (!player.hasPermission("Command.Kick")) {
			return false;
		}
		if (args.length != 1) {
			player.sendMessage("§l§e| §r§fИспользование §7- §e/kick §7(§3игрок§7)");
			return true;
		}
		if (kickPlayer == null) {
			player.sendMessage("§fИгрок §e" + args[0] + " §7- §3Оффлайн");
			return true;
		}
		new CustomForm("§fВыгнать Игрока §3" + kickName).addLabel("Текст").addInput("Причина").addToggle("Отправить сообщение о кике в общий чат сервера §7•").send(player, (target, form, data) -> {
			if (data == null) return;
			kickPlayer.close("", "§l§c| §r§fВас кикнули с сервера§7! §l§e| §r§fПричина §7- §3" + data.get(1).toString());
			if ((boolean)data.get(2)) {
				Server.getInstance().broadcastMessage("§l§e| §r§fАдминистратор §3" + playerName + " §fвыгнал Игрока §3" + kickPlayer + " §fс сервера§7, §fпо причине §7- §3" + data.get(1).toString());
			}
		});
		return false;
	}
}