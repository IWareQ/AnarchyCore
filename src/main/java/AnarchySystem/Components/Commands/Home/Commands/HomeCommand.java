package AnarchySystem.Components.Commands.Home.Commands;

import AnarchySystem.Components.Commands.Home.HomeAPI;
import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import AnarchySystem.Utils.ConfigUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;

public class HomeCommand extends Command {

    public HomeCommand() {
        super("home", "§r§fТелепортироватся домой");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!HomeAPI.isHome(player.getName())) {
                player.sendMessage(HomeAPI.PREFIX + "§fТочек дома не обнаружено§7!\n§l§6• §r§fДля создания точки Дома используйте §7/§6sethome");
            } else {
                player.sendMessage(HomeAPI.PREFIX + "§fВы успешно телепортированы домой§7!");
                player.teleport(new Position(ConfigUtils.getHomeConfig().getInt(player.getName().toLowerCase() + ".X") + 0.5, ConfigUtils.getHomeConfig().getInt(player.getName().toLowerCase() + ".Y"), ConfigUtils.getHomeConfig().getInt(player.getName().toLowerCase() + ".Z") + 0.5, WorldSystemAPI.getMap()));
            }
        }
        return false;
    }
}