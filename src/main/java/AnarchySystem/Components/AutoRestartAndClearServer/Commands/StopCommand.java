package AnarchySystem.Components.AutoRestartAndClearServer.Commands;

import AnarchySystem.Components.AutoRestartAndClearServer.Task.RestartTask;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", "§r§fРучная перезагрузка");
        this.setPermission("Command.Stop");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("Command.Stop")) {
            return false;
        }
        sender.sendMessage("§l§7(§3Перезагрузка§7) §r§fНачинается принудительная §6Перезагрузка§7!");
        RestartTask.TIMER = 10;
        return false;
    }
}