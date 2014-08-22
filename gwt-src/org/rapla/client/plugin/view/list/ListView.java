package org.rapla.client.plugin.view.list;

import org.rapla.client.plugin.view.View;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ListView implements View {

	@Override
	public Widget createView() {
		return new HTML("List View");
	}

	@Override
	public String getName() {
		return "list";
	}
}
