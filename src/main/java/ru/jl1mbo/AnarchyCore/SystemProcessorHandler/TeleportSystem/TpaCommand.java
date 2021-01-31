package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Utils.TeleportUtils;

public class TpaCommand extends Command {
    public static String PREFIX = "§l§7(§3Телепорт§7) §r";
    public static HashMap<String, TeleportUtils> TPA_REQUEST = new HashMap<>();
    public static Map<Player, Long> COOLDOWN = new HashMap<>();

    public TpaCommand() {
        super("tpa", "§r§fОтправить запрос на телепортацию");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length != 1) {
                player.sendMessage("§l§6• §r§fИспользование §7- /§6tpa §7(§6игрок§7)");
                return true;
            }
            Long cooldownTime = COOLDOWN.get(player);
            long time = System.currentTimeMillis() / 1000L;
            if (cooldownTime != null && cooldownTime > time) {
                player.sendMessage("§l§7(§3Задержка§7) §r§fСледующее использование будет доступно через §6" + (cooldownTime - time) + " §fсек§7.");
                return false;
            }
            Player target = Server.getInstance().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(PREFIX + "§fИгрок §6" + args[0] + " §fне в сети§7!");
                return true;
            }
            player.sendMessage(PREFIX + "§fЗапрос на телепортицию к Игроку §6" + target.getName() + " §fуспешно отправлен§7!\n§l§6• §r§fЗапрос действует только §630 §fсекунд§7!");
            target.sendMessage(PREFIX + "§fИгрок §6" + player.getName() + " §fхочет телепортироваться к Вам§7!");
            target.sendMessage("§l§a| §r§7/§atpc §7- §fпринять запрос");
            target.sendMessage("§l§c| §r§7/§ctpd §7- §fотклонить запрос");
            TPA_REQUEST.put(target.getName(), new TeleportUtils(player, target, time + 30));
            COOLDOWN.put(player, time + 10);
        }
        return false;
    }
}