package org.rapla.client.edit.reservation.sample;

import java.util.List;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.sample.SampleAppointmentView.Presenter;
import org.rapla.entities.domain.Appointment;

public interface SampleAppointmentView<W> extends View<Presenter>, ReservationEditSubView<W> {
    public interface Presenter
    {
        void newAppButtonPressed();   
    }

    void show(List<Appointment> asList);
    
    void update(List<Appointment> appointments);
    
}
