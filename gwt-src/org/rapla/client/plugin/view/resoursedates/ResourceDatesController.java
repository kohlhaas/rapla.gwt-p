package org.rapla.client.plugin.view.resoursedates;

import javax.inject.Inject;

import org.rapla.client.plugin.view.ContentDrawer;

import com.google.gwt.user.client.ui.Widget;

public class ResourceDatesController implements ContentDrawer {
	
	@Inject
	ResourceDatesDrawer rdDrawer;

	@Override
	public Widget createContent() {
		// TODO Auto-generated method stub
		return rdDrawer.createContent();
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
