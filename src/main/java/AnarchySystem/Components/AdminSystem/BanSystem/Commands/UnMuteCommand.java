package AnarchySystem.Components.AdminSystem.BanSystem.Commands;

import AnarchySystem.Components.AdminSystem.BanSystem.BanSystemAPI;
import AnarchySystem.Utils.Utils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class UnMuteCommand extends Command {

    public UnMuteCommand() {
        super("unmute", "§r§fСнять блокировку чата");
        this.setPermission("Command.UnMute");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Command.UnMute")) {
                return false;
            }
            if (args.length != 1) {
                player.sendMessage("§l§6• §r§fИспользование §7- /§6unmute §7(§6игрок§7)");
                return true;
            }
            String nickname = Utils.implode(args, 0);
            if (BanSystemAPI.IsMuted(nickname)) {
                player.sendMessage(BanSystemAPI.PREFIX + "§fЧат игрока §6" + nickname + " §fбыл разблокирован§7!");
                BanSystemAPI.removeMute(nickname);
            } else {
                player.sendMessage(BanSystemAPI.PREFIX + "§fЧат игрока §6" + nickname + " §fне заблокирован§7!");
            }
        }
        return false;
    }
}