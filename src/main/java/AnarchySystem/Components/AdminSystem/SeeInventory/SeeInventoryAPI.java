package AnarchySystem.Components.AdminSystem.SeeInventory;

import java.util.HashMap;
import java.util.Map;

import AnarchySystem.Components.AdminSystem.SeeInventory.Inventory.DoubleChest;
import AnarchySystem.Manager.FakeInventory.FakeInventoryAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;

public class SeeInventoryAPI {
    public static Map<String, Player> INVENTORY_PLAYER = new HashMap<>();

    public static void checkInventory(String checked, Player player) {
        Player target = Server.getInstance().getPlayer(checked);
        INVENTORY_PLAYER.put(player.getName(), target);
        DoubleChest doubleChest = new DoubleChest("§r§fПросмотр Инвентаря");
        doubleChest.setContents(setItems(target.getInventory().getContents()));
        FakeInventoryAPI.openInventory(player, doubleChest);
    }

    public static Map<Integer, Item> setItems(Map<Integer, Item> items) {
        DoubleChest doubleChest = new DoubleChest("§r§fПросмотр Инвентаря");
        doubleChest.setContents(items);
        doubleChest.setItem(45, Item.get(Item.ENDER_EYE).setCustomName("§r§6Назад").setLore("\n§r§fНажмите§7, §fчтобы вернуться в главное\nменю просмотра Инвентаря§7!"));
        doubleChest.setItem(49, Item.get(Item.ENDER_CHEST).setCustomName("§r§6Открыть Эндер Сундук").setLore("\n§r§fНажмите§7, §fчтобы открыть\n§6Эндер Сундук§7!"));
        doubleChest.setItem(53, Item.get(Item.BOOK).setCustomName("§r§6Справка").setLore("\n§r§fПока что здесь ничего нет§7,\n§fно я уже занимаюсь этим§7!"));
        return doubleChest.getContents();
    }
}