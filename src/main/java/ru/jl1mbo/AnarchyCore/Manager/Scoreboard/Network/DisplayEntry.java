package ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor()
public class DisplayEntry {
	private Scoreboard scoreboard;
	@Getter()
	private long scoreId;

	public DisplayEntry(Scoreboard scoreboard, long scoreId) {
		this.scoreboard = scoreboard;
		this.scoreId = scoreId;
	}

	public long getScoreId() {
		return scoreId;
	}

	public int getScore() {
		return this.scoreboard.getScore(this.scoreId);
	}

	public void setScore(int score) {
		this.scoreboard.updateScore(this.scoreId, score);
	}
}