package org.rapla.client.edit.reservation.impl;

import org.rapla.client.edit.reservation.GWTReservationController;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import java.util.Date;

/**
 * Created by nam on 10.12.14.
 */
public class ReservationController extends RaplaComponent implements GWTReservationController{


  public ReservationController(RaplaContext context) {
    super(context);
  }

  @Override
  public void edit(Reservation event, boolean isNew) throws RaplaException {

  }

  public void createReservation(Date startDate, Date endDate, String title){

  }

  protected Appointment createAppointment(Date startDate, Date endDate){
     return null;
  }

}
