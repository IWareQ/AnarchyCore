package ru.jl1mbo.AnarchyCore.EventsListener;

import java.util.Random;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityEndCrystal;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3f;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CustomManager.Utils.CustomItemID;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

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
			PlayerInventory playerInventory = player.getInventory();
			if (playerInventory.getItemInHand() != null && playerInventory.getItemInHand().getId() == CustomItemID.GOLDEN_MONEY && entity.getLevel().equals(WorldSystemAPI.getSpawn())
					&& entity instanceof EntityEndCrystal) {
				if (Utils.rand(0, 100) < 15) {
					if (playerInventory.canAddItem(getRandomItems())) {
						Server.getInstance().getOnlinePlayers().values().forEach(players -> {
							entity.getLevel().addParticleEffect(new Vector3f(entity.getFloorX(), entity.getFloorY(), entity.getFloorZ()).add(0.5F, 1F, 0.5F), "minecraft:altar_win", -1L, entity.getLevel().getDimension(), players);
						});
						playerInventory.removeItem(Item.get(CustomItemID.GOLDEN_MONEY));
						playerInventory.addItem(getRandomItems());
						player.sendMessage("§l§7(§3Алтарь§7) §r§fСпасибо за §6монетку§7, §fдержи §6награду§7!");
					} else {
						Server.getInstance().getOnlinePlayers().values().forEach(players -> {
							entity.getLevel().addParticleEffect(new Vector3f(entity.getFloorX(), entity.getFloorY(), entity.getFloorZ()).add(0.5F, 1F, 0.5F), "minecraft:altar_win", -1L, entity.getLevel().getDimension(), players);
						});
						player.sendMessage("§l§7(§3Алтарь§7) §r§fВам повезло§7, §fно Ваш инвентарь был переполнен и из§7-§fза этого Ваша награда была удалена§7!");
						playerInventory.removeItem(Item.get(CustomItemID.GOLDEN_MONEY));
					}
				} else {
					player.sendMessage("§l§7(§3Алтарь§7) §r§fТебе §6не повезло§7, §fне растраивайся§7, §fв следующий раз повезет§7!");
					playerInventory.removeItem(Item.get(CustomItemID.GOLDEN_MONEY));
				}
				event.setCancelled(true);
			}
		}
	}
}