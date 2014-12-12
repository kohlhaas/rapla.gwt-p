package org.rapla.client.plugin.view.resoursedates;

import org.rapla.client.plugin.view.ContentDrawer;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ResourceDatesDrawer implements ContentDrawer {

	@Override
	public Widget createContent() {
		FlowPanel fp = new FlowPanel();
		Label test = new Label("test");
		fp.add(test);
		return fp;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
