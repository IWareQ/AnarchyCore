package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks.DefaultBlockProtection;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks.DiamondOreProtection;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks.EmeraldBlockProtection;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks.EmeraldOreProtection;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks.IronBlockProtection;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Commands.RegionCommand;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener.BlockBreakListener;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener.BlockPistonListener;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener.BlockPlaceListener;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener.EntityExplodeListener;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener.ItemFrameDropItemListener;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener.PlayerInteractListener;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Utils.SQLiteUtils;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DefaultGroup;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class BlockProtectionAPI {
	private static HashMap<Integer, DefaultBlockProtection> BLOCK_PROTECTION = new HashMap<>();
	public static String PREFIX = "§l§7(§6Регионы§7) §r";

	public static void register() {
		registerBlocks();
		new File(Main.getInstance().getDataFolder() + "/BlockProtection").mkdir();
		Server.getInstance().getCommandMap().register("", new RegionCommand());
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new BlockBreakListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockPistonListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockPlaceListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityExplodeListener(), Main.getInstance());
		pluginManager.registerEvents(new ItemFrameDropItemListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerInteractListener(), Main.getInstance());
		createSQLiteTable();
	}
	
	private static void registerBlocks() {
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

	private static void createSQLiteTable() {
		SQLiteUtils.query("CREATE TABLE IF NOT EXISTS MEMBERS (Member_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Username TEXT, Region_ID INTEGER);");
		SQLiteUtils.query("CREATE TABLE IF NOT EXISTS AREAS (Region_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, DATE_REG TEXT, Username TEXT, Main_X INTEGER, Main_Y INTEGER, Main_Z INTEGER, Pos1_X INTEGER, Pos1_Y INTEGER, Pos1_Z INTEGER, Pos2_X INTEGER, Pos2_Y INTEGER, Pos2_Z INTEGER);");
	}

	public static void placeRegion(Player player, Block block) {
		if (player.getLevel() != WorldSystemAPI.getMap()) {
			player.sendMessage(PREFIX + "§fРазместите блок в обычном мире§7!");
			player.sendTitle("§l§cОшибка");
			return;
		}
		DefaultGroup defaultGroup = PermissionAPI.getAllGroups().get(PermissionAPI.getGroup(player.getName()));
		if (defaultGroup != null) {
			int regionCount = getRegionsCount(player.getName());
			if (regionCount >= defaultGroup.getMaxRegions()) {
				player.sendMessage(BlockProtectionAPI.PREFIX + "§fВы уже разместили максимальное количество §6Регионов §7(§6" + regionCount + "§7)");
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
			player.sendMessage(PREFIX + "§fНе возможно установить блок в выбраном месте из§7-§fза пересечения регионов§7!");
			player.sendTitle("§l§cОшибка");
			return;
		}
		player.sendMessage(PREFIX +
						   "§fВы успешно создали новый " + BLOCK_PROTECTION.get(block.getId()).getBlockName() + "§7!");
		SQLiteUtils.query("INSERT INTO AREAS (DATE_REG, Username, Main_X, Main_Y, Main_Z, Pos1_X, Pos1_Y, Pos1_Z, Pos2_X, Pos2_Y, Pos2_Z) VALUES ('" + Utils.getDate() + "', '" + player.getName() + "', '" + x
						  + "', '" + y + "', '" + z + "', '" + pos1[0] + "', '" + pos1[1] + "', '" + pos1[2] + "', '" + pos2[0] + "', '" + pos2[1] + "', '" + pos2[2] + "');");
	}

	public static boolean canInteractHere(Player player, Location location) {
		int region_id = getRegionIDByLocation(location);
		if (region_id != -1) {
			return isRegionMember(player.getName(), region_id) && !isRegionOwner(player.getName(), region_id);
		}
		return false;
	}

	public static boolean isRegionOwner(String playerName, int regionID) {
		return playerName.equalsIgnoreCase(getRegionOwner(regionID));
	}

	public static boolean isRegionMember(String playerName, int regionID) {
		return SQLiteUtils.selectInteger("SELECT Member_ID FROM MEMBERS WHERE UPPER(Username) = '" + playerName.toUpperCase() + "' AND Region_ID = '" + regionID + "';") == null;
	}

	public static String getRegionOwner(int regionID) {
		return SQLiteUtils.selectString("SELECT Username FROM AREAS WHERE (Region_ID = '" + regionID + "');");
	}

	public static Integer getRegionIDByPosition(Position position) {
		if (SQLiteUtils.selectInteger("SELECT Region_ID FROM AREAS WHERE (Pos1_X<= " + position.getFloorX() + " AND " + position.getFloorX() + "<= Pos2_X) AND (Pos1_Y<= " + position.getFloorY() + " AND " +
									  position.getFloorY() + "<= Pos2_Y) AND (Pos1_Z<= " + position.getFloorZ() + " AND " + position.getFloorZ() + "<= Pos2_Z);") == null) {
			return -1;
		} else {
			return SQLiteUtils.selectInteger("SELECT Region_ID FROM AREAS WHERE (Pos1_X<= " + position.getFloorX() + " AND " + position.getFloorX() + "<= Pos2_X) AND (Pos1_Y<= " + position.getFloorY() + " AND " +
											 position.getFloorY() + "<= Pos2_Y) AND (Pos1_Z<= " + position.getFloorZ() + " AND " + position.getFloorZ() + "<= Pos2_Z);");
		}
	}

	public static Integer getRegionIDByLocation(Location location) {
		if (SQLiteUtils.selectInteger("SELECT Region_ID FROM AREAS WHERE (Pos1_X<= " + location.getFloorX() + " AND " + location.getFloorX() + "<= Pos2_X) AND (Pos1_Y<= " + location.getFloorY() + " AND " +
									  location.getFloorY() + "<= Pos2_Y) AND (Pos1_Z<= " + location.getFloorZ() + " AND " + location.getFloorZ() + "<= Pos2_Z);") == null) {
			return -1;
		} else {
			return SQLiteUtils.selectInteger("SELECT Region_ID FROM AREAS WHERE (Pos1_X<= " + location.getFloorX() + " AND " + location.getFloorX() + "<= Pos2_X) AND (Pos1_Y<= " + location.getFloorY() + " AND " +
											 location.getFloorY() + "<= Pos2_Y) AND (Pos1_Z<= " + location.getFloorZ() + " AND " + location.getFloorZ() + "<= Pos2_Z);");
		}
	}

	public static boolean canCreateRegion(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
		return SQLiteUtils.selectInteger("SELECT Region_ID FROM AREAS WHERE Pos2_X >= " + minX + " AND Pos1_X<= " + maxX + " AND Pos2_Y >= " + minY + " AND Pos1_Y<= " + maxY + " AND Pos2_Z >= " + minZ +
										 " AND Pos1_Z<= " + maxZ + ";") == null;
	}

	public static int getRegionsCount(String playerName) {
		return SQLiteUtils.selectInteger("SELECT COUNT(*) as COUNT FROM AREAS WHERE UPPER(Username) = '" + playerName.toUpperCase() + "';");
	}

	public static List<String> getRegionMembers(int regionID) {
		return SQLiteUtils.selectStringList("SELECT Username FROM MEMBERS WHERE Region_ID = '" + regionID + "';");
	}
}