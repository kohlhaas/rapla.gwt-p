package org.rapla.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.event.DetailSelectEvent;
import org.rapla.client.event.DetailSelectEvent.DetailSelectEventHandler;
import org.rapla.client.event.RaplaEventBus;
import org.rapla.client.plugin.view.ViewPlugin;
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
import org.rapla.framework.logger.Logger;

@Singleton
public class Application implements DetailSelectEventHandler, MainView.Presenter {
    
    @Inject Logger logger;
    
	@Inject ClientFacade facade;
	@Inject RaplaLocale raplaLocale;
	@Inject CalendarOptions calendarOptions;
	@Inject Provider<ReservationController> controller;
	RaplaEventBus eventBus;
	MainView mainView;
	
	private List<ViewPlugin> views;
	ViewPlugin selectedView;

    @Inject
    public void setViews(Set<ViewPlugin> views)
    {
        this.views = new ArrayList<ViewPlugin>(views);
        if ( views.size() > 0)
        {
            selectedView = this.views.get(0);
        }
        
    }
	
    @Inject public Application(MainView mainView, RaplaEventBus eventBus) {
        this.mainView = mainView;
        this.eventBus = eventBus;
		eventBus.addHandler(DetailSelectEvent.TYPE, this);
		mainView.setPresenter( this);
	}
	
	@Override
	public void setSelectedViewIndex(int index) {
	    if ( index >=0)
	    {
	        selectedView = views.get( index);
	        viewChanged();
	    }
	    
	}

	// private final DataInjector injector2 = GWT.create(DataInjector.class);
	public void createApplication() {
	    List<String> names = new ArrayList<String>();
	    for ( ViewPlugin plugin:views)
	    {
	        names.add( plugin.getName());
	    }
	    mainView.show( names );
		viewChanged();
		facade.addModificationListener( new ModificationListener() {
            
            @Override
            public void dataChanged(ModificationEvent evt) throws RaplaException {
                viewChanged();
            }
        });
	}

	private void viewChanged() {
	    mainView.replaceContent( selectedView);
	}

	@Override
	public void detailsRequested(DetailSelectEvent e) {
	    Object selectedObject = e.getSelectedObject();
	    logger.info("Editing Object");
	    if ( selectedObject != null)
	    {
	        Reservation event = (Reservation) selectedObject;
	        try {
	            Reservation editableEvent = facade.edit( event);
	            ReservationController reservationController = controller.get();
                reservationController.edit( editableEvent, false );
	        } catch (RaplaException e1) {
	            // TODO exception handling
	            logger.error( e1.getMessage(), e1);
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
                ReservationController reservationController = controller.get();
                reservationController.edit( event, true );
            } catch (RaplaException e1) {
                // TODO exception handling
                logger.error( e1.getMessage(), e1);
            }
    
        }
	}

	@Override
	public void addClicked() {
	    logger.info( "Add clicked");
	    new TestHandler().handle();
	}

}
