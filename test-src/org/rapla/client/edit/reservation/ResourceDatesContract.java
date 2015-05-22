package org.rapla.client.edit.reservation;

import static de.vksi.c4j.Condition.postCondition;
import static de.vksi.c4j.Condition.preCondition;

import org.rapla.client.edit.reservation.sample.InfoView;
import org.rapla.client.edit.reservation.sample.InfoViewPresenter;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;
import org.rapla.client.edit.reservation.sample.ResourceDatesView;
import org.rapla.client.edit.reservation.sample.ResourceDatesViewPresenter;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.vksi.c4j.Target;

public class ResourceDatesContract  extends ResourceDatesViewPresenter {

//f�r diesen contract sind Zugriffe auf alle GWT-Elemente n�tig
	@Target
	private ReservationPresenter target;
	

	public ResourceDatesContract(ResourceDatesView view) {
		super(view);
	
				 if (preCondition()) {
//			         assert hour >= HOUR_MIN : "hour >= HOUR_MIN";
//			         assert hour <= HOUR_MAX : "hour <= HOUR_MAX";
//			         assert minute >= MINUTE_MIN : "minute >= MINUTE_MIN";
//			         assert minute <= MINUTE_MAX : "minute <= MINUTE_MAX";
//			         assert second >= SECOND_MIN : "second >= SECOND_MIN";
//			         assert second <= SECOND_MAX : "second <= SECOND_MAX";
			      }
			      if (postCondition()) {
//			         assert target.getHour() == hour : "hour set";
//			         assert target.getMinute() == minute : "minute set";
//			         assert target.getSecond() == second : "second set";
			      }}
			
	

	public void onGarbageButtonClicked() {
		if (preCondition()) {
			//Wenn Zugriff auf die GWT-Elemente bestehen w�rde, dann k�nnte man abfragen, ob der Termin in der Liste �berhaupt exisitiert,
			//der durch den Buttondruck gel�scht werden soll. 
		}
		if (postCondition()) {

		}
	}
	

	public void onWholeDaySelected() {
		if (preCondition()) {
			//Wenn Zugriff auf die GWT-Elemente bestehen w�rde, dann k�nnte man abfragen, ob vorher nicht ganzt�gig ausgew�hlt ist
			//
			
		}
		if (postCondition()) {
			
		}
	}
	public void onRewriteDateClicked() {
		if (preCondition()) {
			//Gab es den Termin vorher schon?
		}
		if (postCondition()) {
			//Termin wirklich in Liste �bernommen? 
			//Ist der Termin anders als vorher?
		}
	}
	
	public void onAddDateClicked() {
		if (preCondition()) {
			
		}
		if (postCondition()) {
			//weiterer Termin in der Liste enthalten
		}
	}
	
	public void onAddTerminButtonClicked() {
		if (preCondition()) {
			
		}
		if (postCondition()) {
			//neuer Termin hinzugegf�gt
		}
	}
	
	public void onRepeatTypeClicked() {
		if (preCondition()) {
			
		}
		if (postCondition()) {

		}
	}
	
	public void onFilterClicked() {
		if (preCondition()) {
			
		}
		if (postCondition()) {

		}
	}
	
	public void onSetResourcesToAll() {
		if (preCondition()) {
			//Ausgew�hlter Termin besitzt �berhaupt Ressourcen, die f�r die weiteren Termine �bernommen werden k�nnen.
		}
		if (postCondition()) {
			//alle Termine haben die gleichen Ressourcen
		}
	}
}