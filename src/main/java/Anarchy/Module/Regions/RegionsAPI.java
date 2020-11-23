package Anarchy.Module.Regions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Anarchy.Functions.FunctionsAPI;
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
import cn.nukkit.level.Sound;

public class RegionsAPI {
	public static String PREFIX = "§l§7(§3Регионы§7) §r";
	public static String FREE = "  §l§fТерритория свободна§7!";
	public static String BUSY = "  §l§fТерритория не доступна для взаимодействия§7!";
	public static String BUSY_BY = "  §l§fТерритория занята Игроком §3{PLAYER}";
	public static String UNBREAK = "  §l§fЭтот блок невозможно сломать§7!";
	public static String UNPLACE = "  §l§fЭтот блок не возможно установить тут§7!";
	public static String BIOME = "  §l§fЭтот биом не доступен для строительства§7!";
	public static Map<Integer, Integer> REGIONS = new HashMap<>();

	public static void register() {
		REGIONS.put(Item.IRON_BLOCK, 2);
		REGIONS.put(Item.DIAMOND_ORE, 4);
		REGIONS.put(Item.EMERALD_ORE, 8);
		REGIONS.put(Item.EMERALD_BLOCK, 10);
	}

	public static void placeRegion(Player player, Block block, BlockPlaceEvent event) {
		if (player.getLevel() != FunctionsAPI.MAP) {
			player.sendMessage(PREFIX + "§fРазместите блок в обычном мире§7!");
			player.sendTitle("§l§cОшибка");
			return;
		}
		GroupAllow groupAllow = PermissionsAPI.getGroupAllows(PermissionsAPI.getGroup(player.getName()));
		if (groupAllow != null) {
			int regionCount = RegionsAPI.getRegionsCount(player.getName());
			if (regionCount >= groupAllow.MAX_REGIONS) {
				player.sendMessage(RegionsAPI.PREFIX + "§fВы уже разместили максимальное количество §6Регионов §7(§6" + regionCount + "§7)");
				player.sendTitle("§l§cОшибка");
				return;
			}
		}
		int radius = REGIONS.get(block.getId());
		int x = block.getFloorX();
		int y = block.getFloorY();
		int z = block.getFloorZ();
		int[] pos1 = {Math.min(x - radius, x + radius), y - radius, Math.min(z - radius, z + radius)};
		int[] pos2 = {Math.max(x - radius, x + radius), y + radius, Math.max(z - radius, z + radius)};
		if (!RegionsAPI.canCreateRegion(pos1[0], pos2[0], pos1[1], pos2[1], pos1[2], pos2[2])) {
			player.sendMessage(PREFIX + "§fНе возможно установить блок в выбраном месте из§7-§fза пересечения регионов§7!");
			player.sendTitle("§l§cОшибка");
			return;
		}
		player.sendMessage(PREFIX + "§fВы успешно создали новый защищеный регион§7!\n§l§6• §r§fДля проверки владений используйте палку§7!");
		SQLiteUtils.query("Regions.db", "INSERT INTO AREAS (DATE_REG, Username, Main_X, Main_Y, Main_Z, Pos1_X, Pos1_Y, Pos1_Z, Pos2_X, Pos2_Y, Pos2_Z) VALUES ('" + StringUtils.getDate() + "', '" + player.getName() + "', '" + x + "', '" + y + "', '" + z + "', '" + pos1[0] + "', '" + pos1[1] + "', '" + pos1[2] + "', '" + pos2[0] + "', '" + pos2[1] + "', '" + pos2[2] + "');");
		player.getLevel().addSound(player, Sound.HIT_ANVIL, 1, 1, player);
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
		return SQLiteUtils.selectInteger("Regions.db", "SELECT Member_ID FROM MEMBERS WHERE UPPER(Username) = '" + playerName.toUpperCase() + "' AND Region_ID = '" + regionID + "';") != -1;
	}

	public static String getRegionOwner(int regionID) {
		return SQLiteUtils.selectString("Regions.db", "SELECT Username FROM AREAS WHERE (Region_ID = '" + regionID + "');");
	}

	public static Map<String, String> getRegionInfo(int regionID) {
		return SQLiteUtils.selectStringMap("Regions.db", "SELECT * FROM AREAS WHERE (Region_ID = '" + regionID + "');");
	}

	public static Integer getRegionIDByPosition(Position position) {
		return SQLiteUtils.selectInteger("Regions.db", "SELECT Region_ID FROM AREAS WHERE (Pos1_X<= " + position.x + " AND " + position.x + "<= Pos2_X) AND (Pos1_Y<= " + position.y + " AND " + position.y + "<= Pos2_Y) AND (Pos1_Z<= " + position.z + " AND " + position.z + "<= Pos2_Z);");
	}

	public static Integer getRegionIDByLocation(Location location) {
		return SQLiteUtils.selectInteger("Regions.db", "SELECT Region_ID FROM AREAS WHERE (Pos1_X<= " + location.x + " AND " + location.x + "<= Pos2_X) AND (Pos1_Y<= " + location.y + " AND " + location.y + "<= Pos2_Y) AND (Pos1_Z<= " + location.z + " AND " + location.z + "<= Pos2_Z);");
	}

	public static boolean canCreateRegion(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
		return SQLiteUtils.selectInteger("Regions.db", "SELECT Region_ID FROM AREAS WHERE Pos2_X >= " + minX + " AND Pos1_X<= " + maxX + " AND Pos2_Y >= " + minY + " AND Pos1_Y<= " + maxY + " AND Pos2_Z >= " + minZ + " AND Pos1_Z<= " + maxZ + ";") == -1;
	}

	public static int getRegionsCount(String playerName) {
		return SQLiteUtils.selectInteger("Regions.db", "SELECT COUNT(*) as COUNT FROM AREAS WHERE UPPER(Username) = '" + playerName.toUpperCase() + "';");
	}

	public static ArrayList<String> getRegionMembers(int regionID) {
		return SQLiteUtils.selectList("Regions.db", "SELECT Username FROM MEMBERS WHERE Region_ID = '" + regionID + "';");
	}
}