package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.potion.Effect;

public class NightVisionCommand extends Command {

	public NightVisionCommand() {
		super("nightvision", "§r§fВключить ночное зрение", "", new String[] {"nv"});
		this.setPermission("Command.NightVision");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.NightVision")) {
				return false;
			}
			player.addEffect(Effect.getEffect(Effect.NIGHT_VISION).setAmplifier(0).setDuration(9999999 * 20).setVisible(false));
			player.sendMessage("§l§6• §r§fНочное зрение включено§7!");
		}
		return false;
	}
}