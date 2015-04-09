package org.rapla.client.plugin.weekview;

import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import org.rapla.client.base.CalendarPlugin;
import org.rapla.client.event.DetailSelectEvent;
import org.rapla.client.plugin.weekview.CalendarWeekView.Presenter;
import org.rapla.entities.User;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.ClassificationFilter;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.logger.Logger;
import org.rapla.rest.gwtjsonrpc.common.AsyncCallback;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;

import com.google.web.bindery.event.shared.EventBus;

public class CalendarWeekViewPresenter<W> implements Presenter, CalendarPlugin {

    private CalendarWeekView<W> view;
    @Inject
    private Logger logger;
    @Inject
    private ClientFacade facade;
    @Inject
    private EventBus eventBus;

    @Inject
    public CalendarWeekViewPresenter(CalendarWeekView view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public String getName() {
        return "week";
    }

    @Override
    public W provideContent() {
        return view.provideContent();
    }
    
    @Override
    public void selectReservation(Reservation selectedObject){
        DetailSelectEvent event2 = new DetailSelectEvent(selectedObject);
        eventBus.fireEvent(event2);
        logger.info("selection changed");

    }

    @Override
    public void updateContent() {
        Allocatable[] allocatables = null;
        Date start = null;
        Date end = null;
        User user = null;
        ClassificationFilter[] reservationFilters = null;
        FutureResult<Collection<Reservation>> reservationsAsync = facade.getReservationsAsync(user, allocatables, start, end, reservationFilters);
        reservationsAsync.get( new AsyncCallback<Collection<Reservation>>() {
            
            @Override
            public void onSuccess(Collection<Reservation> result) {
                logger.info(result.size() + " Reservations loaded.");
                view.update(result);                }
            
            @Override
            public void onFailure(Throwable e) {
                logger.error(e.getMessage(), e.getCause());
            }
        });

    }

}
