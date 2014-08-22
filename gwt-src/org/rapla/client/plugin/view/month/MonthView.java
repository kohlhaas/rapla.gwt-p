package org.rapla.client.plugin.view.month;

import org.rapla.client.plugin.view.View;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class MonthView implements View{

	@Override
	public Widget createView() {
		return new HTML("Month View");
	}

	@Override
	public String getName() {
		return "Month";
	}

}
