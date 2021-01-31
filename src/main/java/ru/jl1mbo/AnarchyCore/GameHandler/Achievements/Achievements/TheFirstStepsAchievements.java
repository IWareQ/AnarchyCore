package ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Achievements;

import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Utils.Achievement;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Utils.Difficulty;

public class TheFirstStepsAchievements extends Achievement {

	@Override
	public String getAchievementId() {
		return "the_first_steps";
	}

	@Override
	public String getAchievementName() {
		return "Первые Шаги";
	}

	@Override
	public String getAchievementDescription() {
		return "Впервые зайти в портал";
	}

	@Override
	public String getAchievementIcon() {
		return "textures/ui/dressing_room_animation";
	}

	@Override
	public Difficulty getAchievementDifficulty() {
		return Difficulty.EASY;
	}

	@Override
	public int getAchievementMaxPoints() {
		return 1;
	}

	public void action(Player player) {
		player.sendMessage("§l§7(§3Достижения§7) §r§fДостижение §6" + this.getAchievementName() + " §fуспешно пройдено§7!");
		player.sendTitle("§6Испытание", "§fзавершено", 20, 5 * 20, 5);
		player.getLevel().addSound(player, Sound.RANDOM_TOAST, 1.0F, 1.0F, player);
	}
}