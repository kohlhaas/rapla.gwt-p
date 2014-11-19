package org.rapla.client.plugin.view.month;

import org.rapla.client.plugin.view.ContentDrawer;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class MonthDrawer implements ContentDrawer {

	@Override
	public Widget createContent() {
		return new HTML("Monat-Darstellung");
	}

	@Override
	public void updateContent() {
		// Check
	}

}
