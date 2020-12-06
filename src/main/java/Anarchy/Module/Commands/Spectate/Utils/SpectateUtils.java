package Anarchy.Module.Commands.Spectate.Utils;

import java.util.Map;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;

public class SpectateUtils {
	private String spectateName;
	private Integer X;
	private Integer Y;
	private Integer Z;
	private Map<Integer, Item> items;

	public SpectateUtils(String spectateName, Integer X, Integer Y, Integer Z, Map<Integer, Item> items) {
		this.spectateName =	spectateName;
		this.X =	X;
		this.Y =	Y;
		this.Z =	Z;
		this.items =	items;
	}

	public String getSpectateName() {
		return spectateName;
	}

	public Position getPlayerPosition() {
		return new Position(X, Y, Z);
	}

	public Map<Integer, Item> getPlayerInventory() {
		return items;
	}
}