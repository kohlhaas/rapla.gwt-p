package org.rapla.client.edit.reservation.sample;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.sample.ResourceDatesView.Presenter;
import org.rapla.entities.domain.Reservation;

import com.google.gwt.user.client.ui.CheckBox;

public class ResourceDatesViewPresenter implements Presenter{

    private ResourceDatesView view;
    private Reservation reservation;

    
    @Inject
    public ResourceDatesViewPresenter(ResourceDatesView view) {
        this.view = view;
        this.view.setPresenter(this);
    }
    
	@Override
	public void newAppButtonPressed() {
		// TODO Auto-generated method stub
		
	}

	

	public ReservationEditSubView getView() {
		// TODO Auto-generated method stub
		return view;
	}

	public void setReservation(Reservation event) {
		this.reservation = reservation;
//        List<Appointment> asList = Arrays.asList(reservation.getAppointments());
//        view.show(asList);
	}

	@Override
	public void onGarbageCanButtonClicked() {
		view.clearInputFields();
			
	}

	@Override
	public void onWholeDaySelected() {
		
		view.setVisiblityOfDateElements();
	}
	

	@Override
	public void onButtonPlusClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRewriteDateClicked() {
		
		view.RewriteDate();
		
	}

	@Override
	public void onAddDateClicked() {
		view.addDateWidget();
		
	}

	@Override
	public void onResourcesAdded() {
		view.addResources();
	}

}

