package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;

public class SpawnCommand extends Command {

	public SpawnCommand() {
		super("spawn", "§rТелепортироваться на спавн");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.getLevel().equals(WorldSystemAPI.Map)) {
				player.sendMessage("Запускаем перемещение...\nПрибытие через 4 сек");
				CooldownAPI.addTask(player, () -> {
					player.teleport(WorldSystemAPI.Spawn.getSafeSpawn());
				}, 4);
			} else {
				player.sendMessage("§l§7(§3Телепорт§7) §rС этого измерения §6запрещено §fтелепортироваться§7!");
			}
		}

		return false;
	}
}
