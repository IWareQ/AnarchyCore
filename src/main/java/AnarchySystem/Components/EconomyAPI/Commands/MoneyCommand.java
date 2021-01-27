package AnarchySystem.Components.EconomyAPI.Commands;

import AnarchySystem.Components.EconomyAPI.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class MoneyCommand extends Command {

    public MoneyCommand() {
        super("money", "§r§fИгровой баланс", "", new String[]{"mymoney"});
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(EconomyAPI.PREFIX + "§fВаш баланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(sender.getName())) + "");
        }
        return false;
    }
}