package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Sound;

public class PayCommand extends Command {

	public PayCommand() {
		super("pay", "§r§fПеревод монет");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("money", CommandParamType.INT, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (args.length < 2) {
				player.sendMessage("§l§6• §r§fИспользование §7- /§6pay §7(§3игрок§7) (§3сумма§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + args[0] + " §fне в сети§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			if (!StringUtils.isDouble(args[1]) || Double.parseDouble(args[1]) < 0) {
				player.sendMessage(EconomyAPI.PREFIX + "§fСумма может быть только положительным числом§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			Double money = EconomyAPI.myMoney(player);
			if (money < Double.parseDouble(args[1])) {
				player.sendMessage(EconomyAPI.PREFIX + "§fВам не хватает §6монет §fдля перевода§7.\n§l§6• §r§fВаш баланс§7: §6" + String.format("%.1f", money) + "");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return true;
			}
			player.sendMessage(EconomyAPI.PREFIX + "§fВы успешно перевели §6" + String.format("%.1f", Double.parseDouble(args[1])) + " §fИгроку §6" + player.getName());
			target.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fперевел Вам §6" + String.format("%.1f", Double.parseDouble(args[1])) + "");
			EconomyAPI.reduceMoney(player, Double.parseDouble(args[1]));
			EconomyAPI.addMoney(target, Double.parseDouble(args[1]));
		}
		return false;
	}
}