package org.rapla.client.plugin.view;

import com.google.gwt.user.client.ui.Widget;

public interface View {
	Widget createView();
	
	String getName();
}
