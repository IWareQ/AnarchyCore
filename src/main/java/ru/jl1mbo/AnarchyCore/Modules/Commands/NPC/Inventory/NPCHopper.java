package ru.jl1mbo.AnarchyCore.Modules.Commands.NPC.Inventory;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.Inventory.DefaultHopper;
import ru.jl1mbo.AnarchyCore.Modules.CustomRecipes.Utils.CustomItemID;

public class NPCHopper extends DefaultHopper {

	public NPCHopper() {
		super("§6Собиратель Артефактов");
	}

	@Override
	public void onOpen(Player player) {
		super.onOpen(player);
		this.addItem(Item.get(CustomItemID.TARANTULA_WEB).setLore("\n§rНажмите§7, §fчтобы §6обменяться§7!"));
		this.addItem(Item.get(CustomItemID.SUN_WAND).setLore("\n§rНажмите§7, §fчтобы §6обменяться§7!"));
		this.addItem(Item.get(CustomItemID.PIECE_ICE).setLore("\n§rНажмите§7, §fчтобы §6обменяться§7!"));
		this.addItem(Item.get(CustomItemID.REMAINS_GUARDIAN).setLore("\n§rНажмите§7, §fчтобы §6обменяться§7!"));
		this.addItem(Item.get(CustomItemID.GOLDEN_MONEY).setLore("\n§rЭтот §6предмет §fВы §6получите §fпосле обмена§7.\n§fИспользуйте его на §6алтаре§7!"));
	}
}