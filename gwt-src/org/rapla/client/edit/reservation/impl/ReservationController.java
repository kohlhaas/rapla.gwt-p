package org.rapla.client.edit.reservation.impl;

import org.rapla.client.edit.reservation.GWTReservationController;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nam on 10.12.14.
 */
public class ReservationController extends RaplaComponent implements GWTReservationController {


  private ClientFacade facade = super.getClientFacade();
  ;
  final Logger logger = Logger.getLogger("ReserverationController");
  private Appointment appointment;


  public ReservationController(RaplaContext context) {
    super(context);
  }

  @Override
  public void edit(Reservation event, boolean isNew) throws RaplaException {

  }

  public void createReservation(Date startDate, Date endDate, String title) {

  }

  protected void createAppointment(Date startDate, Date endDate) {

    try {
      appointment = facade.newAppointment(startDate, endDate);
    } catch (RaplaException e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
    }


  }

  public Appointment getAppointment() {
    return this.appointment;
  }

  public void setFacade(ClientFacade facade) {
    this.facade = facade;
  }
}
