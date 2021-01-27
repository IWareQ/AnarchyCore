package AnarchySystem.Components.BlockProtection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AnarchySystem.Main;
import AnarchySystem.Components.BlockProtection.EventsListener.BlockBreakListener;
import AnarchySystem.Components.BlockProtection.EventsListener.BlockPistonListener;
import AnarchySystem.Components.BlockProtection.EventsListener.BlockPlaceListener;
import AnarchySystem.Components.BlockProtection.EventsListener.EntityExplodeListener;
import AnarchySystem.Components.BlockProtection.EventsListener.ItemFrameDropItemListener;
import AnarchySystem.Components.BlockProtection.EventsListener.PlayerInteractListener;
import AnarchySystem.Components.BlockProtection.Utils.SQLiteUtils;
import AnarchySystem.Components.Permissions.PermissionAPI;
import AnarchySystem.Components.Permissions.Utils.GroupAllow;
import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import AnarchySystem.Utils.Utils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.plugin.PluginManager;

public class BlockProtectionAPI {
    public static String PREFIX = "§l§7(§6Регионы§7) §r";
    public static String FREE = "  §l§fТерритория свободна§7!";
    public static String BUSY = "  §l§fТерритория не доступна для взаимодействия§7!";
    public static String BUSY_BY = "  §l§fТерритория занята Игроком §6{PLAYER}";
    public static String UNBREAK = "  §l§fЭтот блок невозможно сломать§7!";
    public static String UNPLACE = "  §l§fЭтот блок не возможно установить тут§7!";
    public static String BIOME = "  §l§fЭтот биом не доступен для строительства§7!";
    public static Map<Integer, Integer> REGIONS = new HashMap<>();

    public static void register() {
        registerBlocks();
        registerEvents();
    }
    
    private static void registerBlocks() {
    	REGIONS.put(Item.IRON_BLOCK, 2);
        REGIONS.put(Item.DIAMOND_ORE, 4);
        REGIONS.put(Item.EMERALD_ORE, 8);
        REGIONS.put(Item.EMERALD_BLOCK, 10);
    }
    
    private static void registerEvents() {
    	PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new BlockBreakListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockPistonListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockPlaceListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityExplodeListener(), Main.getInstance());
		pluginManager.registerEvents(new ItemFrameDropItemListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerInteractListener(), Main.getInstance());
    }

    public static void placeRegion(Player player, Block block) {
        if (player.getLevel() != WorldSystemAPI.getMap()) {
            player.sendMessage(PREFIX + "§fРазместите блок в обычном мире§7!");
            player.sendTitle("§l§cОшибка");
            return;
        }
        GroupAllow groupAllow = PermissionAPI.getGroupAllows(PermissionAPI.getGroup(player.getName()));
        if (groupAllow != null) {
            int regionCount = getRegionsCount(player.getName());
            if (regionCount >= groupAllow.getMaxRegions()) {
                player.sendMessage(BlockProtectionAPI.PREFIX + "§fВы уже разместили максимальное количество §6Регионов §7(§6" + regionCount + "§7)");
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
        if (!canCreateRegion(pos1[0], pos2[0], pos1[1], pos2[1], pos1[2], pos2[2])) {
            player.sendMessage(PREFIX + "§fНе возможно установить блок в выбраном месте из§7-§fза пересечения регионов§7!");
            player.sendTitle("§l§cОшибка");
            return;
        }
        player.sendMessage(PREFIX + "§fВы успешно создали новый защищеный регион§7!\n§l§6• §r§fДля проверки владений используйте палку§7!");
        SQLiteUtils.query("INSERT INTO AREAS (DATE_REG, Username, Main_X, Main_Y, Main_Z, Pos1_X, Pos1_Y, Pos1_Z, Pos2_X, Pos2_Y, Pos2_Z) VALUES ('" + Utils.getDate() + "', '" + player.getName() + "', '" + x + "', '" + y + "', '" + z + "', '" + pos1[0] + "', '" + pos1[1] + "', '" + pos1[2] + "', '" + pos2[0] + "', '" + pos2[1] + "', '" + pos2[2] + "');");
        player.getLevel().addSound(player, Sound.HIT_ANVIL, 1, 1, player);
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
        if (SQLiteUtils.selectInteger("SELECT Region_ID FROM AREAS WHERE (Pos1_X<= " + position.getFloorX() + " AND " + position.getFloorX() + "<= Pos2_X) AND (Pos1_Y<= " + position.getFloorY() + " AND " + position.getFloorY() + "<= Pos2_Y) AND (Pos1_Z<= " + position.getFloorZ() + " AND " + position.getFloorZ() + "<= Pos2_Z);") == null) {
            return -1;
        } else {
            return SQLiteUtils.selectInteger("SELECT Region_ID FROM AREAS WHERE (Pos1_X<= " + position.getFloorX() + " AND " + position.getFloorX() + "<= Pos2_X) AND (Pos1_Y<= " + position.getFloorY() + " AND " + position.getFloorY() + "<= Pos2_Y) AND (Pos1_Z<= " + position.getFloorZ() + " AND " + position.getFloorZ() + "<= Pos2_Z);");
        }
    }

    public static Integer getRegionIDByLocation(Location location) {
        if (SQLiteUtils.selectInteger("SELECT Region_ID FROM AREAS WHERE (Pos1_X<= " + location.getFloorX() + " AND " + location.getFloorX() + "<= Pos2_X) AND (Pos1_Y<= " + location.getFloorY() + " AND " + location.getFloorY() + "<= Pos2_Y) AND (Pos1_Z<= " + location.getFloorZ() + " AND " + location.getFloorZ() + "<= Pos2_Z);") == null) {
            return -1;
        } else {
            return SQLiteUtils.selectInteger("SELECT Region_ID FROM AREAS WHERE (Pos1_X<= " + location.getFloorX() + " AND " + location.getFloorX() + "<= Pos2_X) AND (Pos1_Y<= " + location.getFloorY() + " AND " + location.getFloorY() + "<= Pos2_Y) AND (Pos1_Z<= " + location.getFloorZ() + " AND " + location.getFloorZ() + "<= Pos2_Z);");
        }
    }

    public static boolean canCreateRegion(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        return SQLiteUtils.selectInteger("SELECT Region_ID FROM AREAS WHERE Pos2_X >= " + minX + " AND Pos1_X<= " + maxX + " AND Pos2_Y >= " + minY + " AND Pos1_Y<= " + maxY + " AND Pos2_Z >= " + minZ + " AND Pos1_Z<= " + maxZ + ";") == null;
    }

    public static int getRegionsCount(String playerName) {
        return SQLiteUtils.selectInteger("SELECT COUNT(*) as COUNT FROM AREAS WHERE UPPER(Username) = '" + playerName.toUpperCase() + "';");
    }

    public static List<String> getRegionMembers(int regionID) {
        return SQLiteUtils.selectStringList("SELECT Username FROM MEMBERS WHERE Region_ID = '" + regionID + "';");
    }
}