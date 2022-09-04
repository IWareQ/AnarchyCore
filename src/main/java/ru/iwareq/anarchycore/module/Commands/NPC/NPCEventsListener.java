package ru.iwareq.anarchycore.module.Commands.NPC;

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
import ru.iwareq.anarchycore.entity.NPC.VillagerNPC;
import ru.iwareq.anarchycore.entity.NPC.WanderingTraderNPC;
import ru.iwareq.anarchycore.manager.FakeInventory.FakeInventoryAPI;
import ru.iwareq.anarchycore.module.Auction.Auction;
import ru.iwareq.anarchycore.module.Commands.NPC.Inventory.NPCHopper;

import java.util.Arrays;

public class NPCEventsListener implements Listener {

	@EventHandler()
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (event instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			if (entity instanceof VillagerNPC) {
				Auction.showAuction((Player) damager, 1);
			} else if (entity instanceof WanderingTraderNPC) {
				FakeInventoryAPI.openInventory((Player) damager, new NPCHopper());
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
				double xdiff = player.getX() - entity.getX();
				double zdiff = player.getZ() - entity.getZ();
				double angle = Math.atan2(zdiff, xdiff);
				double yaw = ((angle * 180) / Math.PI) - 90;
				double ydiff = player.getY() - entity.getY();
				Vector2 v = new Vector2(entity.getX(), entity.getZ());
				double dist = v.distance(player.getX(), player.getZ());
				angle = Math.atan2(dist, ydiff);
				double pitch = ((angle * 180) / Math.PI) - 90;
				MoveEntityAbsolutePacket moveEntityAbsolutePacket = new MoveEntityAbsolutePacket();
				moveEntityAbsolutePacket.eid = entity.getId();
				moveEntityAbsolutePacket.x = entity.getX();
				moveEntityAbsolutePacket.y = entity.getY();
				moveEntityAbsolutePacket.z = entity.getZ();
				moveEntityAbsolutePacket.yaw = yaw;
				moveEntityAbsolutePacket.pitch = pitch;
				moveEntityAbsolutePacket.headYaw = yaw;
				moveEntityAbsolutePacket.onGround = entity.isOnGround();
				player.dataPacket(moveEntityAbsolutePacket);
			}
		});
	}
}