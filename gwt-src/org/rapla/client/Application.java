package org.rapla.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.rapla.client.base.CalendarPlugin;
import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.event.DetailSelectEvent;
import org.rapla.components.util.DateTools;
import org.rapla.entities.Entity;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.facade.CalendarOptions;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.ModificationEvent;
import org.rapla.facade.ModificationListener;
import org.rapla.facade.internal.FacadeImpl;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;
import org.rapla.rest.gwtjsonrpc.common.AsyncCallback;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;
import org.rapla.rest.gwtjsonrpc.common.VoidResult;

import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class Application implements ApplicationView.Presenter {
    
    @Inject Logger logger;
    
	@Inject ClientFacade facade;
	@Inject RaplaLocale raplaLocale;
	@Inject CalendarOptions calendarOptions;
	@Inject Provider<ReservationController> controller;
	@Inject Provider<ActivityManager> activityManager;
	EventBus eventBus;
	ApplicationView mainView;
	
	private List<CalendarPlugin> viewPluginPresenter;
	CalendarPlugin selectedView;

    @Inject
    public void setViews(Set<CalendarPlugin> views)
    {
        this.viewPluginPresenter = new ArrayList<CalendarPlugin>(views);
        if ( views.size() > 0)
        {
            selectedView = this.viewPluginPresenter.get(0);
        }
    }
	
    @Inject public Application(ApplicationView mainView, EventBus eventBus) {
        this.mainView = mainView;
        this.eventBus = eventBus;
		mainView.setPresenter( this);
	}
	
	@Override
	public void setSelectedViewIndex(int index) {
	    if ( index >=0)
	    {
	        selectedView = viewPluginPresenter.get( index);
	        viewChanged();
	    }
	}

	public void createApplication() {
        FacadeImpl facadeImpl = (FacadeImpl) facade;
        facadeImpl.setCachingEnabled( false );
        FutureResult<VoidResult> load = facadeImpl.load();
        logger.info("Loading resources");
        load.get( new AsyncCallback<VoidResult>() {
            
            @Override
            public void onSuccess(VoidResult result) {
               try {
            	   activityManager.get().init();
                   Collection<Allocatable> allocatables = Arrays.asList(facade.getAllocatables());
                   logger.info("loaded " + allocatables.size() + " resources. Starting application");
                   start();
               } catch (RaplaException e) {
                   onFailure(e);
               }
            }
            
            @Override
            public void onFailure(Throwable e) {
                logger.error(e.getMessage(), e);
                
            }
        });
	}

    private void start()  {
        // Test for the resources
        List<String> names = new ArrayList<String>();
	    for ( CalendarPlugin plugin:viewPluginPresenter)
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
	    mainView.replaceContent( selectedView );
	    selectedView.updateContent();
	}

	public void detailsRequested(DetailSelectEvent e) {
		final Entity<?> selectedObject = e.getSelectedObject();
	    if ( selectedObject == null || !(selectedObject instanceof Reservation))
	    {
	    	logger.error("Should not happen");
	    	return;
	    }
	    logger.info("Editing Object: "+ selectedObject.getId());
    	// edit an existing reservation
    	try {
    		final Reservation event = (Reservation) selectedObject;
    		final boolean readOnly = event.isReadOnly();
    		Reservation editableEvent = event.isReadOnly() ? facade.edit(event) : event;
    		ReservationController reservationController = controller.get();
    		reservationController.edit( editableEvent, readOnly );
    	} catch (RaplaException e1) {
    		logger.error( e1.getMessage(), e1);
    	}       
	}
	
	@Override
	public void addClicked() {
	    logger.info( "Add clicked");
        try {
            Reservation newEvent = facade.newReservation();
            final Date selectedDate =facade.today();
            final Date time = new Date (DateTools.MILLISECONDS_PER_MINUTE * calendarOptions.getWorktimeStartMinutes());
            final Date startDate = raplaLocale.toDate(selectedDate,time);
            final Classification classification = newEvent.getClassification();
            final Attribute first = classification.getType().getAttributes()[0];
            classification.setValue(first, "Test");
            
            final Date endDate = new Date( startDate.getTime() + DateTools.MILLISECONDS_PER_HOUR);
            final Appointment newAppointment = facade.newAppointment( startDate, endDate);
            newEvent.addAppointment( newAppointment);
            final Allocatable[] resources = facade.getAllocatables();
            newEvent.addAllocatable( resources[0]);
            eventBus.fireEvent(new DetailSelectEvent(newEvent));
        } catch (RaplaException e1) {
            logger.error( e1.getMessage(), e1);
        } 
	}

}
