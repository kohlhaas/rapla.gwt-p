package org.rapla.client.plugin.view.month;

import javax.inject.Inject;

import org.rapla.client.factory.ViewServiceProviderInterface;
import org.rapla.client.plugin.view.ViewPlugin;

public class MonthView implements ViewPlugin {
	@Inject
    private MonthDrawer md;

	public MonthView() {
	}

	@Override
	public ViewServiceProviderInterface getContentDrawer() {
		return md;
	}

	@Override
	public String getName() {
		return "Month";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void enable() {

	}

}
