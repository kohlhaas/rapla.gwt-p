package org.rapla.client.edit.reservation.sample;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.SampleReservationEditView.Presenter;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;

public class SampleReservationController implements ReservationController,Presenter {

    @Inject
    Logger logger;
    @Inject
    RaplaLocale raplaLocale;
    @Inject
    ClientFacade facade;
    
    SampleReservationEditView editView;
    
    Reservation event;
    boolean isNew;
    
    public @Inject SampleReservationController(SampleReservationEditView editView)
    {
        this.editView = editView;
        editView.setPresenter( this );
    }
    
    @Override
    public void edit(final Reservation event, boolean isNew) {
        this.event = event;
        this.isNew = isNew;
        editView.show(event);
    }

    @Override
    public void onSaveButtonClicked() {
        logger.info("save clicked");
        try {
            facade.store( event);
        } catch (RaplaException e1) {
            logger.error( e1.getMessage(), e1);
        }
        editView.hide();
    }

    @Override
    public void onDeleteButtonClicked() {
        logger.info("delete clicked");
        try {
            facade.remove( event);
        } catch (RaplaException e1) {
            logger.error( e1.getMessage(), e1);
        }
        editView.hide();
    }

    @Override
    public void onCancelButtonClicked() {
        logger.info("cancel clicked");
        editView.hide();
    }

    
    @Override
    public void changeEventName(String newName) {
        logger.info("Name changed to " + newName);
        Classification classification = event.getClassification();
        Attribute first = classification.getType().getAttributes()[0];
        classification.setValue(first, newName);
    }
    
    @Override
    public void newAppButtonPressed() 
    {
        
    }

    @Override
    public boolean isDeleteButtonEnabled() 
    {
        return !isNew;
    }
    
    

}
