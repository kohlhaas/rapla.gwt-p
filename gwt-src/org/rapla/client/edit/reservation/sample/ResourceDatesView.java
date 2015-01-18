package org.rapla.client.edit.reservation.sample;

import java.util.List;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.sample.ResourceDatesView.Presenter;
import org.rapla.entities.domain.Appointment;

public interface ResourceDatesView<W>  extends View<Presenter>, ReservationEditSubView<W> {
    public interface Presenter
    {
        void newAppButtonPressed();
        //add handler here 

		void onGarbageCanButtonClicked();

		void onWholeDaySelected();

		void onButtonPlusClicked();

		void onRewriteDateClicked();

		void onAddDateClicked();


    }


    void hide();
    
    void update(List<Appointment> appointments);
    //add  methoden des alten infoViewInterface

	void setHeightAndWidth(int height, int width);
	void show();

	void addDateWidget();

	void RewriteDate();
    
}

