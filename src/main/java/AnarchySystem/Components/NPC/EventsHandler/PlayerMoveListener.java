package AnarchySystem.Components.NPC.EventsHandler;

import java.util.Arrays;

import AnarchySystem.Components.NPC.Entity.VillagerNPC;
import AnarchySystem.Components.NPC.Entity.WanderingTraderNPC;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.math.Vector2;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;

public class PlayerMoveListener implements Listener {

    @EventHandler()
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Arrays.asList(player.getLevel().getNearbyEntities(player.getBoundingBox().clone().expand(16, 16, 16), player)).forEach((entity) -> {
            if (entity instanceof Player) {
                return;
            }
            if (entity.distance(player) < 0.5) {
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