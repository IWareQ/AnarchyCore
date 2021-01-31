package ru.jl1mbo.AnarchyCore.GameHandler.ExperienceBottle.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import ru.jl1mbo.AnarchyCore.GameHandler.ExperienceBottle.ExperienceBottleAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.ExperienceBottle.Inventory.Chest;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class PlayerInteractListener implements Listener {

	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Item item = event.getItem();
		if (block != null && block.getId() == Block.BOOKSHELF && block.getLevel().equals(WorldSystemAPI.getSpawn())) {
			Chest chest = new Chest("§r§fОбмен Опыта");
			chest.setItem(11, Item.get(Item.EXPERIENCE_BOTTLE).setCustomName("§r§fБутыль Опыта §7(§630 LVL§7) (§61395 EXP§7)").setLore("\n§r§l§6• §r§fУ Вас §6" +
						  ExperienceBottleAPI.convert(
							  player) + " §fопыта§7!"));
			chest.setItem(13, Item.get(Item.EXPERIENCE_BOTTLE).setCustomName("§r§fБутыль Опыта §7(§640 LVL§7) (§62921 EXP§7)").setLore("\n§r§l§6• §r§fУ Вас §6" +
						  ExperienceBottleAPI.convert(
							  player) + " §fопыта§7!"));
			chest.setItem(15, Item.get(Item.EXPERIENCE_BOTTLE).setCustomName("§r§fБутыль Опыта §7(§660 LVL§7) (§68672 EXP§7)").setLore("\n§r§l§6• §r§fУ Вас §6" +
						  ExperienceBottleAPI.convert(
							  player) + " §fопыта§7!"));
			FakeInventoryAPI.openInventory(player, chest);
		}
		if (item != null && item.getId() == Item.EXPERIENCE_BOTTLE && item.getDamage() > 0) {
			item.setCount(item.getCount() - 1);
			player.getInventory().setItemInHand(item);
			player.addExperience(item.getDamage());
			event.setCancelled(true);
		}
	}
}