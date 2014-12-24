package org.rapla.client.edit.reservation.sample.gwt;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.SampleReservationController;
import org.rapla.client.edit.reservation.sample.SampleReservationEditView;

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.binder.GinBinder;

public class SampleReservationEditGWTModule implements GinModule{
    @Override
    public void configure(GinBinder binder) {
        binder.bind( ReservationController.class).to(SampleReservationController.class);
        binder.bind( SampleReservationEditView.class).to(SampleReservationEditViewImpl.class);
    }
}
