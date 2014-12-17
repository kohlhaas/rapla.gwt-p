package org.rapla.client.plugin.view;

import com.google.gwt.user.client.ui.Widget;

public interface ViewServiceProviderInterface {
	public Widget createContent();
	
	public void updateContent();
}
