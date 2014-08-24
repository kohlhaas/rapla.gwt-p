package org.rapla.client.plugin.view.month;

import org.rapla.client.content.ContentDrawer;
import org.rapla.client.plugin.view.ViewPlugin;

import com.google.gwt.core.shared.GWT;

public class MonthViewPlugin implements ViewPlugin {
	private MonthDrawer md;

	public MonthViewPlugin() {
		md = GWT.create(MonthDrawer.class);
	}

	@Override
	public ContentDrawer getContentDrawer() {
		return md;
	}

	@Override
	public String getName() {
		return "Month";
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
