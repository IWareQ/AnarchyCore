package AnarchySystem.Components.EconomyAPI.Commands;

import AnarchySystem.Components.EconomyAPI.EconomyAPI;
import AnarchySystem.Utils.Utils;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class SetMoneyCommand extends Command {

    public SetMoneyCommand() {
        super("setmoney", "§r§fУстановить баланс");
        this.setPermission("Command.SetMoney");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("money", CommandParamType.INT, false), new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("Command.SetMoney")) {
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("§l§6• §r§fИспользование §7- /§6setmoney §7(§6сумма§7) (§6игрок§7)");
            return true;
        }
        String nickname = Utils.implode(args, 1);
        sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + nickname + " §fтеперь имеет §6" + String.format("%.1f", Double.parseDouble(args[0])) + "");
        EconomyAPI.setMoney(nickname, Double.parseDouble(args[0]));
        return false;
    }
}