package ru.iwareq.anarchycore.module.Commands.Teleport.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.Commands.Teleport.TeleportAPI;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;

public class RtpCommand extends Command {


	public RtpCommand() {
		super("rtp", "§rТелепортироваться в случайное место");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (player.getLevel().equals(WorldSystemAPI.Map) || player.getLevel().equals(WorldSystemAPI.Spawn)) {
				player.sendMessage("Запускаем рандомное перемещение...\nПрибытие через 5 сек");
				CooldownAPI.addTask(player, () -> {
					CooldownAPI.addCooldown(player, this.getName(), 20);
					WorldSystemAPI.findRandomPositionAndTp(WorldSystemAPI.Map, pos -> {
						player.teleport(pos);
						player.sendTitle("Телепортация§7...");
						player.getFoodData().setLevel(player.getFoodData().getLevel() - 3, 0F);
					});
				}, 5);
			} else {
				player.sendMessage(TeleportAPI.PREFIX + "С этого измерения §6запрещено §fтелепортироваться§7!");
			}
		}

		return false;
	}
}
