package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Commands;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.BlockProtectionAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Utils.SQLiteUtils;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.AuthorizationAPI;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.CustomForm;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class RegionCommand extends Command {

    public RegionCommand() {
        super("region", "§r§fСистема регионов", "", new String[]{"rg"});
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("string", new String[]{"add", "del", "help"}), new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    private static void regionEdit(Player player) {
        SimpleForm simpleForm = new SimpleForm("§fУправление регионами");
        simpleForm.setContent("§fВыберите нужное Вам действие§7, §fкоторое хотите применить к данному §6Региону§7:");
        simpleForm.addButton("§fДобавить Игрока");
        simpleForm.addButton("§fУдалить Игрока");
        simpleForm.addButton("§fУдалить регион");
        simpleForm.addButton("§fНазад");
        simpleForm.send(player, (targetPlayer, targetForm, data) -> {
            int regionID = BlockProtectionAPI.getRegionIDByPosition(player);
            switch (data) {
                case 0: {
                    addRegionPlayer(player, regionID);
                    break;
                }

                case 1: {
                    removeRegionPlayer(player, regionID);
                    break;
                }

                case 2: {
                    if (regionID != -1 && BlockProtectionAPI.isRegionOwner(player.getName(), regionID)) {
                        player.sendMessage(BlockProtectionAPI.PREFIX + "§fРегион §7#§6" + regionID + " §fуспешно удален§7!");
                        SQLiteUtils.query("DELETE FROM AREAS WHERE Region_ID = '" + regionID + "';");
                        SQLiteUtils.query("DELETE FROM MEMBERS WHERE Region_ID = '" + regionID + "';");
                    }
                    break;
                }

                case 3: {
                    regionMenu(player);
                    break;
                }

            }
        });
    }

    private static void addRegionPlayer(Player player, Integer regionID) {
        CustomForm customForm = new CustomForm("§fУправление Участниками §7› §fДобавить Игрока");
        customForm.addLabel("§l§6Добавить Игрока\n\n§r§fВведите §6Nickname §fИгрока§7, §fкоторого хотите добавить в свой регион§7!");
        customForm.addInput("§fНапример§7: §6Steve");
        customForm.send(player, (targetPlayer, form, data) -> {
            if (data == null) return;
            String nickname = (String) data.get(1);
            if (!AuthorizationAPI.isRegister(nickname)) {
                player.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + nickname + " §fни разу не заходил на сервер§7!");
                return;
            }
            Player target = Server.getInstance().getPlayerExact(nickname);
            if (player.equals(target)) {
                player.sendMessage(BlockProtectionAPI.PREFIX + "§fНельзя добавить себя в свой регион§7!");
                return;
            }
            if (SQLiteUtils.selectString("SELECT Username FROM MEMBERS WHERE UPPER(Username) = '" + player.getName().toUpperCase() + "' AND Region_ID = '" + regionID + "';") != null) {
                player.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + nickname + " §fуже состоит в Вашем регионе§7!");
                return;
            }
            player.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + nickname + " §fбыл успешно добавлен в Ваш регион§7!");
            if (target != null) {
                target.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fдобавил Вас в свой регион§7!");
            }
            SQLiteUtils.query("INSERT INTO MEMBERS (Region_ID, Username) VALUES ('" + regionID + "', '" + target.getName() + "');");
        });
    }

    private static void removeRegionPlayer(Player player, Integer regionID) {
        SimpleForm simpleForm = new SimpleForm("§fУдалить Участника");
        simpleForm.setContent("§fВыберите Участника§7, §fкоторого хотите удалить из региона§7:");
        for (String removePlayer : BlockProtectionAPI.getRegionMembers(regionID)) {
            simpleForm.addButton(removePlayer);
        }
        simpleForm.send(player, (targetPlayer, form, data) -> {
            if (data == -1) return;
            player.sendMessage(form.getResponse().getClickedButton().getText());
        });
    }

    private static void regionControls(Player player) {
        SimpleForm simpleForm = new SimpleForm("§fУправление регионами");
        simpleForm.setContent("§fВыберите регион§7, §fкоторый хотите §6Отредактировать§7:");
        List<Integer> regionsData = SQLiteUtils.selectIntegerList("SELECT Region_ID FROM AREAS WHERE Username = '" + player.getName() + "';");
        if (regionsData == null || regionsData.isEmpty()) {
            simpleForm.addContent("\n\n§fВы не имеете регионов§7!");
        } else {
            for (int region_id : regionsData) {
                Integer mainX = SQLiteUtils.selectInteger("SELECT Main_X FROM AREAS WHERE (Region_ID = '" + region_id + "');");
                Integer mainY = SQLiteUtils.selectInteger("SELECT Main_Y FROM AREAS WHERE (Region_ID = '" + region_id + "');");
                Integer mainZ = SQLiteUtils.selectInteger("SELECT Main_Z FROM AREAS WHERE (Region_ID = '" + region_id + "');");
                simpleForm.addButton("§f" + mainX + "§7, §f" + mainY + "§7, §f" + mainZ);
            }
        }
        simpleForm.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            regionEdit(player);
        });
    }

    private static void regionGuid(Player player) {
        SimpleForm simpleForm = new SimpleForm("§fГайд");
        simpleForm.setContent("§fХочешь создать свой регион§7? §fНе проблема§7! §fМожешь следовать пунктам§7:\n\n§6• §fДобудь блок привта\n§6• §fПроверь§7, §fнет ли вблизи другого региона\n§6• §fПоставь блок для привата и будь уверен §7- §fтвою постройку не тронут§7!\n\nБлоки§7, §fкоторыми можно приватить показаны на спавне§7!");
        simpleForm.addButton("§fНазад");
        simpleForm.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            regionMenu(player);
        });
    }

    private static void regionMenu(Player player) {
        SimpleForm simpleForm = new SimpleForm("§fМеню Регионов");
        simpleForm.setContent("§fВы находитесь в главном меню взаимодействия с регионами на §6Сервере§7!\n\n§fВыберите нужный Вам пункт§7:");
        simpleForm.addButton("§fУправление регионами");
        simpleForm.addButton("§fЧленство в регионах");
        simpleForm.addButton("§fГайд");
        simpleForm.send(player, (targetPlayer, targetForm, data) -> {
            switch (data) {
                case 0: {
                    regionControls(player);
                    break;
                }

                case 1: {
                    membershipInRegions(player);
                    break;
                }

                case 2: {
                    regionGuid(player);
                    break;
                }

            }
        });
    }

    private static void membershipInRegions(Player player) {
        SimpleForm simpleForm = new SimpleForm("§r§fЧленство в Регионах");
        simpleForm.setContent("§r§fРегионы§7, §fв которых Вас добавили§7:");
        List<Integer> regionsData = SQLiteUtils.selectIntegerList("SELECT Region_ID FROM MEMBERS WHERE Username = '" + player.getName() + "';");
        if (regionsData == null || regionsData.isEmpty()) {
            simpleForm.addContent("\n\nВас не добавили ни в §61 §fиз регионов!");
        } else {
            for (int region_id : regionsData) {
                String username = SQLiteUtils.selectString("SELECT Username FROM AREAS WHERE (Region_ID = '" + region_id + "');");
                Integer mainX = SQLiteUtils.selectInteger("SELECT Main_X FROM AREAS WHERE (Region_ID = '" + region_id + "');");
                Integer mainY = SQLiteUtils.selectInteger("SELECT Main_Y FROM AREAS WHERE (Region_ID = '" + region_id + "');");
                Integer mainZ = SQLiteUtils.selectInteger("SELECT Main_Z FROM AREAS WHERE (Region_ID = '" + region_id + "');");
                simpleForm.addButton("§r§6" + username + "\n§r§f" + mainX + "§7, §f" + mainY + "§7, §f" + mainZ);
            }
        }
        simpleForm.send(player, (targetPlayer, form, data) -> {
            if (data == -1) return;
            player.sendMessage(form.getResponse().getClickedButton().getText());
        });
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("§l§6• §r§fИспользование §7- /§6rg help");
                return true;
            }
            switch (args[0]) {
                case "add": {
                    if (args.length != 2) {
                        player.sendMessage("§l§6• §r§fИспользование §7- /§6rg add §7(§6игрок§7)");
                        return true;
                    }
                    int regionID = BlockProtectionAPI.getRegionIDByPosition(player);
                    if (regionID == -1 || !BlockProtectionAPI.isRegionOwner(player.getName(), regionID)) {
                        player.sendMessage(BlockProtectionAPI.PREFIX + "§fВы должны находиться внутри Вашего региона§7!");
                        return true;
                    }
                    String nickname = Utils.implode(args, 1);
                    if (!AuthorizationAPI.isRegister(nickname)) {
                        player.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + nickname + " §fне зарегистрирован§7!");
                        return true;
                    }
                    if (SQLiteUtils.selectString("SELECT Username FROM MEMBERS WHERE UPPER(Username) = '" + player.getName().toUpperCase() + "' AND Region_ID = '" + regionID + "';") != null) {
                        player.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + nickname + " §fуже состоит в Вашем регионе§7!");
                        return true;
                    }
                    player.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + nickname + " §fбыл успешно добавлен в Ваш регион§7!");
                    Player target = Server.getInstance().getPlayerExact(nickname);
                    if (target != null) {
                        target.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fдобавил Вас в свой регион§7!");
                    }
                    SQLiteUtils.query("INSERT INTO MEMBERS (Region_ID, Username) VALUES ('" + regionID + "', '" + nickname + "');");
                }
                break;

                case "del": {
                    if (args.length != 2) {
                        player.sendMessage("§l§6• §r§fИспользование §7- /§6rg del §7(§6игрок§7)");
                        return true;
                    }
                    int regionID = BlockProtectionAPI.getRegionIDByPosition(player);
                    if (regionID == -1 || !BlockProtectionAPI.isRegionOwner(player.getName(), regionID)) {
                        player.sendMessage(BlockProtectionAPI.PREFIX + "§fВы должны находиться внутри Вашего региона§7!");
                        return true;
                    }
                    String nickname = Utils.implode(args, 1);
                    if (BlockProtectionAPI.isRegionMember(nickname, regionID)) {
                        player.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + nickname + " §fне состоит в Вашем регионе§7!");
                        return true;
                    }
                    player.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + nickname + " §fудален из региона§7! (" + Utils.getOnlineString(nickname) + "§7)");
                    Player target = Server.getInstance().getPlayerExact(nickname);
                    if (target != null) {
                        target.sendMessage(BlockProtectionAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fудалил Вас из своего региона§7!");
                    }
                    SQLiteUtils.query("DELETE FROM MEMBERS WHERE UPPER(Username) = '" + nickname.toUpperCase() + "' AND Region_ID = '" + regionID + "';");
                }
                break;

                default: {
                    regionMenu(player);
                }
                break;

            }
        }
        return false;
    }
}