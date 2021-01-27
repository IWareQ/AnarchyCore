package AnarchySystem.Components.EventsListener;

import java.util.Random;

import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import AnarchySystem.Utils.Utils;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityEndCrystal;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import ru.dragonestia.ironlib.IronLib;

public class EntityDamageByEntityListener implements Listener {
	
	private static Item getRandomItems() {
		Integer[] items = new Integer[] {Item.END_CRYSTAL, Item.GOLDEN_PICKAXE, Item.TOTEM, Item.DRAGON_EGG, Item.GHAST_TEAR, Item.DIAMOND, Item.SPONGE, Item.GOLD_INGOT, Item.IRON_INGOT, Item.TNT, Item.GOLDEN_APPLE};
		int itemId = items[new Random().nextInt(11)];
		Item item;
		if (itemId == Item.GOLDEN_PICKAXE || itemId == Item.TOTEM || itemId == Item.DRAGON_EGG) {
			item = Item.get(itemId, 0, 1);
		} else {
			item = Item.get(itemId, 0, Utils.rand(0, 10));
		}
		return item;
	}

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			Item goldenMoney = IronLib.getInstance().getPrefabManager().getPrefab("golden_money").getItem(1);
			PlayerInventory playerInventory = player.getInventory();
			if (playerInventory.getItemInHand() != null && playerInventory.getItemInHand().equals(goldenMoney) && entity.getLevel().equals(WorldSystemAPI.getSpawn()) && entity instanceof EntityEndCrystal) {
				if (Utils.rand(0, 100) < 15) {
					if (playerInventory.canAddItem(getRandomItems())) {
						playerInventory.removeItem(goldenMoney);
						playerInventory.addItem(getRandomItems());
						player.sendMessage("§l§7(§3Алтарь§7) §r§fСпасибо за §6монетку§7, §fдержи §6награду§7!");
					} else {
						player.sendMessage("§l§7(§3Алтарь§7) §r§fВам повезло§7, §fно Ваш инвентарь был переполнен и из§7-§fза этого Ваша награда была удалена§7!");
						playerInventory.removeItem(goldenMoney);
					}
				} else {
					player.sendMessage("§l§7(§3Алтарь§7) §r§fТебе §6не повезло§7, §fне растраивайся§7, §fв следующий раз повезет§7!");
					playerInventory.removeItem(goldenMoney);
				}
				event.setCancelled(true);
			}
		}
	}
}