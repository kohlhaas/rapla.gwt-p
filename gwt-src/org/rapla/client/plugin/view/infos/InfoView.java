package org.rapla.client.plugin.view.infos;

import javax.inject.Inject;

import org.rapla.client.plugin.view.ContentDrawer;
import org.rapla.client.plugin.view.ViewPlugin;

public class InfoView implements ViewPlugin {

	@Inject
	private InfoDrawer id;
	
	public InfoView(){	
	}	
	
	@Override
	public ContentDrawer getContentDrawer() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Veranstaltungsinfos";
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub

	}

}
