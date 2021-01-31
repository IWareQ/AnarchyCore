package ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.Item;

import cn.nukkit.item.Item;
import ru.dragonestia.ironlib.item.PrefabItem;

import javax.annotation.Nonnull;

public class GoldenMoneyItem extends PrefabItem {

    public GoldenMoneyItem(Item item) {
        super(item);
        item.setCustomName("§r§6Золотая Монета");
        item.setLore("\n§r§fКинь §6Золотую Монетку §fв §6Зачарованный\nАлтарь§7, §fкоторый находится на спавне§7.\n§fС §6шансом 20§7% §fты можешь получить\nодну из ценных§7, §fа может и не очень вещей§7!");
    }

    @Nonnull()
    @Override()
    public String getKey() {
        return "golden_money";
    }

    @Override()
    public boolean isIgnoreDamage() {
        return false;
    }
}