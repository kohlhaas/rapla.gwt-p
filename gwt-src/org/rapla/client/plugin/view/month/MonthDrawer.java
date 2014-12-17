package org.rapla.client.plugin.view.month;

import org.rapla.client.plugin.view.ViewServiceProviderInterface;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class MonthDrawer implements ViewServiceProviderInterface {

	@Override
	public Widget createContent() {
		return new HTML("Monat-Darstellung");
	}

	@Override
	public void updateContent() {
		// Check
	}

}
