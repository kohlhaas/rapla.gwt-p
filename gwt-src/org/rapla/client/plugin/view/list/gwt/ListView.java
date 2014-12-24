package org.rapla.client.plugin.view.list.gwt;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.rapla.client.event.DetailSelectEvent;
import org.rapla.client.event.RaplaEventBus;
import org.rapla.client.plugin.view.ViewPlugin;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class ListView implements ViewPlugin {

    @Inject
    private Logger logger;
    @Inject
    private ClientFacade facade;
    @Inject
    private RaplaLocale raplaLocale;
    @Inject
    private RaplaEventBus eventBus;

	public ListView() {
	}

	@Override
	public String getName() {
		return "list";
	}


	private class ReservationCell extends AbstractCell<Reservation>
    {

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context, Reservation value, SafeHtmlBuilder sb) {
            String name = value.getName( raplaLocale.getLocale());
            sb.appendEscaped( name);
        }
        
    }
    

    @Override
    public Widget provideContent() {
        final CellList<Reservation> list = new CellList<Reservation>(new ReservationCell());
        list.setStyleName("RaplaListDrawerList");
        final ListDataProvider<Reservation> data = new ListDataProvider<>();
        data.getList().clear();
        data.addDataDisplay(list);
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
            logger.error( e.getMessage(), e.getCause());
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
                eventBus.fireEvent(event2);
                logger.info("selection changed");
            }
        });     
        return list;
    }
}
