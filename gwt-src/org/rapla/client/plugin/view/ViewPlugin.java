package org.rapla.client.plugin.view;


public interface ViewPlugin {

	ViewServiceProviderInterface getContentDrawer();

	String getName();
	
	boolean isEnabled();
	
	void enable();
}
