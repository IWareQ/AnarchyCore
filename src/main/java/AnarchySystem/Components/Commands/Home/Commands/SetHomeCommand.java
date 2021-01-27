package AnarchySystem.Components.Commands.Home.Commands;

import AnarchySystem.Components.BlockProtection.BlockProtectionAPI;
import AnarchySystem.Components.Commands.Home.HomeAPI;
import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SetHomeCommand extends Command {

    public SetHomeCommand() {
        super("sethome", "§r§fУстановить новую точку дома");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getLevel() != WorldSystemAPI.getMap()) {
                player.sendMessage(HomeAPI.PREFIX + "§fВ этом мире §6запрещено §fустанавливать точки §6Дома§7!");
                return false;
            }
            if (BlockProtectionAPI.canInteractHere(player, player.getLocation())) {
                player.sendMessage(HomeAPI.PREFIX + "§fЗапрещено ставить §6точки дома §fв чужих приватах§7!");
                return false;
            }
            if (!HomeAPI.isHome(player.getName())) {
                HomeAPI.addHome(player.getName(), player.getFloorX(), player.getFloorY(), player.getFloorZ());
                player.sendMessage(HomeAPI.PREFIX + "§fНовая точка дома §6упешно §fустановлена§7!");
            } else {
                HomeAPI.addHome(player.getName(), player.getFloorX(), player.getFloorY(), player.getFloorZ());
                player.sendMessage(HomeAPI.PREFIX + "§fНовая точка дома §6упешно §fустановлена§7!");
            }
        }
        return false;
    }
}