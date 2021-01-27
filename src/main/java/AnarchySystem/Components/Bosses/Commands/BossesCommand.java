package AnarchySystem.Components.Bosses.Commands;

import AnarchySystem.Manager.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;

public class BossesCommand extends Command {

    public BossesCommand() {
        super("bosses", "§r§fМеню боссов");
        this.setPermission("Command.Bosses");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Command.Bosses")) {
                return false;
            }
            SimpleForm simpleForm = new SimpleForm("§r§fМеню Боссов");
            simpleForm.addButton("§r§fФараон");
            simpleForm.addButton("§r§fТарантул");
            simpleForm.addButton("§r§fДревний Колдун");
            simpleForm.addButton("§r§fХранитель Кладбища");
            simpleForm.send(player, (targetPlayer, targetForm, data) -> {
                switch (data) {
                    case 0: {
                        Entity entity = Entity.createEntity("HuskBoss", player.getPosition());
                        if (entity != null) {
                            entity.spawnToAll();
                        }
                    }
                    break;

                    case 1: {
                        Entity entity = Entity.createEntity("SpiderBoss", player.getPosition());
                        if (entity != null) {
                            entity.spawnToAll();
                        }
                    }
                    break;

                    case 2: {
                        Entity entity = Entity.createEntity("EvokerBoss", player.getPosition());
                        if (entity != null) {
                            entity.spawnToAll();
                        }
                    }
                    break;

                    case 3: {
                        Entity entity = Entity.createEntity("WitherSkeletonBoss", player.getPosition());
                        if (entity != null) {
                            entity.spawnToAll();
                        }
                    }

                }
            });
        }
        return false;
    }
}