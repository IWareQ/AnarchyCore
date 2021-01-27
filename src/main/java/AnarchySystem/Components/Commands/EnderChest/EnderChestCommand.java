package AnarchySystem.Components.Commands.EnderChest;

import AnarchySystem.Components.Commands.EnderChest.Inventory.EnderChest;
import AnarchySystem.Manager.FakeInventory.FakeInventoryAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

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