package org.rapla.client.plugin.view.infos;

import java.util.List;

import org.rapla.client.event.RaplaEventBus;
import org.rapla.client.event.ViewChangedEvent;
import org.rapla.client.plugin.view.ContentDrawer;
import org.rapla.client.plugin.view.ViewPlugin;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
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
	    //bar.setTabEnabled(0, false);
	    bar.selectTab(0);
	    

   
		FlowPanel content = new FlowPanel();
		
		bar.addSelectionHandler(new SelectionHandler<Integer>() {
	        public void onSelection(SelectionEvent<Integer> event) {
	          // Let the user know what they just did.
	          Window.alert("You clicked tab " + event.getSelectedItem());
	          
	          // add all three different views here 
	        }});
		 
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
