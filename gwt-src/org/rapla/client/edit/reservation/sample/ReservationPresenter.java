package org.rapla.client.edit.reservation.sample;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.Category;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.Conflict;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;

import de.vksi.c4j.ContractReference;

import javax.inject.Inject;

import java.util.*;
import java.util.logging.Level;

/**
 * Each Reservation has a classification, some resources(allocatable) and appointments
 * Classifications kinds are : Reservation, Resource, Person.
 * Each Classification rhas a type : f.e. a resource can be type: course
 * A Classification for a reservation is classified as a reservation
 * A Appointment is a kind of timeline (from x to y)
 * A Allocatable has to be classified as resource and hold some attributes
 */
@ContractReference(ReservationPresenterContract.class)
public class ReservationPresenter implements ReservationController, Presenter {

    @Inject
    Logger logger;
    @Inject
    RaplaLocale raplaLocale;
    @Inject
    ClientFacade facade;

    private ReservationView view;
    private AppointmentPresenter appointmentPresenter;
    private Reservation reservation;
    boolean isNew;

    String tabName = "Termin- und Ressourcenplanung";

    @Inject
    public ReservationPresenter(ReservationView view, AppointmentPresenter appointmentPresenter) {
        this.view = view;
        view.setPresenter(this);
        this.appointmentPresenter = appointmentPresenter;
        view.addSubView(tabName, appointmentPresenter.getView());
    }

    @Override
    public void edit(final Reservation event, boolean isNew) {
        this.reservation = event;
        this.isNew = isNew;
        appointmentPresenter.setReservation(event);
        view.show(event);
    }

    @Override
    public void onSaveButtonClicked() {
        logger.info("save clicked");
        logger.info(getConflicts().length + " conflicts found.");
        if (getConflicts().length > 0) {
            view.showConflicts(getConflicts());
        } else {
            try {
                facade.store(reservation);
            } catch (RaplaException e1) {
                logger.error(e1.getMessage(), e1);
            }
            view.hide();
        }
    }

    public String getCurrentReservationName(Locale locale) {
        return reservation.getName(locale);
    }

    @Override
    public void onDeleteButtonClicked() {
        logger.info("delete clicked");
        try {
            facade.remove(reservation);
        } catch (RaplaException e1) {
            logger.error(e1.getMessage(), e1);
        }
        view.hide();
    }

    @Override
    public void onCancelButtonClicked() {
        logger.info("cancel clicked");
        view.hide();
    }

    /**
     * @return all "Veranstaltungstypen" eventTypes, null if error
     */
    @Override
    public DynamicType[] getAllEventTypes() {
        try {
            return facade.getDynamicTypes("reservation");
        } catch (RaplaException e) {
            logger.error("error while using facade: ", e);
        }
        return null;
    }

    /**
     * @param neededCategory Studiengaenge, Benutzergruppen....
     * @return null if error
     */
    public Category[] getCategoryAttributes(Locale locale, String neededCategory) {
        Category courseCategory = null;

        Category superCategory = facade.getSuperCategory();
        Category[] categories = superCategory.getCategories();
        for (Category category : categories) {
            logger.info("name: " + category.getName());
            if (category.getName(locale).equals(neededCategory)) {
                courseCategory = category;
            }
        }
        if (courseCategory == null) {
            logger.error("there is no : " + neededCategory);
        }

        if (courseCategory != null) {
            return courseCategory.getCategories();
        } else return null;
    }


    @Override
    public void changeReservationName(String newName) {
        logger.info("Name changed to " + newName);
        Classification classification = reservation.getClassification();
        Attribute attribute = classification.getType().getAttributes()[0];
        classification.setValue(attribute, newName);
    }

    /**
     * for now you have to save the original type and not as string or smth similiar
     * TODO: need a way to get the type and only save the specific type etc.., for now its only objects and that's not save
     *
     * @param attributeNames input a map of string and the corresponding object --> searching for the object in the attributes and change the value
     */
    public void changeAttributesOfCLassification(Map<String, Object> attributeNames, Locale locale) {
        logger.info("adding number of attributes: " + attributeNames.size());

        Classification classification = reservation.getClassification();
        DynamicType type = classification.getType();
        Attribute[] attributes = type.getAttributes();
        for (Attribute attribute : attributes) {
            for (Map.Entry<String, Object> entry : attributeNames.entrySet()) {
                if (entry.getKey().equalsIgnoreCase((attribute.getName(locale)))) {
                    classification.setValue(attribute, entry.getValue());
                }
            }
        }

    }

    public Attribute[] getAllCurrentAttributes() {
        return reservation.getClassification().getAttributes();
    }

    /**
     * @return all current Values as a String
     */
    public List<String> getAllCurrentAttributesAsStrings(Locale locale) {
        List<String> list = new ArrayList<>();
        Classification classification = reservation.getClassification();
        DynamicType type = classification.getType();
        for (Attribute attribute : type.getAttributes()) {
            String valueAsString = classification.getValueAsString(attribute, locale);
            if (valueAsString == null || valueAsString.isEmpty()) {
                valueAsString = "not defined yet";
            }
            list.add(attribute.getName() + " : " + valueAsString + " Typ: " + attribute.getType().name());
        }
//        list.add("");
//        for (Attribute attribute : type.getAttributes()) {
//            list.add(attribute.getKey());
//            list.add(attribute.getId());
//        }

        logger.info("all attributes length: " + list.size());
        return list;
    }


    /**
     * Each DynamicType (Lehrveranstaltung, Prüfung etc) has N Attributes (Titel, Sprache etc)
     * With the Map u can give each attribute a new value
     * overwrites current values
     *
     * @param valuesToSave a Map with a name of the attribute and a value, IT OVERWRITES ALL CURRENT ATTRIBUTES, SO SAVE NAME TOO
     */
    public void setAttributesOfReservation(Map<Attribute, Object> valuesToSave) {

        Classification classification = reservation.getClassification();
        DynamicType type = classification.getType();
        Classification newClassification = type.newClassification();
        logger.info("saving Map:" + valuesToSave.toString() + "and size:" + valuesToSave.size());
        for (Map.Entry<Attribute, Object> stringObjectEntry : valuesToSave.entrySet()) {
            newClassification.setValue(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        reservation.setClassification(newClassification);
        logger.info("new Classification" + Arrays.toString(newClassification.getAttributes()));
    }

    /**
     * change String, but it will be added as parameter!
     */
    public String getEventType(Locale locale) {
        if (!isNew) {
            Classification classification = reservation.getClassification();
            logger.info("returning Eventtype: " + classification.getType().getName());
            return "current Event Type: " + classification.getType().getName(locale);
        }
        return "is new";

    }

    /**
     * returns true if the user clicked on the "+" button
     *
     * @return if the event is new or not
     */

    public boolean getIsNew() {
        return isNew;
    }

    @Override
    public boolean isDeleteButtonEnabled() {
        return !isNew;
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


}
