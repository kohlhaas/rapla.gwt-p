package org.rapla.client;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.rapla.client.edit.reservation.GWTReservationController;
import org.rapla.client.edit.reservation.impl.SampleReservationController;
import org.rapla.client.event.AddEvent;
import org.rapla.client.event.AddEvent.AddEventHandler;
import org.rapla.client.event.DetailSelectEvent;
import org.rapla.client.event.DetailSelectEvent.DetailSelectEventHandler;
import org.rapla.client.event.RaplaEventBus;
import org.rapla.client.event.AddReservationEvent;
import org.rapla.client.event.AddReservationEvent.AddReservationEventHandler;
import org.rapla.client.internal.RaplaGWTClient;
import org.rapla.client.plugin.view.ViewController;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent.ViewSelectionChangedHandler;
import org.rapla.client.edit.reservation.impl.ReservationController;
import org.rapla.client.factory.ViewServiceProviderInterface;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.ModificationEvent;
import org.rapla.facade.ModificationListener;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

@Singleton
public class Application implements ViewSelectionChangedHandler,
		DetailSelectEventHandler, AddEventHandler, AddReservationEventHandler {

	final Logger logger = Logger.getLogger("Application");

	FlowPanel drawingContent = new FlowPanel();
	PopupPanel popupContent = new PopupPanel();
	@Inject
	ViewController viewController;	
	@Inject
	private RaplaGWTClient service;
	RootPanel root;

	
	GWTReservationController controller;
	@Inject
	ReservationController reservationController;

	public Application() {
		drawingContent.setStyleName("raplaDrawingContent");
		RaplaEventBus.getInstance().addHandler(ViewSelectionChangedEvent.TYPE,
				this);
		RaplaEventBus.getInstance().addHandler(DetailSelectEvent.TYPE, this);
		RaplaEventBus.getInstance().addHandler(AddEvent.TYPE, this);
		RaplaEventBus.getInstance().addHandler(AddReservationEvent.TYPE, this);
	}

	// private final DataInjector injector2 = GWT.create(DataInjector.class);
	public void createApplication() {
		controller = new SampleReservationController(service.getContext());
		reservationController.setContext(service.getContext());
		root = RootPanel.get("raplaRoot");
		root.clear();
		root.add(viewController.createContent());
		viewChanged();
		service.getFacade().addModificationListener(new ModificationListener() {

			@Override
			public void dataChanged(ModificationEvent evt)
					throws RaplaException {
				viewChanged();
			}
		});
	}
	
	

	@Override
	public void viewChanged() {

		// drawingContent.add(infoController.createContent());

		if (drawingContent != null) {
			root.remove(drawingContent);
		}
		drawingContent = new FlowPanel();
		ViewServiceProviderInterface selectedContentDrawer = viewController
				.getSelectedContentDrawer();
		Widget createContent = selectedContentDrawer.createContent();
		drawingContent.add(createContent);
		root.add(drawingContent);

	}

	@Override
	public void detailsRequested(DetailSelectEvent e) {
		Object selectedObject = e.getSelectedObject();
		if (selectedObject != null) {
			Reservation event = (Reservation) selectedObject;
			try {
				ClientFacade facade = service.getFacade();
				Reservation editableEvent = facade.edit(event);
				controller.edit(editableEvent, false);
			} catch (RaplaException e1) {
				// TODO exception handling
				logger.log(Level.SEVERE, e1.getMessage(), e1);
			}
		}
	}

	class TestHandler extends RaplaComponent {

		public TestHandler(RaplaContext context) {
			super(context);
		}

		public void handle() {
			ClientFacade facade = getClientFacade();
			try {
				final Reservation event = facade.newReservation();

				Date selectedDate = facade.today();
				Date time = new Date(DateTools.MILLISECONDS_PER_MINUTE
						* getCalendarOptions().getWorktimeStartMinutes());
				Date startDate = getRaplaLocale().toDate(selectedDate, time);
				Classification classification = event.getClassification();
				Attribute first = classification.getType().getAttributes()[0];
				classification.setValue(first, "Test");

				Date endDate = new Date(startDate.getTime()
						+ DateTools.MILLISECONDS_PER_HOUR);
				Appointment newAppointment = facade.newAppointment(startDate,
						endDate);
				event.addAppointment(newAppointment);
				Allocatable[] resources = facade.getAllocatables();
				event.addAllocatable(resources[0]);
				controller.edit(event, true);
			} catch (RaplaException e1) {
				// TODO exception handling
				logger.log(Level.SEVERE, e1.getMessage(), e1);
			}

		}
	}

	class AddReservationHandler extends RaplaComponent {

		public AddReservationHandler(RaplaContext context) {
			super(context);
		}

		public void handle() {
			// need to be able to dynamic repositioning if the size of the
			// browserwindow changes (maybe
			// popupContent.setPopupPositionAndShow())
//			popupContent = new PopupPanel();
//			popupContent.setGlassEnabled(true);
//			popupContent.setAnimationEnabled(true);
//			popupContent.setAnimationType(PopupPanel.AnimationType.ROLL_DOWN);
//			Integer height = (int) (Window.getClientHeight() * 0.90);
//			Integer width = (int) (Window.getClientWidth() * 0.90);
//			popupContent.setHeight(height.toString() + "px");
//			popupContent.setWidth(width.toString() + "px");
//
//			Widget createContent = reservationController.createContent();
//			root.add(popupContent);
//			popupContent.add(createContent);
//			popupContent.center();
			ClientFacade facade = getClientFacade();
			try {
				final Reservation event = facade.newReservation();

				Date selectedDate = facade.today();
				Date time = new Date(DateTools.MILLISECONDS_PER_MINUTE
						* getCalendarOptions().getWorktimeStartMinutes());
				Date startDate = getRaplaLocale().toDate(selectedDate, time);
				Classification classification = event.getClassification();
				Attribute first = classification.getType().getAttributes()[0];
				classification.setValue(first, "Test");

				Date endDate = new Date(startDate.getTime()
						+ DateTools.MILLISECONDS_PER_HOUR);
				Appointment newAppointment = facade.newAppointment(startDate,
						endDate);
				event.addAppointment(newAppointment);
				Allocatable[] resources = facade.getAllocatables();
				event.addAllocatable(resources[0]);
				reservationController.edit(event, true);
			} catch (RaplaException e1) {
				// TODO exception handling
				logger.log(Level.SEVERE, e1.getMessage(), e1);
			}
			
		

			reservationController.setFacade(getClientFacade());
			PopupPanel createContent = reservationController.createContent();
			root.add(createContent);
			createContent.center();
		}

	}

	@Override
	public void addRequested(AddEvent e) {
		TestHandler testHandler = new TestHandler(service.getContext());
		testHandler.handle();
	}

	public void addRequested(AddReservationEvent e) {
		AddReservationHandler vcHandler = new AddReservationHandler(
				service.getContext());
		vcHandler.handle();
	}

}
