package org.rapla.client.edit.reservation.sample;

import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.CalendarOptions;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.Conflict;
import org.rapla.facade.internal.CalendarOptionsImpl;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AppointmentPresenter implements Presenter {
    private AppointmentView view;
    @Inject
    ClientFacade facade;
    @Inject
    Logger logger;
    @Inject
    RaplaLocale locale;

    private Reservation reservation;

    @Inject
    public AppointmentPresenter(AppointmentView view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void newAppointmentButtonPressed() {
        Appointment newAppointment;
        try {
            newAppointment = facade.newAppointment(new Date(), new Date());
            reservation.addAppointment(newAppointment);
            List<Appointment> appointmentList = Arrays.asList(reservation.getAppointments());
            view.updateAppointmentList(appointmentList, appointmentList.size()-1);
        } catch (RaplaException e) {
            logger.error(e.getMessage(), e);
        }
        //List<Appointment> asList = Arrays.asList(reservation.getAppointments());
        //        view.update(asList);
    }

    /**
     * gets all conflicts for the existing reservation, if a new appButton has been pressed, a new app will be added to
     * the reservation.. conflicts in this reservation will be shown. Else it returns NULL
     * @return NULL if error
     */
    public Conflict[] getConflicts() {
        try {
            return facade.getConflicts(this.reservation);
        } catch (RaplaException e) {
            logger.error("error while using facade: ", e);
            return null;
        }
    }// its possible that this method is only needed as a private one, TODO: frontend needs Conflicts Object or the Conflict Dates ?

    /**
     * get the next Free date, depending on the startDate and endDate and CalendarOption, if an exception is thrown
     * THE METHOD RETURNS NULL
     * @return NULL if error
     * @param startDate
     * @param endDate
     */
    public Date nextFreeDateButtonPressed(Date startDate, Date endDate) {
        Appointment newAppointment;
        try {
            logger.info("accessing facade");
            newAppointment = facade.newAppointment(startDate, endDate);
            logger.info("new appointment for Dates: " + startDate.toString() + " - " + endDate.toString());
            List<Allocatable> asList = Arrays.asList(facade.getAllocatables());
            CalendarOptions options = new CalendarOptionsImpl();//TODO: find which CalendarOption is important
            FutureResult<Date> nextAllocatableDate = facade.getNextAllocatableDate(asList, newAppointment, options);
            logger.info("next allo date: "+ nextAllocatableDate.get().toString());
            return nextAllocatableDate.get();
        } catch (RaplaException e) {
            logger.error("error while using facade: ", e);
            return null;
        } catch (Exception e) {
            logger.error("error while getting nextAlloDate: ", e);
            return null;
        }
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        List<Appointment> asList = Arrays.asList(reservation.getAppointments());
        view.show(asList);
    }

    public AppointmentView getView() {
        return view;
    }


    @Override
    public void appointmentSelected(int selectedIndex) {
        view.updateAppointmentOptionsPanel(reservation.getAppointments()[selectedIndex]);
    }

	@Override
	public void removeAppointmentButtonPressed(int selectedIndex) {
		// TODO delete appointment from list, then call view.updateAppointmentList(..)
	}

}
