package org.rapla.client.edit.reservation.sample;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.Category;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.DynamicType;

import java.util.List;
import java.util.Locale;

public interface ReservationView<W> extends View<Presenter> {

  public interface Presenter {
    void onSaveButtonClicked();
    void onDeleteButtonClicked();
    void changeReservationName(String newEvent);
    boolean isDeleteButtonEnabled();
    void onCancelButtonClicked();
    DynamicType[] getAllEventTypes();
    Category[] getCategory(Locale locale, String searchCategory);
    String getCurrentReservationName(Locale locale);
    String getEventType(Locale locale);
    void changeAttributes(Attribute[] attributes);
  }

  void show(Reservation event);
  void hide();
  void addSubView(String tabName,ReservationEditSubView<W> view);
}
