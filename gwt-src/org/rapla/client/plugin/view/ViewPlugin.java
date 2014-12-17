package org.rapla.client.plugin.view;

import org.rapla.client.factory.ViewServiceProviderInterface;


public interface ViewPlugin {

	ViewServiceProviderInterface getContentDrawer();

	String getName();
	
	boolean isEnabled();
	
	void enable();
}
