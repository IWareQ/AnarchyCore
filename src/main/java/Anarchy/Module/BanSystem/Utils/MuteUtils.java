package Anarchy.Module.BanSystem.Utils;

public class MuteUtils {
	private String player;
	private String reason;
	private String banner;
	private long time;

	public MuteUtils(String player, String reason, String banner, long time) {
		this.player = player;
		this.reason = reason;
		this.banner = banner;
		this.time = time;
	}

	public String getPlayer() {
		return player;
	}

	public String getReason() {
		return reason;
	}

	public String getBanner() {
		return banner;
	}

	public Long getTime() {
		return time;
	}
}