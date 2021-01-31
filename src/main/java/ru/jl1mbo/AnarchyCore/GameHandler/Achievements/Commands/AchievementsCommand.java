package ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Commands;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.AchievementsAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Utils.Achievement;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.ImageType;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;

public class AchievementsCommand extends Command {

	public AchievementsCommand() {
		super("achievements", "§r§fДостижения");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			sendMainForm(player);
		}
		return false;
	}

	public static void sendMainForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Достижения",
											   "§r§fВыберите ниже из списка §6Достижение §fчтобы узнать информацию о нем§7.");
		for (Achievement achievement : AchievementsAPI.getAchievements().values()) {
			simpleForm.addButton(achievement.getAchievementName(), ImageType.PATH, AchievementsAPI.isCompleted(player.getName(),
								 achievement.getAchievementId()) ? achievement.getAchievementIcon() : "textures/ui/lock_color");
		}
		simpleForm.send(player, (targetPlayer, targetForm, data)-> {
			if (data == -1) return;
			new ArrayList<>(AchievementsAPI.getAchievements().values()).get(data).sendInformationForm(player);
		});
	}
}