package org.rapla.client.edit.reservation.sample;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.facade.Conflict;

public interface AppointmentView<W> extends View<Presenter>, ReservationEditSubView<W> {
    public interface Presenter {
        Conflict[] getConflicts();
        void newAppointmentButtonPressed();
        Date nextFreeDateButtonPressed(Date startDate, Date endDate);
        void appointmentSelected(int selectedIndex);
        void removeAppointmentButtonPressed(int selectedIndex);
        void addResourceButtonPressed(int selectedIndex, String resourceTypeName, Locale locale);
        Allocatable[] getAllocatables();
        Map<DynamicType, List<Allocatable>> getSortedAllocatables();
    }

    void show(Reservation reservation);

    void updateAppointmentOptionsPanel(Appointment selectedAppointment);
    void updateAppointmentList(List<Appointment> appointments, int focus);
    void updateResources(List<Allocatable> resources);
    void updateBookedResources(List<Allocatable> resources);
}
