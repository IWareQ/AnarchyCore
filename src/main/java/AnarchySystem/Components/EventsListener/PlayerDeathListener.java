package AnarchySystem.Components.EventsListener;

import AnarchySystem.Components.EconomyAPI.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.math.Vector3;

public class PlayerDeathListener implements Listener {

	@EventHandler()
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		EntityDamageEvent cause = player.getLastDamageCause();
		switch (cause == null ? DamageCause.CUSTOM : cause.getCause()) {
		case ENTITY_ATTACK:
			if (cause instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
				if (damager instanceof Player) {
					player.sendMessage("§l§c• §r§fВы были убиты Игроком §6" + damager.getName());
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fпогиб от руки Игрока §6" + damager.getName());
					Double money = EconomyAPI.myMoney(player.getName()) * 20 / 100;
					if (money > 1.0) {
						player.sendMessage("§l§c• §r§fПри смерти Вы потеряли §6" + String.format("%.1f", money) + " §7(§f20§7%)");
						((Player)damager).sendMessage("§l§6• §r§fВо время убийства§7, §fВы украли §6" + String.format("%.1f", money) + " §fу Игрока §6" + player.getName());
						EconomyAPI.reduceMoney(player.getName(), money);
						EconomyAPI.addMoney(((Player)damager).getName(), money);
					}
					break;
				} else if (damager instanceof EntityLiving) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fпогиб от моба");
					break;
				} else {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fпогиб");
				}
			}
			break;
		case PROJECTILE:
			if (cause instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
				if (damager instanceof Player) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл застрелен игроком §6" + damager.getName());
				} else if (damager instanceof EntityLiving) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл застрелен");
					break;
				} else {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fпогиб");
				}
			}
			break;
		case VOID:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fупал в бездну");
			break;
		case FALL:
			if (cause.getFinalDamage() > 2) {
				event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fразбился");
				break;
			}
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fразбился");
			break;

		case SUFFOCATION:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fзадохнулся");
			break;

		case LAVA:
			Block block = player.getLevel().getBlock(new Vector3(player.getX(), player.getY() - 1, player.getZ()));
			if (block.getId() == Block.MAGMA) {
				event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fсгорел");
				break;
			}
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fсгорел");
			break;

		case FIRE:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fсгорел");
			
			break;

		case FIRE_TICK:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fсгорел");
			break;

		case DROWNING:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fутанул");
			break;

		case CONTACT:
			if (cause instanceof EntityDamageByBlockEvent) {
				if (((EntityDamageByBlockEvent) cause).getDamager().getId() == Block.CACTUS) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fумер от кактуса");
				}
			}
			break;
		case BLOCK_EXPLOSION:
		case ENTITY_EXPLOSION:
			if (cause instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
				if (damager instanceof Player) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл взорван игроком §6" + damager.getName());
				} else if (damager instanceof EntityLiving) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл взорван");
					break;
				} else {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл взорван");
				}
			} else {
				event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл взорван");
			}
			break;
		case MAGIC:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fумер от магии");
			break;
		case HUNGER:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fумер от голодп");
			break;
		default:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fпогиб");
			break;
		}
	}
}