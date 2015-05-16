package org.rapla.client.edit.reservation.sample;

import java.util.List;

import org.rapla.client.base.View;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.client.edit.reservation.sample.InfoView.Presenter;

;

public interface InfoView<W> extends View<Presenter>, ReservationEditSubView<W> {
	public interface Presenter {
		void newAppButtonPressed();

		// add handler here

		void onEventTypesChanged();

		void onStudiengangChanged();
	}

	void hide();

	void update(List<Appointment> appointments);

	void createContent();
	void show();

	String getTitelInput();

	String getVorlesungsStundenInput();

	String getSelectedEventType();

	void setTitelInput(String reservationName);

	void setSelectedEventType(String select);

	void setVorlesungsStundenInput(String input);

	int getItemCountOfListBoxStudiengang();

	String getItemTextOfListBoxStudiengang(int index);

	void setSelectedIndexOfListBoxStudiengang(int index);

	void setContentOfListBoxStudiengang(List<String> content);

	void setEventTypes(List<String> eventTypes);

	void setDynamicFields(Attribute[] attributes);


}
