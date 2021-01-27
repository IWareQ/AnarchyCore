package AnarchySystem.Components.AdminSystem.SeeInventory.EventsListener;

import java.util.HashMap;
import java.util.Map;

import AnarchySystem.Components.AdminSystem.SeeInventory.SeeInventoryAPI;
import AnarchySystem.Components.AdminSystem.SeeInventory.Inventory.DoubleChest;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;

public class InventoryTransactionListener implements Listener {
    private static final Map<Integer, Item> shulkerItems = new HashMap<>();

    @EventHandler()
    public void onInventoryTransaction(InventoryTransactionEvent event) {
        for (InventoryAction action : event.getTransaction().getActions()) {
            if (action instanceof SlotChangeAction) {
                SlotChangeAction slotChange = (SlotChangeAction) action;
                if (slotChange.getInventory() instanceof DoubleChest) {
                    Player player = event.getTransaction().getSource();
                    DoubleChest doubleChest = (DoubleChest) slotChange.getInventory();
                    Item sourceItem = action.getSourceItem();
                    event.setCancelled(true);
                    if (sourceItem.getName().equals("§r§6Назад")) {
                        doubleChest.setContents(SeeInventoryAPI.setItems(SeeInventoryAPI.INVENTORY_PLAYER.get(player.getName()).getInventory().getContents()));
                    } else if (sourceItem.getName().equals("§r§6Открыть Эндер Сундук")) {
                        doubleChest.setContents(SeeInventoryAPI.setItems(SeeInventoryAPI.INVENTORY_PLAYER.get(player.getName()).getEnderChestInventory().getContents()));
                    } else if (sourceItem.getName().equals("§r§6Справка")) {
                        player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
                    } else if (sourceItem.getId() == Item.UNDYED_SHULKER_BOX) {
                        ListTag<CompoundTag> list = (ListTag<CompoundTag>) sourceItem.getNamedTag().getList("Items");
                        if (list != null) {
                            for (CompoundTag compound : list.getAll()) {
                                Item item = NBTIO.getItemHelper(compound);
                                for (int i = 0; i < list.size(); i++) {
                                    shulkerItems.put(i, item);
                                }
                                doubleChest.setContents(SeeInventoryAPI.setItems(shulkerItems));
                                shulkerItems.clear();
                            }
                        }
                    } else {
                        player.getLevel().addSound(player, Sound.NOTE_BASSATTACK, 1, 1, player);
                    }
                }
            }
        }
    }
}