package AnarchySystem.Components.Commands;

import AnarchySystem.Manager.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DonateCommand extends Command {

    public DonateCommand() {
        super("donate", "§r§fОписание Донат Услуг");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            donateList((Player) sender);
        }
        return false;
    }

    private void donateList(Player player) {
        SimpleForm simpleForm = new SimpleForm("§r§fОписание Донат Услуг");
        simpleForm.setContent("§r§fЕсли Вас §6устраивает §fлюбая привилегия§7, §fто Вы можете пройти на наш §6сайт §fи приобрести ее§7!\n\n§r§fНаш сайт§7: §6death§7-§6mc§7.§6online");
        simpleForm.addButton("§r§fДля чего нужен Донат§7?");
        simpleForm.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            if (data == 0) {
                whatIsDonation(player);
            }
        });
    }

    private void whatIsDonation(Player player) {
        SimpleForm simpleForm = new SimpleForm("§r§fДля чего нужен Донат§7?");
        simpleForm.setContent("§r§fДля того чтобы проект §6жил §fи §6развивался §fему нужны деньги §7:§fc\n§r§fК сожалению для всего в этом мире нужны деньги§7...\n§r§fДеньги идут на§7:\n§l§6• §r§fОплату хостинга §7(§fстоит достаточно дорого§7);\n§l§6• §r§fЗарплату сотрудникам§7;\n§l§6• §r§fРазработка собственных §6плагинов §fи §6режимов§7.\n\n§r§fНа это все идет много средств§7.\n\n§r§fИ мы безумно сильно благодарны за поддержку §6Проекта§7!");
        simpleForm.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) donateList(player);
        });
    }
}