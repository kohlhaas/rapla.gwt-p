package org.rapla.client.edit.reservation.sample;

import static de.vksi.c4j.Condition.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.RepeatingType;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.facade.Conflict;

import de.vksi.c4j.*;

public class AppointmentPresenterContract extends AppointmentPresenter {

	@Target
	private AppointmentPresenter target;
	
	@ClassInvariant
	public void classInvariant() {
		// TODO: write invariants if required
	}

	public AppointmentPresenterContract(AppointmentView view) {
		super(view);
		if (preCondition()) {
			// TODO: write preconditions if required
			assert view != null : "view not null" ;
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public void newAppointmentButtonPressed(Date startDate, Date endDate, RepeatingType type) {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public Conflict[] getConflicts() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
			
		}
		return ignored();
	}

	@Override
	public Date[] nextFreeDateButtonPressed(Date startDate, Date endDate) {
		if (preCondition()) {
			assert startDate != null : "startDate != null";
			assert endDate != null : "endDate != null";
			assert endDate.after(startDate) :"endDate after startDate";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
		return ignored();
	}

	@Override
	public void setReservation(Reservation reservation) {
		if (preCondition()) {
			assert reservation != null : "Reservation != null";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
			assert target.getReservation() == reservation : "getReservation = reservation";
		}
	}

	@Override
	public AppointmentView getView() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
		return ignored();
	}

	@Override
	public void appointmentSelected(int selectedIndex) {
		if (preCondition()) {
			// TODO: write preconditions if required
			assert selectedIndex >=0 : "selectedIndex >=0";
			assert selectedIndex <= target.getReservation().getAppointments().length : "selectedIndex <= Appointments[]";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public void removeAppointmentButtonPressed(int selectedIndex) {
		if (preCondition()) {
			// TODO: write preconditions if required
			assert selectedIndex >=0 : "selectedIndex >=0";
			assert selectedIndex <= target.getReservation().getAppointments().length : "selectedIndex <= Appointments[]";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public void addResourceButtonPressed(int selectedIndex,
			String resourceTypeName, Locale locale) {
		if (preCondition()) {
			// TODO: write preconditions if required
			assert selectedIndex >=0 : "selectedIndex >=0";
			assert resourceTypeName != null : "resourceTypeName != null";
			assert locale != null : "locale != null";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public Map<DynamicType, List<Allocatable>> getSortedAllocatables() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
		return ignored();
	}

	@Override
	public Allocatable[] getAllocatables() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
		return ignored();
	}

	@Override
	public DynamicType[] getEventTypes() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
		return ignored();
	}

	@Override
	public DynamicType[] getResourceTypes() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
		return ignored();
	}

	@Override
	public Reservation getReservation() {
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
		return ignored();
	}

}
