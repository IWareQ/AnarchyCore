package AnarchySystem.Components.EconomyAPI.Commands;

import AnarchySystem.Components.EconomyAPI.EconomyAPI;
import AnarchySystem.Utils.Utils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class SeeMoneyCommand extends Command {

    public SeeMoneyCommand() {
        super("seemoney", "§r§fПросмотр баланса");
        this.setPermission("Command.SeeMoney");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Command.SeeMoney")) {
                return true;
            }
            if (args.length < 1) {
                player.sendMessage("§l§6• §r§fИспользование §7- /§6seemoney §7(§6игрок§7)");
                return true;
            }
            String nickname = Utils.implode(args, 0);
            if (!EconomyAPI.isRegister(nickname)) {
                player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + nickname + " §fне зарегистрирован§7!");
                return true;
            }
            player.sendMessage(EconomyAPI.PREFIX + "§fБаланс Игрока §6" + nickname + " §7- §6" + String.format("%.1f", EconomyAPI.myMoney(nickname)) + "");
        }
        return false;
    }
}