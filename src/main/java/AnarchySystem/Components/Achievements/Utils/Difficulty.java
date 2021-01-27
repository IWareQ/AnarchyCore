package AnarchySystem.Components.Achievements.Utils;

public enum Difficulty {
	EASY("Легкое"),
	NORMAL("Среднее"),
	HARD("Сложное"),
	LEGENDARY("Легендарное");
	private final String difficultyName;

	Difficulty(String difficultyName) {
		this.difficultyName = difficultyName;
	}

	public String getDifficultyName() {
		return difficultyName;
	}
}