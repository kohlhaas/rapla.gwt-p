package org.rapla.client.mwi14_1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
/*
import org.rapla.client.mwi14_1.factory.InfoViewInterface;
import org.rapla.client.mwi14_1.factory.ResourceDatesInterface;
import org.rapla.client.mwi14_1.factory.ViewEnumTypes;
import org.rapla.client.mwi14_1.factory.ViewFactory;
import org.rapla.client.mwi14_1.factory.ViewServiceProviderInterface;
import org.rapla.client.mwi14_1.view.infos.InfoView;
import org.rapla.client.mwi14_1.view.resoursedates.ResourceDatesView;
import org.rapla.entities.User;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.PermissionContainer;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.DynamicTypeAnnotations;
import org.rapla.entities.dynamictype.internal.AttributeImpl;
import org.rapla.entities.dynamictype.internal.DynamicTypeImpl;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReservationController implements org.rapla.client.edit.reservation.ReservationController
{
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
	RaplaLocale GWTRaplaLocale;

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

		Locale locale = GWTRaplaLocale.getLocale();
		reservationName = event.getName(locale);
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

				if (currentView != null
						&& currentView instanceof InfoViewInterface) {
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
				ReservationController.this.getContentOfElements();

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

	public void saveTemporaryChanges() throws RaplaException {
		saveTemporaryChanges(currentView);
	}

	private void saveTemporaryChanges(ViewServiceProviderInterface currentView)
			throws RaplaException {

		Classification classificationTmp = reservationTmp.getClassification();

		if (currentView instanceof InfoViewInterface) {

			reservationName = ((InfoViewInterface) currentView).getTitelInput()
					.getText();

			// // DynamicType[] types;
			// types = ReservationController.this
			// .getFacade()
			// .getDynamicTypes(
			// DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);

			// String text = reservationName;
			// List<DynamicType> eventTypes = new ArrayList<DynamicType>();

			// Set new Titel/Name for reservationTmp
			Attribute first = classificationTmp.getType().getAttributes()[0];
			classificationTmp.setValue(first, reservationName);

			// Attribute first = classification.getType().getAttributes()[0];

			// List<DynamicType> eventTypes = new ArrayList<DynamicType>();
			// User user = getFacade().getUser();

			// String key = null;
			//
			// for (DynamicType type : types) {
			// if (PermissionContainer.Util.canCreate(type, user))
			// eventTypes.add(type);
			// }
			//
			// for (int i = 0; i < eventTypes.size(); i++) {
			// String name = eventTypes.get(i).getName(
			// GWTRaplaLocale.getLocale());
			//
			// if (name.equalsIgnoreCase(((InfoViewInterface) currentView)
			// .getSelectedEventType())) {
			// key = eventTypes.get(i).getKey();
			// }
			// }

//			// maybe useful for another case
//			for (Allocatable a : facade.getAllocatables()) {
//				if (a.getName(GWTRaplaLocale.getLocale()).equals(
//						((InfoViewInterface) currentView)
//								.getSelectedEventType())) {
//					reservationTmp.addAllocatable((Allocatable) a);
//				}
//			}

			// change reservation type
			for (DynamicType type : facade
					.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)) {
				if (!(type.getName(GWTRaplaLocale.getLocale())
						.equals(classificationTmp.getType()))
						&& type.getName(GWTRaplaLocale.getLocale()).equals(
								((InfoViewInterface) currentView)
										.getSelectedEventType())) {
//					Reservation event = facade.newReservation(type
////							.newClassification());
//					reservationTmp = facade.edit(reservationTmp);
					

//					Allocatable[] allocatables = reservationTmp
//							.getAllocatables();
//					Appointment[] appointments = reservationTmp
//							.getAppointments();
//					Collection<Permission> permissions = reservationTmp
//							.getPermissionList();
					HashMap<Attribute, Object> attributes = new HashMap<Attribute, Object>();
					for (Attribute a : classificationTmp.getAttributes()) {
						attributes.put(a, classificationTmp.getValue(a));
					}
//
////					facade.remove(reservationTmp);
//					Classification classificationNew = event
//							.getClassification();
					reservationTmp.setClassification(type
					.newClassification());

					classificationTmp = reservationTmp.getClassification();
//
//					for (Allocatable allocatable : allocatables) {
//						event.addAllocatable(allocatable.clone());
//					}
//					for (Appointment a : appointments) {
//						event.addAppointment(a.clone());
//					}
//					for (Permission a : permissions) {
//						event.addPermission(a.clone());
//					}
					for (Attribute a : classificationTmp.getAttributes()) {
						classificationTmp
								.setValue(a, attributes.get(a));
					}
//					reservationTmp = event;
//					classificationTmp = classificationNew;
				}
			}

			// Classification classificationTmp2 = facade.getDynamicType(key)
			// .newClassification();
			//
			// Allocatable resourceEventType = facade.newAllocatable(
			// classificationTmp2, user);
			//
			// reservationTmp.addAllocatable(resourceEventType);

			// classification.setType(((facade.getDynamicType((((InfoViewInterface)
			// currentView).getSelectedEventType())))));

			// reservationTmp.((InfoViewInterface) currentView).getResources();

			// reservationTmp.addAllocatable(new Allocatable());
			// facade.getDynamicType(((InfoViewInterface)
			// currentView).getSelectedEventType()).get;

			// ((InfoViewInterface) currentView).getVorlesungsStundenInput();
			// Attribute attrTmp = facade.newAttribute(AttributeType.INT);
			// Allocatable resourceDuration = facade.newResource();

			// set Vorlesungstunden
			if (reservationTmp
					.getClassification()
					.getType()
					.equals(facade
							.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)[0])) {
				Attribute second = classificationTmp.getType().getAttributes()[5];
				classificationTmp.setValue(second, Integer
						.parseInt(((InfoViewInterface) currentView)
								.getVorlesungsStundenInput().getText()));
			}

			// classificationTmp = facade
			// .getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE)[0]
			// .newClassification();
			// classificationTmp.setValue("name", "Vorlesungsstunden");
			// classificationTmp.setValue("duration",
			// ((InfoViewInterface) currentView)
			// .getVorlesungsStundenInput().getText());
			//
			// Allocatable resourceDuration = facade.newAllocatable(
			// classificationTmp, user);
			// // facade.newPeriod();
			// resourceDuration.setAnn...
			// resourceDuration.setAnnotation("duration", ((InfoViewInterface)
			// currentView).getVorlesungsStundenInput().getText());
			// TO DO: look up real key for setAnnotation (is duration the
			// correct one?)
			//
			// reservationTmp.addAllocatable(resourceDuration);

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
			// classificationTmp = facade
			// .getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE)[0]
			// .newClassification();
			// classificationTmp.setValue("name", "Studiengang");
			// classificationTmp.setValue("course",
			// ((InfoViewInterface) currentView).getStudiengangListBox()
			// .getSelectedItemText());
			//
			//
			// Allocatable resourceCourse = facade.newAllocatable(
			// classificationTmp, user);
			//
			// reservationTmp.addAllocatable(resourceCourse);

//			// set Studiengang
//			Attribute third = classificationTmp.getType().getAttributes()[2];
//			Allocatable[] resources = facade.getAllocatables();
//			for (Allocatable a : resources) {
//				if (a.getName(this.GWTRaplaLocale.getLocale()).equals(
//						((InfoViewInterface) currentView)
//								.getStudiengangListBox().getSelectedItemText())) {
//					a.get ?
//					classificationTmp.setValue(third, a.getName(this.GWTRaplaLocale.getLocale()));
//					//was muss hier als value übergeben werden??? Mal nachgucken
//				}
//			}

			// getCreateableDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE);

		} else {
			currentView = (ResourceDatesInterface) currentView;
		}

//		logger.log(Level.WARNING, "All Attributes - ");
//		for (Attribute a : classificationTmp.getType().getAttributes()) {
//			logger.log(
//					Level.WARNING,
//					"Allocatable: "
//							+ a.getName(this.GWTRaplaLocale.getLocale())
//							+ "-"
//							+ a.getName()
//							+ "-"
//							+ a.getKey()
//							+ "-"
//							+ classificationTmp.getValueAsString(a,
//									this.GWTRaplaLocale.getLocale()));
//		}
//
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

	public List<String> getEventTypes() throws RaplaException {

		DynamicType[] types;
		List<String> items = new ArrayList<String>();
		types = getFacade().getDynamicTypes(
				DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);
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
		;

		return items;
	}

	public Attribute[] getDynamicFields(String eventKey) throws RaplaException {

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
			String name = eventTypes.get(i).getName(GWTRaplaLocale.getLocale());
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
		// ArrayList<Attributes> attributes = new ArrayList<Attributes>();
		//
		//
		Attribute[] attributes = { new AttributeImpl() };

		for (int i = 0; i < type.getAttributes().length; i++) {
			//
			attributes[i] = type.getAttributes()[i];
			attributes[6] = new AttributeImpl();
			//
			// attributes.add(i, new
			// Attributes(type.getAttributes()[i].toString()));
			//
			// for(int j = 0; j <
			// type.getAttributes()[i].getConstraintKeys().length; j++ ){
			//
			// attributes.get(i).constraintKeys.add((type.getAttributes()[i].getConstraintKeys()[j]));
			// }
			//
			//
		}
		//

		return attributes;
	}

	public String[] getConstraintKeys(Attribute attribute)
			throws RaplaException {

		String[] constraintKeys = null;

		for (int i = 0; i < attribute.getConstraintKeys().length; i++) {
			constraintKeys[i] = attribute.getConstraintKeys()[i];
		}

		return constraintKeys;
	}

	// public class Attributes{
	//
	// ArrayList constraintKeys;
	// String name;
	//
	// public Attributes(String name){
	// this.name = name;
	// }
	//
	// public String getName(){
	// return this.name;
	// }
	//
	// public void setName(String name){
	// this.name = name;
	// }
	//
	//
	//
	// }

	public void getContentOfElements() {
		this.loadDataFromReservation(currentView);
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

	protected void loadDataFromReservation(
			ViewServiceProviderInterface currentView2) {

		Locale locale = this.GWTRaplaLocale.getLocale();
		Classification classificationTmp = reservationTmp.getClassification();

		Allocatable[] allocatables = reservationTmp.getAllocatables();

		Allocatable[] resources = reservationTmp.getAllocatables();
		Appointment[] appointments = reservationTmp.getAppointments();

		// I just want to see all
		// this.logAllElements();

//		logger.log(Level.WARNING, "All Attributes - ");
//		for (Attribute a : classificationTmp.getType().getAttributes()) {
//			logger.log(
//					Level.WARNING,
//					"Allocatable: " + a.getName(locale) + "-" + a.getName()
//							+ "-" + a.getKey() + "-"
//							+ classificationTmp.getValueAsString(a, locale));
//		}

		if (currentView2 instanceof InfoViewInterface) {
			if (reservationName != null) {
				((InfoViewInterface) currentView).getTitelInput().setText(
						reservationName);
			}

			((InfoViewInterface) currentView)
					.setSelectedEventType(classificationTmp.getType().getName(
							locale));

			if (allocatables != null) {
				for (int a = 0; a < allocatables.length; a++) {
					DynamicType tmp = allocatables[a].getClassification()
							.getType();
					String tmp2 = tmp.toString();

					if (tmp2.equals(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)) {
						((InfoViewInterface) currentView)
								.setSelectedEventType(allocatables[a]
										.getName(locale));
					}
					// classificationTmp.setValue("name", "Studiengang");
					// classificationTmp.setValue("course",
					// ((InfoViewInterface) currentView).getStudiengangListBox()
					// .getSelectedItemText());
					// if (tmp.equals("Studiengang")) {
					// tmp2 = (String) allocatables[a].getClassification()
					// .getValue("course");
					// for (int i = 0; i < (((InfoViewInterface) currentView2)
					// .getStudiengangListBox().getItemCount()); i++) {
					// if (tmp.equals(((InfoViewInterface) currentView2)
					// .getStudiengangListBox().getItemText(i))) {
					// ((InfoViewInterface) currentView2)
					// .getStudiengangListBox()
					// .setSelectedIndex(i);
					// }
					// }
					// }
					// // classificationTmp.setValue("name",
					// "Vorlesungsstunden");
					// // classificationTmp.setValue("duration",
					// // ((InfoViewInterface) currentView)
					// // .getVorlesungsStundenInput().getText());
					// if (tmp.equals("Vorlesungsstunden")) {
					// tmp2 = (String) allocatables[a].getClassification()
					// .getValue("duration");
					// ((InfoViewInterface) currentView2)
					// .getVorlesungsStundenInput().setText(tmp2);
					// }
				}
			}

			for (Attribute a : classificationTmp.getAttributes()) {
				String tmp = a.getName(locale);
				String tmp2 = classificationTmp.getValueAsString(a, locale);
				if (tmp.equals("Studiengang")) {
					for (int i = 0; i < (((InfoViewInterface) currentView2)
							.getStudiengangListBox().getItemCount()); i++) {
						if (tmp2.equals(((InfoViewInterface) currentView2)
								.getStudiengangListBox().getItemText(i))) {
							((InfoViewInterface) currentView2)
									.getStudiengangListBox()
									.setSelectedIndex(i);
						}
					}

				}
				if (tmp.equals("Gepl. Vorlesungsstunden")) {
					((InfoViewInterface) currentView2)
							.getVorlesungsStundenInput().setText(tmp2);
				}
			}

		} else {

		}

	}

	private void logAllElements() {
		Classification classificationTmp = reservationTmp.getClassification();
		Allocatable[] resources = reservationTmp.getAllocatables();
		Appointment[] appointments = reservationTmp.getAppointments();

		logger.log(Level.WARNING, "All Allocatables - ");
		try {
			for (Allocatable a : facade.getAllocatables()) {

				logger.log(
						Level.WARNING,
						"Allocatable: "
								+ a.getName(this.GWTRaplaLocale.getLocale()));
			}
		} catch (RaplaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.log(Level.WARNING, "All Attributes - ");
		for (Attribute a : classificationTmp.getType().getAttributes()) {
			logger.log(
					Level.WARNING,
					"Allocatable: "
							+ a.getName(this.GWTRaplaLocale.getLocale()) + "-"
							+ a.getName() + "-" + a.getKey());
		}

		{
			StringBuilder builder = new StringBuilder();
			for (Allocatable res : resources) {
				builder.append(res.getName(this.GWTRaplaLocale.getLocale()));
			}
			logger.log(Level.WARNING, "Ressourcen: " + builder.toString());

		}
		{
			StringBuilder builder = new StringBuilder();
			for (Appointment app : appointments) {
				builder.append(app.toString());
			}
			logger.log(Level.WARNING, "Termine: " + builder.toString());
		}

		logger.log(Level.WARNING, "reservationname: " + reservationName);

	}
}
*/
