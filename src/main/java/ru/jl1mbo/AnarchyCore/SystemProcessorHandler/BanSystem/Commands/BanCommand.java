package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.CustomForm;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class BanCommand extends Command {

    public BanCommand() {
        super("ban", "§r§fБлокировка Аккаунта");
        this.setPermission("Command.Ban");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    public static void banPlayerForm(Player player, String targetName) {
        CustomForm customForm = new CustomForm("§l§fБлокировка Аккаунта");
        customForm.addLabel("§l§6• §r§fИгрок§7: §6" + targetName + "\n§l§6• §r§fРанг§7: " + PermissionAPI.getAllGroups().get(PermissionAPI.getGroup(targetName)).getGroupName() + "\n");
        customForm.addInput("§l§6• §r§fПричина блокировки Аккаунта§7:");
        customForm.addLabel("§l§6• §r§fВремя блокировки Аккаунта§7:");
        customForm.addSlider("§6Минуты§7", 0, 60, 5, 0);
        customForm.addSlider("§6Часы§7", 0, 24, 1, 0);
        customForm.addSlider("§6Дни§7", 0, 30, 1, 0);
        customForm.send(player, (targetPlayer, targetForm, data) -> {
            if (data == null) return;
            if (BanSystemAPI.IsBanned(targetName)) {
                player.sendMessage(BanSystemAPI.PREFIX + "§fИгрок §6" + targetName + " §fуже заблокирован§7!");
                return;
            }
            float time = ((float) data.get(3) * 1 * 60) + ((float) data.get(4) * 1 * 3600) + ((float) data.get(5) * 1 * 86400);
            for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
                BanSystemAPI.sendMessageAdmin(admin, "", 1);
            }
            BanSystemAPI.addBan(targetName, data.get(1).toString(), player.getName(), (int) time);
        });
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Command.Ban")) {
                return false;
            }
            if (args.length != 1) {
                player.sendMessage("§l§6• §r§fИспользование §7- /§6ban §7(§6игрок§7)");
                return true;
            }
            String nickname = Utils.implode(args, 0);
            Player target = Server.getInstance().getPlayer(nickname);
            if (target == null) {
                banPlayerForm(player, nickname);
                return false;
            }
            banPlayerForm(player, target.getName());
        }
        return false;
    }
}