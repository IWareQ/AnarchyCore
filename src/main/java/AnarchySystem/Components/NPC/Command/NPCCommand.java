package AnarchySystem.Components.NPC.Command;

import AnarchySystem.Manager.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;

public class NPCCommand extends Command {

    public NPCCommand() {
        super("npc", "§r§fМеню NPC");
        this.setPermission("Command.NPC");
        this.commandParameters.clear();
    }

    @Override()
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Command.NPC")) {
                return false;
            }
            player.teleport(new Position(player.getFloorX() + 0.5, player.getFloorY(), player.getFloorZ() + 0.5));
            SimpleForm simpleForm = new SimpleForm("§r§fМеню NPC");
            simpleForm.setContent("§r§l§6• §r§fВыберите нужного Вам §6NPC§7:");
            simpleForm.addButton("§r§6Барыга");
            simpleForm.addButton("§r§6Аукционер");
            simpleForm.addButton("§r§6Собиратель Артефактов");
            simpleForm.send(player, (targetPlayer, targetForm, data) -> {
                switch (data) {
                    case 0: {
                        Entity entity = Entity.createEntity("PiglinNPC", player.getPosition());
                        if (entity != null) {
                            entity.spawnToAll();
                            player.sendMessage("§l§7(§3NPC§7) §r§f NPC §7«§6Барыга§7» §fуспешно поставлен§7!");
                        }
                        break;
                    }
                    case 1: {
                        Entity entity = Entity.createEntity("VillagerNPC", player.getPosition());
                        if (entity != null) {
                            entity.spawnToAll();
                            player.sendMessage("§l§7(§3NPC§7) §r§f NPC §7«§6Аукционер§7» §fуспешно поставлен§7!");
                        }
                        break;
                    }
                    case 2: {
                        Entity entity = Entity.createEntity("WanderingTraderNPC", player.getPosition());
                        if (entity != null) {
                            entity.spawnToAll();
                            player.sendMessage("§l§7(§3NPC§7) §r§f NPC §7«§6Собиратель Артефактов§7» §fуспешно поставлен§7!");
                        }
                        break;
                    }
                }
            });
        }
        return false;
    }
}