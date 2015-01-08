package org.rapla.client.edit.reservation.sample.gwt;

import javax.inject.Singleton;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.SampleAppointmentView;
import org.rapla.client.edit.reservation.sample.SampleReservationPresenter;
import org.rapla.client.edit.reservation.sample.SampleReservationView;
import org.rapla.client.edit.reservation.sample.gwt.SampleAppointmentViewImpl;
import org.rapla.client.edit.reservation.sample.gwt.SampleReservationViewImpl;
import org.rapla.mwi14_2.edit.GeneralInformationPresenter;
import org.rapla.mwi14_2.edit.GeneralInformationView;
import org.rapla.mwi14_2.edit.gwt.GeneralInformationViewImpl;

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.binder.GinBinder;

public class SampleReservationEditModule implements GinModule{
    @Override
    public void configure(GinBinder binder) {
        binder.bind( ReservationController.class).to(SampleReservationPresenter.class).in( Singleton.class);
        binder.bind( SampleReservationView.class).to(SampleReservationViewImpl.class);
        binder.bind( SampleAppointmentView.class).to(SampleAppointmentViewImpl.class);
    }
}
