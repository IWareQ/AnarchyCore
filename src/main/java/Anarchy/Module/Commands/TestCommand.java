package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;

public class TestCommand extends Command {
	
	public TestCommand() {
		super("test", "§l§fТестовая команда");
		setPermission("Command.Test");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.Test")) {
			return false;
		}
		if (args.length == 0) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6test §31§7/§32§7/§33§7/§34");
			return true;
		}
		switch (args[0]) {
			case "1": 
			{
				Position position = player.getPosition();
				Entity entity = Entity.createEntity("SilverfishBoss", position);
				entity.setScale((float)5.0);
				entity.spawnToAll();
				player.sendMessage("§l§7(§3Боссы§7) §r§fбосс §6Чешуйница §fбыл призван§7!\n §fУрон §7- §35\n §fЗдоровья §7- §350 \n §fОпыт §7- §330\n §fДроп §7- §39 Железных слитков§7, §310-30 Железных самородков\n §fСложность §7- §3Легкий\n §fДальность ударов §7- §31 блок\n §fСкорость ударов §7- §3Стандартное");
			}
			break;
			
			case "2": 
			{
				player.sendMessage("Скоро");
			}
			break;
			
			case "3": 
			{
				player.sendMessage("Скоро");
			}
			break;
			
			case "4": 
			{
				player.sendMessage("Скоро");
			}
			break;
			
		}
		return false;
	}
}