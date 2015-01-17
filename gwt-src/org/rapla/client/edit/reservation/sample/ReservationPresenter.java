package org.rapla.client.edit.reservation.sample;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;

import javax.inject.Inject;

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

    // TODO: have to add an appropiate action
    @Override
    public void onCourseButtonClicked() {
        logger.info("Course clicked");
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
