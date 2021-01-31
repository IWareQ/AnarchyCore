package ru.jl1mbo.AnarchyCore.CommandsHandler;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class NearCommand extends Command {
    private static int RADIUS = 0;

    public NearCommand() {
        super("near", "§r§fПосмотреть кто рядом с Вами");
        this.setPermission("Command.Near");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Command.Near")) {
                return false;
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (player.hasPermission("Command.Near.65")) {
                RADIUS = 65;
            } else if (player.hasPermission("Command.Near.100")) {
                RADIUS = 100;
            }
            for (Player players : Server.getInstance().getOnlinePlayers().values()) {
                if (players.distance(player) < RADIUS && players.getGamemode() != 3) {
                    stringBuilder.append("§7, §6").append(players.getName());
                }
            }
            player.sendMessage("§l§6• §r§fИгроки в радиусе §6" + RADIUS + " §fблоков§7: §6" + stringBuilder.toString());
        }
        return false;
    }
}