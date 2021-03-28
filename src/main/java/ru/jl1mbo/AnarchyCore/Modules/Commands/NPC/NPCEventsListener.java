package ru.jl1mbo.AnarchyCore.Modules.Commands.NPC;

import java.util.Arrays;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector2;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import ru.jl1mbo.AnarchyCore.Entity.NPC.VillagerNPC;
import ru.jl1mbo.AnarchyCore.Entity.NPC.WanderingTraderNPC;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Modules.Auction.AuctionAPI;
import ru.jl1mbo.AnarchyCore.Modules.Commands.NPC.Inventory.NPCHopper;
import ru.jl1mbo.AnarchyCore.Modules.CustomRecipes.Utils.CustomItemID;

public class NPCEventsListener implements Listener {

	@EventHandler()
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (event instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			if (entity instanceof VillagerNPC) {
				AuctionAPI.AUCTION_PAGE.put((Player) damager, 0);
				AuctionAPI.showAuction((Player) damager, true);
			} else if (entity instanceof WanderingTraderNPC) {
				NPCHopper traderHopper = new NPCHopper("§6Собиратель Артефактов");
				traderHopper.addItem(Item.get(
										 CustomItemID.TARANTULA_WEB).setLore("\n§rНажмите§7, §fчтобы §6обменяться§7!"));
				traderHopper.addItem(Item.get(
										 CustomItemID.SUN_WAND).setLore("\n§rНажмите§7, §fчтобы §6обменяться§7!"));
				traderHopper.addItem(Item.get(
										 CustomItemID.PIECE_ICE).setLore("\n§rНажмите§7, §fчтобы §6обменяться§7!"));
				traderHopper.addItem(Item.get(
										 CustomItemID.REMAINS_GUARDIAN).setLore("\n§rНажмите§7, §fчтобы §6обменяться§7!"));
				traderHopper.addItem(Item.get(
										 CustomItemID.GOLDEN_MONEY).setLore("\n§rЭтот §6предмет §fВы §6получите §fпосле обмена§7.\n§fИспользуйте его на §6алтаре§7!"));
				FakeInventoryAPI.openInventory((Player) damager, traderHopper);
			}
		}
	}

	@EventHandler()
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction) action;
				if (slotChange.getInventory() instanceof NPCHopper) {
					Player player = event.getTransaction().getSource();
					Item sourceItem = action.getSourceItem();
					event.setCancelled(true);
					Item goldenMoney = Item.get(CustomItemID.GOLDEN_MONEY, 0, 5);
					switch (sourceItem.getId()) {
					case CustomItemID.TARANTULA_WEB: {
						PlayerInventory playerInventory = player.getInventory();
						Item artifact = Item.get(CustomItemID.TARANTULA_WEB, 0, 1);
						if (playerInventory.contains(artifact)) {
							if (playerInventory.canAddItem(goldenMoney)) {
								playerInventory.removeItem(artifact);
								playerInventory.addItem(goldenMoney);
								player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
								player.sendMessage("§l§7(§3Собиратель§7) §rСпасибо за §6Артефакт§7, §fприходи еще§7!");
							}
						} else {
							player.sendMessage("§l§7(§3Собиратель§7) §rНедостаточно §6Артефактов §fдля совершения обмена§7!");
							player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
						}
					}
					break;

					case CustomItemID.SUN_WAND: {
						PlayerInventory playerInventory = player.getInventory();
						Item artifact = Item.get(CustomItemID.SUN_WAND, 0, 1);
						if (playerInventory.contains(artifact)) {
							if (playerInventory.canAddItem(goldenMoney)) {
								playerInventory.removeItem(artifact);
								playerInventory.addItem(goldenMoney);
								player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
								player.sendMessage("§l§7(§3Собиратель§7) §rСпасибо за §6Артефакт§7, §fприходи еще§7!");
							}
						} else {
							player.sendMessage("§l§7(§3Собиратель§7) §rНедостаточно §6Артефактов §fдля совершения обмена§7!");
							player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
						}
					}
					break;
					case CustomItemID.PIECE_ICE: {
						PlayerInventory playerInventory = player.getInventory();
						Item artifact = Item.get(CustomItemID.PIECE_ICE, 0, 1);
						if (playerInventory.contains(artifact)) {
							if (playerInventory.canAddItem(goldenMoney)) {
								playerInventory.removeItem(artifact);
								playerInventory.addItem(goldenMoney);
								player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
								player.sendMessage("§l§7(§3Собиратель§7) §rСпасибо за §6Артефакт§7, §fприходи еще§7!");
							}
						} else {
							player.sendMessage("§l§7(§3Собиратель§7) §rНедостаточно §6Артефактов §fдля совершения обмена§7!");
							player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
						}
					}
					break;
					case CustomItemID.REMAINS_GUARDIAN: {
						PlayerInventory playerInventory = player.getInventory();
						Item artifact = Item.get(CustomItemID.REMAINS_GUARDIAN, 0, 1);
						if (playerInventory.contains(artifact)) {
							if (playerInventory.canAddItem(goldenMoney)) {
								playerInventory.removeItem(artifact);
								playerInventory.addItem(goldenMoney);
								player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
								player.sendMessage("§l§7(§3Собиратель§7) §rСпасибо за §6Артефакт§7, §fприходи еще§7!");
							}
						} else {
							player.sendMessage("§l§7(§3Собиратель§7) §rНедостаточно §6Артефактов §fдля совершения обмена§7!");
							player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
						}
					}
					break;

					case CustomItemID.GOLDEN_MONEY: {
						event.setCancelled(true);
					}
					break;
					}
				}
			}
		}
	}

	@EventHandler()
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Arrays.asList(player.getLevel().getNearbyEntities(player.getBoundingBox().clone().expand(16, 16, 16), player)).forEach((entity) -> {
			if (entity instanceof Player) {
				return;
			}
			if (entity.distance(player) < 1.0) {
				return;
			}
			if (entity instanceof WanderingTraderNPC || entity instanceof VillagerNPC) {
				double xdiff = player.x - entity.x;
				double zdiff = player.z - entity.z;
				double angle = Math.atan2(zdiff, xdiff);
				double yaw = ((angle * 180) / Math.PI) - 90;
				double ydiff = player.y - entity.y;
				Vector2 v = new Vector2(entity.x, entity.z);
				double dist = v.distance(player.x, player.z);
				angle = Math.atan2(dist, ydiff);
				double pitch = ((angle * 180) / Math.PI) - 90;
				MoveEntityAbsolutePacket absolutePacket = new MoveEntityAbsolutePacket();
				absolutePacket.eid = entity.getId();
				absolutePacket.x = entity.x;
				absolutePacket.y = entity.y;
				absolutePacket.z = entity.z;
				absolutePacket.yaw = yaw;
				absolutePacket.pitch = pitch;
				absolutePacket.headYaw = yaw;
				absolutePacket.onGround = entity.onGround;
				player.directDataPacket(absolutePacket);
			}
		});
	}
}