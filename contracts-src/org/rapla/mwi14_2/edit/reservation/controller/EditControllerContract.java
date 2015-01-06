package org.rapla.mwi14_2.edit.reservation.controller;

import static de.vksi.c4j.Condition.postCondition;
import static de.vksi.c4j.Condition.preCondition;

import org.rapla.entities.domain.Reservation;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import de.vksi.c4j.ClassInvariant;
import de.vksi.c4j.Target;

public class EditControllerContract extends EditController {

	@Target
	private EditController target;

	@ClassInvariant
	public void classInvariant() {
		// TODO: write invariants if required
	}

	public EditControllerContract(RaplaContext context) {
		super(context);
		if (preCondition()) {
			// TODO: write preconditions if required
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

	@Override
	public void edit(Reservation event, boolean isNew) throws RaplaException {
		if (preCondition()) {
			assert event!=null : "Event != null";
		}
		if (postCondition()) {
			// TODO: write postconditions if required
		}
	}

}
