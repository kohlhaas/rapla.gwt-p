package org.rapla.client.edit.reservation;

import java.lang.annotation.*;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.sample.InfoViewPresenter;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ResourceDatesViewPresenter;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;

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
	@Inject
	ClientFacade facade;

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
			//Bei Zugriff auf die tatsächlichen Termine in der Datenbank, könnte zusätzlich abgefragt werden, ob
			//die Veranstaltung (bei Hinzufügen eines neuen Termins) vorher noch nicht vorhanden ist
			//oder (bei Änderung eines Termins) der Termin bereits vorhanden war
			//Weitere Precondition: Hat Veranstaltung mindestens einen Termin?

			
		}
		if (postCondition()) {
			assert !unchanged(reservation);
			//Zusätzlich: Gab es eine Änderung in der Datenbank?
			
		}
	}

	@Override
	public void onDeleteButtonClicked() {
		if (preCondition()) {
			assert reservation != null;
			//Zusätzlich bei vollem Zugriff: Gibt es die Veranstaltung in der Datenbank?
		}
		if (postCondition()) {
			//Veranstaltung existiert im Backend nicht mehr
		}
	}

	@Override
	public void onCancelButtonClicked() {
		if (preCondition()) {
			assert reservation != null;

		}
		if (postCondition()) {
			assert unchanged(reservation); //nichts passiert

		}
	}

	@Override
	public void onTabChanged(int selectedTab) {
		if (preCondition()) {
			
			assert selectedTab <= 0;
			assert selectedTab >= 1;
		//	assert target.saveTemporaryChanges();
			
		}
		if (postCondition()) {
			//alle Daten wie vorher vorhanden
		}
	}

	@Override
	public boolean isDeleteButtonEnabled() {
		if (preCondition()) {
			assert reservation != null;
			//ist deleteButton vorher disabled

		}
		if (postCondition()) {

		}
		return ignored();
	}


	public void loadDataFromReservationIntoView() {
		if (preCondition()) {
			assert reservation != null;
			
		}
		if (postCondition()) {
			//alle Daten vorhanden?
		}
	}
	
}

