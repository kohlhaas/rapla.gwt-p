package org.rapla.client.edit.reservation.sample;

import java.util.HashMap;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.DynamicTypeAnnotations;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;

public class ReservationPresenter implements ReservationController,Presenter {

    @Inject
    Logger logger;
    @Inject
    RaplaLocale raplaLocale;
    @Inject
    ClientFacade facade;
    
    private ReservationView view;
    private SampleAppointmentPresenter appointmentPresenter;
    private Reservation tempReservation;
    
    @Inject
    public ReservationPresenter(ReservationView view, SampleAppointmentPresenter appointmentPresenter) {
        this.view = view;
        view.setPresenter(this);
        this.appointmentPresenter = appointmentPresenter;
        view.addSubView( appointmentPresenter.getView());
    }

    Reservation event;
    boolean isNew;
    
    
    @Override
    public void edit(final Reservation event, boolean isNew) {
        tempReservation = event;
        this.isNew = isNew;
        appointmentPresenter.setReservation( event);
        view.show(event);
    }

    @Override
    public void onSaveButtonClicked() {
        logger.info("save clicked");
        try {
   
        	 saveTemporaryChanges();
			 Classification classification = tempReservation.getClassification();
			 Attribute first =
			 classification.getType().getAttributes()[0];
			 String text = view.getTitelInput();
			 classification.setValue(first, text);       	
            facade.store(tempReservation);
        } catch (RaplaException e1) {
            logger.error( e1.getMessage(), e1);
        }
        view.hide();
    }

    private void saveTemporaryChanges() {
    	Classification classificationTmp = tempReservation.getClassification();

    			String reservationName = view.getTitelInput();

			Attribute first = classificationTmp.getType().getAttributes()[0];
			classificationTmp.setValue(first, reservationName);

	
			// change reservation type
			try {
				for (DynamicType type : facade
						.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)) {
					if (!(type.getName(raplaLocale.getLocale())
							.equals(classificationTmp.getType()))
							//&& type.getName(raplaLocale.getLocale()).equals(
							//		((InfoViewInterface) currentView)
							//				.getSelectedEventType())) 
							){

						HashMap<Attribute, Object> attributes = new HashMap<Attribute, Object>();
						for (Attribute a : classificationTmp.getAttributes()) {
							attributes.put(a, classificationTmp.getValue(a));
						}

						tempReservation.setClassification(type.newClassification());

						classificationTmp = tempReservation.getClassification();

						for (Attribute a : classificationTmp.getAttributes()) {
							classificationTmp
									.setValue(a, attributes.get(a));
						}

					}
				}
			} catch (RaplaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// set Vorlesungstunden
			try {
				if (tempReservation
						.getClassification()
						.getType()
						.equals(facade
								.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)[0])) {
					Attribute second = classificationTmp.getType().getAttributes()[5];
					classificationTmp.setValue("",view.getVorlesungsStundenInput());
				} else {
				//?
}
			} catch (RaplaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}

	@Override
    public void onDeleteButtonClicked() {
        logger.info("delete clicked");
        try {
            facade.remove( event);
        } catch (RaplaException e1) {
            logger.error( e1.getMessage(), e1);
        }
        view.hide();
    }

    @Override
    public void onCancelButtonClicked() {
        logger.info("cancel clicked");
        view.hide();
    }

    
    @Override
    public void changeEventName(String newName) {
        logger.info("Name changed to " + newName);
        Classification classification = event.getClassification();
        Attribute first = classification.getType().getAttributes()[0];
        classification.setValue(first, newName);
    }
    
    
    @Override
    public boolean isDeleteButtonEnabled() 
    {
        return !isNew;
    }

	@Override
	public void onTabChanged(int selectedTab) {
		view.update(selectedTab);
	}

	@Override
	public void onEventTypesChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStudiengangChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGarbageCanButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWholeDaySelected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onButtonPlusClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRewriteDateClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddDateClicked() {
		// TODO Auto-generated method stub
		
	}    
    

}
