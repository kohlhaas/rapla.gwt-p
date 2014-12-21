package org.rapla.client;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.rapla.client.edit.reservation.GWTReservationController;
import org.rapla.client.event.AddEvent;
import org.rapla.client.event.AddEvent.AddEventHandler;
import org.rapla.client.event.DetailSelectEvent;
import org.rapla.client.event.DetailSelectEvent.DetailSelectEventHandler;
import org.rapla.client.event.RaplaEventBus;
import org.rapla.client.plugin.view.ContentDrawer;
import org.rapla.client.plugin.view.ViewController;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent.ViewSelectionChangedHandler;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.facade.CalendarOptions;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.ModificationEvent;
import org.rapla.facade.ModificationListener;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
@Singleton
public class Application implements ViewSelectionChangedHandler,
		DetailSelectEventHandler, AddEventHandler {

    final Logger logger = Logger.getLogger("Application");
    
	FlowPanel drawingContent = new FlowPanel();
	@Inject
	ViewController viewController;
	@Inject
    ClientFacade facade;
	@Inject
    RaplaLocale raplaLocale;
	@Inject
    CalendarOptions calendarOptions;
	@Inject
    GWTReservationController controller;
	
	RootPanel root;
	
	
	public Application() {
		drawingContent.setStyleName("raplaDrawingContent");
		RaplaEventBus.getInstance().addHandler(ViewSelectionChangedEvent.TYPE, this);
		RaplaEventBus.getInstance().addHandler(DetailSelectEvent.TYPE, this);
		RaplaEventBus.getInstance().addHandler(AddEvent.TYPE, this);
	}

	// private final DataInjector injector2 = GWT.create(DataInjector.class);
	public void createApplication() {
	    root = RootPanel.get("raplaRoot");
		root.clear();
		root.add(viewController.createContent());
		viewChanged();
		facade.addModificationListener( new ModificationListener() {
            
            @Override
            public void dataChanged(ModificationEvent evt) throws RaplaException {
                viewChanged();
            }
        });
	}

	@Override
	public void viewChanged() {
	    if (drawingContent != null)
	    {
	        root.remove( drawingContent);
	    }
	    drawingContent = new FlowPanel();
	    ContentDrawer selectedContentDrawer = viewController.getSelectedContentDrawer();
        Widget createContent = selectedContentDrawer.createContent();
        drawingContent.add(createContent);
        root.add(drawingContent);
	}

	@Override
	public void detailsRequested(DetailSelectEvent e) {
	    Object selectedObject = e.getSelectedObject();
	    if ( selectedObject != null)
	    {
	        Reservation event = (Reservation) selectedObject;
	        try {
	            Reservation editableEvent = facade.edit( event);
	            controller.edit( editableEvent, false );
	        } catch (RaplaException e1) {
	            // TODO exception handling
	            logger.log(Level.SEVERE, e1.getMessage(), e1);
	        }       
	    }
	}
	
	class TestHandler 
	{

        public TestHandler() {
            super();
        }

        public void handle()
        {
            try {
                final Reservation event = facade.newReservation();
         
                Date selectedDate =facade.today();
                Date time = new Date (DateTools.MILLISECONDS_PER_MINUTE * calendarOptions.getWorktimeStartMinutes());
                Date startDate = raplaLocale.toDate(selectedDate,time);
                Classification classification = event.getClassification();
                Attribute first = classification.getType().getAttributes()[0];
                classification.setValue(first, "Test");
                
                Date endDate = new Date( startDate.getTime() + DateTools.MILLISECONDS_PER_HOUR);
                Appointment newAppointment = facade.newAppointment( startDate, endDate);
                event.addAppointment( newAppointment);
                Allocatable[] resources = facade.getAllocatables();
                event.addAllocatable( resources[0]);
                controller.edit( event, true );
            } catch (RaplaException e1) {
                // TODO exception handling
                logger.log(Level.SEVERE, e1.getMessage(), e1);
            }
    
        }
	}

	@Override
	public void addRequested(AddEvent e) {
	    TestHandler testHandler = new TestHandler( );
	    testHandler.handle();
	}

}
