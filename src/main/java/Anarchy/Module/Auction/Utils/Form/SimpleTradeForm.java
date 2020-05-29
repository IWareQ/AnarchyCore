package Anarchy.Module.Auction.Utils.Form;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;

import java.util.List;

public class SimpleTradeForm extends FormWindowSimple {
	public SimpleTradeForm(String title, String content) {
		super(title, content);
	}

	public SimpleTradeForm(String title, String content, List<ElementButton> buttons) {
		super(title, content, buttons);
	}
}