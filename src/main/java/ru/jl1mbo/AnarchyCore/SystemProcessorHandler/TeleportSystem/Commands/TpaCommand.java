package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Commands;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.TeleportAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Utils.TeleportUtils;

public class TpaCommand extends Command {
	private static Map<Player, Long> COOLDOWN = new HashMap<>();

	public TpaCommand() {
		super("tpa", "§rОтправить запрос на телепортацию");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length != 1) {
				player.sendMessage("§l§6• §rИспользование §7- /§6tpa §7(§6игрок§7)");
				return true;
			}
			Long cooldownTime = COOLDOWN.get(player);
			long time = System.currentTimeMillis() / 1000L;
			if (cooldownTime != null && cooldownTime > time) {
				player.sendMessage("§l§7(§3Задержка§7) §rСледующее использование будет доступно через §6" + (cooldownTime - time) + " §fсек§7.");
				return false;
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
			TeleportAPI.getTpaRequest().removeIf(tpUtils::equals);
			TeleportAPI.getTpaRequest().add(tpUtils);
			player.sendMessage(TeleportAPI.PREFIX + "Запрос на телепортицию к игроку §6" + target.getName() +
							   " §fуспешно отправлен§7!\n§l§6• §rЗапрос действует только §630 §fсекунд§7!");
			target.sendMessage(TeleportAPI.PREFIX + "Игрок §6" + player.getName() + " §fхочет телепортироваться к Вам§7!");
			target.sendMessage("§l§6• §r§7/§atpc §7- §fпринять запрос");
			COOLDOWN.put(player, time + 10);
		}
		return false;
	}
}