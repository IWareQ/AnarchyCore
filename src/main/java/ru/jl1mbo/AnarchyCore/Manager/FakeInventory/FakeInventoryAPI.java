package ru.jl1mbo.AnarchyCore.Manager.FakeInventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.scheduler.NukkitRunnable;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.Utils.FakeChests;

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
		}.runTaskLater(Main.getInstance(), 10);
	}

	public static void openDoubleChestInventory(Player player, Inventory inventory) {
		new NukkitRunnable() {

			@Override()
			public void run() {
				if (player != null && inventory != null && !fakeChests.getFakeInventory(player).isPresent()) {
					player.addWindow(inventory);
				}
			}
		}.runTaskLater(Main.getInstance(), 12);
	}

	public static void closeDoubleChestInventory(Player player, Inventory inventory) {
		new NukkitRunnable() {

			@Override()
			public void run() {
				if (player != null && inventory != null && fakeChests.getFakeInventory(player).isPresent()) {
					player.removeWindow(inventory);
				}
			}
		}.runTaskLater(Main.getInstance(), 10);
	}

	public static void closeInventory(Player player, Inventory inventory) {
		if (player != null && inventory != null && fakeChests.getFakeInventory(player).isPresent()) {
			player.removeWindow(inventory);
		}
	}
}