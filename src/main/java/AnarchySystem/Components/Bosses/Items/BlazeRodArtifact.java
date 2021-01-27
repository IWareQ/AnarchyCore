package AnarchySystem.Components.Bosses.Items;

import cn.nukkit.item.Item;
import ru.dragonestia.ironlib.item.PrefabItem;

import javax.annotation.Nonnull;

public class BlazeRodArtifact extends PrefabItem {

    public BlazeRodArtifact(Item item) {
        super(item);
        item.setCustomName("§r§6Жезл Солнца");
        item.setLore("\n§r§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7!");
    }

    @Nonnull()
    @Override()
    public String getKey() {
        return "blaze_rod_artifact";
    }

    @Override()
    public boolean isIgnoreDamage() {
        return false;
    }
}