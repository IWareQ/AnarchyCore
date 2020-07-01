package Anarchy.Module.Commands;

import FormAPI.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;

public class ExchangerCommand extends Command {
	
	public ExchangerCommand() {
		super("exchanger", "Обменщик");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player player = (Player)commandSender;
		PlayerInventory playerInventory = player.getInventory();
		new SimpleForm("§l§fОбменщик", "Данная возможность обмена позволяет получить желаемый §6предмет §fили §6ресурс §fза определённую плату§7. §fДля совершения обмена необходимые ресурсы должны быть в §6Инвентаре").addButton("§63 §fПороха\n§fза §e1 §fИзумруд").addButton("§61 §fЭлитра\n§fза §e32 §fИзумруда").addButton("§61 §fШалкер\n§fза §e22 §fАлмаза и §e1 §fсундук").addButton("§61 §fЗачарованное Яблоко\n§fза §e5 §fЗолотых блоков и §e16 §fЗолотых яблок").addButton("§61 §fТотем Бессмертия\n§fза §e2 §fАдских звезды").addButton("§66 §fИзумрудной руды\n§fза §e22 §fАлмаза").addButton("§612 §fАлмазов\n§fза §e48 §fЗолота").addButton("§627 §fЗолота\n§fза §e64 §fЖелеза").addButton("§632 §fЖелеза\n§fза §e96 §fУгля").addButton("§616 §fГубки\n§fза §e10 §fИзумрудов").send(player, (target, form, data)->{
			if (data == -1) return;
			if (data == 0) {
				Item itemGive = Item.get(388, 0, 1);
				Item itemTake = Item.get(289, 0, 3);
				if (playerInventory.contains(itemGive)) {
					playerInventory.addItem(itemTake);
					playerInventory.removeItem(itemGive);
					player.sendMessage("§l§a| §r§fВы §6успешно §fобменяли §e1 §fИзумруд на §e3 §fПороха§7!");
				} else {
					player.sendMessage("§l§c| §r§fНе хватает §e1§7-§fго Изумруда§7!");
				}
			}
			if (data == 1) {
				Item itemGive = Item.get(388, 0, 32);
				Item itemTake = Item.get(444, 0, 1);
				if (playerInventory.contains(itemGive)) {
					playerInventory.addItem(itemTake);
					playerInventory.removeItem(itemGive);
					player.sendMessage("§l§a| §r§fВы §6успешно §fобменяли §e32 §fИзумруда на §e1§7-§fну §fЭлитру§7!");
				} else {
					player.sendMessage("§l§c| §r§fНе хватает §e32§7-§fух Изумрудов§7!");
				}
			}
			if (data == 2) {
				Item itemGive = Item.get(264, 0, 22);
				Item itemGiveTwo = Item.get(54, 0, 1);
				Item itemTake = Item.get(218, 0, 1);
				if (playerInventory.contains(itemGive) && playerInventory.contains(itemGiveTwo)) {
					playerInventory.addItem(itemTake);
					playerInventory.removeItem(itemGive);
					playerInventory.removeItem(itemGiveTwo);
					player.sendMessage("§l§a| §r§fВы §6успешно §fобменяли §e22 §fАлмаза и §e1 §fСундук на §e1 §fШалкеровый ящик§7!");
				} else {
					player.sendMessage("§l§c| §r§fНе хватает §e22§7-§fух Алмазов и §e1§7-§fго Сундука§7!");
				}
			}
			if (data == 3) {
				Item itemGive = Item.get(41, 0, 5);
				Item itemGiveTwo = Item.get(322, 0, 16);
				Item itemTake = Item.get(466, 1, 1);
				if (playerInventory.contains(itemGive) && playerInventory.contains(itemGiveTwo)) {
					playerInventory.addItem(itemTake);
					playerInventory.removeItem(itemGive);
					playerInventory.removeItem(itemGiveTwo);
					player.sendMessage("§l§a| §r§fВы §6успешно §fобменяли §e5 §fЗолотых блоков и §e16§7-§fти Золотых Яблок на §e1§7-§fно §fЗачарованное Яблоко§7!");
				} else {
					player.sendMessage("§l§c| §r§fНе хватает §e5 §fЗолотых блоков и §e16§7-§fти Золотых Яблок§7!");
				}
			}
			if (data == 4) {
				Item itemGive = Item.get(399, 0, 2);
				Item itemTake = Item.get(450, 0, 1);
				if (playerInventory.contains(itemGive)) {
					playerInventory.addItem(itemTake);
					playerInventory.removeItem(itemGive);
					player.sendMessage("§l§a| §r§fВы §6успешно §fобменяли §e2§7-§fве Адских звезды на §e1 §fТотем Бессмертия§7!");
				} else {
					player.sendMessage("§l§c| §r§fНе хватает §e2§7-§fве Адских звезды§7!");
				}
			}
			if (data == 5) {
				Item itemGive = Item.get(264, 0, 22);
				Item itemTake = Item.get(129, 0, 6);
				if (playerInventory.contains(itemGive)) {
					playerInventory.addItem(itemTake);
					playerInventory.removeItem(itemGive);
					player.sendMessage("§l§a| §r§fВы §6успешно §fобменяли §e22 §fАлмаза на §e6 §fИзумрудов§7!");
				} else {
					player.sendMessage("§l§c| §r§fНе хватает §e22§7-§fух Алмазов§7!");
				}
			}
			if (data == 6) {
				Item itemGive = Item.get(266, 0, 48);
				Item itemTake = Item.get(56, 0, 12);
				if (playerInventory.contains(itemGive)) {
					playerInventory.addItem(itemTake);
					playerInventory.removeItem(itemGive);
					player.sendMessage("§l§a| §r§fВы §6успешно §fобменяли §e48 §fЗолота на §e12 §fАлмазов§7!");
				} else {
					player.sendMessage("§l§c| §r§fНе хватает §e48 §fЗолота§7!");
				}
			}
			if (data == 7) {
				Item itemGive = Item.get(265, 0, 64);
				Item itemTake = Item.get(14, 0, 27);
				if (playerInventory.contains(itemGive)) {
					playerInventory.addItem(itemTake);
					playerInventory.removeItem(itemGive);
					player.sendMessage("§l§a| §r§fВы §6успешно §fобменяли §e64 §fЖелеза на §e27 §fЗолота§7!");
				} else {
					player.sendMessage("§l§c| §r§fНе хватает §e64 §fЖелеза§7!");
				}
			}
			if (data == 8) {
				Item itemGive = Item.get(263, 0, 96);
				Item itemTake = Item.get(15, 0, 32);
				if (playerInventory.contains(itemGive)) {
					playerInventory.addItem(itemTake);
					playerInventory.removeItem(itemGive);
					player.sendMessage("§l§a| §r§fВы §6успешно §fобменяли §e96 §fУгля на §e64 §fЖелеза§7!");
				} else {
					player.sendMessage("§l§c| §r§fНе хватает §e96 §fУгля§7!");
				}
			}
			if (data == 9) {
				Item itemGive = Item.get(388, 0, 10);
				Item itemTake = Item.get(19, 0, 16);
				if (playerInventory.contains(itemGive)) {
					playerInventory.addItem(itemTake);
					playerInventory.removeItem(itemGive);
					player.sendMessage("§l§a| §r§fВы §6успешно §fобменяли §e10 §fИзумрудов на §e16 §fГубки§7!");
				} else {
					player.sendMessage("§l§c| §r§fНе хватает §e10§7-§fти §fИзумрудов§7!");
				}
			}
		});
		return false;
	}
}