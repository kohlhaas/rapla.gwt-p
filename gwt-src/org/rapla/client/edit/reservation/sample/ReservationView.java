package org.rapla.client.edit.reservation.sample;

import java.util.Collection;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.User;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.DynamicType;

public interface ReservationView<W> extends View<Presenter> {

	public interface Presenter {
		void onSaveButtonClicked();

		void onDeleteButtonClicked();

		boolean isDeleteButtonEnabled();

		void onCancelButtonClicked();
		
		void changeAttribute(Attribute attribute, Object newValue);

        Collection<DynamicType> getChangeableReservationDynamicTypes();

        void changeClassification(Reservation reservation, DynamicType newDynamicType);
	}

	void show(Reservation event, User user);

	void hide();

    void showWarning(String string, String string2);
}
