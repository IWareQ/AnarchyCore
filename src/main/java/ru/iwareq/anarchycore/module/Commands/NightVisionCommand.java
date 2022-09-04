package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.potion.Effect;

public class NightVisionCommand extends Command {

	public NightVisionCommand() {
		super("nightvision", "§rУправление ночным зрением", "", new String[]{"nv"});
		this.setPermission("Command.NightVision");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (!player.hasEffect(Effect.NIGHT_VISION)) {
				player.addEffect(Effect.getEffect(Effect.NIGHT_VISION).setAmplifier(Integer.MAX_VALUE).setDuration(Integer.MAX_VALUE).setVisible(false));
				player.sendMessage("§l§6• §rНочное зрение включено§7!");
			} else {
				player.removeEffect(Effect.NIGHT_VISION);
				player.sendMessage("§l§6• §rНочное зрение выключено§7!");
			}
		}
		return false;
	}
}