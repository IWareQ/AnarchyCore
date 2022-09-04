package ru.iwareq.anarchycore.manager.Scoreboard.Network.Packets;

import cn.nukkit.network.protocol.DataPacket;

import java.util.List;

public class SetScorePacket extends DataPacket {

	public static final byte NETWORK_ID = 108;
	private byte type;
	private List<ScoreEntry> entries;

	public byte pid() {
		return 108;
	}

	public void decode() {

	}

	public void encode() {
		this.reset();
		this.putByte(this.type);
		this.putUnsignedVarInt(this.entries.size());
		for (final ScoreEntry entry : this.entries) {
			this.putVarLong(entry.scoreId);
			this.putString(entry.objective);
			this.putLInt(entry.score);
			if (this.type == 0) {
				this.putByte(entry.entityType);
				switch (entry.entityType) {
					case 3:
						this.putString(entry.fakeEntity);
						continue;

					case 1:

					case 2:
						this.putUnsignedVarLong(entry.entityId);
						continue;

				}
				throw new IllegalStateException("Unexpected value: " + entry.entityType);
			}
		}
	}

	public byte getType() {
		return this.type;
	}

	public void setType(final byte type) {
		this.type = type;
	}

	public List<ScoreEntry> getEntries() {
		return this.entries;
	}

	public void setEntries(final List<ScoreEntry> entries) {
		this.entries = entries;
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SetScorePacket)) {
			return false;
		}
		final SetScorePacket other = (SetScorePacket) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.getType() != other.getType()) {
			return false;
		}
		final Object this$entries = this.getEntries();
		final Object other$entries = other.getEntries();
		if (this$entries == null) {
			return other$entries == null;
		} else {
			return this$entries.equals(other$entries);
		}
	}

	protected boolean canEqual(final Object other) {
		return other instanceof SetScorePacket;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * 59 + this.getType();
		final Object $entries = this.getEntries();
		result = result * 59 + (($entries == null) ? 43 : $entries.hashCode());
		return result;
	}

	public String toString() {
		return "SetScorePacket(type=" + this.getType() + ", entries=" + this.getEntries() + ")";
	}

	public static class ScoreEntry {

		private final long scoreId;
		private final String objective;
		private final int score;
		private byte entityType;
		private String fakeEntity;
		private long entityId;

		public ScoreEntry(final long scoreId, final String objective, final int score, final byte entityType, final String fakeEntity, final long entityId) {
			this.scoreId = scoreId;
			this.objective = objective;
			this.score = score;
			this.entityType = entityType;
			this.fakeEntity = fakeEntity;
			this.entityId = entityId;
		}

		public ScoreEntry(final long scoreId, final String objective, final int score) {
			this.scoreId = scoreId;
			this.objective = objective;
			this.score = score;
		}

		public long getScoreId() {
			return this.scoreId;
		}

		public String getObjective() {
			return this.objective;
		}

		public int getScore() {
			return this.score;
		}

		public byte getEntityType() {
			return this.entityType;
		}

		public String getFakeEntity() {
			return this.fakeEntity;
		}

		public long getEntityId() {
			return this.entityId;
		}

		@Override()
		public String toString() {
			return "SetScorePacket.ScoreEntry(scoreId=" + this.getScoreId() + ", objective=" + this.getObjective() + ", score=" + this.getScore() + ", entityType=" + this.getEntityType() + ", fakeEntity=" + this.getFakeEntity() + ", entityId=" + this.getEntityId() + ")";
		}
	}
}