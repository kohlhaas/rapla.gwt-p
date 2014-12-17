package org.rapla.client.plugin.view.list;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.impl.ReservationController;
import org.rapla.client.event.DetailSelectEvent;
import org.rapla.client.event.RaplaEventBus;
import org.rapla.client.internal.RaplaGWTClient;
import org.rapla.client.plugin.view.ViewServiceProviderInterface;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.rest.gwtjsonrpc.common.AsyncCallback;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;

import sun.security.action.GetLongAction;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class ListDrawer implements ViewServiceProviderInterface {
    private final Logger logger = Logger.getLogger("componentClass");
    @Inject
    private RaplaGWTClient service;
    
ReservationController reservationController;
	
	public ReservationController getReservationController() {
		return reservationController;
	}

	public void setReservationController(ReservationController reservationController) {
		this.reservationController = reservationController;
	}
	
	private static class ReservationCell extends AbstractCell<Reservation>
	{

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context, Reservation value, SafeHtmlBuilder sb) {
            String name = value.getName( Locale.GERMANY);
            sb.appendEscaped( name);
        }
	    
	}
	public ListDrawer() {
	    
        
        
	}

	@Override
	public Widget createContent() {
	    final CellList<Reservation> list = new CellList<Reservation>(new ReservationCell());
	    list.setStyleName("RaplaListDrawerList");
        final ListDataProvider<Reservation> data = new ListDataProvider<>();
        logger.info("Service " + service);
        data.getList().clear();
        data.addDataDisplay(list);
        ClientFacade facade = service.getFacade();
        Locale locale = service.getRaplaLocale().getLocale();
        Allocatable[] allocatables = null;
        Date start = null;
        Date end = null;
        try {
            List<Reservation> result = Arrays.asList(facade.getReservations(allocatables, start, end));
            logger.info( result.size()+ " Reservations loaded.");
            for ( Reservation event:result)
            {
                data.getList().add(event);
            }
            list.setPageSize(data.getList().size());
        } catch (RaplaException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.getCause());
        }
       
        //FutureResult<List<ReservationImpl>> reservations =  facade.getReservations(null, null, null, null);
//        reservations.get(new AsyncCallback<List<ReservationImpl>>() {
//            
//             public void onFailure(Throwable caught) {
//                 logger.log(Level.SEVERE, "get Reservation failed:" + caught.getMessage(),caught);
//                 for (int i = 0; i < 100; i++)
//                 {
//                     data.getList().add("Test" + i);
//                 }
//                 list.setPageSize(data.getList().size());
//             }
//            
//             @Override
//             public void onSuccess(List<ReservationImpl> result) {
//                 logger.info( result.size()+ " Reservations loaded.");
//                 for ( ReservationImpl event:result)
//                 {
//                     data.getList().add(event.toString());
//                 }
//                 list.setPageSize(data.getList().size());
//             }
//        });
        
        
        
        
        final SingleSelectionModel<Reservation> singleSelectionModel = new SingleSelectionModel<>();
        list.setSelectionModel(singleSelectionModel);
        singleSelectionModel.addSelectionChangeHandler(new Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Reservation selectedObject = singleSelectionModel.getSelectedObject();
                DetailSelectEvent event2 = new DetailSelectEvent(selectedObject);
                RaplaEventBus instance = RaplaEventBus.getInstance();
                instance.fireEvent(event2);
                logger.info("selection changed");
            }
        });		
        return list;
	}

	@Override
	public void updateContent() {
	    
	}

}
