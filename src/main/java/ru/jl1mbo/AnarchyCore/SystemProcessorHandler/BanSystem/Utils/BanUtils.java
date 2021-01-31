package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Utils;

public class BanUtils {
    private final String player;
    private final String reason;
    private final String banner;
    private final long time;

    public BanUtils(String player, String reason, String banner, long time) {
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