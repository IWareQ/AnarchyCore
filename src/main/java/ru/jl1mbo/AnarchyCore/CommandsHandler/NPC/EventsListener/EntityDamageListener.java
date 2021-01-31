package ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.EventsListener.Inventory.Hopper;
import ru.jl1mbo.AnarchyCore.Entity.NPC.VillagerNPC;
import ru.jl1mbo.AnarchyCore.Entity.NPC.WanderingTraderNPC;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.AuctionAPI;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;

public class EntityDamageListener implements Listener {

	@EventHandler()
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (event instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			if (entity instanceof VillagerNPC) {
				AuctionAPI.AUCTION_PAGE.put((Player) damager, 0);
				AuctionAPI.showAuction((Player) damager, true);
			} else if (entity instanceof WanderingTraderNPC) {
				Hopper traderHopper = new Hopper("§r§l§6Собиратель Артефактов");
				traderHopper.addItem(Item.get(Item.COBWEB, 0,
											  1).setCustomName("§r§6Паутина Таратнула").setLore("\n§r§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7!"));
				traderHopper.addItem(Item.get(Item.BLAZE_ROD, 0,
											  1).setCustomName("§r§6Жезл Солнца").setLore("\n§r§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7!"));
				traderHopper.addItem(Item.get(Item.ICE_FROSTED, 0,
											  1).setCustomName("§r§6Льдышка").setLore("\n§r§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7!"));
				traderHopper.addItem(Item.get(Item.SKULL, 0,
											  1).setCustomName("§r§6Череп Стража").setLore("\n§r§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7!"));
				FakeInventoryAPI.openInventory((Player) damager, traderHopper);
			}
		}
	}
}