package org.rapla.client.edit.reservation.sample;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.User;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.PermissionContainer;
import org.rapla.entities.domain.Repeating;
import org.rapla.entities.domain.RepeatingType;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.entities.dynamictype.ClassificationFilter;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.DynamicTypeAnnotations;
import org.rapla.entities.dynamictype.internal.AttributeImpl;
import org.rapla.entities.dynamictype.internal.DynamicTypeImpl;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;
import org.rapla.client.edit.reservation.sample.InfoView;
import org.rapla.client.edit.reservation.sample.gwt.RaplaDate;

import de.vksi.c4j.ContractReference;

@ContractReference(org.rapla.client.edit.reservation.ReservationContract.class)
public class ReservationPresenter implements ReservationController, Presenter {

	@Inject
	Logger logger;
	@Inject
	RaplaLocale raplaLocale;
	@Inject
	ClientFacade facade;

	private ReservationView view;
	private InfoViewPresenter infoViewPresenter;

	private Reservation tempReservation;
	private ResourceDatesViewPresenter resourceDatesPresenter;

	@Inject
	public ReservationPresenter(ReservationView view,
			InfoViewPresenter infoViewPresenter,
			ResourceDatesViewPresenter resourceDatesPresenter) {
		this.view = view;
		view.setPresenter(this);
		this.infoViewPresenter = infoViewPresenter;
		this.resourceDatesPresenter = resourceDatesPresenter;

		// ((InfoView) infoViewPresenter.getView()).createContent();
		// ((ResourceDatesView)
		// resourceDatesPresenter.getView()).createContent();

		view.addSubView(infoViewPresenter.getView());
		view.addSubView(resourceDatesPresenter.getView());

		// InfoView test2 = (InfoView) infoViewPresenter.getView();
		// test2.hide();

	}

	Reservation event;
	boolean isNew;

	@Override
	public void edit(final Reservation event, boolean isNew) {
		tempReservation = event;
		this.isNew = isNew;
		infoViewPresenter.setReservation(event);
		// resourceViewPresenter.setReservation(event);

		((InfoView) infoViewPresenter.getView()).createContent();
		((ResourceDatesView) resourceDatesPresenter.getView()).createContent();

		view.show(event);
	}

	@Override
	public void onSaveButtonClicked() {
		logger.info("save clicked");
		try {

			saveTemporaryChanges();
			// Classification classification =
			// tempReservation.getClassification();
			// Attribute first = classification.getType().getAttributes()[0];
			// String text = view.getTitelInput();
			// classification.setValue(first, text);
			facade.store(tempReservation);
		} catch (RaplaException e1) {
			logger.error(e1.getMessage(), e1);
		}
		view.hide();
		view.setCurrentSubView(null);
	}

	public void saveTemporaryChanges() throws RaplaException {
		if (view.getCurrentSubView() != null)
			saveTemporaryChanges(view.getCurrentSubView());
	}

	private void saveTemporaryChanges(ReservationEditSubView currentView)
			throws RaplaException {

		logger.info("Save temporary changes");
		Classification classificationTmp = tempReservation.getClassification();
		String reservationName;
		Locale locale = raplaLocale.getLocale();
		logger.info("...");
		if (currentView instanceof InfoView) {

			logger.info("1...");
			reservationName = ((InfoView) currentView).getTitelInput();

			// Set new Titel/Name for reservationTmp
			Attribute first = classificationTmp.getType().getAttributes()[0];
			classificationTmp.setValue(first, reservationName);
			logger.info("2...");
			// // DynamicType[] types;
			// types = ReservationController.this
			// .getFacade()
			// .getDynamicTypes(
			// DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);

			// String text = reservationName;
			// List<DynamicType> eventTypes = new ArrayList<DynamicType>();

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

			// // maybe useful for another case
			// for (Allocatable a : facade.getAllocatables()) {
			// if (a.getName(GWTRaplaLocale.getLocale()).equals(
			// ((InfoViewInterface) currentView)
			// .getSelectedEventType())) {
			// reservationTmp.addAllocatable((Allocatable) a);
			// }
			// }

			logger.info("3...");
			// change reservation type
			for (DynamicType type : facade
					.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)) {
				if (!(type.getName(locale).equals(classificationTmp.getType()))
						&& type.getName(locale)
								.equals(((InfoView) currentView)
										.getSelectedEventType())) {
					// Reservation event = facade.newReservation(type
					// // .newClassification());
					// reservationTmp = facade.edit(reservationTmp);

					// Allocatable[] allocatables = reservationTmp
					// .getAllocatables();
					// Appointment[] appointments = reservationTmp
					// .getAppointments();
					// Collection<Permission> permissions = reservationTmp
					// .getPermissionList();
					HashMap<Attribute, Object> attributes = new HashMap<Attribute, Object>();
					for (Attribute a : classificationTmp.getAttributes()) {
						attributes.put(a, classificationTmp.getValue(a));
					}
					//
					// // facade.remove(reservationTmp);
					// Classification classificationNew = event
					// .getClassification();
					tempReservation.setClassification(type.newClassification());

					classificationTmp = tempReservation.getClassification();
					//
					// for (Allocatable allocatable : allocatables) {
					// event.addAllocatable(allocatable.clone());
					// }
					// for (Appointment a : appointments) {
					// event.addAppointment(a.clone());
					// }
					// for (Permission a : permissions) {
					// event.addPermission(a.clone());
					// }
					for (Attribute a : classificationTmp.getAttributes()) {
						classificationTmp.setValue(a, attributes.get(a));
					}
					// reservationTmp = event;
					// classificationTmp = classificationNew;
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

			logger.info("4...");
			// set Vorlesungstunden
			if (tempReservation
					.getClassification()
					.getType()
					.equals(facade
							.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)[0])) {
				Attribute second = classificationTmp.getType().getAttributes()[5];
				classificationTmp.setValue(second, Integer
						.parseInt(((InfoView) currentView)
								.getVorlesungsStundenInput()));
			}
			logger.info("5...");
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

			// // set Studiengang
			// Attribute third = classificationTmp.getType().getAttributes()[2];
			// Allocatable[] resources = facade.getAllocatables();
			// for (Allocatable a : resources) {
			// if (a.getName(this.GWTRaplaLocale.getLocale()).equals(
			// ((InfoViewInterface) currentView)
			// .getStudiengangListBox().getSelectedItemText())) {
			// a.get ?
			// classificationTmp.setValue(third,
			// a.getName(this.GWTRaplaLocale.getLocale()));
			// //was muss hier als value übergeben werden??? Mal nachgucken
			// }
			// }

			// getCreateableDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE);

		} else {
			// Date endDate = null;
			// Date startDate = null;
			// currentView = (ResourceDatesView) currentView;
			logger.warn("...Save dates..");
			Appointment[] deleteAppointments = tempReservation
					.getAppointments();
			for (Appointment deleteAppointment : deleteAppointments) {
				tempReservation.removeAppointment(deleteAppointment);
			}
			Allocatable[] deleteAllocatables = tempReservation
					.getAllocatables();
			for (Allocatable deleteAllocatable : deleteAllocatables) {
				tempReservation.removeAllocatable(deleteAllocatable);
			}

			Appointment appointment = null;

			logger.warn("Save dates..");

			List<RaplaDate> dateList = ((ResourceDatesView) currentView)
					.getDates();

			Date startTime;
			Date endTime;

			for (RaplaDate raplaDate : dateList) {
				ArrayList<List<String>> resources;

				if (raplaDate.getRaplaDateList().size() > 0) {
					startTime = raplaDate.getRaplaDateList().get(0)
							.getBeginTime();
					endTime = raplaDate.getRaplaDateList().get(0).getEndTime();
					Date recurringEndTime = raplaDate.getRaplaDateList()
							.get(raplaDate.getRaplaDateList().size() - 1)
							.getEndTime();

					appointment = facade.newAppointment(startTime, endTime);

					appointment.setRepeatingEnabled(true);
					Repeating repeating = appointment.getRepeating();
					repeating.setEnd(recurringEndTime);
					raplaDate.getRaplaDateList();

					RepeatingType repeatingType;
					switch (raplaDate.getReccuringType()) {
					case 1:
						repeatingType = Repeating.DAILY;
						break;
					case 2:
						repeatingType = Repeating.WEEKLY;
						break;
					case 3:
						repeatingType = Repeating.MONTHLY;
						break;
					case 4:
						repeatingType = Repeating.YEARLY;
						break;
					default:
						repeatingType = Repeating.DAILY;
						break;
					}
					repeating.setType(repeatingType);

					resources = raplaDate.getRaplaDateList().get(0)
							.getResources();
				} else {
					startTime = raplaDate.getBeginTime();
					endTime = raplaDate.getEndTime();
					appointment = facade.newAppointment(startTime, endTime);
					resources = raplaDate.getResources();
				}

				// Allocatable allocatable = null;

				List<Allocatable> tempAllocatables = new ArrayList<Allocatable>();

				for (List<String> resouceList : resources) {

					logger.warn("1..");
					ClassificationFilter[] filters = new ClassificationFilter[1];

					if (resouceList.get(0).toString()
							.equalsIgnoreCase("Professoren")) {
						logger.warn("1a..");
						filters[0] = getProfFilter();
					} else if (resouceList.get(0).toString()
							.equalsIgnoreCase("Kurse")) {
						logger.warn("1b..");
						filters[0] = getCourseFilter();
					} else {

						logger.warn("1c..");
						filters[0] = getRoomFilter();
					}

					logger.warn("2..");
					Allocatable[] allocatables = facade
							.getAllocatables(filters);

					logger.warn("2a..");
					logger.warn("2a.. resouceList.size(): "
							+ resouceList.size());
					// if (resouceList.size() > 1) {
					for (int i = 1; i < resouceList.size(); i++) { // TODO
																	// maybe
																	// change

						logger.warn("2b..");
						for (Allocatable tempAllocatable : allocatables) {

							logger.warn("2c..");
							if (resouceList
									.get(i)
									.toString()
									.equalsIgnoreCase(
											tempAllocatable
													.getName(this.raplaLocale
															.getLocale()))) {

								logger.warn("2d..");
								tempAllocatables.add(tempAllocatable);
							}
						}

					}
					// }

				}

				logger.warn("3..");
				Allocatable[] tmpAllocatables = new Allocatable[tempAllocatables
						.size()];
				Allocatable[] restrictedAllocatables = (Allocatable[]) tempAllocatables
						.toArray(tmpAllocatables);
				logger.warn("3a..");
				tempReservation.addAppointment(appointment);
				logger.warn("3b..");
				for (Allocatable allocatable : restrictedAllocatables) {
					tempReservation.addAllocatable(allocatable);
				}
				tempReservation.setRestriction(appointment,
						restrictedAllocatables);
				logger.warn("fin..");

			}

			// tempReservation.setRestriction(appointment,
			// restrictedAllocatables);
			// tempReservation.addAppointment(appointment);
		}

		// logger.log(Level.WARNING, "All Attributes - ");
		// for (Attribute a : classificationTmp.getType().getAttributes()) {
		// logger.log(
		// Level.WARNING,
		// "Allocatable: "
		// + a.getName(this.GWTRaplaLocale.getLocale())
		// + "-"
		// + a.getName()
		// + "-"
		// + a.getKey()
		// + "-"
		// + classificationTmp.getValueAsString(a,
		// this.GWTRaplaLocale.getLocale()));
		// }
		//
	}

	@Override
	public void onDeleteButtonClicked() {
		logger.info("delete clicked");
		try {
			facade.remove(tempReservation);
		} catch (RaplaException e1) {
			logger.error(e1.getMessage(), e1);
		}
		view.hide();
		view.setCurrentSubView(null);
	}

	@Override
	public void onCancelButtonClicked() {
		logger.info("cancel clicked");
		view.hide();
		view.setCurrentSubView(null);
	}

	@Override
	public void changeEventName(String newName) {
		logger.info("Name changed to " + newName);
		Classification classification = event.getClassification();
		Attribute first = classification.getType().getAttributes()[0];
		classification.setValue(first, newName);
	}

	@Override
	public boolean isDeleteButtonEnabled() {
		return !isNew;
	}

	@Override
	public void onTabChanged(int selectedTab) throws RaplaException,
			ParseException {
		logger.info("Changed Tab");
		try {
			this.saveTemporaryChanges();
		} catch (RaplaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Saved temporary changes");
		ReservationEditSubView tempView = infoViewPresenter.getView();

		if (selectedTab == 0) {
			tempView = infoViewPresenter.getView();
		} else if (selectedTab == 1) {
			tempView = resourceDatesPresenter.getView();
		} else {

		}
		logger.info("update view");
		logger.info("tempView = " + selectedTab);
		view.update(tempView);
		logger.info("updated view");

		if (tempView instanceof InfoView) {
			this.configure((InfoView) tempView);
		} else if (tempView instanceof ResourceDatesView) {
			this.configure(((ResourceDatesView) tempView));
		} else {

		}

		// this.loadPersonsIntoView(); not ready yet
		this.loadDataFromReservationToView();

	}

	private void configure(InfoView view) throws RaplaException {
		view.setEventTypes(this.getEventTypes());

	}

	private void configure(ResourceDatesView view) throws RaplaException {
		// view.setChosenResouces();
		view.clear();
		view.setResourcesPerson(this.getPersonData());
		view.setResourcesCourse(this.getCourseData());
		view.setResourcesRoom(this.getRoomData());
		view.createResourceTree();

	}

	private List<String> getCourseData() throws RaplaException {
		List<String> course = new ArrayList<String>();
		ClassificationFilter[] filters = new ClassificationFilter[1];

		filters[0] = getCourseFilter();
		Allocatable[] allocatables = facade.getAllocatables(filters);
		// logger.warn("Load course data into resourcesDates");
		for (Allocatable allocatable : allocatables) {
			// logger.warn("Course: " +
			// allocatable.getName(raplaLocale.getLocale()));
			course.add(allocatable.getName(raplaLocale.getLocale()));
		}

		return course;
	}

	private ClassificationFilter getCourseFilter() throws RaplaException {
		DynamicType[] types = facade
				.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE);

		// logger.warn("Load data into resourcesDates");
		ClassificationFilter filter = null;
		for (DynamicType type : types) {
			// logger.warn(type.toString());

			if (type.getName(this.raplaLocale.getLocale()).toString()
					.equalsIgnoreCase("kurs")) {
				filter = type.newClassificationFilter();
			}
		}
		return filter;
	}

	private List<String> getPersonData() throws RaplaException {
		List<String> persons = new ArrayList<String>();
		ClassificationFilter[] filters = new ClassificationFilter[1];
		filters[0] = getProfFilter();
		Allocatable[] allocatables = facade.getAllocatables(filters);
		// logger.warn("Load person data into resourcesDates");
		for (Allocatable allocatable : allocatables) {
			// logger.warn("Person: " +
			// allocatable.getName(raplaLocale.getLocale()));
			persons.add(allocatable.getName(raplaLocale.getLocale()));
		}

		return persons;
	}

	private ClassificationFilter getProfFilter() throws RaplaException {
		DynamicType[] types = facade
				.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_PERSON);

		// logger.warn("Load data into resourcesDates");
		ClassificationFilter filter = null;
		for (DynamicType type : types) {
			// logger.warn(type.toString());

			if (type.getKey().toString().equalsIgnoreCase("professor")) {
				// logger.warn(type.getName().toString());
				filter = type.newClassificationFilter();
			}
			// if(type.getKey().toString().equalsIgnoreCase("honorarkraft")){
			// logger.warn(type.getName().toString());
			// filters[1] = type.newClassificationFilter();
			// }
		}

		return filter;
	}

	private List<String> getRoomData() throws RaplaException {
		List<String> rooms = new ArrayList<String>();
		ClassificationFilter[] filters = new ClassificationFilter[1];
		filters[0] = getRoomFilter();
		Allocatable[] allocatables = facade.getAllocatables(filters);
		// logger.warn("Load room data into resourcesDates");
		for (Allocatable allocatable : allocatables) {
			// logger.warn("Room: " +
			// allocatable.getName(raplaLocale.getLocale()));
			rooms.add(allocatable.getName(raplaLocale.getLocale()));
		}

		return rooms;
	}

	private ClassificationFilter getRoomFilter() throws RaplaException {
		DynamicType[] types = facade
				.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE);
		// logger.warn("Load data into resourcesDates");
		ClassificationFilter filter = null;
		for (DynamicType type : types) {
			// logger.warn(type.toString());

			if (type.getName(this.raplaLocale.getLocale()).toString()
					.equalsIgnoreCase("raum")) {
				filter = type.newClassificationFilter();
			}
		}

		return filter;
	}

	public List<DynamicType> getCreateableDynamicTypes(String classificationType)
			throws RaplaException {

		DynamicType[] types;
		List<String> items = new ArrayList<String>();
		types = facade.getDynamicTypes(classificationType);
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = facade.getUser();
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
		types = facade.getDynamicTypes(classificationType);
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = facade.getUser();
		for (DynamicType type : types) {
			if (PermissionContainer.Util.canCreate(type, user))
				eventTypes.add(type);
		}

		for (int i = 0; i < eventTypes.size(); i++) {
			String name = eventTypes.get(i).getName(raplaLocale.getLocale());
			items.add(name);
		}

		return items;
	}

	public List<String> getEventTypes() throws RaplaException {

		DynamicType[] types;
		List<String> items = new ArrayList<String>();
		types = facade
				.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = facade.getUser();
		for (DynamicType type : types) {
			if (PermissionContainer.Util.canCreate(type, user))
				eventTypes.add(type);
		}

		for (int i = 0; i < eventTypes.size(); i++) {
			String name = eventTypes.get(i).getName(raplaLocale.getLocale());
			items.add(name);
		}

		return items;
	}

	public Attribute[] getDynamicFields(String eventKey) throws RaplaException {

		AttributeImpl attr = new AttributeImpl();

		DynamicTypeImpl type = new DynamicTypeImpl();

		DynamicType[] types;
		types = facade
				.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);

		List<String> items = new ArrayList<String>();
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = facade.getUser();

		String key = eventKey;

		for (DynamicType typen : types) {
			if (PermissionContainer.Util.canCreate(typen, user))
				eventTypes.add(typen);
		}

		for (int i = 0; i < eventTypes.size(); i++) {
			String name = eventTypes.get(i).getName(raplaLocale.getLocale());
			items.add(name);
			// if (eventTypes
			// .get(i)
			// .getName(raplaLocale.getLocale())
			// .equalsIgnoreCase(view.getSelectedEventType())) {
			// key = eventTypes.get(i).getKey();
			// }
		}
		//
		// type.setKey(key);
		Attribute[] attributes = { new AttributeImpl() };

		for (int i = 0; i < type.getAttributes().length; i++) {

			attributes[i] = type.getAttributes()[i];
			attributes[6] = new AttributeImpl();
		}

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

	private void loadPersonsIntoView() {
		loadPersonsIntoView(view.getCurrentSubView());
	}

	public void loadPersonsIntoView(ReservationEditSubView view) {

		if (view instanceof InfoView) {
			List<String> personsString;
			try {
				personsString = this.getPersonTypesNames();

				// for (Allocatable person : persons) {
				// personsString.add(person.getName(this.raplaLocale.getLocale()));
				// }
				((InfoView) this.infoViewPresenter.getView())
						.setContentOfListBoxStudiengang(personsString);
			} catch (RaplaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

		}
	}

	protected void loadDataFromReservationToView() throws ParseException,
			RaplaException {
		if (view.getCurrentSubView() != null)
			loadDataFromReservationToView(view.getCurrentSubView());
	}

	private void loadDataFromReservationToView(
			ReservationEditSubView currentView) throws ParseException,
			RaplaException {
		logger.info("Load data from reservation");
		Locale locale = this.raplaLocale.getLocale();
		Classification classificationTmp = tempReservation.getClassification();

		Allocatable[] allocatables = tempReservation.getAllocatables();

		Allocatable[] resources = tempReservation.getAllocatables();
		// Appointment[] appointments = tempReservation.getAppointments();

		Allocatable[] persons = tempReservation.getPersons();

		Attribute[] attributes = classificationTmp.getAttributes();

		// I just want to see all
		// this.logAllElements();

		logger.warn("All Attributes - ");
		for (Attribute a : classificationTmp.getType().getAttributes()) {
			logger.warn("Allocatable: " + a.getName(locale) + "-" + a.getName()
					+ "-" + a.getKey() + "-"
					+ classificationTmp.getValueAsString(a, locale));
		}

		if (currentView instanceof InfoView) {
			Attribute tmpAttribute = classificationTmp.getAttribute("title");
			String reservationName = (String) classificationTmp.getValue(
					tmpAttribute).toString();

			if (reservationName != null) {
				((InfoView) currentView).setTitelInput(reservationName);
			}

			((InfoView) currentView).setSelectedEventType(classificationTmp
					.getType().getName(locale));

			if (allocatables != null) {
				for (int a = 0; a < allocatables.length; a++) {
					DynamicType tmp = allocatables[a].getClassification()
							.getType();
					String tmp2 = tmp.toString();

					if (tmp2.equals(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)) {
						((InfoView) currentView)
								.setSelectedEventType(allocatables[a]
										.getName(locale));
					}

				}
			}

			for (Attribute a : attributes) {
				String tmp = a.getName(locale);
				String tmp2 = classificationTmp.getValueAsString(a, locale);
				if (tmp.equals("Studiengang")) {
					for (int i = 0; i < (((InfoView) currentView)
							.getItemCountOfListBoxStudiengang()); i++) {
						if (tmp2.equals(((InfoView) currentView)
								.getItemTextOfListBoxStudiengang(i))) {
							((InfoView) currentView)
									.setSelectedIndexOfListBoxStudiengang(i);
						}
					}

				}
				if (tmp.equals("Gepl. Vorlesungsstunden")) {
					((InfoView) currentView).setVorlesungsStundenInput(tmp2);
				}
			}

		} else {

			List<RaplaDate> dateList = new ArrayList<RaplaDate>();

			Appointment[] appointments = tempReservation.getAppointments();
			Allocatable[] tmpAllocatables = null;
			RaplaDate raplaDate;
			if (tempReservation.getAppointments().length > 0) {
				// tmpAllocatables = tempReservation
				// .getAllocatablesFor(appointments[0]);
				//
				// ArrayList<List<String>> res = new ArrayList<List<String>>();
				//
				// List<String> profList = new ArrayList<String>();
				// profList.add("Professoren");
				// res.add(profList);
				// List<String> courseList = new ArrayList<String>();
				// courseList.add("Kurse");
				// res.add(courseList);
				// List<String> roomList = new ArrayList<String>();
				// roomList.add("Raeume");
				// res.add(roomList);
				//
				// logger.warn("1..");
				// for (Allocatable tmpAllocatable : tmpAllocatables) {
				// if (this.getProfFilter().matches(
				// tmpAllocatable.getClassification())) {
				// profList.add(tmpAllocatable.getName(this.raplaLocale
				// .getLocale()));
				// } else if (this.getCourseFilter().matches(
				// tmpAllocatable.getClassification())) {
				// courseList.add(tmpAllocatable.getName(this.raplaLocale
				// .getLocale()));
				// } else if (this.getRoomFilter().matches(
				// tmpAllocatable.getClassification())) {
				// roomList.add(tmpAllocatable.getName(this.raplaLocale
				// .getLocale()));
				// }
				// }

				logger.warn("save dates..");
				for (Appointment appointment : appointments) {
					tmpAllocatables = tempReservation
							.getAllocatablesFor(appointment);

					ArrayList<List<String>> res = new ArrayList<List<String>>();

					List<String> profList = new ArrayList<String>();
					profList.add("Professoren");
					res.add(profList);
					List<String> courseList = new ArrayList<String>();
					courseList.add("Kurse");
					res.add(courseList);
					List<String> roomList = new ArrayList<String>();
					roomList.add("Raeume");
					res.add(roomList);

					logger.warn("1..");
					for (Allocatable tmpAllocatable : tmpAllocatables) {
						if (this.getProfFilter().matches(
								tmpAllocatable.getClassification())) {
							profList.add(tmpAllocatable
									.getName(this.raplaLocale.getLocale()));
						} else if (this.getCourseFilter().matches(
								tmpAllocatable.getClassification())) {
							courseList.add(tmpAllocatable
									.getName(this.raplaLocale.getLocale()));
						} else if (this.getRoomFilter().matches(
								tmpAllocatable.getClassification())) {
							roomList.add(tmpAllocatable
									.getName(this.raplaLocale.getLocale()));
						}
					}

					logger.warn("2..");
					boolean calculateLectureHours = true;

					if (appointment.isRepeatingEnabled() == true) {

						int reccuringType;

						if (appointment.getRepeating().getType() == Repeating.DAILY) {
							reccuringType = 1;
						} else if (appointment.getRepeating().getType() == Repeating.WEEKLY) {
							reccuringType = 2;
						} else if (appointment.getRepeating().getType() == Repeating.MONTHLY) {
							reccuringType = 3;
						} else if (appointment.getRepeating().getType() == Repeating.YEARLY) {
							reccuringType = 4;
						} else {
							reccuringType = 1;

						}
						// raplaDate.setReccuringType(reccuringType);

						List<RaplaDate> repeatingRaplaDates = RaplaDate
								.recurringDates(appointment.getStart(),
										appointment.getRepeating().getEnd(), 0,
										appointment.getEnd().getTime()
												- appointment.getStart()
														.getTime(), res,
										reccuringType);
						repeatingRaplaDates.add(new RaplaDate(appointment
								.getStart(), appointment.getEnd(), res,
								calculateLectureHours));

						raplaDate = new RaplaDate(repeatingRaplaDates,
								reccuringType);
					} else {
						raplaDate = new RaplaDate(appointment.getStart(),
								appointment.getEnd(), res,
								calculateLectureHours);
					}

					dateList.add(raplaDate);
				}
			}

			// Allocatable[] allocatables = tempReservation.getAllocatables()
			logger.warn("3a..");
			logger.warn("Dates list: " + dateList.size());
			logger.warn("3b..");
			((ResourceDatesView) currentView).setDates(dateList);
			logger.warn("loaded date & resource data..");
		}

	}

	private void logAllElements() {
		Classification classificationTmp = tempReservation.getClassification();
		Allocatable[] resources = tempReservation.getAllocatables();
		Appointment[] appointments = tempReservation.getAppointments();

		logger.warn("All Allocatables - ");
		try {
			for (Allocatable a : facade.getAllocatables()) {

				logger.warn("Allocatable: "
						+ a.getName(raplaLocale.getLocale()));
			}
		} catch (RaplaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.warn("All Attributes - ");
		for (Attribute a : classificationTmp.getType().getAttributes()) {
			logger.warn("Allocatable: " + a.getName(raplaLocale.getLocale())
					+ "-" + a.getName() + "-" + a.getKey());
		}

		{
			StringBuilder builder = new StringBuilder();
			for (Allocatable res : resources) {
				builder.append(res.getName(raplaLocale.getLocale()));
			}
			logger.warn("Ressourcen: " + builder.toString());

		}
		{
			StringBuilder builder = new StringBuilder();
			for (Appointment app : appointments) {
				builder.append(app.toString());
			}
			logger.warn("Termine: " + builder.toString());
		}

		logger.warn("reservationname: "
				+ ((InfoView) infoViewPresenter.getView()).getTitelInput());

	}
	
	public Reservation getTempReservation(){
		return tempReservation;
	}

}
