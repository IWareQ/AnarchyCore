package AnarchySystem.Components.CombatLogger.EventsListener;

import AnarchySystem.Components.CombatLogger.CombatLoggerAPI;
import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler()
    public void onEntityDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity victim = event.getEntity();
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Player) {
                ((Player) damager).sendTip("§l§f" + String.format("%.0f", victim.getHealth()) + " §c❤");
                if (victim instanceof Player && victim.getLevel() != WorldSystemAPI.getSpawn() && damager != victim) {
                    for (Player players : new Player[]{(Player) victim, (Player) damager}) {
                        CombatLoggerAPI.addCombat(players);
                    }
                }
            }
        }
    }
}