package org.rapla.client.edit.reservation.impl;

import java.util.List;

import org.rapla.client.event.RaplaEventBus;
import org.rapla.client.event.ViewChangedEvent;
import org.rapla.client.plugin.view.ContentDrawer;
import org.rapla.client.plugin.view.ViewPlugin;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;

public class ReservationController implements ContentDrawer {

	
	private List<ViewPlugin> views;
	
	private ListBox listBox;

	private Label cancel;
	private Label save;
	private Label delete;
	
	
	@Override
	public Widget createContent() {
		// TODO Auto-generated method stub
		
	    final TabBar bar = new TabBar();
	   
	    cancel = new Label("abbrechen");
	    cancel.setStyleName("cancelButton");
	    cancel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	    
	    save = new Label("speichern");
	    save.setStyleName("saveButton");
	    
	    delete = new Label("löschen");
	    delete.setStyleName("deleteButton");
	    
	    bar.addTab("Veranstaltungsinfos");
	    bar.addTab("Termine und Resourcen");
	    //bar.setTabEnabled(0, false);
	    bar.selectTab(0);
	    
	    listBox = new ListBox();
	    // add the Event Types from data.xml here
	    listBox.addItem("Lehrveranstaltung");
	    listBox.addItem("Püfung");
	    listBox.addItem("Sonstige Veranstaltung");
	    
	    
	    DockPanel mainPanel = new DockPanel();
	    FlowPanel tabBar = new FlowPanel();
		FlowPanel content = new FlowPanel();
		FlowPanel buttons = new FlowPanel();
		
		bar.addSelectionHandler(new SelectionHandler<Integer>() {
	        public void onSelection(SelectionEvent<Integer> event) {
	          // Let the user know what they just did.
	          Window.alert("You clicked tab " + event.getSelectedItem());
	          
	          // add all three different views here 
	        }});
		 
		tabBar.add(bar);
	
		content.add(listBox);
		
		buttons.add(cancel);
		buttons.add(save);
		buttons.add(delete);
		
		mainPanel.add(tabBar, DockPanel.NORTH);
		mainPanel.add(buttons, DockPanel.SOUTH);
		mainPanel.add(content, DockPanel.CENTER);
		
		return mainPanel;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}
	
	public ContentDrawer getSelectedContentDrawer() {		
		return this;
	}	

}
