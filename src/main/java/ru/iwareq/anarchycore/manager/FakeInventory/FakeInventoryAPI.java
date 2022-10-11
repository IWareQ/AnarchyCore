package ru.iwareq.anarchycore.manager.FakeInventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.scheduler.NukkitRunnable;
import ru.iwareq.anarchycore.manager.FakeInventory.Utils.FakeChests;
import ru.iwareq.anarchycore.AnarchyCore;

public class FakeInventoryAPI {

	private static final FakeChests fakeChests = new FakeChests();

	public static void openInventory(Player player, Inventory inventory) {
		new NukkitRunnable() {

			@Override()
			public void run() {
				if (player != null && inventory != null && !fakeChests.getFakeInventory(player).isPresent()) {
					player.addWindow(inventory);
				}
			}
		}.runTaskLater(AnarchyCore.getInstance(), 10);
	}

	public static void openDoubleChestInventory(Player player, Inventory inventory) {
		new NukkitRunnable() {

			@Override()
			public void run() {
				if (player != null && inventory != null && !fakeChests.getFakeInventory(player).isPresent()) {
					player.addWindow(inventory);
				}
			}
		}.runTaskLater(AnarchyCore.getInstance(), 12);
	}

	public static void closeDoubleChestInventory(Player player, Inventory inventory) {
		new NukkitRunnable() {

			@Override()
			public void run() {
				if (player != null && inventory != null && fakeChests.getFakeInventory(player).isPresent()) {
					player.removeWindow(inventory);
				}
			}
		}.runTaskLater(AnarchyCore.getInstance(), 10);
	}

	public static void closeInventory(Player player, Inventory inventory) {
		if (player != null && inventory != null && fakeChests.getFakeInventory(player).isPresent()) {
			player.removeWindow(inventory);
		}
	}
}