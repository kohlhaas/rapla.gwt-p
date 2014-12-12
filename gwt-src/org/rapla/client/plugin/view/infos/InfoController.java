package org.rapla.client.plugin.view.infos;

import javax.inject.Inject;

import org.rapla.client.plugin.view.ContentDrawer;

import com.google.gwt.user.client.ui.Widget;

public class InfoController implements ContentDrawer {
	
	@Inject
	InfoDrawer infoDrawer;

	
	@Override
	public Widget createContent() {
		// TODO Auto-generated method stub
		return infoDrawer.createContent();
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
