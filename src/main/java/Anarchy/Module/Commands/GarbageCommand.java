package Anarchy.Module.Commands;

import Anarchy.Functions.FunctionsAPI;
import Anarchy.Module.Boss.EvokerBoss;
import Anarchy.Module.Boss.RavagerBoss;
import Anarchy.Module.Boss.SilverfishBoss;
import Anarchy.Module.Boss.SlimeBoss;
import Anarchy.Module.Boss.WitchBoss;
import FormAPI.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import nukkitcoders.mobplugin.entities.monster.flying.Wither;

public class GarbageCommand extends Command {
	private static int LIMIT = 400;
	private static int COUNT = 0;

	public GarbageCommand() {
		super("garbage", "§r§fОчистка дропа на Сервере", "", new String[] {"gb"});
		this.setPermission("Command.Garbage");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("args", new String[]{"get", "process", "start"})});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (args.length == 0) {
				SimpleForm simpleForm = new SimpleForm("§r§fОчистка Дропа");
				simpleForm.setContent("§r§l§6• §r§fБыстрые команды§7:\n\n§7/§6gb get §7- §fколличество дропа\n§7/§6gb process §7- §fпроверка нна превышение лимита\n§7/§6gb start §7- §fпринудительная очистка\n\nТекущий лимит дропа§7: §6" + LIMIT + " §fпредметов\nОбнаружено дропа§7: §6" + getGarbageCount() + " §fшт§7.");
				simpleForm.send(player);
				return true;
			}
			switch (args[0]) {
			case "get": {
				player.sendMessage("§l§7(§3Очистка§7) §r§fНа всем сервере обнаружено §6" + getGarbageCount() + " §fшт§7. §fдропа§7!");
			}
			break;

			case "process": {
				//TODO
			}
			break;

			case "start": {
				//TODO
			}
			break;

			default: {
				SimpleForm simpleForm = new SimpleForm("§r§fОчистка Дропа");
				simpleForm.setContent("§r§l§6• §r§fБыстрые команды§7:\n\n§7/§6gb get §7- §fколличество дропа\n§7/§6gb process §7- §fпроверка нна превышение лимита\n§7/§6gb start §7- §fпринудительная очистка\n\nТекущий лимит дропа§7: §6" + LIMIT + " §fпредметов\nОбнаружено дропа§7: §6" + getGarbageCount() + " §fшт§7.");
				simpleForm.send(player);
			}
			break;
			}
		}
		return false;
	}

	private int getGarbageCount() {
		COUNT = 0;
		for (Level level : Server.getInstance().getLevels().values()) {
			for (Entity entity : level.getEntities()) {
				if (!(entity instanceof Player) && !(entity instanceof Wither) && !(entity.getLevel().equals(FunctionsAPI.SPAWN)) && !(entity instanceof SilverfishBoss) && !(entity instanceof SlimeBoss) && !(entity instanceof EvokerBoss) && !(entity instanceof RavagerBoss) && !(entity instanceof WitchBoss)) {
					COUNT++;
				}
			}
		}
		return COUNT;
	}
}