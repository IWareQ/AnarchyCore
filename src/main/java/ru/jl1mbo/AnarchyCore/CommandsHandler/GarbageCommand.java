package ru.jl1mbo.AnarchyCore.CommandsHandler;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityMinecartChest;
import cn.nukkit.level.Level;
import nukkitcoders.mobplugin.entities.monster.flying.Wither;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.EvokerBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.HuskBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.SpiderBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.WitherSkeletonBoss;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Task.ClearTask;

public class GarbageCommand extends Command {

    public GarbageCommand() {
        super("garbage", "§r§fОчистка дропа на Сервере", "", new String[]{"gb"});
        this.setPermission("Command.Garbage");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("args", new String[]{"get", "process", "start"})});
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                SimpleForm simpleForm = new SimpleForm("§r§fОчистка Дропа");
                simpleForm.setContent("§r§l§6• §r§fБыстрые команды§7:\n\n§7/§6gb get §7- §fколличество дропа\n§7/§6gb start §7- §fпринудительная очистка\nОбнаружено дропа§7: §6" + getGarbageCount() + " §fшт§7.");
                simpleForm.send(player);
                return true;
            }
            switch (args[0]) {
                case "get":
                    player.sendMessage("§l§7(§3Очистка§7) §r§fНа всем сервере обнаружено §6" + getGarbageCount() + " §fшт§7. §fдропа§7!");
                    break;
                case "start":
                    player.sendMessage("§l§7(§3Очистка§7) §r§fНачинается принудительная §6Очистка§7!");
                    ClearTask.seconds = 10;
                    break;
                default:
                    SimpleForm simpleForm = new SimpleForm("§r§fОчистка Дропа");
                    simpleForm.setContent("§r§l§6• §r§fБыстрые команды§7:\n\n§7/§6gb get §7- §fколличество дропа\n§7/§6gb start §7- §fпринудительная очистка\nОбнаружено дропа§7: §6" + getGarbageCount() + " §fшт§7.");
                    simpleForm.send(player);
                    break;
            }
        }
        return false;
    }

    private int getGarbageCount() {
        int COUNT = 0;
        for (Level level : Server.getInstance().getLevels().values()) {
            for (Entity entity : level.getEntities()) {
                if (!(entity instanceof Player) && !(entity instanceof EntityMinecartChest) && !(entity instanceof Wither) && !(entity.getLevel().equals(WorldSystemAPI.getSpawn())) && !(entity instanceof WitherSkeletonBoss) && !(entity instanceof HuskBoss) && !(entity instanceof EvokerBoss) && !(entity instanceof SpiderBoss)) {
                    COUNT++;
                }
            }
        }
        return COUNT;
    }
}