package org.rapla.client.edit.reservation.sample.gwt;

import javax.inject.Singleton;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.SampleAppointmentView;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.binder.GinBinder;

public class ReservationEditModule implements GinModule{
    @Override
    public void configure(GinBinder binder) {
        binder.bind( ReservationController.class).to(ReservationPresenter.class).in( Singleton.class);
        binder.bind( ReservationView.class).to(ReservationViewImpl.class);
        binder.bind( SampleAppointmentView.class).to(SampleAppointmentViewImpl.class);
    }
}
