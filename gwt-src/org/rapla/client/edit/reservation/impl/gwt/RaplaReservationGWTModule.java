package org.rapla.client.edit.reservation.impl.gwt;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.impl.ReservationEditView;
import org.rapla.client.edit.reservation.impl.SampleReservationController;

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.binder.GinBinder;

public class RaplaReservationGWTModule implements GinModule{
    @Override
    public void configure(GinBinder binder) {
        binder.bind( ReservationController.class).to(SampleReservationController.class);
        binder.bind( ReservationEditView.class).to(ReservationEditViewImpl.class);
    }
}
