package org.rapla.client.edit.reservation.impl;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Appointment;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaContext;

import java.util.Date;

import javax.inject.Inject;

public class ReservationControllerTest extends TestCase {


  ReservationController reservationController;

  @Inject
  RaplaContext context;

  ClientFacade facade;

  @Before
  public void setUp() {
    reservationController = new ReservationController(context);
  }

  @Test
  public void createAppointmentTest() {

    Date startDate = facade.today();
    Date endDate = new Date(startDate.getTime() + DateTools.MILLISECONDS_PER_HOUR);
    Appointment appointment = reservationController.createAppointment(startDate, endDate);
    Assert.assertEquals(startDate, appointment.getStart());
    Assert.assertEquals(endDate, appointment.getEnd());


  }


}