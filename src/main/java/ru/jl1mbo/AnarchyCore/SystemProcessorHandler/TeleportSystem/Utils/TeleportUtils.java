package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Utils;

import cn.nukkit.Player;

public class TeleportUtils {
    private final Player PLAYER;
    private final Player TARGET;
    private final Long TIME;

    public TeleportUtils(Player player, Player target, Long time) {
        this.PLAYER = player;
        this.TARGET = target;
        this.TIME = time;
    }

    public Player getPlayer() {
        return PLAYER;
    }

    public Player getTarget() {
        return TARGET;
    }

    public Long getTime() {
        return TIME;
    }
}