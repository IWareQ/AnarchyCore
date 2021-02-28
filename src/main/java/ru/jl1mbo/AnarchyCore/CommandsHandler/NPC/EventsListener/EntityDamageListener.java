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
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CustomManager.Utils.CustomItemID;

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
				traderHopper.addItem(Item.get(
										 CustomItemID.TARANTULA_WEB).setLore("\n§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7!"));
				traderHopper.addItem(Item.get(
										 CustomItemID.SUN_WAND).setLore("\n§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7!"));
				traderHopper.addItem(Item.get(
										 CustomItemID.PIECE_ICE).setLore("\n§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7!"));
				traderHopper.addItem(Item.get(
										 CustomItemID.REMAINS_GUARDIAN).setLore("\n§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7!"));
				FakeInventoryAPI.openInventory((Player) damager, traderHopper);
			}
		}
	}
}