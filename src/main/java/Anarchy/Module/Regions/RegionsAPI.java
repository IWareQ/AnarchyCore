package Anarchy.Module.Regions;

import java.util.HashMap;
import java.util.Map;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Module.Permissions.Utils.GroupAllow;
import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;

public class RegionsAPI {
	public static String PREFIX = "§l§7(§cПриват§7) §r";
	public static String FREE = "§l§a| §fТерритория свободна! §l§a|",
	BUSY = "§l§c| §fТерритория не доступна для взаимодействия! §l§c|",
	BUSY_BY = "§l§c| §fТерритория занята Игроком §a{PLAYER} §l§c|",
	EDIT = "§l§cВнимание! §fВы изменяете чужой регион!",
	UNBREAK = "§l§c| §fЭтот блок невозможно сломать! §l§c|",
	UNPLACE = "§l§c| §fЭтот блок не возможно установить тут! §l§c|",
	BIOME = "§l§c| §fЭтот биом не доступен для строительства! §l§c|";
	public static Map <Integer, Integer> REGIONS = new HashMap <>();

	public static void register() {
		REGIONS.put(Item.IRON_BLOCK, 3);
		REGIONS.put(Item.DIAMOND_BLOCK, 6);
		REGIONS.put(Item.EMERALD_BLOCK, 10);
	}

	public static void placeRegion(Player player, Block block, BlockPlaceEvent event) {
		if (player.getLevel() != FunctionsAPI.WORLD) {
			player.sendMessage(PREFIX + "§fРазместите блок в обычном мире!");
			event.setCancelled();
			return;
		}

		GroupAllow groupAllow = PermissionsAPI.getGroupAllows(PermissionsAPI.getGroup(player));
		if (groupAllow != null) {
			int regionCount = RegionsAPI.getRegionsCount(player.getName());
			if (regionCount >= groupAllow.MAX_REGIONS) {
				player.sendMessage(RegionsAPI.PREFIX + "§fМаксимальное количество регионов §7- §c" + regionCount + "\n§l§e| §r§fЧтобы содать новый регион потребуется удалить §e§l1 §r§fиз старых!");
				event.setCancelled();
				return;
			}
		}

		int radius = REGIONS.get(block.getId()),
		x = block.getFloorX(),
		y = block.getFloorY(),
		z = block.getFloorZ();
		int[] pos1 = {
			Math.min(x - radius, x + radius),
			y - radius,
			Math.min(z - radius, z + radius)
		};
		int[] pos2 = {
			Math.max(x - radius, x + radius),
			y + radius,
			Math.max(z - radius, z + radius)
		};

		if (!RegionsAPI.canCreateRegion(pos1[0], pos2[0], pos1[1], pos2[1], pos1[2], pos2[2])) {
			player.sendMessage(PREFIX + "§fРядом уже установлен чужой регион!\n§l§e| §r§fДля проверки владений используйте палку!");
			event.setCancelled();
			return;
		}

		player.sendMessage(PREFIX + "§fВы создали приват! §7(§fРадиус §a" + radius + " §fбл.§7)\n§l§e| §r§fДля проверки владений используйте палку!");
		SQLiteUtils.query("Regions.db", "INSERT INTO `AREAS` (`DATE_REG`, `Username`, `Main_X`, `Main_Y`, `Main_Z`, `Pos1_X`, `Pos1_Y`, `Pos1_Z`, `Pos2_X`, `Pos2_Y`, `Pos2_Z`) VALUES ('" + StringUtils.getDate() + "', '" + player.getName() + "', '" + x + "', '" + y + "', '" + z + "', '" + pos1[0] + "', '" + pos1[1] + "', '" + pos1[2] + "', '" + pos2[0] + "', '" + pos2[1] + "', '" + pos2[2] + "');");
	}

	public static boolean canInteractHere(Player player, Location location) {
		int region_id = getRegionIDByLocation(location);
		if (region_id != -1) {
			return isRegionMember(player.getName(), region_id) || isRegionOwner(player.getName(), region_id);
		}
		return true;
	}

	public static boolean isRegionOwner(String playerName, int regionID) {
		return playerName.equalsIgnoreCase(getRegionOwner(regionID));
	}

	public static boolean isRegionMember(String playerName, int regionID) {
		return SQLiteUtils.selectInteger("Regions.db", "SELECT `Member_ID` FROM `MEMBERS` WHERE UPPER(`Username`) = '" + playerName.toUpperCase() + "' AND `Region_ID` = '" + regionID + "';") != -1;
	}

	public static String getRegionOwner(int regionID) {
		return SQLiteUtils.selectString("Regions.db", "SELECT `Username` FROM `AREAS` WHERE (`Region_ID` = '" + regionID + "');");
	}

	public static Map <String, String> getRegionInfo(int regionID) {
		return SQLiteUtils.selectStringMap("Regions.db", "SELECT * FROM `AREAS` WHERE (`Region_ID` = '" + regionID + "');");
	}

	public static Integer getRegionIDByPosition(Position position) {
		return SQLiteUtils.selectInteger("Regions.db", "SELECT `Region_ID` FROM `AREAS` WHERE (`Pos1_X`<= " + position.x + " AND " + position.x + "<= `Pos2_X`) AND (`Pos1_Y`<= " + position.y + " AND " + position.y + "<= `Pos2_Y`) AND (`Pos1_Z`<= " + position.z + " AND " + position.z + "<= `Pos2_Z`);");
	}

	public static Integer getRegionIDByLocation(Location location) {
		return SQLiteUtils.selectInteger("Regions.db", "SELECT `Region_ID` FROM `AREAS` WHERE (`Pos1_X`<= " + location.x + " AND " + location.x + "<= `Pos2_X`) AND (`Pos1_Y`<= " + location.y + " AND " + location.y + "<= `Pos2_Y`) AND (`Pos1_Z`<= " + location.z + " AND " + location.z + "<= `Pos2_Z`);");
	}

	public static boolean canCreateRegion(int min_x, int max_x, int min_y, int max_y, int min_z, int max_z) {
		return SQLiteUtils.selectInteger("Regions.db", "SELECT `Region_ID` FROM `AREAS` WHERE `Pos2_X` >= " + min_x + " AND `Pos1_X`<= " + max_x + " AND `Pos2_Y` >= " + min_y + " AND `Pos1_Y`<= " + max_y + " AND `Pos2_Z` >= " + min_z + " AND `Pos1_Z`<= " + max_z + ";") == -1;
	}

	public static int getRegionsCount(String playerName) {
		return SQLiteUtils.selectInteger("Regions.db", "SELECT COUNT(*) as COUNT FROM `AREAS` WHERE UPPER(`Username`) = '" + playerName.toUpperCase() + "';");
	}
}