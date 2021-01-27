package AnarchySystem.Components.Bosses.Items;

import cn.nukkit.item.Item;
import ru.dragonestia.ironlib.item.PrefabItem;

import javax.annotation.Nonnull;

public class CobwebArtifact extends PrefabItem {

    public CobwebArtifact(Item item) {
        super(item);
        item.setCustomName("§r§6Паутина Таратнула");
        item.setLore("\n§r§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7!");
    }

    @Nonnull()
    @Override()
    public String getKey() {
        return "cobweb_artifact";
    }

    @Override()
    public boolean isIgnoreDamage() {
        return false;
    }
}