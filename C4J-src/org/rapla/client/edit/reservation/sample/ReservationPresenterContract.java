
package org.rapla.client.edit.reservation.sample;

import static de.vksi.c4j.Condition.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.rapla.entities.Category;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.framework.RaplaLocale;

import de.vksi.c4j.ClassInvariant;
import de.vksi.c4j.Target;

public class ReservationPresenterContract extends ReservationPresenter {

	@Inject
	RaplaLocale raplaLocale;
	
	@Target
	private ReservationPresenter target;

	@ClassInvariant
	public void classInvariant() {
		// TODO: write invariants if required
		
	}

	public ReservationPresenterContract(ReservationView view,
			AppointmentPresenter appointmentPresenter) {
		super(view, appointmentPresenter);
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public void edit(Reservation event, boolean isNew) {
		if (preCondition()) {
			// TODO: write preconditions if required 
			assert event != null : "event != null";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public void onSaveButtonClicked() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public String getCurrentReservationName(Locale locale) {
		if (preCondition()) {
			// TODO: write preconditions if required
			assert locale != null : "locale != null";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
			assert unchanged(target.getCurrentReservationName(locale));
			}
		return ignored();
	}

	@Override
	public void onDeleteButtonClicked() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public void onCancelButtonClicked() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public DynamicType[] getAllEventTypes() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
			assert unchanged(target.getAllEventTypes());
		}
		return ignored();
	}

	@Override
	public void changeReservationName(String newName) {
		if (preCondition()) {
			// TODO: write preconditions if required
			assert newName != null : "newName != null";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
			Locale locale = raplaLocale.getLocale();
			assert !unchanged(target.getCurrentReservationName(locale)):"Reservationname changed";
		}
	}

	@Override
	public void changeAttributesOfCLassification(
			Map<String, Object> attributeNames, Locale locale) {
		if (preCondition()) {
			// TODO: write preconditions if required
			assert attributeNames != null : "attributeNames != null";
			assert locale != null : "locale != null";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
			assert !unchanged(target.getAllCurrentAttributesAsStrings(locale));
		}
	}

	@Override
	public List<String> getAllCurrentAttributesAsStrings(Locale locale) {
		if (preCondition()) {
			// TODO: write preconditions if required
			assert locale != null : "locale != null";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
			assert unchanged(target.getAllCurrentAttributesAsStrings(locale));
		}
		return ignored();
	}

	@Override
	public String getEventType(Locale locale) {
		if (preCondition()) {
			// TODO: write preconditions if required
			assert locale != null : "locale != null";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
			assert unchanged(target.getEventType(locale));
		}
		return ignored();
	}

	@Override
	public boolean getIsNew() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
			assert unchanged(target.getIsNew());
		}
		return ignored();
	}

	@Override
	public boolean isDeleteButtonEnabled() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
			assert unchanged(target.isDeleteButtonEnabled());
		}
		return ignored();
	}

}

