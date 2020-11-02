package Anarchy.Module.Commands;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Sound;

public class RepairCommand extends Command {
	public static Map<Player, Long> COOLDOWN = new HashMap<>();

	public RepairCommand() {
		super("repair", "§l§fПочинить предмет в руке§7/§fинвентаре");
		this.setPermission("Command.Repair");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Repair")) {
				return false;
			}
			Long cooldownTime = COOLDOWN.get(player);
			Long nowTime = System.currentTimeMillis() / 1000;
			if (cooldownTime != null && cooldownTime > nowTime) {
				player.sendMessage("§l§7(§3Задержка§7) §r§fСледующее использование будет доступно через §6" + (cooldownTime - nowTime) + " §fсек§7.");
				return false;
			}
			PlayerInventory inventory = player.getInventory();
			Item item = inventory.getItemInHand();
			if (args.length == 0) {
				if (!(item instanceof ItemTool) && !(item instanceof ItemArmor)) {
					player.sendMessage("§l§6| §r§fЧинить можно только §6Инструменты §fи §6Броню\n§l§6• §r§f");
					return false;
				}
				item.setDamage(0);
				inventory.setItemInHand(item);
				player.sendExperienceLevel(player.getExperienceLevel() - 2);
				player.sendMessage("§l§a| §r§fПредмет в руке починен§7!");
				player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
			}
			if (args.length == 1 && args[0].equals("all")) {
				if (player.getExperienceLevel() >= 2) {
					Map<Integer, Item> contents = player.getInventory().getContents();
					for (Item items : contents.values()) {
						if (items instanceof ItemTool || items instanceof ItemArmor) {
							if (items.getName().equals("§r§fЗлодейская кирка")) {
								player.sendMessage("§l§c• §r§fВ инвентаре найден предмет§7, §fкоторый невозможно починить§7!");
								return false;
							}
							items.setDamage(0);
							player.getInventory().setContents(contents);
						}
						player.sendMessage("§l§a| §r§fВсе предметы в Инвентаре успешно починены§7! (§6" + contents.size() + "§7)");
						player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
						COOLDOWN.put(player, nowTime + 10);
					}
				} else {
					player.sendMessage("§l§c• §r§fНе достаточно уровней§7, §fдля починки вещей§7!");
				}
			}
		}
		return false;
	}
}