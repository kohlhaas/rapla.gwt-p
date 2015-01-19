package org.rapla.client.edit.reservation.sample;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.Category;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReservationPresenter implements ReservationController, Presenter {

    @Inject
    Logger logger;
    @Inject
    RaplaLocale raplaLocale;
    @Inject
    ClientFacade facade;

    private ReservationView view;
    private AppointmentPresenter appointmentPresenter;
    private Reservation event;
    boolean isNew;


    @Inject
    public ReservationPresenter(ReservationView view, AppointmentPresenter appointmentPresenter) {
        this.view = view;
        view.setPresenter(this);
        this.appointmentPresenter = appointmentPresenter;
        view.addSubView(appointmentPresenter.getView());
    }


    @Override
    public void edit(final Reservation event, boolean isNew) {
        this.event = event;
        this.isNew = isNew;
        appointmentPresenter.setReservation(event);
        view.show(event);
    }

    @Override
    public void onSaveButtonClicked() {
        logger.info("save clicked");
        try {
            facade.store(event);
        } catch (RaplaException e1) {
            logger.error(e1.getMessage(), e1);
        }
        view.hide();
    }

    @Override
    public void onDeleteButtonClicked() {
        logger.info("delete clicked");
        try {
            facade.remove(event);
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
     * @return all "Veranslatungstypen" eventTypes, null if error
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
     * @param Category Studiengänge, Benutzergruppen.
     * @return null if error
     */
    public Category[] getCategory(Locale locale, String Category) {
        Category courseCategory = null;
        Category superCategory = facade.getSuperCategory();
        Category[] categories = superCategory.getCategories();
        for (Category category : categories) {
            if (category.getName(locale).equals(Category)) {
                courseCategory = category;
            }
        }
        if (courseCategory == null) {
            logger.error("there is no : " + Category);
        }

        if (courseCategory != null) {
            return courseCategory.getCategories();
        }
        else return null;
    }


    @Override
    public void changeEventName(String newName) {
        logger.info("Name changed to " + newName);
        Classification classification = event.getClassification();
        Attribute first = classification.getType().getAttributes()[0];
        classification.setValue(first, newName);
    }


    @Override
    public boolean isDeleteButtonEnabled() {
        return !isNew;
    }


}
