package Anarchy.Module.Commands;

import Anarchy.Module.Economy.EconomyAPI;
import FormAPI.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;

public class ShopCommand extends Command {
	
	public ShopCommand() {
		super("shop", "Магазин");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player player = (Player)commandSender;
		PlayerInventory playerInventory = player.getInventory();
		new SimpleForm("§l§fМагазин", "Текст").addButton("Уголь").send(player, (target, form, data)->{
			if (data == -1) return;
			if (data == 0) {
				Item item = Item.get(263, 0, 1);
				if (playerInventory.contains(item)) {
					playerInventory.removeItem(item);
					EconomyAPI.addMoney(player, 1);
					player.sendMessage("Вы успешно продали 1 уголь за 1-ну монету");
				} else {
					player.sendMessage("Не хватает 1-го угля");
				}
			}
		});
		return false;
	}
}