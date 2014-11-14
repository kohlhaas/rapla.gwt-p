package org.rapla.client.plugin.view.list;

import javax.inject.Inject;

import org.rapla.client.content.ContentDrawer;
import org.rapla.client.plugin.view.ViewPlugin;

public class ListViewPlugin implements ViewPlugin {

    @Inject
	private ListDrawer ld;

	public ListViewPlugin() {
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public ContentDrawer getContentDrawer() {
		return ld;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		
	}
}
