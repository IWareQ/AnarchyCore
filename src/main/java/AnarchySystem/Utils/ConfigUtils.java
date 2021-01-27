package AnarchySystem.Utils;

import AnarchySystem.Main;
import cn.nukkit.utils.Config;

public class ConfigUtils {

	public static Config getSpectateConfig() {
		return new Config(Main.getInstance().getDataFolder() + "/AdminSystem/Spectate.yml", Config.YAML);
	}

	public static Config getPermissionConfig() {
		return new Config(Main.getInstance().getDataFolder() + "/Permission/Users.yml", Config.YAML);
	}

	public static Config getAuctionConfig() {
		return new Config(Main.getInstance().getDataFolder() + "/Auction/Items.yml", Config.YAML);
	}

	public static Config getBanConfig() {
		return new Config(Main.getInstance().getDataFolder() + "/BanSystem/Bans.yml", Config.YAML);
	}
	
	public static Config getMuteConfig() {
		return new Config(Main.getInstance().getDataFolder() + "/BanSystem/Mutes.yml", Config.YAML);
	}
	
	public static Config getHomeConfig() {
		return new Config(Main.getInstance().getDataFolder() + "/Home/Homes.yml", Config.YAML);
	}

	public static Config getAuctionStorageConfig(String playerName) {
		return new Config(Main.getInstance().getDataFolder() + "/Auction/PlayerItems/" + playerName.toLowerCase() + ".yml", Config.YAML);
	}

	public static Config getAuthorizationConfig() {
		return new Config(Main.getInstance().getDataFolder() + "/Authorization/Users.yml", Config.YAML);
	}

	public static Config getEconomyConfig() {
		return new Config(Main.getInstance().getDataFolder() + "/Economy/Users.yml", Config.YAML);
	}

	public static Config getStorageItemsConfig(String playerName) {
		return new Config(Main.getInstance().getDataFolder() + "/StorageItems/" + playerName.toLowerCase() + ".yml", Config.YAML);
	}

	public static Config getAchievementsConfig() {
		return new Config(Main.getInstance().getDataFolder() + "/Achievements/Users.yml", Config.YAML);
	}
}