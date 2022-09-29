package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;

import java.util.Arrays;

public class GarbageCommand extends Command {

	private static final int[] TRASH_ITEMS = {
			-179, // idk
			Item.STONE,
			Item.STONE,
			Item.GRASS,
			Item.DIRT,
			Item.COBBLE,
			Item.SAND,
			Item.MOSSY_STONE,
			Item.MYCELIUM,
			Item.RED_SANDSTONE_SLAB,
			Item.PODZOL
	};

	public GarbageCommand() {
		super("garbage", "§rОчистить инвентарь от мусора");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			PlayerInventory inventory = player.getInventory();
			inventory.getContents().values().forEach(item -> {
				if (this.containsInTrash(item.getId())) {
					inventory.remove(item);
				}
			});

			player.sendMessage("Инвентарь очищен");
		}

		return false;
	}

	private boolean containsInTrash(int itemId) {
		return Arrays.stream(TRASH_ITEMS).anyMatch(id -> id == itemId);
	}
}
