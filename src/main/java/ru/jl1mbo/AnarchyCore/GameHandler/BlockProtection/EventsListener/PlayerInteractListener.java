package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.BlockProtectionAPI;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class PlayerInteractListener implements Listener {

    @EventHandler()
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Item item = event.getItem();
        if (item != null && item.getId() == Item.STICK) {
            if (player.getLevel() != WorldSystemAPI.getMap()) {
                player.sendTip(BlockProtectionAPI.BIOME);
                return;
            }
            int regionID = BlockProtectionAPI.getRegionIDByLocation(block.getLocation());
            if (regionID != -1) {
                player.sendTip(BlockProtectionAPI.BUSY_BY.replace("{PLAYER}", BlockProtectionAPI.getRegionOwner(regionID)));
                player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
            } else {
                player.sendTip(BlockProtectionAPI.FREE);
                player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
            }
            event.setCancelled(true);
            return;
        }
        if (BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
            player.sendTip(BlockProtectionAPI.BUSY);
            event.setCancelled(true);
        }
    }
}