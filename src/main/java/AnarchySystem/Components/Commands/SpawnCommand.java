package AnarchySystem.Components.Commands;

import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SpawnCommand extends Command {

    public SpawnCommand() {
        super("spawn", "§r§fТелепортироваться на спавн");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getLevel().equals(WorldSystemAPI.getMap())) {
                player.teleport(WorldSystemAPI.getSpawn().getSafeSpawn());
                player.sendMessage("§l§a• §r§fВы §6успешно §fтелепортировались на спавн§7!");
            } else {
                player.sendMessage("§l§7(§3Телепорт§7) §r§fС этого измерения запрещено телепортироваться§7!");
            }
        }
        return false;
    }
}