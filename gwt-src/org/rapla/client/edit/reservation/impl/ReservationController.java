package org.rapla.client.edit.reservation.impl;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.GWTReservationController;
import org.rapla.client.factory.InfoViewInterface;
import org.rapla.client.factory.ResourceDatesInterface;
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
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.PermissionContainer;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.domain.internal.AllocatableImpl;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.AttributeType;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.entities.dynamictype.ClassificationFilter;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.DynamicTypeAnnotations;
import org.rapla.entities.dynamictype.internal.AttributeImpl;
import org.rapla.entities.dynamictype.internal.ClassificationImpl;
import org.rapla.entities.dynamictype.internal.DynamicTypeImpl;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import com.google.gwt.dev.util.collect.HashMap;
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
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Button;

import org.rapla.facade.ClientFacade;
import org.rapla.gui.internal.TreeAllocatableSelection;

public class ReservationController implements GWTReservationController,
		ViewSelectionChangedHandler {

	// private ContentDrawer infoDrawer;

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

	public Reservation reservationTmp;

	public String reservationName;

	private RaplaContext context;

	private ClientFacade facade;

	ViewServiceProviderInterface currentView;
	ViewServiceProviderInterface view1;
	ViewServiceProviderInterface view2;
	Collection<ViewServiceProviderInterface> views = new ArrayList();

	@Inject
	GWTRaplaLocale GWTRaplaLocale;

	Logger logger = Logger.getLogger("reservationController");

	BasicButtonHandler buttonHandler;

	boolean isNew;

	public void setContext(RaplaContext context) {
		this.context = context;
	}

	public RaplaContext getContext() {
		return this.context;
	}

	public void setFacade(ClientFacade facade) {
		this.facade = facade;
	}

	public ClientFacade getFacade() {
		return this.facade;
	}

	@Override
	public void edit(final Reservation event, boolean isNew)
			throws RaplaException {
		reservationTmp = event;
		this.isNew = isNew;

	}

	public PopupPanel createContent() {

		// loadViews();
		currentView = view1;

		popupContent = new PopupPanel();
		popupContent.setGlassEnabled(true);
		popupContent.setAnimationEnabled(true);
		popupContent.setAnimationType(PopupPanel.AnimationType.ROLL_DOWN);
		Integer height = (int) (Window.getClientHeight() * 0.90);
		Integer width = (int) (Window.getClientWidth() * 0.90);
		popupContent.setHeight(height.toString() + "px");
		popupContent.setWidth(width.toString() + "px");

		buttonHandler = new BasicButtonHandler(context, this);

		// reservation = this.facade.newReservation();
		// ///

		bar = new TabBar();
		bar.addTab("Veranstaltungsinfos");
		bar.addTab("Termine und Resourcen");
		// bar.setTabEnabled(0, false);
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

				if (currentView != null) {
					try {
						ReservationController.this
								.saveTemporaryChanges(currentView);
					} catch (RaplaException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

				// Let the user know what they just did.
				// Window.alert("You clicked tab " + event.getSelectedItem());

				contentPanel.clear();
				// contentPanel.add(event.getSelectedItem() == 0 ?
				// infoView.createContent() :
				// resourceDatesView.createContent());

				currentView = ViewFactory
						.getInstance((event.getSelectedItem() == 0 ? ViewEnumTypes.INFOVIEW_DESKTOP
								: ViewEnumTypes.RESOURCEDATESVIEW_DESKTOP));
				contentPanel.add(currentView.createContent());

				if (currentView != null && currentView instanceof InfoViewInterface) {
					currentView = (InfoViewInterface) currentView;
					try {
						((InfoViewInterface) currentView)
								.setEventTypes(ReservationController.this
										.getEventTypes());
						((InfoViewInterface) currentView)
								.setDynamicFields(ReservationController.this
										.getDynamicFields("reservation2"));

					} catch (RaplaException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

				}

			}
		});

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
	public void saveTemporaryChanges()
			throws RaplaException {		
		saveTemporaryChanges(currentView);
	}

	private void saveTemporaryChanges(ViewServiceProviderInterface currentView)
			throws RaplaException {

		

		if (currentView instanceof InfoViewInterface) {
			
			reservationName = ((InfoViewInterface) currentView).getTitelInput()
					.getText();

			DynamicType[] types;
			types = ReservationController.this.getFacade().getDynamicTypes(
					DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);

			// String text = reservationName;
			// List<DynamicType> eventTypes = new ArrayList<DynamicType>();
			Classification classification = reservationTmp.getClassification();

			// Set new Titel/Name for reservationTmp
			Attribute first = classification.getType().getAttributes()[0];
			classification.setValue(first, reservationName);

			// Attribute first = classification.getType().getAttributes()[0];
			List<String> items = new ArrayList<String>();
			List<DynamicType> eventTypes = new ArrayList<DynamicType>();
			User user = getFacade().getUser();

			Classification classificationTmp;

			String key = null;

			for (DynamicType type : types) {
				if (PermissionContainer.Util.canCreate(type, user))
					eventTypes.add(type);
			}

			for (int i = 0; i < eventTypes.size(); i++) {
				String name = eventTypes.get(i).getName(
						GWTRaplaLocale.getLocale());
				items.add(name);
				if (eventTypes
						.get(i)
						.getName(GWTRaplaLocale.getLocale())
						.equalsIgnoreCase(
								((InfoViewInterface) currentView)
										.getSelectedEventType())) {
					key = eventTypes.get(i).getKey();
				}
			}

			classificationTmp = facade.getDynamicType(key).newClassification();

			Allocatable resourceEventType = facade.newAllocatable(
					classificationTmp, user);

			reservationTmp.addAllocatable(resourceEventType);

			// classification.setType(((facade.getDynamicType((((InfoViewInterface)
			// currentView).getSelectedEventType())))));

			// reservationTmp.((InfoViewInterface) currentView).getResources();

			// reservationTmp.addAllocatable(new Allocatable());
			// facade.getDynamicType(((InfoViewInterface)
			// currentView).getSelectedEventType()).get;

			// ((InfoViewInterface) currentView).getVorlesungsStundenInput();
			// Attribute attrTmp = facade.newAttribute(AttributeType.INT);
			// Allocatable resourceDuration = facade.newResource();
			classificationTmp = facade
					.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE)[0]
					.newClassification();
			classificationTmp.setValue("name", "Vorlesungsstunden");
			classificationTmp.setValue("duration",
					((InfoViewInterface) currentView)
							.getVorlesungsStundenInput().getText());

			Allocatable resourceDuration = facade.newAllocatable(
					classificationTmp, user);
			// facade.newPeriod();
			// resourceDuration.setAnn...
			// resourceDuration.setAnnotation("duration", ((InfoViewInterface)
			// currentView).getVorlesungsStundenInput().getText());
			// TO DO: look up real key for setAnnotation (is duration the
			// correct one?)

			reservationTmp.addAllocatable(resourceDuration);

			// ((InfoViewInterface) currentView).getStudiengangListBox();
			// Allocatable resourceCourse = facade.newResource();
			// := (entspricht)
			// facade.newAllocatable((facade.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE)[0].newClassification()));
			// resourceDuration.
			// resourceCourse.setAnnotation("course", ((InfoViewInterface)
			// currentView).getStudiengangListBox().getSelectedItemText());
			// TO DO: look up real key for setAnnotation (is course the correct
			// one?)
			// ((InfoViewInterface) currentView).getStudiengangListBoxAuswahl();
			classificationTmp = facade
					.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE)[0]
					.newClassification();
			classificationTmp.setValue("name", "Studiengang");
			classificationTmp.setValue("course",
					((InfoViewInterface) currentView).getStudiengangListBox()
							.getSelectedItemText());

			Allocatable resourceCourse = facade.newAllocatable(
					classificationTmp, user);

			reservationTmp.addAllocatable(resourceCourse);

			// getCreateableDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE);

		} else {
			currentView = (ResourceDatesInterface) currentView;
		}

	}

	private void loadViews() {

		views.add(ViewFactory.getInstance(ViewEnumTypes.INFOVIEW_DESKTOP));
		views.add(ViewFactory
				.getInstance(ViewEnumTypes.RESOURCEDATESVIEW_DESKTOP));

		for (ViewServiceProviderInterface view : views) {
			view.createContent();
		}

		if (currentView instanceof InfoViewInterface) {
			currentView = (InfoViewInterface) currentView;
			try {
				((InfoViewInterface) currentView)
						.setEventTypes(ReservationController.this
								.getEventTypes());
				((InfoViewInterface) currentView)
						.setDynamicFields(ReservationController.this
								.getDynamicFields("reservation2"));

			} catch (RaplaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

		}

	}

	public void updateContent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void viewChanged() {
		// TODO Auto-generated method stub

	}

	public List<DynamicType> getCreateableDynamicTypes(String classificationType)
			throws RaplaException {

		DynamicType[] types;
		List<String> items = new ArrayList<String>();
		types = getFacade().getDynamicTypes(classificationType);
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = getFacade().getUser();
		for (DynamicType type : types) {
			if (PermissionContainer.Util.canCreate(type, user))
				eventTypes.add(type);
		}

		return eventTypes;
	}

	public List<String> getResourceTypesNames() throws RaplaException {

		return getDynamicTypesNames(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE);
	}

	public List<String> getPersonTypesNames() throws RaplaException {

		return getDynamicTypesNames(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_PERSON);
	}

	public List<String> getDynamicTypesNames(String classificationType)
			throws RaplaException {

		DynamicType[] types;
		List<String> items = new ArrayList<String>();
		types = getFacade().getDynamicTypes(classificationType);
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = getFacade().getUser();
		for (DynamicType type : types) {
			if (PermissionContainer.Util.canCreate(type, user))
				eventTypes.add(type);
		}

		for (int i = 0; i < eventTypes.size(); i++) {
			String name = eventTypes.get(i).getName(GWTRaplaLocale.getLocale());
			items.add(name);
		}

		return items;
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
	
	public Attribute[] getDynamicFields(String eventKey) throws RaplaException{

		AttributeImpl attr = new AttributeImpl();
		
		DynamicTypeImpl type = new DynamicTypeImpl();
		
		
		
		DynamicType[] types;
		types = ReservationController.this.getFacade().getDynamicTypes(
				DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);
		
		List<String> items = new ArrayList<String>();
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = getFacade().getUser();

		

		String key = eventKey;

		for (DynamicType typen : types) {
			if (PermissionContainer.Util.canCreate(typen, user))
				eventTypes.add(typen);
		}

		for (int i = 0; i < eventTypes.size(); i++) {
			String name = eventTypes.get(i).getName(
					GWTRaplaLocale.getLocale());
			items.add(name);
			if (eventTypes
					.get(i)
					.getName(GWTRaplaLocale.getLocale())
					.equalsIgnoreCase(
							((InfoViewInterface) currentView)
									.getSelectedEventType())) {
				key = eventTypes.get(i).getKey();
			}
		}

		
		type.setKey(key);
		
		
//		
//		
//		ArrayList<Attributes> attributes = new ArrayList<Attributes>();
//		
//
		Attribute[] attributes = {new AttributeImpl()}; 
		
		for(int i = 0; i < type.getAttributes().length; i++){
//
			attributes[i] = type.getAttributes()[i];
			attributes[6] = new AttributeImpl(); 
//				
//			attributes.add(i, new Attributes(type.getAttributes()[i].toString()));
//				
//				for(int j = 0; j < type.getAttributes()[i].getConstraintKeys().length; j++ ){
//				
//				attributes.get(i).constraintKeys.add((type.getAttributes()[i].getConstraintKeys()[j]));
//				}
//		
//				
		}
//		
				
		
		return attributes;	
	}
	
	public String[] getConstraintKeys(Attribute attribute) throws RaplaException{
		
	
		
		String[] constraintKeys = null; 
		
		for(int i = 0; i < attribute.getConstraintKeys().length; i++){
			constraintKeys[i] = attribute.getConstraintKeys()[i];
		}
		
		return constraintKeys;
	}

//	public class Attributes{
//	     
//		ArrayList constraintKeys; 
//		String name;
//		
//		public Attributes(String name){
//			this.name = name; 
//		}
//		
//		public String getName(){
//			return this.name;
//		}
//		
//		public void setName(String name){
//			this.name = name; 
//		}
//		
//		
//		
//	}


	public void getContentOfElements() {

	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public ViewServiceProviderInterface getCurrentView() {
		return currentView;
	}

	public void setCurrentView(ViewServiceProviderInterface currentView) {
		this.currentView = currentView;
	}

	protected void loadDataFromReservation(ViewServiceProviderInterface currentView2) {
		Classification classificationTmp = reservationTmp.getClassification();
		Allocatable[] allocatables = reservationTmp.getAllocatables();

		if (currentView2 instanceof InfoViewInterface) {

			for (int a=0; a < allocatables.length; a++) {
				String tmp = (String) allocatables[a].getClassification()
						.getValue("name");

				// classificationTmp.setValue("name", "Studiengang");
				// classificationTmp.setValue("course",
				// ((InfoViewInterface) currentView).getStudiengangListBox()
				// .getSelectedItemText());

				for (int i = 0; i < (((InfoViewInterface) currentView2)
						.getStudiengangListBox().getItemCount()); i++) {
					if (tmp.equals(((InfoViewInterface) currentView2)
							.getStudiengangListBox().getItemText(i))) {
						((InfoViewInterface) currentView2)
								.getStudiengangListBox().setSelectedIndex(i);
					}
				}
				

			}
		} else {

		}

	}


}