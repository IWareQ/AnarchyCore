package AnarchySystem.Components.EventsListener;

import AnarchySystem.Components.EconomyAPI.EconomyAPI;
import AnarchySystem.Utils.Utils;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import nukkitcoders.mobplugin.entities.Boss;
import nukkitcoders.mobplugin.entities.animal.Animal;
import nukkitcoders.mobplugin.entities.monster.Monster;

public class EntityDeathListener implements Listener {

	@EventHandler()
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent cause = (EntityDamageByEntityEvent) entity.getLastDamageCause();
			if (cause.getDamager() instanceof Player) {
				Player player = (Player) cause.getDamager();
				double animalMoney = Utils.rand(0.1, 1.0);
				double monsterMoney = Utils.rand(0.1, 2.0);
				double bossMoney = Utils.rand(10.0, 30.0);
				if (entity instanceof Animal) {
					player.sendTip("§7+ §6" + String.format("%.1f", animalMoney) + "");
					EconomyAPI.addMoney(player.getName(), animalMoney);
				} else if (entity instanceof Monster) {
					player.sendTip("§7+ §6" + String.format("%.1f", monsterMoney) + "");
					EconomyAPI.addMoney(player.getName(), monsterMoney);
				} else if (entity instanceof Boss) {
					player.sendTip("§7+ §6" + String.format("%.1f", bossMoney) + "");
					EconomyAPI.addMoney(player.getName(), bossMoney);
				}
			}
		}
	}
}