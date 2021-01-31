package ru.jl1mbo.AnarchyCore.CommandsHandler.EnderChest;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.CommandsHandler.EnderChest.Inventory.EnderChest;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;

public class EnderChestCommand extends Command {
    public EnderChestCommand() {
        super("enderchest", "§r§fОткрыть Сундук края", "", new String[]{"ec"});
        this.setPermission("Command.EnderChest");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Command.EnderChest")) {
                return false;
            }
            EnderChest enderChest = new EnderChest();
            enderChest.setContents(player.getEnderChestInventory().getContents());
            FakeInventoryAPI.openInventory(player, enderChest);
        }
        return false;
    }
}