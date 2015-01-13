package org.rapla.client.edit.reservation.sample;

import java.util.Date;
import java.util.List;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.domain.Appointment;
import org.rapla.facade.CalendarOptions;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;

public interface AppointmentView<W> extends View<Presenter>, ReservationEditSubView<W> {
    public interface Presenter {
        void newAppointmentButtonPressed(Date startDate, Date endDate);
        Date nextFreeDateButtonPressed(Date startDate, Date endDate);
        void appointmentSelected(int selectedIndex);
    }

    void show(List<Appointment> asList);

    void updateAppointmentOptionsPanel(Appointment selectedAppointment);
    //void update(List<Appointment> appointments);

}
