package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CustomManager;

import java.util.Collections;
import java.util.HashMap;

import cn.nukkit.Server;
import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;

public class CustomManagerAPI {

	public static void register() {

	}

	private static void registerElytraCraft() {
		Item elytra = Item.get(Item.ELYTRA);
		String[] shape =  {"ABA", "ACA", "ADA"};
		HashMap<Character, Item> ingredients = new HashMap<>();
		ingredients.put('A', Item.get(Item.PHANTOM_MEMBRANE));
		ingredients.put('B', Item.get(Item.LEAD));
		ingredients.put('C', Item.get(Item.LEATHER));
		ingredients.put('D', Item.get(Item.STRING));
		Server.getInstance().getCraftingManager().registerRecipe(new ShapedRecipe(elytra, shape, ingredients, Collections.emptyList()));
		Server.getInstance().getCraftingManager().rebuildPacket();
	}
}