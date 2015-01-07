package org.rapla.mwi14_2.edit;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.sample.SampleReservationView.Presenter;
import org.rapla.entities.domain.Reservation;

public interface GeneralInformationView<W> extends View<Presenter> {

  public interface Presenter {
    void onSaveButtonClicked();
    void onDeleteButtonClicked();
    void changeEventName(String newEvent);
    boolean isDeleteButtonEnabled();
    void onCancelButtonClicked();
  }

  void show(Reservation event);
  void hide();
  //void addSubView(ReservationEditSubView<W> view);
}
