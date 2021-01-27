package AnarchySystem.Components.Bosses.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;

public class BossesSpawnTask extends Task {
    private static final Integer[] RANDOM_TP = new Integer[]{-1500, 1500, -1500, 1500};
    private static int TIMER_BOSS = 24;

    public static void findRandomSafePosition(Level level, Consumer<Position> callback) {
        CompletableFuture.runAsync(() -> callback.accept(randomPos(new Position(0, 0, 0, level))));
    }

    private static Position randomPos(Position base) {
        int x = rand(RANDOM_TP[0], RANDOM_TP[1]);
        int z = rand(RANDOM_TP[2], RANDOM_TP[3]);
        base.setComponents(x, 67, z);
        int cx = base.getChunkX();
        int cz = base.getChunkZ();
        while (!base.getLevel().isChunkGenerated(cx, cz) || !base.getLevel().isChunkLoaded(cx, cz)) {
            base.getLevel().generateChunk(cx, cz, true);
            base.getLevel().loadChunk(cx, cz, true);
        }
        for (int i = 60; i < 120; i++) {
            base.setComponents(x, i, z);
            Block ground = base.getLevel().getBlock(base);
            Block body = base.getLevel().getBlock(base.setComponents(x, i + 1, z));
            Block head = base.getLevel().getBlock(base.setComponents(x, i + 2, z));
            if (head.getId() == 0 && body.getId() == 0) {
                if (ground.getId() != Block.LAVA || ground.getId() != Block.STILL_LAVA && ground.isSolid()) {
                    return base.setComponents(x + 0.5, i + 1, z + 0.5);
                }
            }
        }
        return randomPos(base);
    }

    public static int rand(int min, int max) {
        if (min == max) {
            return max;
        }
        return new Random().nextInt(max + 1 - min) + min;
    }

    @Override()
    public void onRun(int currentTick) {
        if (TIMER_BOSS == 0) {
            if (Server.getInstance().getOnlinePlayers().size() >= 5) {
                Random rand = new Random();
                List<String> givenList = Arrays.asList("SpiderBoss", "WitherSkeletonBoss", "HuskBoss", "EvokerBoss");
                for (int i = 0; i < 1; i++) {
                    int randomIndex = rand.nextInt(givenList.size());
                    String randomElement = givenList.get(randomIndex);
                    findRandomSafePosition(WorldSystemAPI.getMap(), position -> {
                        if (randomElement.equals("SpiderBoss")) {
                            Entity entity = Entity.createEntity("SpiderBoss", position.setLevel(WorldSystemAPI.getMap()));
                            if (entity != null) {
                                entity.spawnToAll();
                                Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY() + "§7, §6" + entity.getFloorZ());
                            }
                        }
                        if (randomElement.equals("WitherSkeletonBoss")) {
                            Entity entity = Entity.createEntity("WitherSkeletonBoss", position.setLevel(WorldSystemAPI.getMap()));
                            if (entity != null) {
                                entity.spawnToAll();
                                Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY() + "§7, §6" + entity.getFloorZ());
                            }
                        }
                        if (randomElement.equals("HuskBoss")) {
                            Entity entity = Entity.createEntity("HuskBoss", position.setLevel(WorldSystemAPI.getMap()));
                            if (entity != null) {
                                entity.spawnToAll();
                                Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY() + "§7, §6" + entity.getFloorZ());
                            }
                        }
                        if (randomElement.equals("EvokerBoss")) {
                            Entity entity = Entity.createEntity("EvokerBoss", position.setLevel(WorldSystemAPI.getMap()));
                            if (entity != null) {
                                entity.spawnToAll();
                                Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY() + "§7, §6" + entity.getFloorZ());
                            }
                        }
                    });
                }
            }
            TIMER_BOSS = 24;
        }
        --TIMER_BOSS;
    }
}