package ru.iwareq.anarchycore.module.BlockProtection;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.BlockProtection.Blocks.DefaultBlockProtection;
import ru.iwareq.anarchycore.module.BlockProtection.Blocks.Protect.DiamondOreProtection;
import ru.iwareq.anarchycore.module.BlockProtection.Blocks.Protect.EmeraldBlockProtection;
import ru.iwareq.anarchycore.module.BlockProtection.Blocks.Protect.EmeraldOreProtection;
import ru.iwareq.anarchycore.module.BlockProtection.Blocks.Protect.IronBlockProtection;
import ru.iwareq.anarchycore.module.Permissions.Group.DefaultGroup;
import ru.iwareq.anarchycore.module.Permissions.PermissionAPI;

import java.util.HashMap;
import java.util.List;

public class BlockProtectionAPI {

	public static final String PREFIX = "§l§7(§6Регионы§7) §r";

	private static final HashMap<Integer, DefaultBlockProtection> BLOCK_PROTECTION = new HashMap<>();

	private static final BlockProtectionDB DB = new BlockProtectionDB();

	public static void register() {
		registerBlockProtection(new IronBlockProtection());
		registerBlockProtection(new DiamondOreProtection());
		registerBlockProtection(new EmeraldOreProtection());
		registerBlockProtection(new EmeraldBlockProtection());
	}

	private static void registerBlockProtection(DefaultBlockProtection blockProtection) {
		BLOCK_PROTECTION.put(blockProtection.getBlockId(), blockProtection);
	}

	public static HashMap<Integer, DefaultBlockProtection> getAllBlocks() {
		return BLOCK_PROTECTION;
	}

	public static void placeRegion(Player player, Block block) {
		if (player.getLevel() != WorldSystemAPI.Map) {
			player.sendMessage(PREFIX + "Разместите блок в §6обычном мире§7!");
			player.sendTitle("§l§cОшибка");
			return;
		}
		DefaultGroup defaultGroup = PermissionAPI.getPlayerGroup(player.getName());
		if (defaultGroup != null) {
			int regionCount = DB.getRegionsCount(player.getName());
			if (regionCount >= defaultGroup.getMaxRegions()) {
				player.sendMessage(BlockProtectionAPI.PREFIX + "Вы уже разместили §6максимальное §fколичество §6Регионов §7(§6" + regionCount + "§7)");
				player.sendTitle("§l§cОшибка");
				return;
			}
		}
		int radius = BLOCK_PROTECTION.get(block.getId()).getRadius();
		int x = block.getFloorX();
		int y = block.getFloorY();
		int z = block.getFloorZ();
		int[] pos1 = {Math.min(x - radius, x + radius), y - radius, Math.min(z - radius, z + radius)};
		int[] pos2 = {Math.max(x - radius, x + radius), y + radius, Math.max(z - radius, z + radius)};
		if (!DB.canCreateRegion(pos1[0], pos2[0], pos1[1], pos2[1], pos1[2], pos2[2])) {
			player.sendMessage(PREFIX + "Не возможно установить блок §6в выбраном месте §fиз§7-§fза §6пересечения §fрегионов§7!");
			player.sendTitle("§l§cОшибка");
			return;
		}
		player.sendMessage(PREFIX + "Вы успешно создали новый " + BLOCK_PROTECTION.get(block.getId()).getBlockName() + "§7!");
		DB.createRegion(player, x, y, z, pos1[0], pos1[1], pos1[2], pos2[0], pos2[1], pos2[2]);
	}

	public static boolean canInteractHere(Player player, Location location) {
		int region_id = DB.getRegionIDByPosition(location);
		if (region_id != -1) {
			if (player.isOp()) {
				return true;
			}

			return DB.isRegionMember(player.getName(), region_id) || isRegionOwner(player.getName(), region_id);
		}
		return true;
	}

	public static boolean isRegionOwner(String playerName, int regionId) {
		return playerName.equalsIgnoreCase(DB.getRegionOwner(regionId));
	}

	public static int getRegionIDByPosition(Location location) {
		return DB.getRegionIDByPosition(location);
	}

	public static Location getRegionBlockLocation(int regionId) {
		return DB.getRegionBlockLocation(regionId);
	}

	public static void deleteRegion(int regionId) {
		DB.deleteRegion(regionId);
	}

	public static List<String> getRegionMembers(int regionId) {
		return DB.getRegionMembers(regionId);
	}

	public static void removeMember(String targetName, int regionID) {
		DB.removeMember(targetName, regionID);
	}

	public static List<Integer> getRegionsByName(String name) {
		return DB.getRegionsByName(name);
	}

	public static List<Integer> getRegionsMembers(String name) {
		return DB.getRegionsMembers(name);
	}

	public static String getRegionOwner(int region_id) {
		return DB.getRegionOwner(region_id);
	}

	public static boolean isRegionMember(String targetName, int regionID) {
		return DB.isRegionMember(targetName, regionID);
	}

	public static void addMember(String targetName, int regionID) {
		DB.addMember(targetName, regionID);
	}
}