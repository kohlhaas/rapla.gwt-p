package org.rapla.client.plugin.view.termine;

import javax.inject.Inject;

import org.rapla.client.plugin.view.ContentDrawer;
import org.rapla.client.plugin.view.ViewPlugin;

public class TerminView implements ViewPlugin {

	@Inject
	private TerminDrawer td;
	
	public TerminView(){	
	}	
	
	@Override
	public ContentDrawer getContentDrawer() {
		// TODO Auto-generated method stub
		return td;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Termine";
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
