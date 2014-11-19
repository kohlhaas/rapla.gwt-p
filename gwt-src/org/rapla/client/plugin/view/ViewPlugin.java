package org.rapla.client.plugin.view;


public interface ViewPlugin {

	ContentDrawer getContentDrawer();

	String getName();
	
	boolean isEnabled();
	
	void enable();
}
