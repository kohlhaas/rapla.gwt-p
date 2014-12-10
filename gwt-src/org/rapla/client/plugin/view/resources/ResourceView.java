package org.rapla.client.plugin.view.resources;

import javax.inject.Inject;

import org.rapla.client.plugin.view.ContentDrawer;
import org.rapla.client.plugin.view.ViewPlugin;

public class ResourceView implements ViewPlugin {
    
	@Inject
	private ResourceDrawer rd;
	
	public ResourceView(){	
	}
	
	@Override
	public ContentDrawer getContentDrawer() {
		// TODO Auto-generated method stub
		return rd;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Resourcen";
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
