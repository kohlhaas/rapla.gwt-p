package org.rapla.client.edit.reservation.sample;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.sample.SampleAppointmentView.Presenter;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.logger.Logger;

public class SampleAppointmentPresenter implements Presenter{
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
    public void newAppButtonPressed() 
    {
        Date startDate = new Date(facade.today().getTime() + DateTools.MILLISECONDS_PER_HOUR * 8);
        Date endDate = new Date(startDate.getTime() + DateTools.MILLISECONDS_PER_HOUR);
        Appointment newAppointment;
        try {
            newAppointment = facade.newAppointment(startDate, endDate);
            reservation.addAppointment( newAppointment);
        } catch (RaplaException e) {
            logger.error(e.getMessage(), e);
        }
        List<Appointment> asList = Arrays.asList(reservation.getAppointments());
        view.update( asList);

    }
    
    public void setReservation(Reservation reservation)
    {
        this.reservation = reservation;
        List<Appointment> asList = Arrays.asList(reservation.getAppointments());
        view.show(asList);
    }
    
    public SampleAppointmentView getView()
    {
        return view;
    }

}
