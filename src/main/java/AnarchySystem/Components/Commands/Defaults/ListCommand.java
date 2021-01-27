package AnarchySystem.Components.Commands.Defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ListCommand extends Command {
    public ListCommand() {
        super("list", "§r§fСписок иигроков которые онлайн");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        int onlineCount = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (Player players : sender.getServer().getOnlinePlayers().values()) {
            if (players.isOnline()) {
                stringBuilder.append("§7, §6").append(players.getDisplayName());
            }
            ++onlineCount;
        }
        sender.sendMessage("§l§6• §r§fНа сервере §6" + onlineCount + " §fонлайна§7!\n§fИгроки§7: §6" + (stringBuilder.length() > 0 ? stringBuilder.substring(4) : "§7..."));
        return true;
    }
}