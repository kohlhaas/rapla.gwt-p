package org.rapla.client.edit.reservation.sample;

import de.vksi.c4j.ContractReference;

import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.domain.*;
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

@ContractReference(AppointmentPresenterContract.class)
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

    /**
     * @param repeatingType use null, if no repeating is needed
     */
    @Override
    public void newAppointmentButtonPressed(Date startDate, Date endDate, RepeatingType repeatingType) {
        Appointment newAppointment;
        try {
            newAppointment = facade.newAppointment(startDate, endDate);
            if (repeatingType != null) {
                logger.info("saved appointment: "+newAppointment +" and repeatingType "+ repeatingType);
                newAppointment.setRepeatingEnabled(true);
                Repeating repeating = newAppointment.getRepeating();
                repeating.setType(repeatingType);
            }
            reservation.addAppointment(newAppointment);
            List<Appointment> appointmentList = Arrays.asList(reservation.getAppointments());
            view.updateAppointmentList(appointmentList, appointmentList.size() - 1);
        } catch (RaplaException e) {
            logger.error(e.getMessage(), e);
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
    public Date[] nextFreeDateButtonPressed(Date startDate, Date endDate) {
        Appointment appointment;
        try {
            logger.info("accessing facade");
            appointment = facade.newAppointment(startDate, endDate);
            logger.info("new appointment for Dates: " + startDate.toString() + " - " + endDate.toString());
            List<Allocatable> asList = Arrays.asList(facade.getAllocatables());
            FutureResult<Date> nextAllocatableDate = facade.getNextAllocatableDate(asList, appointment, calendarOptions);
            logger.info("next allo date: " + nextAllocatableDate.get().toString());
            Date newStart = nextAllocatableDate.get();
            if (newStart != null) {
                appointment.move(newStart);
                return new Date[]{appointment.getStart(), appointment.getEnd()};
            } else {
                return null;
            }
        } catch (RaplaException e) {
            logger.error("error while using facade: ", e);
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
        int focus;
        if (reservation.getAppointments().length < 1)
            focus = -1;
        else if (selectedIndex == reservation.getAppointments().length)
            focus = selectedIndex - 1;
        else
            focus = selectedIndex;
        this.view.updateAppointmentList(Arrays.asList(reservation.getAppointments()), focus);
    }

    @Override
    public void addResourceButtonPressed(int selectedIndex, String resourceTypeName, Locale locale) {
        logger.info("calling addResourceButtonPressed");

        DynamicType dynamicType = null;

        Map<DynamicType, List<Allocatable>> sortedResources = this.getSortedAllocatables();
        for (DynamicType type : sortedResources.keySet()) {
            if (type.getName(locale).equals(resourceTypeName)) {
                dynamicType = type;
            }
        }
        List<Allocatable> allocatables = sortedResources.get(dynamicType);
        logger.info("saving Allocatable with name: " + allocatables.get(selectedIndex).getName(locale));
        this.reservation.addAllocatable(allocatables.get(selectedIndex));
        view.updateBookedResources(Arrays.asList(reservation.getResources()));
    }

    @Override
    /**
     * returns a map(K,V) with a Type and the corespondent Resources
     */
    public Map<DynamicType, List<Allocatable>> getSortedAllocatables() {
        logger.info("calling getSortedAllo");
        Map<DynamicType, List<Allocatable>> sortedResources = new HashMap<>();
        Allocatable[] allResources = getAllocatables();
        for (Allocatable resource : allResources) {
            DynamicType type = resource.getClassification().getType();
            if (!sortedResources.containsKey(type)) {
                sortedResources.put(type, new ArrayList<Allocatable>());
            }
            sortedResources.get(type).add(resource);
        }
        logger.info("returning sortedAllocatable Map with Size: " + sortedResources.size());
        return sortedResources;
    }

    @Override
    /**
     * all allocatable are under types, returns null if an error has happened
     * example : resourceTypes: rooms, persons, objects|things ||| resources: rooms = chairs,tables... ; persons = age,semestre...; things: pen,beamer...
     */
    public Allocatable[] getAllocatables() {
        try {
            return facade.getAllocatables();
        } catch (RaplaException e) {
            logger.error("error while using facade: ", e);
        }
        return null;
    }

    @Override
    /**
     * @return all "Veranslatungstypen" eventTypes
     */
    public DynamicType[] getEventTypes() {
        return getDynamicTypes("reservation");
    }

    @Override
    /**
     * @return all "Ressourcen Typen" resourcestypes
     */
    public DynamicType[] getResourceTypes() {
        return getDynamicTypes("resource");
    }

    /**
     * possible keys are reservation(Veranstaltungstyp), person(..) and resource(ressourcetypes), returns null if an error has happened
     * Obergruppen(TYPEN = REssourcenTypen, VeranstaltungsTypen..)
     * example : resourceTypes: rooms, persons, objects|things ||| resources: rooms = chairs,tables... ; persons = age,semestre...; things: pen,beamer...
     */
    private DynamicType[] getDynamicTypes(String name) {
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

	@Override
	public void removeResourceButtonPressed(int selectedIndex) {
        Allocatable allocatable = reservation.getAllocatables()[selectedIndex];
        logger.info("removing Allocatable with id: " + reservation.getAllocatables()[selectedIndex].getId());
        reservation.removeAllocatable(allocatable);
        view.updateBookedResources(Arrays.asList(reservation.getAllocatables()));
	}

	@Override
	public Conflict[] getConflicts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Conflict[] saveAppointment(Appointment appointment, Date startDate,
			Date endDate, RepeatingType repeatingType) {
		// TODO Auto-generated method stub
		return null;
	}



}

