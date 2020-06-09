package Anarchy.Module.Commands.Spectate;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.potion.Effect;

public class SpectateCommand extends Command {
	public static Map <String, SpectatePlayer> SPECTATE_PLAYERS = new HashMap <>();

	public SpectateCommand() {
		super("sp", "Наблюдение за игроками", "", new String[] {
			"spec"
		});
		setPermission("Command.Spectate");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {
			new CommandParameter("player", CommandParamType.TARGET, false)
		});
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Spectate")) {
			return false;
		}

		Player player = (Player) commandSender;
		if (strings.length == 0) {
			SpectatePlayer spectatePlayer = SPECTATE_PLAYERS.get(player.getName());
			if (spectatePlayer == null) {
				commandSender.sendMessage("§l§e| §fИспользование §7- §e/sp <игрок>");
				return false;
			} else {
				player.setGamemode(0);
				player.removeAllEffects();
				player.getInventory().setContents(spectatePlayer.playerInventory);
				spectatePlayer.playerPosition.level.loadChunk(spectatePlayer.playerPosition.getFloorX() >> 4, spectatePlayer.playerPosition.getFloorZ() >> 4);
				player.teleport(spectatePlayer.playerPosition);
				player.sendMessage("§l§a| §r§fВы закончили наблюдение за Игроком §a" + spectatePlayer.spectateName);
				SPECTATE_PLAYERS.remove(player.getName());
			}
		} else {
			SpectatePlayer spectatePlayer = SPECTATE_PLAYERS.get(player.getName());
			if (spectatePlayer != null) {
				commandSender.sendMessage("§l§c| §r§fВы уже наблюдаете за Игроком §a" + spectatePlayer.spectateName + "\n§l§e| §r§fДля окончания слежки введите §e§l/sp");
				return false;
			}

			Player specPlayer = Server.getInstance().getPlayer(strings[0]);
			if (specPlayer == null) {
				commandSender.sendMessage("§l§c| §r§fИгрок §a" + strings[0] + " §7- §cОффлайн");
				return false;
			}

			if (player == specPlayer) {
				commandSender.sendMessage("§l§c| §r§fВы пытаетесь наблюдать за собой!");
				return false;
			}

			SPECTATE_PLAYERS.put(player.getName(), new SpectatePlayer(player, specPlayer));
			player.addEffect(Effect.getEffect(Effect.NIGHT_VISION).setAmplifier(0).setDuration(9999999 * 20).setVisible(false));
			player.setGamemode(3);
			player.getInventory().clearAll();
			player.teleport(specPlayer);
			player.sendMessage("§l§a| §r§fВы начали наблюдать за Игроком §a" + specPlayer.getName() + "\n§l§e| §r§fДля окончания наблюдения введите §e/sp");
		}
		return false;
	}
}