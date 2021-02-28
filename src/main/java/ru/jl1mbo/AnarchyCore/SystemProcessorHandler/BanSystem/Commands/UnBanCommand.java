package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class UnBanCommand extends Command {

    public UnBanCommand() {
        super("unban", "§r§fСнять блокировку аккаунта");
        this.setPermission("Command.UnBan");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("Command.UnBan")) {
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage("§l§6• §r§fИспользование §7- /§6unban §7(§6игрок§7)");
            return true;
        }
        String nickname = Utils.implode(args, 0);
        if (BanSystemAPI.IsBanned(nickname)) {
            sender.sendMessage(BanSystemAPI.PREFIX + "§fАккаунт игрока §6" + nickname + " §fбыл разблокирован§7!");
            Utils.sendMessageToChat("🔓Разблокировка аккаунта\n\nИгрок: " + nickname + "\nАдминистратор: " + sender.getName());
            BanSystemAPI.removeBan(nickname);
        } else {
            sender.sendMessage(BanSystemAPI.PREFIX + "§fАккаунт игрока §6" + nickname + " §fне заблокирован§7!");
        }
        return false;
    }
}