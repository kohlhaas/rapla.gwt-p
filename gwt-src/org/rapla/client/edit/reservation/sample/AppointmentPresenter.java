package org.rapla.client.edit.reservation.sample;

import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.RaplaType;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.CalendarOptions;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.Conflict;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;

import javax.inject.Inject;
import java.util.*;

public class AppointmentPresenter implements Presenter {
    private AppointmentView view;
    @Inject
    ClientFacade facade;
    @Inject
    Logger logger;
    @Inject
    RaplaLocale locale;
    @Inject
    CalendarOptions calendarOptions;

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
            view.updateAppointmentList(appointmentList, appointmentList.size() - 1);
        } catch (RaplaException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * gets all conflicts for the existing reservation, if a new appButton has been pressed, a new app will be added to
     * the reservation.. conflicts in this reservation will be shown. Else it returns NULL
     *
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
     *
     * @param startDate
     * @param endDate
     * @return NULL if error
     */
    public Date nextFreeDateButtonPressed(Date startDate, Date endDate) {
        Appointment newAppointment;
        try {
            logger.info("accessing facade");
            newAppointment = facade.newAppointment(startDate, endDate);
            logger.info("new appointment for Dates: " + startDate.toString() + " - " + endDate.toString());
            List<Allocatable> asList = Arrays.asList(facade.getAllocatables());
            FutureResult<Date> nextAllocatableDate = facade.getNextAllocatableDate(asList, newAppointment, calendarOptions);
            logger.info("next allo date: " + nextAllocatableDate.get().toString());
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
        view.show(reservation);
    }

    public AppointmentView getView() {
        return view;
    }


    @Override
    public void appointmentSelected(int selectedIndex) {
        view.updateAppointmentOptionsPanel(reservation.getAppointments()[selectedIndex]);
    }

    /**
     * removes the Appointment, chosen by the given index
     */
    @Override
    public void removeAppointmentButtonPressed(int selectedIndex) {
        logger.info("removing app with Index: " + selectedIndex);
        Appointment selectedAppointment = reservation.getAppointments()[selectedIndex];
        logger.info("removing app toString: " + selectedAppointment.toString());
        this.reservation.removeAppointment(selectedAppointment);
        this.view.updateAppointmentList(Arrays.asList(reservation.getAppointments()), selectedIndex - 1);
    }

    @Override
    public void addResourceButtonPressed(int selectedIndex, String resourceTypeName) {
        try {
            RaplaType<Allocatable> raplaTypeKey = null;

            //TODO: very vague, needs proper error handling, ex; allocatable from map == null? ClassCastException??..
            Map<RaplaType<Allocatable>, List<Allocatable>> sortedResources = this.sortResources(Arrays.asList(facade.getAllocatables()));
            for (RaplaType<Allocatable> raplaType : sortedResources.keySet()) {
                if (raplaType.getLocalName().equals(resourceTypeName)) {
                    raplaTypeKey = raplaType;
                }
            }
            List<Allocatable> allocatables = sortedResources.get(raplaTypeKey);
            this.reservation.addAllocatable(allocatables.get(selectedIndex));
            view.updateBookedResources((Arrays.asList(reservation.getAppointments())));
        } catch (RaplaException e) {
            logger.error("error while using facade: ", e);
        }
    }

    @Override
    public Map<RaplaType<Allocatable>, List<Allocatable>> sortResources(List<Allocatable> resources) {
        Map<RaplaType<Allocatable>, List<Allocatable>> sortedResources = new HashMap<RaplaType<Allocatable>, List<Allocatable>>();
        for (Allocatable resource : resources) {
            RaplaType<Allocatable> resourceType = resource.getRaplaType();
            if (!sortedResources.containsKey(resourceType)) {
                sortedResources.put(resourceType, new ArrayList<Allocatable>());
            }
            sortedResources.get(resourceType).add(resource);
        }
        return sortedResources;
    }

}
