package AnarchySystem.Manager.FakeInventory.Utils;

import cn.nukkit.Player;
import cn.nukkit.math.BlockVector3;

import java.util.List;
import java.util.Optional;

public class FakeChests {

    public List<BlockVector3> getFakeInventoryPositions(Player player) {
        FakeChest inventory = FakeChest.open.get(player);
        return inventory == null ? null : inventory.getPosition(player);
    }

    public Optional<FakeChest> getFakeInventory(Player player) {
        return Optional.ofNullable(FakeChest.open.get(player));
    }

    public void removeFakeInventory(FakeChest fakeChest) {
        fakeChest.close();
    }
}