package ru.jl1mbo.AnarchyCore.CommandsHandler;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

import java.util.HashMap;

public class ReportCommand extends Command {
    private static final HashMap<Player, Long> COOLDOWN = new HashMap<>();

    public ReportCommand() {
        super("report", "§r§fПожаловаться на Игрока");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("cheater", CommandParamType.TARGET, false), new CommandParameter("reason", CommandParamType.INT, false)});
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage("§l§6•  §r§fИспользование §7- /§6report §7(§3игрок§7) (§3текст§7)");
                return true;
            }
            Long cooldownTime = COOLDOWN.get(player);
            long nowTime = System.currentTimeMillis() / 1000;
            if (cooldownTime != null && cooldownTime > nowTime) {
                player.sendMessage("§l§7(§3Задержка§7) §r§fСледующее использование будет доступно через §6" + (cooldownTime - nowTime) + " §fсек§7.");
                return false;
            }
            Player target = Server.getInstance().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§l§6•  §r§fИгрок §6" + args[0] + " §fне в сети§7!");
                return true;
            }
            String message = Utils.implode(args, 1);
            player.sendMessage("§l§7(§3Репорты§7) §r§fРепорт был отправлен§7!");
            COOLDOWN.put(player, nowTime + 30);
            Utils.sendMessageToChat("Новая жалоба! (" + player.getName() + ")\nНарушитель: " + target.getName() + "\nПричина: " + message, 2000000001);
        }
        return false;
    }
}