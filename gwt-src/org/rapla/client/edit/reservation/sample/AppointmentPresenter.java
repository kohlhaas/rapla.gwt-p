package org.rapla.client.edit.reservation.sample;

import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.components.util.DateTools;
import org.rapla.entities.RaplaType;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.DynamicType;
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
    @Inject
    ClientFacade facade;
    @Inject
    Logger logger;
    @Inject
    RaplaLocale locale;
    @Inject
    CalendarOptions calendarOptions;

    private AppointmentView view;
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
            Date startDate = new Date(facade.today().getTime() + DateTools.MILLISECONDS_PER_HOUR * 8);
            Date endDate = new Date(startDate.getTime() + DateTools.MILLISECONDS_PER_HOUR);
            newAppointment = facade.newAppointment(startDate, endDate);
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
            logger.error(e.getMessage(), e);
            return null;
        }
    }

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
    public void addResourceButtonPressed(int selectedIndex, String resourceTypeName, Locale locale) {
        DynamicType raplaTypeKey = null;

        Map<DynamicType, List<Allocatable>> sortedResources = this.getSortedAllocatables();
        for (DynamicType type : sortedResources.keySet()) {
            if (type.getName(locale).equals(resourceTypeName)) {
                raplaTypeKey = type;
            }
        }
        List<Allocatable> allocatables = sortedResources.get(raplaTypeKey);
        this.reservation.addAllocatable(allocatables.get(selectedIndex));
        view.updateBookedResources((Arrays.asList(reservation.getAppointments())));
    }

    @Override
    /**
     * returns a map(K,V) with a Type and the corespondent Resources
     */
    public Map<DynamicType, List<Allocatable>> getSortedAllocatables() {
        Map<DynamicType, List<Allocatable>> sortedResources = new HashMap<>();
        Allocatable[] allResources = getAllocatables();
        for (Allocatable resource : allResources) {
            DynamicType type = resource.getClassification().getType();
            if (!sortedResources.containsKey(type)) {
                sortedResources.put(type, new ArrayList<Allocatable>());
            }
            sortedResources.get(type).add(resource);
        }
        return sortedResources;
    }


    /**
     * all allocatable are under types, returns null if an error has happened
     * example : resourceTypes: rooms, persons, objects|things ||| resources: rooms = chairs,tables... ; persons = age,semestre...; things: pen,beamer...
     */
    public Allocatable [] getAllocatables(){
        try {
            return facade.getAllocatables();
        } catch (RaplaException e) {
            logger.error("error while using facade: ", e);
        }
        return null;
    }

    /**
     * @return all "Veranslatungstypen" eventTypes
     */
    public DynamicType[] getEventTypes(){
        return getDynamicTypes("reservation");
    }

    /**
     * @return all "Ressourcen Typen" resourcestypes
     */
    public DynamicType[] getResourceTypes(){
        return getDynamicTypes("resource");
    }

    /**
     *  possible keys are reservation(Veranstaltungstyp), person(..) and resource(ressourcetypes), returns null if an error has happened
     *  Obergruppen(TYPEN = REssourcenTypen, VeranstaltungsTypen..)
     * example : resourceTypes: rooms, persons, objects|things ||| resources: rooms = chairs,tables... ; persons = age,semestre...; things: pen,beamer...
     */
    private DynamicType[] getDynamicTypes(String name){
        try {
            return facade.getDynamicTypes(name);
        } catch (RaplaException e) {
            logger.error("error while using facade: ", e);
        }
        return null;
    }
    
    public Reservation getReservation() {
		return reservation;
	}

    /**
     * every Classification exist of: DynamicType(take a look above), Attributes...Names
     * Attributes are the properties of the ObjectType
     * DynamicType is the ObjectType
     * ...
     */

}

