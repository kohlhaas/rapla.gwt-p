package org.rapla.client.edit.reservation.impl;


import org.rapla.client.plugin.view.ContentDrawer;
import org.rapla.client.plugin.view.infos.InfoDrawer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;

public class ReservationController implements ContentDrawer {

//	private ContentDrawer infoDrawer;
	
	private DockPanel mainPanel;
	private FlowPanel tabBarPanel;
	private FlowPanel contentPanel;
	private FlowPanel buttonsPanel;
	
	private TabBar bar;
	
	private Label cancel;
	private Label save;
	private Label delete;
		
	private ListBox listBox;	
	
	@Override
	public Widget createContent() {
		// TODO Auto-generated method stub
		
	    bar = new TabBar();
	    bar.addTab("Veranstaltungsinfos");
	    bar.addTab("Termine und Resourcen");
	    //bar.setTabEnabled(0, false);
	    bar.selectTab(0);
	    
	    cancel = new Label("abbrechen");
	    cancel.setStyleName("cancelButton");
	    cancel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// PopUp wird geschlossen ohne speichern oder löschen
				
			}
	    	
	    });
	    
	    save = new Label("speichern");
	    save.setStyleName("saveButton");
	    save.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// Veranstaltung wird gespeichert
				
			}
	    	
	    });	    
	    
	    delete = new Label("loeschen");
	    delete.setStyleName("deleteButton");
	    delete.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// Veranstaltung wird gelöscht
				
			}
	    	
	    });

	    mainPanel = new DockPanel();
	    tabBarPanel = new FlowPanel();
		contentPanel = new FlowPanel();
		buttonsPanel = new FlowPanel();
		
		bar.addSelectionHandler(new SelectionHandler<Integer>() {
	        public void onSelection(SelectionEvent<Integer> event) {
	          // Let the user know what they just did.
	          Window.alert("You clicked tab " + event.getSelectedItem());
	          
	          // add all three different views here 
	        }});
		 
		tabBarPanel.add(bar);
	
		
		listBox = new ListBox();
        
	    // add the Event Types from data.xml here
	    listBox.addItem("Lehrveranstaltung");
	    listBox.addItem("Püfung");
	    listBox.addItem("Sonstige Veranstaltung");
	    
		
		contentPanel.add(listBox);
		//Hier wird je nach dem, welcher Tab ausgewählt, der InfoView oder der ResourceDatesView in den ContentPanel geladen. 
		//(Bzw. InfoDrawer und ResourceDateDrawer? ist das nicht das gleiche?)
		
		buttonsPanel.add(cancel);
		buttonsPanel.add(save);
		buttonsPanel.add(delete);
		
		mainPanel.add(tabBarPanel, DockPanel.NORTH);
		mainPanel.add(buttonsPanel, DockPanel.SOUTH);
		mainPanel.add(contentPanel, DockPanel.CENTER);
		
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
