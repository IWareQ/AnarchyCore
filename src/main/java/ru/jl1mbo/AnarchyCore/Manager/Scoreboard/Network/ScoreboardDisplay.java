package ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network;

import java.util.LinkedHashMap;

import cn.nukkit.entity.Entity;
import lombok.Data;

@Data()
public class ScoreboardDisplay {
	private Scoreboard scoreboard;
	private String objectiveName;
	private String displayName;
	private SortOrder sortOrder;
	private LinkedHashMap<Integer, String> lineEntry;
	
	public ScoreboardDisplay(Scoreboard scoreboard, String objectiveName, String displayName, SortOrder sortOrder, LinkedHashMap<Integer, String> linkedHashMap) {
		this.scoreboard = scoreboard;
		this.objectiveName = objectiveName;
		this.displayName = displayName;
		this.sortOrder = sortOrder;
		this.lineEntry = linkedHashMap;
	}
	
	public SortOrder getSortOrder() {
		return this.sortOrder;
	}
	
	public String getObjectiveName() {
		return objectiveName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public DisplayEntry addEntity(Entity entity, int score) {
		long scoreId = this.scoreboard.addOrUpdateEntity(entity, this.objectiveName, score);
		return new DisplayEntry(this.scoreboard, scoreId);
	}
	
	public DisplayEntry addLine(String line, int score) {
		long scoreId = this.scoreboard.addOrUpdateLine(line, this.objectiveName, score);
		this.lineEntry.put(score, line);
		return new DisplayEntry(this.scoreboard, scoreId);
	}
	
	public void removeEntry(DisplayEntry entry) {
		this.scoreboard.removeScoreEntry(entry.getScoreId());
	}
	
	public String getLine(int score) {
		return this.lineEntry.getOrDefault(score, null);
	}
}