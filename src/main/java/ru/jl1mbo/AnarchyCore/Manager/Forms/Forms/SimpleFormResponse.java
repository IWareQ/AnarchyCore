package ru.jl1mbo.AnarchyCore.Manager.Forms.Forms;

import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindowSimple;

public interface SimpleFormResponse extends FormResponse {

	void handle(Player targetPlayer, FormWindowSimple targetForm, int data);
}