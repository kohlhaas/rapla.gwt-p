package org.rapla.client.plugin.view.list;

import org.rapla.client.content.ContentDrawer;
import org.rapla.client.plugin.view.ViewPlugin;

import com.google.gwt.core.shared.GWT;

public class ListViewPlugin implements ViewPlugin {

	private ListDrawer ld;

	public ListViewPlugin() {
		ld = GWT.create(ListDrawer.class);
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
