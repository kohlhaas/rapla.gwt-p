package org.rapla.client.edit.reservation;

import static de.vksi.c4j.Condition.*;

import java.util.List;

import org.rapla.client.edit.reservation.sample.InfoView;
import org.rapla.client.edit.reservation.sample.InfoViewPresenter;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ResourceDatesViewPresenter;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;

import de.vksi.c4j.Target;

public class InfoViewContract extends InfoViewPresenter {

	@Target
	private InfoViewPresenter target;
	private Reservation reservation;


	public InfoViewContract(InfoView view) {
		super(view);

		if (preCondition()) {
			assert view instanceof InfoView;
			// assert hour >= HOUR_MIN : "hour >= HOUR_MIN";
			// assert hour <= HOUR_MAX : "hour <= HOUR_MAX";
			// assert minute >= MINUTE_MIN : "minute >= MINUTE_MIN";
			// assert minute <= MINUTE_MAX : "minute <= MINUTE_MAX";
			// assert second >= SECOND_MIN : "second >= SECOND_MIN";
			// assert second <= SECOND_MAX : "second <= SECOND_MAX";
		}
		if (postCondition()) {
			// assert target.getHour() == hour : "hour set";
			// assert target.getMinute() == minute : "minute set";
			// assert target.getSecond() == second : "second set";
		}
	}


	public void onEventTypesChanged() {
		if (preCondition()) {
		
		}
		if (postCondition()) {

		}
	}

	public void onStudiengangChanged() {
		if (preCondition()) {
			
		}
		if (postCondition()) {

		}
	}

	public void hide() {
		if (preCondition()) {

		}
		if (postCondition()) {

		}
	}

	public String getTitelInput() {
		if (preCondition()) {
			
		}
		if (postCondition()) {

		}
		return ignored();
	}

	public String getVorlesungsStundenInput() {
		if (preCondition()) {

		}
		if (postCondition()) {

		}
		return ignored();
	}

	public String getSelectedEventType() {
		if (preCondition()) {

		}
		if (postCondition()) {

		}

		return ignored();
	}

	public void setTitelInput(String reservationName) {
		if (preCondition()) {
			assert reservationName != null;
		}
		if (postCondition()) {

		}
	}

	public void setSelectedEventType(String select) {
		if (preCondition()) {
			assert select != null;
		}
		if (postCondition()) {

		}
	}

	public void setVorlesungsStundenInput(String input) {
		if (preCondition()) {
			assert input != null; 
		}
		if (postCondition()) {

		}
	}

	public int getItemCountOfListBoxStudiengang() {
		if (preCondition()) {

		}
		if (postCondition()) {

		}

		return ignored();
	}

	public String getItemTextOfListBoxStudiengang(int index) {
		if (preCondition()) {
		//	assert index != null; 
			assert index  >= 0; 
			assert index  <= 7;
		}
		if (postCondition()) {

		}

		return ignored();
	}

	public void setSelectedIndexOfListBoxStudiengang(int index) {
		if (preCondition()) {
		//	assert index != null;
			assert index <=0 ;
			assert index >=7;

		}
		if (postCondition()) {
		//	assert index != null;
		}
	}

	public void setContentOfListBoxStudiengang(List<String> content) {
		if (preCondition()) {
			assert content != null; 

		}
		if (postCondition()) {

		}
	}

	public void setEventTypes(List<String> eventTypes) {
		if (preCondition()) {
			assert eventTypes != null;
		}
		if (postCondition()) {

		}
	}

	public void setDynamicFields(Attribute[] attributes) {
		if (preCondition()) {
			assert attributes != null; 

		}
		if (postCondition()) {
		

		}
	}

}