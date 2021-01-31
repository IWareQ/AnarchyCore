package ru.jl1mbo.AnarchyCore.Manager.Forms.EventsListener;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Forms.CustomFormResponse;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Forms.Form;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Forms.ModalFormResponse;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Forms.SimpleFormResponse;

public class PlayerFormRespondedListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        FormWindow formWindow = event.getWindow();
        FormResponse formResponse = formWindow.getResponse();
        if (Form.playersForm.containsKey(player.getName())) {
            ru.jl1mbo.AnarchyCore.Manager.Forms.Forms.FormResponse temp = Form.playersForm.get(player.getName());
            Form.playersForm.remove(player.getName());
            Object data;
            if (formResponse == null || event.wasClosed()) {
                if (temp instanceof CustomFormResponse) {
                    ((CustomFormResponse) temp).handle(player, (FormWindowCustom) formWindow, null);
                } else if (temp instanceof ModalFormResponse) {
                    ((ModalFormResponse) temp).handle(player, (FormWindowModal) formWindow, -1);
                } else if (temp instanceof SimpleFormResponse) {
                    ((SimpleFormResponse) temp).handle(player, (FormWindowSimple) formWindow, -1);
                }
                return;
            }
            if (formWindow instanceof FormWindowSimple) {
                data = ((FormResponseSimple) formResponse).getClickedButtonId();
                ((SimpleFormResponse) temp).handle(player, (FormWindowSimple) formWindow, (int) data);
                return;
            }
            if (formWindow instanceof FormWindowCustom) {
                data = new ArrayList<>(((FormResponseCustom) formResponse).getResponses().values());
                ((CustomFormResponse) temp).handle(player, (FormWindowCustom) formWindow, (List<Object>) data);
                return;
            }
            if (formWindow instanceof FormWindowModal) {
                data = ((FormResponseModal) formResponse).getClickedButtonId();
                ((ModalFormResponse) temp).handle(player, (FormWindowModal) formWindow, (int) data);
            }
        }
    }
}