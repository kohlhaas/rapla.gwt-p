package org.rapla.client.edit.reservation.sample;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.Category;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.DynamicType;

import java.util.List;
import java.util.Locale;

public interface ReservationView<W> extends View<Presenter> {

  public interface Presenter {
    void onSaveButtonClicked();
    void onDeleteButtonClicked();
    void changeEventName(String newEvent);
    boolean isDeleteButtonEnabled();
    void onCancelButtonClicked();
    DynamicType[] getAllEventTypes();
    Category[] getCategory(Locale locale, String courses);
  }

  void show(Reservation event);
  void hide();
  void addSubView(String name,ReservationEditSubView<W> view);
}
