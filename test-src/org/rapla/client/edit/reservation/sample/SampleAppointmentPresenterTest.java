package org.rapla.client.edit.reservation.sample;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.internal.RaplaJDKLoggingAdapter;
import org.rapla.framework.internal.RaplaLocaleImpl;

@RunWith(JukitoRunner.class)
public class SampleAppointmentPresenterTest {

  @Inject SampleAppointmentPresenter controller;
  
  ClientFacade facade;
  Reservation event;
  @Before
  public void setupMocks(ClientFacade facade, Reservation event) {
      this.facade = facade;
      when( facade.today()).thenReturn(DateTools.cutDate( new Date()));
      this.event = event;
      when(event.getAppointments()).thenReturn(new Appointment[1]);
  }

  public static class Module extends JukitoModule {
      protected void configureTest() {
          bind(org.rapla.framework.logger.Logger.class).toProvider(RaplaJDKLoggingAdapter.class);
          bind( RaplaLocale.class).to(RaplaLocaleImpl.class).in(Singleton.class);
      }
    }
  
  @Test
  public void shouldCallShowOnEdit() throws RaplaException {
    boolean isNew = false;
    SampleAppointmentView editView = controller.getView();
    
    // WHEN
    controller.setReservation(event);
    
    // THEN
    // test if presenter is called
    verify(editView).setPresenter(controller);

    // test if appointment show is called
    verify(editView).show(Arrays.asList(event.getAppointments()));
    
    // WHEN
    controller.newAppButtonPressed();
    
    // THEN
    // test if newAppointment is called
    Date startDate = new Date(facade.today().getTime() + DateTools.MILLISECONDS_PER_HOUR * 8);
    Date endDate = new Date(startDate.getTime() + DateTools.MILLISECONDS_PER_HOUR);
    verify(facade).newAppointment(startDate, endDate);
  }
  
}