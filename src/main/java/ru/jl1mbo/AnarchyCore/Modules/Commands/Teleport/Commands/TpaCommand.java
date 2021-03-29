package ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.TeleportAPI;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Utils.TeleportUtils;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.CooldownAPI;

public class TpaCommand extends Command {

	public TpaCommand() {
		super("tpa", "§rОтправить запрос на телепортацию");
		this.commandParameters.clear();
		this.commandParameters.put("tpa", new CommandParameter[] {CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length != 1) {
				player.sendMessage("§l§6• §rИспользование §7- /§6tpa §7(§6игрок§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6• §rИгрок §6" + args[0] + " §fне в сети§7!");
				return true;
			}
			if (player.equals(target)) {
				player.sendMessage(TeleportAPI.PREFIX + "Вы пытаетесь §6телепортироваться §fк себе§7!");
				return true;
			}
			TeleportUtils tpUtils = new TeleportUtils(player, target);
			TeleportAPI.getTpaRequests().removeIf(tpUtils::equals);
			TeleportAPI.getTpaRequests().add(tpUtils);
			player.sendMessage(TeleportAPI.PREFIX + "Запрос на телепортицию к игроку §6" + target.getName() +
							   " §fуспешно отправлен§7!\n§l§6• §rЗапрос действует только §630 §fсекунд§7!");
			target.sendMessage(TeleportAPI.PREFIX + "Игрок §6" + player.getName() + " §fхочет телепортироваться к Вам§7!");
			target.sendMessage("§l§6• §r§7/§atpc §7- §fпринять запрос");
			CooldownAPI.addCooldown(player, this.getName(), 20);
		}
		return false;
	}
}