package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;

public class TestCommand extends Command {

	public TestCommand() {
		super("test", "§l§fТестовая команда");
		this.setPermission("Command.Test");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Test")) {
				return false;
			}
			if (args.length == 0) {
				player.sendMessage("§l§6| §r§fИспользование §7- /§6test §31§7/§32§7/§33§7/§34§7/§35");
				return true;
			}
			switch (args[0]) {
			case "1": {
				String device = player.getLoginChainData().getDeviceModel();
				String brand = device.split("\\s+")[0];
				player.sendMessage(brand + "\n\n" + brand.toUpperCase());
				if (!brand.equals(brand.toUpperCase())) {
					player.sendMessage("Ты с ToolBox");
				} else {
					player.sendMessage("ты не с ToolBox");
				}
			}
			break;

			case "2": {
				Position position = player.getPosition();
				Entity entity = Entity.createEntity("RavagerBoss", position);
				entity.setScale((float)1.0);
				entity.setNameTag("Зверь");
				entity.setNameTagAlwaysVisible();
				entity.spawnToAll();
				player.sendMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY()
								   + "§7, §6" + entity.getFloorZ());
			}
			break;

			case "3": {
				Position position = player.getPosition();
				Entity entity = Entity.createEntity("WitchBoss", position);
				entity.setScale((float)2.0);
				entity.setNameTag("Алхимик");
				entity.setNameTagAlwaysVisible();
				entity.spawnToAll();
				player.sendMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY()
								   + "§7, §6" + entity.getFloorZ());
			}
			break;

			case "4": {
				Position position = player.getPosition();
				Entity entity = Entity.createEntity("SlimeBoss", position);
				entity.setNameTag("Слизьняк");
				entity.setNameTagAlwaysVisible();
				entity.spawnToAll();
				player.sendMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY()
								   + "§7, §6" + entity.getFloorZ());
			}
			break;

			case "5": {
				Position position = player.getPosition();
				Entity entity = Entity.createEntity("EvokerBoss", position);
				entity.setScale((float)2.0);
				entity.setNameTag("Колдун");
				entity.setNameTagAlwaysVisible();
				entity.spawnToAll();
				player.sendMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY()
								   + "§7, §6" + entity.getFloorZ());
			}
			break;
			
			case "6": {
				for (int x = -1000; x <= 1000; x++) {
					for (int z = -1000; z <= 1000; z++) {
						player.getLevel().setBlock(new Position(x, 0, 1000), Block.get(212), false);
						player.getLevel().setBlock(new Position(x, 0, -1000), Block.get(212), false);
						player.getLevel().setBlock(new Position(-1000, 0, z), Block.get(212), false);
						player.getLevel().setBlock(new Position(1000, 0, z), Block.get(212), false);
					}
				}
				player.sendMessage("Великая стена успешно построенна :)");
			}
			break;
			}
		}
		return false;
	}
}