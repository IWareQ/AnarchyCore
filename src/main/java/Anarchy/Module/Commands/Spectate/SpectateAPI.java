package Anarchy.Module.Commands.Spectate;

import java.util.HashMap;
import java.util.Map;

import Anarchy.AnarchyMain;
import Anarchy.Manager.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.network.protocol.GameRulesChangedPacket;
import cn.nukkit.potion.Effect;

public class SpectateAPI {
	public static Map<String, SpectatePlayer> SPECTATE_PLAYERS = new HashMap<>();
	public static HashMap<String, Map<Integer, Item>> INVENTORY_PLAYER = new HashMap<>();
	
	public static void addSpectate(Player player, Player target) {
		SpectatePlayer spectatePlayer = SPECTATE_PLAYERS.get(player.getName());
		if (SPECTATE_PLAYERS.containsKey(player.getName())) {
			player.sendMessage(AnarchyMain.PREFIX + "§fВы уже наблюдаете за Игроком §6" + spectatePlayer.spectateName + "\n§l§6| §r§fДля окончания наблюдения используйте §6Редстоун§7!");
		} else {
			if (target != null) {
				GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
				gameRulesChanged.gameRules = FunctionsAPI.COORDINATE.getGameRules();
				gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, false);
				player.dataPacket(gameRulesChanged);
				SPECTATE_PLAYERS.put(player.getName(), new SpectatePlayer(player, target));
				INVENTORY_PLAYER.put(player.getName(), player.getInventory().getContents());
				player.addEffect(Effect.getEffect(Effect.NIGHT_VISION).setAmplifier(0).setDuration(9999999 * 20).setVisible(false));
				player.setGamemode(3);
				player.getInventory().clearAll();
				player.getInventory().setHeldItemIndex(0);
				player.getInventory().setItem(5, Item.get(Item.STICK).setCustomName("§l§fПроверка регионов").setLore("§l§6• §fНажмите7, §fчтобы проверить\n §l§fнет ли в блези регионов"));
				player.getInventory().setItem(8, Item.get(Item.REDSTONE).setCustomName("§l§fЗавершить Наблюдение"));
				player.teleport(target);
				player.sendMessage(AnarchyMain.PREFIX + "§fВы начали наблюдать за Игроком §6" + target.getName() + "\n§l§6| §r§fДля окончания наблюдения используйте §6Редстоун§7!");
			}
		}
	}
	
	public static void removeSpectate(Player player) {
		SpectatePlayer spectatePlayer = SPECTATE_PLAYERS.get(player.getName());
		if (SPECTATE_PLAYERS.containsKey(player.getName())) {
			player.setGamemode(0);
			player.removeAllEffects();
			player.getInventory().clearAll();
			player.getInventory().setContents(INVENTORY_PLAYER.get(player.getName()));
			INVENTORY_PLAYER.remove(player.getName());
			spectatePlayer.playerPosition.level.loadChunk(spectatePlayer.playerPosition.getFloorX() >> 4, spectatePlayer.playerPosition.getFloorZ() >> 4);
			player.teleport(spectatePlayer.playerPosition);
			GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
			gameRulesChanged.gameRules = FunctionsAPI.COORDINATE.getGameRules();
			gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, true);
			player.dataPacket(gameRulesChanged);
			player.sendMessage("§l§a| §r§fВы закончили наблюдение за Игроком §6" + spectatePlayer.spectateName);
			SPECTATE_PLAYERS.remove(player.getName());
		}
	}
}