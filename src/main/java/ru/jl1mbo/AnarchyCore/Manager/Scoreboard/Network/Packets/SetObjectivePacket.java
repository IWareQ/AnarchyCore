package ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network.Packets;

import cn.nukkit.network.protocol.DataPacket;

public class SetObjectivePacket extends DataPacket {
	public static final byte NETWORK_ID = 107;
	public String displaySlot;
	public String objectiveName;
	public String displayName;
	public String criteriaName;
	public int sortOrder;

	public byte pid() {
		return 107;
	}

	public void decode() {

	}

	public void encode() {
		this.reset();
		this.putString(this.displaySlot);
		this.putString(this.objectiveName);
		this.putString(this.displayName);
		this.putString(this.criteriaName);
		this.putVarInt(this.sortOrder);
	}

	public String getDisplaySlot() {
		return this.displaySlot;
	}

	public void setDisplaySlot(final String displaySlot) {
		this.displaySlot = displaySlot;
	}

	public String getObjectiveName() {
		return this.objectiveName;
	}

	public void setObjectiveName(final String objectiveName) {
		this.objectiveName = objectiveName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public String getCriteriaName() {
		return this.criteriaName;
	}

	public void setCriteriaName(final String criteriaName) {
		this.criteriaName = criteriaName;
	}

	public int getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(final int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SetObjectivePacket)) {
			return false;
		}
		final SetObjectivePacket other = (SetObjectivePacket) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$displaySlot = this.getDisplaySlot();
		final Object other$displaySlot = other.getDisplaySlot();
		Label_0065: {
			if (this$displaySlot == null) {
				if (other$displaySlot == null) {
					break Label_0065;
				}
			} else if (this$displaySlot.equals(other$displaySlot)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$objectiveName = this.getObjectiveName();
		final Object other$objectiveName = other.getObjectiveName();
		Label_0102: {
			if (this$objectiveName == null) {
				if (other$objectiveName == null) {
					break Label_0102;
				}
			} else if (this$objectiveName.equals(other$objectiveName)) {
				break Label_0102;
			}
			return false;
		}
		final Object this$displayName = this.getDisplayName();
		final Object other$displayName = other.getDisplayName();
		Label_0139: {
			if (this$displayName == null) {
				if (other$displayName == null) {
					break Label_0139;
				}
			} else if (this$displayName.equals(other$displayName)) {
				break Label_0139;
			}
			return false;
		}
		final Object this$criteriaName = this.getCriteriaName();
		final Object other$criteriaName = other.getCriteriaName();
		if (this$criteriaName == null) {
			if (other$criteriaName == null) {
				return this.getSortOrder() == other.getSortOrder();
			}
		} else if (this$criteriaName.equals(other$criteriaName)) {
			return this.getSortOrder() == other.getSortOrder();
		}
		return false;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof SetObjectivePacket;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $displaySlot = this.getDisplaySlot();
		result = result * 59 + (($displaySlot == null) ? 43 : $displaySlot.hashCode());
		final Object $objectiveName = this.getObjectiveName();
		result = result * 59 + (($objectiveName == null) ? 43 : $objectiveName.hashCode());
		final Object $displayName = this.getDisplayName();
		result = result * 59 + (($displayName == null) ? 43 : $displayName.hashCode());
		final Object $criteriaName = this.getCriteriaName();
		result = result * 59 + (($criteriaName == null) ? 43 : $criteriaName.hashCode());
		result = result * 59 + this.getSortOrder();
		return result;
	}

	public String toString() {
		return "SetObjectivePacket(displaySlot=" + this.getDisplaySlot() + ", objectiveName=" + this.getObjectiveName() + ", displayName=" + this.getDisplayName() + ", criteriaName=" + this.getCriteriaName()
			   + ", sortOrder=" + this.getSortOrder() + ")";
	}
}