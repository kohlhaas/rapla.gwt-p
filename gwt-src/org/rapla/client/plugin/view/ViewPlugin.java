package org.rapla.client.plugin.view;

import org.rapla.client.content.ContentDrawer;

public interface ViewPlugin {

	ContentDrawer getContentDrawer();

	String getName();
	
	boolean isEnabled();
	
	void enable();
}
