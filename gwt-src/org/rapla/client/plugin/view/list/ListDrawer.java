package org.rapla.client.plugin.view.list;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.rapla.client.content.ContentDrawer;
import org.rapla.client.content.DetailSelectEvent;
import org.rapla.client.event.RaplaEventBus;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.rest.gwtjsonrpc.common.AsyncCallback;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;
import org.rapla.storage.dbrm.RemoteStorage;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class ListDrawer implements ContentDrawer {
    private final Logger logger = Logger.getLogger(ListDrawer.class.getSimpleName());
    @Inject
    private RemoteStorage service;
	private CellList<String> list;
	private ListDataProvider<String> data;

	public ListDrawer() {
        list = new CellList<>(new TextCell());
        list.setStyleName("RaplaListDrawerList");
        data = new ListDataProvider<>();
        data.addDataDisplay(list);
	}

	@Override
	public Widget createContent() {
        
      FutureResult<List<ReservationImpl>> reservations =  service.getReservations(null, null, null, null);
        reservations.get(new AsyncCallback<List<ReservationImpl>>() {
            
             public void onFailure(Throwable caught) {
                 logger.log(Level.SEVERE, "get Reservation failed:" + caught.getMessage(),caught);
             }
            
             @Override
             public void onSuccess(List<ReservationImpl> result) {
                 logger.info( result.size()+ " Reservations loaded.");
             }
        });
        
        
        for (int i = 0; i < 100; i++)
            data.getList().add("Test" + i);
        list.setPageSize(data.getList().size());
        final SingleSelectionModel<String> singleSelectionModel = new SingleSelectionModel<>();
        list.setSelectionModel(singleSelectionModel);
        singleSelectionModel.addSelectionChangeHandler(new Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                RaplaEventBus.getInstance().fireEvent(
                        new DetailSelectEvent(singleSelectionModel
                                .getSelectedObject()));
            }
        });		return list;
	}

	@Override
	public void updateContent() {

	}

}
