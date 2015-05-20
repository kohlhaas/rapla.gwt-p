package org.rapla.client.edit.reservation;

import java.lang.annotation.*;

import org.rapla.client.edit.reservation.sample.InfoViewPresenter;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ResourceDatesViewPresenter;
import org.rapla.entities.domain.Reservation;

import static de.vksi.c4j.Condition.ignored;
import static de.vksi.c4j.Condition.postCondition;
import static de.vksi.c4j.Condition.preCondition;
import static de.vksi.c4j.Condition.unchanged;
import de.vksi.c4j.ClassInvariant;
import de.vksi.c4j.Target;

public class ReservationContract extends ReservationPresenter {

	@Target
	private ReservationPresenter target;
	private Reservation reservation = target.getTempReservation();

	public ReservationContract(ReservationView view,
			InfoViewPresenter infoViewPresenter,
			ResourceDatesViewPresenter resourceDatesPresenter) {
		super(view, infoViewPresenter, resourceDatesPresenter);

		if (preCondition()) {
			// assert hour >= HOUR_MIN : "hour >= HOUR_MIN";
			// assert hour <= HOUR_MAX : "hour <= HOUR_MAX";
			// assert minute >= MINUTE_MIN : "minute >= MINUTE_MIN"
		}
		if (postCondition()) {
			// assert target.getHour() == hour : "hour set";
			// assert target.getMinute() == minute : "minute set";
			// assert target.getSecond() == second : "second set";
		}
	}

	@Override
	public void onSaveButtonClicked() {
		if (preCondition()) {
			assert reservation != null;
				
		}
		if (postCondition()) {
			
		}
	}
	
	@Override
	public void onTabChanged(int selectedTab) {
		if (preCondition()) {
			//View ist anders als der View vorher 
			// zahl zwischen 0 und 1 
			
			assert selectedTab <= 0; 
			assert selectedTab >= 1; 
		}
		if (postCondition()) {
			
		}
	}
	
	
	// @Override
	// public void setHour(int hour) {
	// if (preCondition()) {
	// assert hour >= HOUR_MIN : "hour >= HOUR_MIN";
	// assert hour <= HOUR_MAX : "hour <= HOUR_MAX";
	// }
	// if (postCondition()) {
	// assert target.getHour() == hour : "hour set";
	// assert unchanged(target.getMinute()) : "minute unchanged";
	// assert unchanged(target.getSecond()) : "second unchanged";
	// }
	// }

}