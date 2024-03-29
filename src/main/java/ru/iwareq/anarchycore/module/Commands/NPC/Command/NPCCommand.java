package ru.iwareq.anarchycore.module.Commands.NPC.Command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import ru.contentforge.formconstructor.form.SimpleForm;

public class NPCCommand extends Command {

	public NPCCommand() {
		super("npc", "§rМеню NPC");
		this.setPermission("Command.NPC");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			player.teleport(new Position(player.getFloorX() + 0.5, player.getFloorY(), player.getFloorZ() + 0.5));
			SimpleForm simpleForm = new SimpleForm("Меню NPC");
			simpleForm.addContent("§l§6• §rВыберите нужного Вам §6NPC§7:");
			simpleForm.addButton("Барыга", (p, button) -> {
				Entity entity = Entity.createEntity("PiglinNPC", player.getPosition());
				if (entity != null) {
					entity.spawnToAll();
					player.sendMessage("§l§7(§3NPC§7) §r§f NPC §7«§6Барыга§7» §fуспешно поставлен§7!");
				}
			});
			simpleForm.addButton("Аукционер", (p, button) -> {
				Entity entity = Entity.createEntity("VillagerNPC", player.getPosition());
				if (entity != null) {
					entity.spawnToAll();
					player.sendMessage("§l§7(§3NPC§7) §r§f NPC §7«§6Аукционер§7» §fуспешно поставлен§7!");
				}
			});
			simpleForm.addButton("Собиратель Артефактов", (p, button) -> {
				Entity entity = Entity.createEntity("WanderingTraderNPC", player.getPosition());
				if (entity != null) {
					entity.spawnToAll();
					player.sendMessage("§l§7(§3NPC§7) §r§f NPC §7«§6Собиратель Артефактов§7» §fуспешно поставлен§7!");
				}
			});

			simpleForm.send(player);
		}
		return false;
	}
}