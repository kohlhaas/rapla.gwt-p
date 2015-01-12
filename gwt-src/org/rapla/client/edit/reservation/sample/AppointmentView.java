package org.rapla.client.edit.reservation.sample;

import java.util.Date;
import java.util.List;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.domain.Appointment;

public interface AppointmentView<W> extends View<Presenter>, ReservationEditSubView<W> {
    public interface Presenter
    {
//        void newAppButtonPressed(Date startDate, Date endDate);
		void appointmentSelected(int selectedIndex);
    }

    void show(List<Appointment> asList);
    void updateAppointmentOptionsPanel(Appointment selectedAppointment);
    //void update(List<Appointment> appointments);
    
}
