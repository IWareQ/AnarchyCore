package ru.jl1mbo.AnarchyCore.GameHandler.Auction.Task;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.AuctionAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.Utils.TradeItem;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

public class AuctionUpdateTask extends Task {

    @Override()
    public void onRun(int tick) {
        Iterator<Map.Entry<String, TradeItem>> iterator = AuctionAPI.AUCTION.entrySet().iterator();
        while (iterator.hasNext()) {
            TradeItem tradeItem = (TradeItem) ((Map.Entry) iterator.next()).getValue();
            if (!tradeItem.isValid()) {
                Player player = Server.getInstance().getPlayerExact(tradeItem.sellerName);
                if (player != null) {
                    player.sendMessage(AuctionAPI.PREFIX + "§fВаш товар никто не купил§7, §fпоэтому мы вернули его обртано\n§l§6| §r§fСмотрите вкладку §7(§6Хранилище§7) §fв §7/§6ah");
                }
                Config config = ConfigUtils.getAuctionStorageConfig(tradeItem.sellerName);
                try {
                    config.set(tradeItem.UUID, tradeItem.sellItem.hasCompoundTag() ? new Object[]{tradeItem.sellItem.getId(), tradeItem.sellItem.getDamage(), tradeItem.sellItem.getCount(), NBTIO.write(tradeItem.sellItem.getNamedTag(), ByteOrder.LITTLE_ENDIAN)} : new Object[]{tradeItem.sellItem.getId(), tradeItem.sellItem.getDamage(), tradeItem.sellItem.getCount()});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                config.save();
                config.reload();
                iterator.remove();
            }
        }
    }
}