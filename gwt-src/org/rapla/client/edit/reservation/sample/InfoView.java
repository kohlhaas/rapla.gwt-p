package org.rapla.client.edit.reservation.sample;

import java.util.List;

import org.rapla.client.base.View;
import org.rapla.entities.domain.Appointment;
import org.rapla.client.edit.reservation.sample.InfoView.Presenter;;

public interface InfoView<W> extends View<Presenter>, ReservationEditSubView<W> {
    public interface Presenter
    {
        void newAppButtonPressed();
        //add handler here 

		void onEventTypesChanged();

		void onStudiengangChanged();
    }

    void show(List<Appointment> asList);
    
    void update(List<Appointment> appointments);
    //add  methoden des alten infoViewInterface

	void setHeightAndWidth(int height, int width);
    
}
