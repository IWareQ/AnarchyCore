package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.EventsListener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		PermissionAPI.updatePermissions(event.getPlayer());
		PermissionAPI.updateNamedTag(event.getPlayer());
	}
}