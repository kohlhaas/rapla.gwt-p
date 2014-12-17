package org.rapla.client.factory;

import org.rapla.client.edit.reservation.impl.ReservationController;

import com.google.gwt.user.client.ui.Widget;

public interface ViewServiceProviderInterface {
	public Widget createContent();
	
	public void updateContent();
	
	public void setReservationController(ReservationController reservationController);

	public ReservationController getReservationController();
	
	
	
}
