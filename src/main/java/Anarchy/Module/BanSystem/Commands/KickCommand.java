package Anarchy.Module.BanSystem.Commands;

import Anarchy.AnarchyMain;
import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.Permissions.PermissionsAPI;
import FormAPI.Forms.Elements.CustomForm;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class KickCommand extends Command {

	public KickCommand() {
		super("kick", "§r§fВыгнать с сервера");
		this.setPermission("Command.Kick");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Kick")) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6• §r§fИспользование §7- /§6kick, §7(§3игрок§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6• §r§fИгрок §6" + args[0] + " §fне в сети§7!");
				return true;
			}
			PlayerSession playerSession = PlayerSessionManager.getPlayerSession(target);
			CustomForm customForm = new CustomForm("§fВыгнать с сервера");
			customForm.addLabel("§l§6• §r§fИгрок§7: §6" + target.getName() + "\n§l§6• §r§fРанг§7: " + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")) + "\n");
			customForm.addInput("§l§6• §r§fПричина кика с сервера§7:");
			customForm.send(player, (targetPlayer, targetForm, data)-> {
				if (data == null) return;
				if ((String)data.get(1) == null) {
					player.sendMessage(AnarchyMain.PREFIX + "§fПричина кика не может быть пустой§7!");
				}
				player.sendMessage(AnarchyMain.PREFIX + "§fИгрок §6" + target.getName() + " §fбыл кикнут с сервера§7!\n§l§6• §r§fПричина§7: §6" + (String)data.get(1));
				target.close("", (String)data.get(1));
			});
		}
		return false;
	}
}