package ru.jl1mbo.AnarchyCore.EventsListener;

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
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EconomyAPI;

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
					this.addDeath(player);
					this.addKill(damager.getName());
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
					this.addDeath(player);
					break;
				} else {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fпогиб");
					this.addDeath(player);
				}
			}
			break;
		case PROJECTILE:
			if (cause instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
				if (damager instanceof Player) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл застрелен игроком §6" + damager.getName());
					this.addDeath(player);
					this.addKill(damager.getName());
				} else if (damager instanceof EntityLiving) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл застрелен");
					this.addDeath(player);
					break;
				} else {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fпогиб");
					this.addDeath(player);
				}
			}
			break;
		case VOID:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fупал в бездну");
			this.addDeath(player);
			break;
		case FALL:
			if (cause.getFinalDamage() > 2) {
				event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fразбился");
				this.addDeath(player);
				break;
			}
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fразбился");
			this.addDeath(player);
			break;

		case SUFFOCATION:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fзадохнулся");
			this.addDeath(player);
			break;

		case LAVA:
			Block block = player.getLevel().getBlock(new Vector3(player.getX(), player.getY() - 1, player.getZ()));
			if (block.getId() == Block.MAGMA) {
				event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fсгорел");
				this.addDeath(player);
				break;
			}
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fсгорел");
			this.addDeath(player);
			break;

		case FIRE:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fсгорел");
			this.addDeath(player);
			break;

		case FIRE_TICK:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fсгорел");
			this.addDeath(player);
			break;

		case DROWNING:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fутонул");
			this.addDeath(player);
			break;

		case CONTACT:
			if (cause instanceof EntityDamageByBlockEvent) {
				if (((EntityDamageByBlockEvent) cause).getDamager().getId() == Block.CACTUS) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fумер от кактуса");
					this.addDeath(player);
				}
			}
			break;
		case BLOCK_EXPLOSION:
		case ENTITY_EXPLOSION:
			if (cause instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
				if (damager instanceof Player) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл взорван игроком §6" + damager.getName());
					this.addDeath(player);
					this.addKill(damager.getName());
				} else if (damager instanceof EntityLiving) {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл взорван");
					this.addDeath(player);
					break;
				} else {
					event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл взорван");
					this.addDeath(player);
				}
			} else {
				event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fбыл взорван");
				this.addDeath(player);
			}
			break;
		case MAGIC:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fумер от магии");
			this.addDeath(player);
			break;
		case HUNGER:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fумер от голода");
			this.addDeath(player);
			break;
		default:
			event.setDeathMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fпогиб");
			this.addDeath(player);
			break;
		}
	}
	
	private void addKill(String playerName) {
		Config config = new Config(Main.getInstance().getDataFolder() + "/kills.yml", Config.YAML);
		config.set(playerName, this.getKills(playerName) + 1);
		config.save();
		config.reload();
	}
	
	private int getKills(String playerName) {
		Config config = new Config(Main.getInstance().getDataFolder() + "/kills.yml", Config.YAML);
		return config.get(playerName, 0);
	}
	
	private void addDeath(Player player) {
		Config config = new Config(Main.getInstance().getDataFolder() + "/deaths.yml", Config.YAML);
		config.set(player.getName(), this.getDeaths(player) + 1);
		config.save();
		config.reload();
	}
	
	private int getDeaths(Player player) {
		Config config = new Config(Main.getInstance().getDataFolder() + "/deaths.yml", Config.YAML);
		return config.get(player.getName(), 0);
	}
}