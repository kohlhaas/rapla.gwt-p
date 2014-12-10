package org.rapla.client.plugin.view.infos;

import java.util.List;

import org.rapla.client.plugin.view.ContentDrawer;
import org.rapla.client.plugin.view.ViewPlugin;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;

public class InfoController implements ContentDrawer {

	
	private List<ViewPlugin> views;
	
	private ListBox listBox;
	
	@Override
	public Widget createContent() {
		// TODO Auto-generated method stub
		
	    final TabBar bar = new TabBar();
	    bar.addTab("Veranstaltungsinfos");
	    bar.addTab("Resourcen");
	    bar.addTab("Termine");
		FlowPanel content = new FlowPanel();
		content.add(bar);

		return content;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}
	
	public ContentDrawer getSelectedContentDrawer() {		
		return this;
	}	

}
