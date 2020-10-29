package Anarchy.Module.Commands.NPC;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

public class NPCCommand extends Command {

	public NPCCommand() {
		super("npc", "§r§l§fПоставить NPC");
		this.setPermission("Command.NPC");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("npc", new String[]{"auctioner", "huckster"})});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.NPC")) {
				return false;
			}
			if (args.length == 0) {
				player.sendMessage("§l§6• §r§fИспользование §7- /§6npc §3auctioner§7|§3huckster");
				return true;
			}
			switch (args[0]) {
			case "auctioner": {
				Entity entity = Entity.createEntity("VillagerNPC", player.chunk, getNBT(player));
				entity.setNameTag("§l§6Аукционер");
				entity.setNameTagAlwaysVisible(true);
				entity.setScale((float)2.0);
				entity.spawnToAll();
				player.sendMessage("§l§6• §r§fNPC §7«§6Аукционер§7» §fуспешно поставлен§7!");
			}
			break;
			case "huckster": {
				Entity entity = Entity.createEntity("PiglinBruteNPC", player.chunk, getNBT(player));
				entity.setNameTag("§l§6Барыга");
				entity.setNameTagAlwaysVisible(true);
				entity.spawnToAll();
				player.sendMessage("§l§6• §r§fNPC §7«§6Барыга§7» §fуспешно поставлен§7!");
			}
			break;
			}
		}
		return false;
	}

	private CompoundTag getNBT(Player player) {
		CompoundTag compoundTag = new CompoundTag()
		.putList(new ListTag<>("Pos")
				 .add(new DoubleTag("", player.x))
				 .add(new DoubleTag("", player.y))
				 .add(new DoubleTag("", player.z)))
		.putList(new ListTag<DoubleTag>("Motion")
				 .add(new DoubleTag("", 0))
				 .add(new DoubleTag("", 0))
				 .add(new DoubleTag("", 0)))
		.putList(new ListTag<FloatTag>("Rotation")
				 .add(new FloatTag("", (float) player.getYaw()))
				 .add(new FloatTag("", (float) player.getPitch())))
		.putBoolean("Invulnerable", true)
		.putBoolean("npc", true);
		return compoundTag;
	}
}