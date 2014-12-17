package org.rapla.client.plugin.view.list;

import javax.inject.Inject;

import org.rapla.client.plugin.view.ViewServiceProviderInterface;
import org.rapla.client.plugin.view.ViewPlugin;

public class ListView implements ViewPlugin {

    @Inject
	private ListDrawer ld;

	public ListView() {
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public ViewServiceProviderInterface getContentDrawer() {
		return ld;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		
	}
}
