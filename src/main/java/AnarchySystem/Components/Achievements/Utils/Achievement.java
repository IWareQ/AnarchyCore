package AnarchySystem.Components.Achievements.Utils;

import AnarchySystem.Components.Achievements.AchievementsAPI;
import AnarchySystem.Components.Achievements.Commands.AchievementsCommand;
import AnarchySystem.Manager.Forms.Elements.SimpleForm;
import cn.nukkit.Player;

public abstract class Achievement {
	
	public abstract String getAchievementId();
	
	public abstract String getAchievementName();
	
	public abstract String getAchievementDescription();
	
	public abstract String getAchievementIcon();
	
	public abstract Difficulty getAchievementDifficulty();
	
	public abstract int getAchievementMaxPoints();
	
	public abstract void action(Player player);
	
	public void sendInformationForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Информация о достижении");
		simpleForm.addContent("§l§6• §r§fНазвание§7: §6" + this.getAchievementName());
		simpleForm.addContent("\n§l§6• §r§fСтатус§7: " + (AchievementsAPI.isCompleted(player.getName(), this.getAchievementId()) ? ("§6Выполнено") : (AchievementsAPI.getAchievementPoints(player.getName(), this.getAchievementId()) + "§7/§6" + this.getAchievementMaxPoints())));
		simpleForm.addContent("\n§l§6• §r§fСложность§7: " + this.getAchievementDifficulty().getDifficultyName());
		simpleForm.addContent("\n§l§6• §r§fОписание§7: §6" + this.getAchievementDescription());
		simpleForm.addButton("Назад");
		simpleForm.send(player, (target,form,data)->{
			if (data == -1) return;
			AchievementsCommand.sendMainForm(target);
		});
	}
}