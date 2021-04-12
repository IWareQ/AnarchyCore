package ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network.Packets;

import cn.nukkit.network.protocol.DataPacket;

public class RemoveObjectivePacket extends DataPacket {
	public static final byte NETWORK_ID = 106;
	public String objectiveName;
	
	public byte pid() {
		return 106;
	}
	
	public void decode() {
		
	}
	
	public void encode() {
		this.reset();
		this.putString(this.objectiveName);
	}
	
	public String getObjectiveName() {
		return this.objectiveName;
	}
	
	public void setObjectiveName(final String objectiveName) {
		this.objectiveName = objectiveName;
	}
	
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof RemoveObjectivePacket)) {
			return false;
		}
		final RemoveObjectivePacket other = (RemoveObjectivePacket)o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$objectiveName = this.getObjectiveName();
		final Object other$objectiveName = other.getObjectiveName();
		if (this$objectiveName == null) {
			return other$objectiveName == null;
		} else return this$objectiveName.equals(other$objectiveName);
	}
	
	protected boolean canEqual(final Object other) {
		return other instanceof RemoveObjectivePacket;
	}
	
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $objectiveName = this.getObjectiveName();
		result = result * 59 + (($objectiveName == null) ? 43 : $objectiveName.hashCode());
		return result;
	}
	
	public String toString() {
		return "RemoveObjectivePacket(objectiveName=" + this.getObjectiveName() + ")";
	}
}