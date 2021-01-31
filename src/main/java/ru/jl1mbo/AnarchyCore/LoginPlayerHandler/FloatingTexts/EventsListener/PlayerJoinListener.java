package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.FloatingTexts.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.level.Position;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.FloatingTexts.FloatingTextsAPI;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		FloatingTextsAPI.addFloatingTexts(new Position(90.5, 93.5, 26.5), "§l§6Алтарь", "§l§fСтранная штука§7)", player);
		FloatingTextsAPI.addFloatingTexts(new Position(85.5, 92, 45.5), "§l§6Обменник опыта", "§l§fУ Вас §6" + player.getExperience() + " §fEXP", player);
		FloatingTextsAPI.addFloatingTexts(new Position(71.5, 91.5, 57.5), "§l§6Железный Приват", "§l§f2 §7× §f2", player);
		FloatingTextsAPI.addFloatingTexts(new Position(70.5, 91.5, 55.5), "§l§6Алмазный Приват", "§l§f4 §7× §f4", player);
		FloatingTextsAPI.addFloatingTexts(new Position(70.5, 91.5, 52.5), "§l§6Изумрудный Приват", "§l§f8 §7× §f8", player);
		FloatingTextsAPI.addFloatingTexts(new Position(71.5, 91.5, 50.5), "§l§6Изумрудный Приват", "§l§f10 §7× §f10", player);
		FloatingTextsAPI.addFloatingTexts(new Position(79.5, 92, 20.5), "§l§6Прыгай в портал§7!", "§l§fПросто прыгай и начинай выживать", player);
		FloatingTextsAPI.addFloatingTexts(new Position(72.5, 92, 54.5), "§l§6Как приватить§7?",
										  "§l§fЧтобы запривать регион§7,\n§l§fпросто установи один из блоков\n§l§fкоторые стоят рядом§7. §fКаждый блок имеет\n§l§fограниченный радиус привата§7,\n§l§fкоторый создается вокруг блока§7!",
										  player);
		FloatingTextsAPI.addFloatingKillsTops(new Position(74.5, 92, 25.5), player);
		FloatingTextsAPI.addFloatingDeathsTops(new Position(84.5, 92, 25.5), player);
	}
}