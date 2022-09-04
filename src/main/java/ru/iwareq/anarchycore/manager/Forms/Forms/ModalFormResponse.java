package ru.iwareq.anarchycore.manager.Forms.Forms;

import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindowModal;

public interface ModalFormResponse extends FormResponse {

	void handle(Player targetPlayer, FormWindowModal targetForm, int data);
}