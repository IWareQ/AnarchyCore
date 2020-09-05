package Anarchy.Module.Commands.Spectate;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.GameRulesChangedPacket;
import cn.nukkit.potion.Effect;

public class SpectateCommand extends Command {
	public static Map<String, SpectatePlayer> SPECTATE_PLAYERS = new HashMap<>();
	
	public SpectateCommand() {
		super("sp", "Наблюдение за Игроком", "", new String[]{"spec"});
		setPermission("Command.Spectate");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.Spectate")) {
			return false;
		}
		if (args.length == 0) {
			SpectatePlayer spectatePlayer = SPECTATE_PLAYERS.get(player.getName());
			if (spectatePlayer == null) {
				player.sendMessage("§l§6| §r§fИспользование §7- §6/sp §7(§3игрок§7)");
				return false;
			} else {
				Level level = player.getLevel();
				GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
				gameRulesChanged.gameRules = level.getGameRules();
				gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, true);
				player.dataPacket(gameRulesChanged);
				player.setGamemode(0);
				player.removeAllEffects();
				player.getInventory().setContents(spectatePlayer.playerInventory);
				spectatePlayer.playerPosition.level.loadChunk(spectatePlayer.playerPosition.getFloorX() >> 4, spectatePlayer.playerPosition.getFloorZ() >> 4);
				player.teleport(spectatePlayer.playerPosition);
				player.sendMessage("§l§a| §r§fВы закончили наблюдение за Игроком §6" + spectatePlayer.spectateName);
				SPECTATE_PLAYERS.remove(player.getName());
			}
		} else {
			SpectatePlayer spectatePlayer = SPECTATE_PLAYERS.get(player.getName());
			if (spectatePlayer != null) {
				player.sendMessage("§l§c| §r§fВы уже наблюдаете за Игроком §6" + spectatePlayer.spectateName + "\n§l§6| §r§fДля окончания слежки введите §3§l/sp");
				return false;
			}
			Player specPlayer = Server.getInstance().getPlayer(args[0]);
			if (specPlayer == null) {
				player.sendMessage("§l§c| §r§fИгрок §r" + args[0] + " §7- §3Оффлайн");
				return false;
			}
			if (player == specPlayer) {
				player.sendMessage("§l§c| §r§fВы пытаетесь наблюдать за собой§7!");
				return false;
			}
			Level level = player.getLevel();
			GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
			gameRulesChanged.gameRules = level.getGameRules();
			gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, false);
			player.dataPacket(gameRulesChanged);
			SPECTATE_PLAYERS.put(player.getName(), new SpectatePlayer(player, specPlayer));
			player.addEffect(Effect.getEffect(Effect.NIGHT_VISION).setAmplifier(0).setDuration(9999999 * 20).setVisible(false));
			player.setGamemode(3);
			player.getInventory().clearAll();
			player.teleport(specPlayer);
			player.sendMessage("§l§a| §r§fВы начали наблюдать за Игроком §6" + specPlayer.getName() + "\n§l§6| §r§fДля окончания наблюдения введите §3§l/sp");
		}
		return false;
	}
}