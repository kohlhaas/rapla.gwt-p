package org.rapla.client.edit.reservation.sample;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.RaplaType;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.CalendarOptions;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;

public interface AppointmentView<W> extends View<Presenter>, ReservationEditSubView<W> {
    public interface Presenter {
        void newAppointmentButtonPressed();
        Date nextFreeDateButtonPressed(Date startDate, Date endDate);
        void appointmentSelected(int selectedIndex);
        void removeAppointmentButtonPressed(int selectedIndex);
        void addRessourceButtonPressed(int selectedIndex, String resourceTypeName);
        Map<RaplaType<Allocatable>, List<Allocatable>> sortResources(List<Allocatable> resources);
    }

    void show(Reservation reservation);

    void updateAppointmentOptionsPanel(Appointment selectedAppointment);
    void updateAppointmentList(List<Appointment> appointments, int focus);
    void updateResources(List<Allocatable> resources);
    void updateBookedResources(List<Allocatable> resources);
}
