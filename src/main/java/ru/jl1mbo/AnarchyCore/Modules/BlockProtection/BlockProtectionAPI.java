package ru.jl1mbo.AnarchyCore.Modules.BlockProtection;

import java.util.HashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.Blocks.DefaultBlockProtection;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.Blocks.Protect.DiamondOreProtection;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.Blocks.Protect.EmeraldBlockProtection;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.Blocks.Protect.EmeraldOreProtection;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.Blocks.Protect.IronBlockProtection;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DefaultGroup;
import ru.jl1mbo.MySQLUtils.MySQLUtils;

public class BlockProtectionAPI {
	private static HashMap<Integer, DefaultBlockProtection> BLOCK_PROTECTION = new HashMap<>();
	public static String PREFIX = "§l§7(§6Регионы§7) §r";

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
			int regionCount = getRegionsCount(player.getName());
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
		if (!canCreateRegion(pos1[0], pos2[0], pos1[1], pos2[1], pos1[2], pos2[2])) {
			player.sendMessage(PREFIX + "Не возможно установить блок §6в выбраном месте §fиз§7-§fза §6пересечения §fрегионов§7!");
			player.sendTitle("§l§cОшибка");
			return;
		}
		player.sendMessage(PREFIX +
						   "Вы успешно создали новый " + BLOCK_PROTECTION.get(block.getId()).getBlockName() + "§7!");
		MySQLUtils.query("INSERT INTO `Regions` (`Name`, `Main_X`, `Main_Y`, `Main_Z`, `Pos1_X`, `Pos1_Y`, `Pos1_Z`, `Pos2_X`, `Pos2_Y`, `Pos2_Z`) VALUES ('" +
						  player.getName() + "', '" + x
						  + "', '" + y + "', '" + z + "', '" + pos1[0] + "', '" + pos1[1] + "', '" + pos1[2] + "', '" + pos2[0] + "', '" + pos2[1] + "', '" + pos2[2] + "')");
	}

	public static boolean canInteractHere(Player player, Location location) {
		int region_id = getRegionIDByLocation(location);
		if (region_id != -1) {
			return isRegionMember(player.getName(), region_id) || isRegionOwner(player.getName(), region_id);
		}
		return true;
	}

	public static boolean isRegionOwner(String playerName, int regionId) {
		return playerName.equalsIgnoreCase(getRegionOwner(regionId));
	}

	public static boolean isRegionMember(String playerName, int regionId) {
		return MySQLUtils.getInteger("SELECT `ID` FROM `RegionMembers` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "' AND (`RegionID`) = '" + regionId + "'") != -1;
	}

	public static String getRegionOwner(int regionId) {
		return MySQLUtils.getString("SELECT `Name` FROM `Regions` WHERE (`ID`) = '" + regionId + "'");
	}

	public static Integer getRegionIDByPosition(Position position) {
		return MySQLUtils.getInteger("SELECT `ID` FROM `Regions` WHERE (`Pos1_X` <= " + position.getFloorX() + " AND " + position.getFloorX() + " <= `Pos2_X`) AND (`Pos1_Y` <= " +
									  position.getFloorY() + " AND " + position.getFloorY() + " <= `Pos2_Y`) AND (`Pos1_Z` <= " + position.getFloorZ() + " AND " + position.getFloorZ() + " <= `Pos2_Z`);");
	}

	public static Integer getRegionIDByLocation(Location location) {
		return MySQLUtils.getInteger("SELECT `ID` FROM `Regions` WHERE (`Pos1_X` <= " + location.getFloorX() + " AND " + location.getFloorX() + " <= `Pos2_X`) AND (`Pos1_Y` <= " +
									  location.getFloorY() + " AND " + location.getFloorY() + " <= `Pos2_Y`) AND (`Pos1_Z` <= " + location.getFloorZ() + " AND " + location.getFloorZ() + " <= `Pos2_Z`);");
	}

	public static boolean canCreateRegion(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
		return MySQLUtils.getInteger("SELECT `ID` FROM `Regions` WHERE `Pos2_X` >= " + minX + " AND `Pos1_X` <= " + maxX + " AND `Pos2_Y` >= " + minY + " AND `Pos1_Y` <= " + maxY +
									  " AND `Pos2_Z` >= " + minZ + " AND `Pos1_Z` <= " + maxZ + "") == -1;
	}

	public static Location getRegionBlockLocation(int regionId) {
		int mainX = MySQLUtils.getInteger("SELECT `Main_X` FROM `Regions` WHERE (`ID`) = '" + regionId + "'");
		int mainY = MySQLUtils.getInteger("SELECT `Main_Y` FROM `Regions` WHERE (`ID`) = '" + regionId + "'");
		int mainZ = MySQLUtils.getInteger("SELECT `Main_Z` FROM `Regions` WHERE (`ID`) = '" + regionId + "'");
		return new Location(mainX, mainY, mainZ);
	}

	public static int getRegionsCount(String playerName) {
		return MySQLUtils.getInteger("SELECT COUNT(*) as COUNT FROM `Regions` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static List<String> getRegionMembers(int regionId) {
		return MySQLUtils.getStringList("SELECT `Name` FROM `RegionMembers` WHERE (`RegionID`) = '" + regionId + "'");
	}
}