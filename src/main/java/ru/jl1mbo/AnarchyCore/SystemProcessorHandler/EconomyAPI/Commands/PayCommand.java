package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EconomyAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class PayCommand extends Command {

    public PayCommand() {
        super("pay", "§r§fПеревод монет");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("money", CommandParamType.INT, false), new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 2) {
                player.sendMessage("§l§6• §r§fИспользование §7- /§6pay §7(§6сумма§7) (§6игрок§7)");
                return true;
            }
            String nickname = Utils.implode(args, 1);
            if (!EconomyAPI.isRegister(nickname)) {
                player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + nickname + " §fне зарегистрирован§7!");
                return true;
            }
            if (!Utils.isDouble(args[0]) || Double.parseDouble(args[0]) < 0) {
                player.sendMessage(EconomyAPI.PREFIX + "§fСумма может быть только положительным числом§7!");
                return true;
            }
            if (EconomyAPI.myMoney(player.getName()) < Double.parseDouble(args[0])) {
                player.sendMessage(EconomyAPI.PREFIX + "§fВам не хватает §6монет §fдля перевода§7!\n§l§6• §r§fВаш баланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(player.getName())) + "");
                return true;
            }
            player.sendMessage(EconomyAPI.PREFIX + "§fВы успешно перевели §6" + String.format("%.1f", Double.parseDouble(args[0])) + " §fигроку §6" + nickname);
            Player target = Server.getInstance().getPlayer(nickname);
            if (target != null) {
                target.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fперевел Вам §6" + String.format("%.1f", Double.parseDouble(args[0])) + "");
            }
            EconomyAPI.reduceMoney(player.getName(), Double.parseDouble(args[0]));
            EconomyAPI.addMoney(nickname, Double.parseDouble(args[0]));
        }
        return false;
    }
}