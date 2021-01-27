package AnarchySystem.Manager.Scoreboard.Network;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import AnarchySystem.Manager.Scoreboard.Network.Packets.RemoveObjectivePacket;
import AnarchySystem.Manager.Scoreboard.Network.Packets.SetObjectivePacket;
import AnarchySystem.Manager.Scoreboard.Network.Packets.SetScorePacket;
import AnarchySystem.Manager.Scoreboard.ObjectMap.Long2ObjectArrayMap;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.network.protocol.DataPacket;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import lombok.Data;

public class Scoreboard {
    private final Long2ObjectMap<ScoreboardLine> scoreboardLines = new Long2ObjectArrayMap<>();
    private final Set<Player> viewers = new HashSet<>();
    private final Map<DisplaySlot, ScoreboardDisplay> displays = new EnumMap<>(DisplaySlot.class);
    private long scoreIdCounter = 0;

    public ScoreboardDisplay addDisplay(DisplaySlot slot, String objectiveName, String displayName) {
        return this.addDisplay(slot, objectiveName, displayName, SortOrder.ASCENDING);
    }

    public ScoreboardDisplay addDisplay(DisplaySlot slot, String objectiveName, String displayName, SortOrder sortOrder) {
        ScoreboardDisplay scoreboardDisplay = this.displays.get(slot);
        if (scoreboardDisplay == null) {
            scoreboardDisplay = new ScoreboardDisplay(this, objectiveName, displayName, sortOrder, new LinkedHashMap<>());
            this.displays.put(slot, scoreboardDisplay);
            this.broadcast(this.constructDisplayPacket(slot, scoreboardDisplay));
        }
        return scoreboardDisplay;
    }

    public void removeDisplay(DisplaySlot slot) {
        ScoreboardDisplay display = this.displays.remove(slot);
        if (display != null) {
            LongList validScoreIDs = new LongArrayList();
            Long2ObjectMap.FastEntrySet<ScoreboardLine> fastSet = (Long2ObjectMap.FastEntrySet<ScoreboardLine>) this.scoreboardLines.long2ObjectEntrySet();
            ObjectIterator<Long2ObjectMap.Entry<ScoreboardLine>> fastIterator = fastSet.fastIterator();
            while (fastIterator.hasNext()) {
                Long2ObjectMap.Entry<ScoreboardLine> entry = fastIterator.next();
                if (entry.getValue().objective.equals(display.getObjectiveName())) {
                    validScoreIDs.add(entry.getLongKey());
                }
            }
            for (long scoreID : validScoreIDs) {
                this.scoreboardLines.remove(scoreID);
            }
            this.broadcast(this.constructRemoveScores(validScoreIDs));
            this.broadcast(this.constructRemoveDisplayPacket(display));
        }
    }

    private DataPacket constructDisplayPacket(DisplaySlot slot, ScoreboardDisplay display) {
        SetObjectivePacket packetSetObjective = new SetObjectivePacket();
        packetSetObjective.setCriteriaName("dummy");
        packetSetObjective.setDisplayName(display.getDisplayName());
        packetSetObjective.setObjectiveName(display.getObjectiveName());
        packetSetObjective.setDisplaySlot(slot.name().toLowerCase());
        packetSetObjective.setSortOrder(display.getSortOrder().ordinal());
        return packetSetObjective;
    }

    private void broadcast(DataPacket packet) {
        for (Player viewer : this.viewers) {
            viewer.dataPacket(packet);
        }
    }

    long addOrUpdateLine(String line, String objective, int score) {
        Long2ObjectMap.FastEntrySet<ScoreboardLine> fastEntrySet = (Long2ObjectMap.FastEntrySet<ScoreboardLine>) this.scoreboardLines.long2ObjectEntrySet();
        ObjectIterator<Long2ObjectMap.Entry<ScoreboardLine>> fastIterator = fastEntrySet.fastIterator();
        while (fastIterator.hasNext()) {
            Long2ObjectMap.Entry<ScoreboardLine> entry = fastIterator.next();
            if (entry.getValue().type == 3 && entry.getValue().fakeName.equals(line) && entry.getValue().objective.equals(objective)) {
                return entry.getLongKey();
            }
        }
        long newId = this.scoreIdCounter++;
        ScoreboardLine scoreboardLine = new ScoreboardLine((byte) 3, 0, line, objective, score);
        this.scoreboardLines.put(newId, scoreboardLine);
        this.broadcast(this.constructSetScore(newId, scoreboardLine));
        return newId;
    }

    long addOrUpdateEntity(Entity entity, String objective, int score) {
        Long2ObjectMap.FastEntrySet<ScoreboardLine> fastEntrySet = (Long2ObjectMap.FastEntrySet<ScoreboardLine>) this.scoreboardLines.long2ObjectEntrySet();
        ObjectIterator<Long2ObjectMap.Entry<ScoreboardLine>> fastIterator = fastEntrySet.fastIterator();
        while (fastIterator.hasNext()) {
            Long2ObjectMap.Entry<ScoreboardLine> entry = fastIterator.next();
            if (entry.getValue().entityId == entity.getId() && entry.getValue().objective.equals(objective)) {
                return entry.getLongKey();
            }
        }
        long newId = this.scoreIdCounter++;
        ScoreboardLine scoreboardLine = new ScoreboardLine((byte) ((entity instanceof Player) ? 1 : 2), entity.getId(), "", objective, score);
        this.scoreboardLines.put(newId, scoreboardLine);
        this.broadcast(this.constructSetScore(newId, scoreboardLine));
        return newId;
    }

    private DataPacket constructSetScore(long newId, ScoreboardLine line) {
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.setType((byte) 0);
        setScorePacket.setEntries(new ArrayList<SetScorePacket.ScoreEntry>() {
            {
                this.add(new SetScorePacket.ScoreEntry(newId, line.objective, line.score, line.type, line.fakeName, line.entityId));
            }
        });
        return setScorePacket;
    }

    private DataPacket constructSetScore() {
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.setType((byte) 0);
        List<SetScorePacket.ScoreEntry> entries = new ArrayList<>();
        Long2ObjectMap.FastEntrySet<ScoreboardLine> fastEntrySet = (Long2ObjectMap.FastEntrySet<ScoreboardLine>) this.scoreboardLines.long2ObjectEntrySet();
        ObjectIterator<Long2ObjectMap.Entry<ScoreboardLine>> fastIterator = fastEntrySet.fastIterator();
        while (fastIterator.hasNext()) {
            Long2ObjectMap.Entry<ScoreboardLine> entry = fastIterator.next();
            entries.add(new SetScorePacket.ScoreEntry(entry.getLongKey(), entry.getValue().objective, entry.getValue().score, entry.getValue().type, entry.getValue().fakeName, entry.getValue().entityId));
        }
        setScorePacket.setEntries(entries);
        return setScorePacket;
    }

    public void showFor(Player player) {
        if (this.viewers.add(player)) {
            for (Map.Entry<DisplaySlot, ScoreboardDisplay> entry : this.displays.entrySet()) {
                player.dataPacket(this.constructDisplayPacket(entry.getKey(), entry.getValue()));
            }
            player.dataPacket(this.constructSetScore());
        }
    }

    public void hideFor(Player player) {
        if (this.viewers.remove(player)) {
            LongList validScoreIDs = new LongArrayList();
            Long2ObjectMap.FastEntrySet<ScoreboardLine> fastSet = (Long2ObjectMap.FastEntrySet<ScoreboardLine>) this.scoreboardLines.long2ObjectEntrySet();
            ObjectIterator<Long2ObjectMap.Entry<ScoreboardLine>> fastIterator = fastSet.fastIterator();
            while (fastIterator.hasNext()) {
                validScoreIDs.add(fastIterator.next().getLongKey());
            }
            player.dataPacket(this.constructRemoveScores(validScoreIDs));
            for (Map.Entry<DisplaySlot, ScoreboardDisplay> entry : this.displays.entrySet()) {
                player.dataPacket(this.constructRemoveDisplayPacket(entry.getValue()));
            }
        }
    }

    private DataPacket constructRemoveScores(LongList scoreIDs) {
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.setType((byte) 1);
        List<SetScorePacket.ScoreEntry> entries = new ArrayList<>();
        for (long scoreID : scoreIDs) {
            entries.add(new SetScorePacket.ScoreEntry(scoreID, "", 0));
        }
        setScorePacket.setEntries(entries);
        return setScorePacket;
    }

    private DataPacket constructRemoveDisplayPacket(ScoreboardDisplay display) {
        RemoveObjectivePacket removeObjectivePacket = new RemoveObjectivePacket();
        removeObjectivePacket.setObjectiveName(display.getObjectiveName());
        return removeObjectivePacket;
    }

    public void updateScore(long scoreId, int score) {
        ScoreboardLine line = this.scoreboardLines.get(scoreId);
        if (line != null) {
            line.setScore(score);
            this.broadcast(this.constructSetScore(scoreId, line));
        }
    }

    public void removeScoreEntry(long scoreId) {
        ScoreboardLine line = this.scoreboardLines.remove(scoreId);
        if (line != null) {
            this.broadcast(this.constructRemoveScores(scoreId));
        }
    }

    private DataPacket constructRemoveScores(long scoreId) {
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.setType((byte) 1);
        setScorePacket.setEntries(new ArrayList<SetScorePacket.ScoreEntry>() {
            {
                add(new SetScorePacket.ScoreEntry(scoreId, "", 0));
            }
        });
        return setScorePacket;
    }

    public int getScore(long scoreId) {
        ScoreboardLine line = this.scoreboardLines.remove(scoreId);
        if (line != null) {
            return line.getScore();
        }
        return 0;
    }

    @Data()
    private class ScoreboardLine {
        private final byte type;
        private final long entityId;
        private final String fakeName;
        private final String objective;
        private int score;

        public ScoreboardLine(final byte type, final long entityId, final String fakeName, final String objective, final int score) {
            this.type = type;
            this.entityId = entityId;
            this.fakeName = fakeName;
            this.objective = objective;
            this.score = score;
        }

        public byte getType() {
            return this.type;
        }

        public long getEntityId() {
            return this.entityId;
        }

        public String getFakeName() {
            return this.fakeName;
        }

        public String getObjective() {
            return this.objective;
        }

        public int getScore() {
            return this.score;
        }

        public void setScore(final int score) {
            this.score = score;
        }

        @Override()
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ScoreboardLine)) {
                return false;
            }
            final ScoreboardLine other = (ScoreboardLine) o;
            if (!other.canEqual(this)) {
                return false;
            }
            if (this.getType() != other.getType()) {
                return false;
            }
            if (this.getEntityId() != other.getEntityId()) {
                return false;
            }
            final Object this$fakeName = this.getFakeName();
            final Object other$fakeName = other.getFakeName();
            Label_0092:
            {
                if (this$fakeName == null) {
                    if (other$fakeName == null) {
                        break Label_0092;
                    }
                } else if (this$fakeName.equals(other$fakeName)) {
                    break Label_0092;
                }
                return false;
            }
            final Object this$objective = this.getObjective();
            final Object other$objective = other.getObjective();
            if (this$objective == null) {
                if (other$objective == null) {
                    return this.getScore() == other.getScore();
                }
            } else if (this$objective.equals(other$objective)) {
                return this.getScore() == other.getScore();
            }
            return false;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof ScoreboardLine;
        }

        @Override()
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getType();
            final long $entityId = this.getEntityId();
            result = result * 59 + (int) ($entityId >>> 32 ^ $entityId);
            final Object $fakeName = this.getFakeName();
            result = result * 59 + (($fakeName == null) ? 43 : $fakeName.hashCode());
            final Object $objective = this.getObjective();
            result = result * 59 + (($objective == null) ? 43 : $objective.hashCode());
            result = result * 59 + this.getScore();
            return result;
        }

        @Override()
        public String toString() {
            return "Scoreboard.ScoreboardLine(type=" + this.getType() + ", entityId=" + this.getEntityId() + ", fakeName=" + this.getFakeName() + ", objective=" + this.getObjective() + ", score=" + this.getScore() + ")";
        }
    }
}