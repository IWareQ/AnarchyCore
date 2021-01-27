package AnarchySystem.Manager.FakeInventory;

import AnarchySystem.Main;
import AnarchySystem.Manager.FakeInventory.EventsListener.BlockBreakListener;
import AnarchySystem.Manager.FakeInventory.Utils.FakeChests;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.NukkitRunnable;

public class FakeInventoryAPI {
    private static final FakeChests fakeChests = new FakeChests();
    
    public static void registerEvents() {
    	PluginManager pluginManager = Server.getInstance().getPluginManager();
    	pluginManager.registerEvents(new BlockBreakListener(), Main.getInstance());
    }

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