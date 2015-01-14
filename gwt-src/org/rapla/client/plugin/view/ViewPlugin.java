package org.rapla.client.plugin.view;

import org.rapla.client.mwi14_1.factory.ViewServiceProviderInterface;


public interface ViewPlugin {

	ViewServiceProviderInterface getContentDrawer();

	String getName();
	
	boolean isEnabled();
	
	void enable();
}
