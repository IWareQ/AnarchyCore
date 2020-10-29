package Anarchy.Module.BanSystem.Commands;

import java.io.File;

import Anarchy.AnarchyMain;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

public class MuteCommand extends Command {
	File dataFile = new File(AnarchyMain.datapath + "/Mute.yml");
	Config mute = new Config(dataFile, Config.YAML);

	public MuteCommand() {
		super("mute", "§r§l§fВыдать мут игроку");
		this.setPermission("Command.Mute");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Repair")) {
				return false;
			}
			if (args.length < 1) {
				player.sendMessage("§l§6| §r§fИспользование §7- /§6mute §7(§3игрок§7) (§3время§7) §3sec§7/§3min§7/§3hour§7/§3day");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6| §r§fИгрок §6" + args[0] + " §fне в сети§7, §fили уже забанен§7!");
				return true;
			}
			if (!StringUtils.isInteger(args[1])) {
				player.sendMessage("§l§6• §r§fВремя может быть только положительным числом§7!");
			} else {
				long times = System.currentTimeMillis() / 1000;
				long timings;
				if (args.length == 3) {
					if (args[2].equalsIgnoreCase("sec")) {
						timings = times + Integer.parseInt(args[1]);
					} else if (args[2].equalsIgnoreCase("min")) {
						timings = times + Integer.parseInt(args[1]) * 60;
					} else if (args[2].equalsIgnoreCase("hour")) {
						timings = times + Integer.parseInt(args[1]) * 3600;
					} else if (args[2].equalsIgnoreCase("day")) {
						timings = times + Integer.parseInt(args[1]) * 86400;
					} else {
						player.sendMessage("§l§6• §r§fУкажите правильное получение времени§7!");
						return true;
					}
				} else {
					timings = times + 1 * 60;
				}
				if (timings > (times + 30 * 86400)) {
					sender.sendMessage(TextFormat.colorize("&7[&aMute&7] &cВы не можете замутить игрока больше чем на 30 дней!"));
				} else {
					mute.set(target.getName().toLowerCase(), timings);
					mute.save();
					long seconds = (timings % 60);
					long minutes = (timings % 3600) / 60;
					long hours = (timings % (3600 * 24) / 3600);
					long days = (timings / (3600 * 24));
					String timemute = days + " §fд§7. §6" + hours + " §fч§7. §6" + minutes + " §fмин§7. §6" + seconds + " §fсек§7.";
					target.sendMessage("Ты получил мут на " + timemute);
					player.sendMessage("Игрок " + target.getName() + " был замучен на " + timemute);
				}
			}
		}
		return false;
	}
}