package org.rapla.client.edit.reservation.impl;

import org.rapla.entities.domain.Reservation;



public interface ReservationEditView {

  public interface Presenter {
    void onSaveButtonClicked();
    void onDeleteButtonClicked();
    void changeEventName(String newEvent);
    void newAppButtonPressed();
    boolean isDeleteButtonEnabled();
    void onCancelButtonClicked();
  }

  void show(Reservation event);
  void hide();

  void setPresenter(Presenter presenter);
//  void setColumnDefinitions(List<ColumnDefinition<T>> columnDefinitions);
  //void setRowData(List<T> rowData);
}
