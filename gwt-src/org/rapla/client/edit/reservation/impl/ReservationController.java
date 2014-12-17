package org.rapla.client.edit.reservation.impl;


import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.GWTReservationController;
import org.rapla.client.factory.InfoViewInterface;
import org.rapla.client.factory.ViewEnumTypes;
import org.rapla.client.factory.ViewFactory;
import org.rapla.client.factory.ViewServiceProviderInterface;
import org.rapla.client.internal.GWTRaplaLocale;
import org.rapla.client.internal.RaplaGWTClient;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent.ViewSelectionChangedHandler;
import org.rapla.client.plugin.view.infos.InfoView;
import org.rapla.client.plugin.view.resoursedates.ResourceDatesView;
import org.rapla.components.xmlbundle.I18nBundle;
import org.rapla.entities.User;
import org.rapla.entities.domain.PermissionContainer;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.DynamicTypeAnnotations;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Button;

import org.rapla.facade.ClientFacade;

public class ReservationController implements GWTReservationController, ViewSelectionChangedHandler{

//	private ContentDrawer infoDrawer;
	
	@Inject
	InfoView infoView;
	@Inject
	ResourceDatesView resourceDatesView;
	
	private VerticalPanel layout;
	private FlowPanel tabBarPanel;
	private SimplePanel contentPanel;
	private FlowPanel buttonsPanel;
	
	public PopupPanel popupContent;
	
	private TabBar bar;
	
	private Button cancel, save, delete;
	
	public Reservation reservation;
	
	public TextBox reservationName;
	
    private RaplaContext context;
    
    private ClientFacade facade;
    
    @Inject 
    GWTRaplaLocale GWTRaplaLocale; 
	

    Logger logger = Logger.getLogger("reservationController");
    
    BasicButtonHandler buttonHandler;

    
    public void setContext(RaplaContext context)
    {
        this.context = context;
    }
    
    public RaplaContext getContext()
    {
        return this.context;
    }
    
    public void setFacade(ClientFacade facade)
    {
        this.facade = facade;
    }
    
    
    public ClientFacade getFacade()
    {
        return this.facade;
    }
 
    
    @Override
    public void edit(final Reservation event, boolean isNew) throws RaplaException {
    	reservation = event;
    	
    	popupContent = new PopupPanel();
		popupContent.setGlassEnabled(true);
		popupContent.setAnimationEnabled(true);
		popupContent.setAnimationType(PopupPanel.AnimationType.ROLL_DOWN);
		Integer height = (int) (Window.getClientHeight() * 0.90);
		Integer width = (int) (Window.getClientWidth() * 0.90);
		popupContent.setHeight(height.toString() + "px");
		popupContent.setWidth(width.toString() + "px");
		
		reservationName = new TextBox();
		
		buttonHandler = new BasicButtonHandler(context, this);
		/////
		
	    bar = new TabBar();
	    bar.addTab("Veranstaltungsinfos");
	    bar.addTab("Termine und Resourcen");
	    //bar.setTabEnabled(0, false);
	    bar.selectTab(0);
	    
	    cancel = new Button("abbrechen");
	    cancel.setStyleName("cancelButton");
	    cancel.addClickHandler(buttonHandler);
	    
	    save = new Button("speichern");
	    save.setStyleName("saveButton");
	    save.addClickHandler(buttonHandler);	    
	    
	    delete = new Button("l\u00F6schen");
	    delete.setStyleName("deleteButton");
	    delete.addClickHandler(buttonHandler);

	    layout = new VerticalPanel();
	    tabBarPanel = new FlowPanel();
		contentPanel = new SimplePanel();
		buttonsPanel = new FlowPanel();
		
		tabBarPanel.add(bar);
		
		bar.addSelectionHandler(new SelectionHandler<Integer>() {
	        public void onSelection(SelectionEvent<Integer> event) {
	          // Let the user know what they just did.
	          //Window.alert("You clicked tab " + event.getSelectedItem());
	        	 contentPanel.clear();
	        	 contentPanel.add(event.getSelectedItem() == 0 ? infoView.createContent() : resourceDatesView.createContent());
	        }});
		 
		bar.selectTab(0);
		
		buttonsPanel.add(cancel);
		buttonsPanel.add(save);
		buttonsPanel.add(delete);
		
	    layout.add(buttonsPanel);
	    layout.add(tabBarPanel);
	    layout.add(contentPanel);
	    
		layout.setCellHeight(buttonsPanel, "40px");
		layout.setCellHeight(tabBarPanel, "50px");
	    
		
		////
		popupContent.add(layout);
		popupContent.center();
		
    }
	
	
	public PopupPanel createContent() {
				
		popupContent = new PopupPanel();
		popupContent.setGlassEnabled(true);
		popupContent.setAnimationEnabled(true);
		popupContent.setAnimationType(PopupPanel.AnimationType.ROLL_DOWN);
		Integer height = (int) (Window.getClientHeight() * 0.90);
		Integer width = (int) (Window.getClientWidth() * 0.90);
		popupContent.setHeight(height.toString() + "px");
		popupContent.setWidth(width.toString() + "px");
		
		reservationName = new TextBox();
		
		buttonHandler = new BasicButtonHandler(context, this);
		
		//reservation =  this.facade.newReservation();
		/////
		
	    bar = new TabBar();
	    bar.addTab("Veranstaltungsinfos");
	    bar.addTab("Termine und Resourcen");
	    //bar.setTabEnabled(0, false);
	    bar.selectTab(0);
	    
	    cancel = new Button("abbrechen");
	    cancel.setStyleName("cancelButton");
	    cancel.addClickHandler(buttonHandler);
	    
	    save = new Button("speichern");
	    save.setStyleName("saveButton");
	    save.addClickHandler(buttonHandler);	    
	    
	    delete = new Button("l\u00F6schen");
	    delete.setStyleName("deleteButton");
	    delete.addClickHandler(buttonHandler);

	    layout = new VerticalPanel();
	    tabBarPanel = new FlowPanel();
		contentPanel = new SimplePanel();
		buttonsPanel = new FlowPanel();
		
		tabBarPanel.add(bar);
		
		bar.addSelectionHandler(new SelectionHandler<Integer>() {
	        public void onSelection(SelectionEvent<Integer> event) {
	          // Let the user know what they just did.
	          //Window.alert("You clicked tab " + event.getSelectedItem());
	        	
	        	 contentPanel.clear();
	        //	 contentPanel.add(event.getSelectedItem() == 0 ? infoView.createContent() : resourceDatesView.createContent());
	        	 
	        	 
	        	 ViewServiceProviderInterface view = ViewFactory.getInstance((event.getSelectedItem() == 0 ? ViewEnumTypes.INFOVIEW_DESKTOP : ViewEnumTypes.RESOURCEDATESVIEW_DESKTOP), ReservationController.this);
	        	 contentPanel.add(view.createContent());

	        	 
	        	 if(view instanceof InfoViewInterface){
	        	   view = (InfoViewInterface) view;
	        	   try {
					((InfoViewInterface) view).setEventTypes(ReservationController.this.getEventTypes());
				} catch (RaplaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 }else{
	        		 
	        	 }

	        	 
	        }});
		 
		bar.selectTab(0);
		
		buttonsPanel.add(cancel);
		buttonsPanel.add(save);
		buttonsPanel.add(delete);
		
	    layout.add(buttonsPanel);
	    layout.add(tabBarPanel);
	    layout.add(contentPanel);
	    
		layout.setCellHeight(buttonsPanel, "40px");
		layout.setCellHeight(tabBarPanel, "50px");
	    

		popupContent.add(layout);
		popupContent.center();
		
		return popupContent;

	}

	
	public void updateContent() {
		// TODO Auto-generated method stub

	}
	
	

	@Override
	public void viewChanged() {
		// TODO Auto-generated method stub
		
	}
	
	public List<String> getEventTypes() throws RaplaException{
		
		DynamicType[] types;
		List<String> items = new ArrayList<String>();
		types = getFacade().getDynamicTypes( DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = getFacade().getUser();
		for ( DynamicType type: types)
		{
		if (PermissionContainer.Util.canCreate(type, user))
		eventTypes.add(type);
		}

		for (int i = 0; i < eventTypes.size(); i++) 
		{
		String name = eventTypes.get(i).getName(GWTRaplaLocale.getLocale());
		items.add(name);
		} ;
		
		
		return items;
	}

	

}