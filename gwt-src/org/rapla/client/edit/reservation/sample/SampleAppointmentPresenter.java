package org.rapla.client.edit.reservation.sample;

import org.rapla.client.edit.reservation.sample.SampleAppointmentView.Presenter;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.logger.Logger;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SampleAppointmentPresenter implements Presenter {
    private SampleAppointmentView view;
    @Inject
    ClientFacade facade;
    @Inject
    Logger logger;

    private Reservation reservation;

    @Inject
    public SampleAppointmentPresenter(SampleAppointmentView view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void newAppButtonPressed(Date startDate, Date endDate) {
        Appointment newAppointment;
        try {
            newAppointment = facade.newAppointment(startDate, endDate);
            reservation.addAppointment(newAppointment);
        } catch (RaplaException e) {
            logger.error(e.getMessage(), e);
        }
        List<Appointment> asList = Arrays.asList(reservation.getAppointments());
        view.update(asList);

    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        List<Appointment> asList = Arrays.asList(reservation.getAppointments());
        view.show(asList);
    }

    public SampleAppointmentView getView() {
        return view;
    }

}
