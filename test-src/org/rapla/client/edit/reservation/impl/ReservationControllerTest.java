package org.rapla.client.edit.reservation.impl;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.mockito.Mockito;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.internal.AppointmentImpl;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import java.util.Date;

import static org.mockito.Mockito.when;

public class ReservationControllerTest extends TestCase {


  ReservationController reservationController;
  ClientFacade facade;

  public void setUp() {
    RaplaContext mockedContext = Mockito.mock(RaplaContext.class);
    facade = Mockito.mock(ClientFacade.class);

    reservationController = new ReservationController(mockedContext);

  }

  public void testCreateAppointment() throws RaplaException {

    Date startDate = new Date();
    Date endDate = new Date(startDate.getTime() + DateTools.MILLISECONDS_PER_HOUR);
    when(facade.newAppointment(startDate, endDate)).thenReturn(new AppointmentImpl(startDate, endDate));
    reservationController.setFacade(facade);

    reservationController.createAppointment(startDate, endDate);
    Appointment appointment = reservationController.getAppointment();
    Assert.assertEquals(startDate, appointment.getStart());
    Assert.assertEquals(endDate, appointment.getEnd());


  }


}