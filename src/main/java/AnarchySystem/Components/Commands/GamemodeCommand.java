package AnarchySystem.Components.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class GamemodeCommand extends Command {

    public GamemodeCommand() {
        super("gm", "§r§fСменить режим игры");
        this.setPermission("Command.Gamemode");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Command.Gamemode")) {
                return false;
            }
            if (args.length == 0) {
                player.sendMessage("§l§6• §r§fИспользование §7- /§6gm §30§7/§31§7/§32§7/§33");
                return true;
            }
            switch (args[0]) {
                case "0": {
                    player.setGamemode(0);
                    player.sendMessage("§l§6• §r§fРежим §7(§6Выживание§7) §fуспешно включен§7!");
                }
                break;

                case "1": {
                    player.setGamemode(1);
                    player.sendMessage("§l§6• §r§fРежим §7(§6Творчество§7) §fуспешно включен§7!");
                }
                break;

                case "2": {
                    player.setGamemode(2);
                    player.sendMessage("§l§6• §r§fРежим §7(§6Приключение§7) §fуспешно включен§7!");
                }
                break;

                case "3": {
                    player.setGamemode(3);
                    player.sendMessage("§l§6• §r§fРежим §7(§6Спектатор§7) §fуспешно включен§7!");
                }
                break;
            }
        }
        return false;
    }
}