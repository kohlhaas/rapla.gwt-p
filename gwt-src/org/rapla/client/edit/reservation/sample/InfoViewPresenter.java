package org.rapla.client.edit.reservation.sample;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.sample.InfoView.Presenter;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;

public class InfoViewPresenter implements Presenter{

    private InfoView view;
    private Reservation reservation;

    
    @Inject
    public InfoViewPresenter(InfoView view) {
        this.view = view;
        this.view.setPresenter(this);
    }
    
	@Override
	public void newAppButtonPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventTypesChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStudiengangChanged() {
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

}
