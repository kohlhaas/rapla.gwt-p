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
import org.mockito.Mockito;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.internal.RaplaJDKLoggingAdapter;
import org.rapla.framework.internal.RaplaLocaleImpl;

@RunWith(JukitoRunner.class)
public class AppointmentPresenterTest {

  @Inject
  AppointmentPresenter presenter;
  
  ClientFacade facade;
  Reservation event;
  @Before
  public void setupMocks(ClientFacade facade, Reservation event) {
      this.facade = facade;
      when( facade.today()).thenReturn(DateTools.cutDate( new Date()));
      this.event = event;
      when(event.getAppointments()).thenReturn(new Appointment[1]);
      
  }
  
//  @Before
//  public void setupReservation(){
//	  try {
//		event = facade.newReservation();
//	} catch (RaplaException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//  }

  public static class Module extends JukitoModule {
      protected void configureTest() {
          bind(org.rapla.framework.logger.Logger.class).toProvider(RaplaJDKLoggingAdapter.class);
          bind( RaplaLocale.class).to(RaplaLocaleImpl.class).in(Singleton.class);
      }
    }
  
  @Test
  public void shouldCallShowOnEdit() throws RaplaException {
    AppointmentView editView = presenter.getView();
    
    // WHEN
    presenter.setReservation(event);
    
    // THEN
    // test if presenter is called
    verify(editView).setPresenter(presenter);

    // test if appointment show is called
    verify(editView).show(event);
    
    // WHEN 
    presenter.newAppointmentButtonPressed();
    
    // THEN
    // test if newAppointment is called in facade
    Date startDate = new Date(facade.today().getTime() + DateTools.MILLISECONDS_PER_HOUR * 8);
    Date endDate = new Date(startDate.getTime() + DateTools.MILLISECONDS_PER_HOUR);
    Appointment newAppointment = verify(facade).newAppointment(startDate,endDate);
    
    // test if appointment list is updated
    verify(editView).updateAppointmentList(Arrays.asList(event.getAppointments()), Arrays.asList(event.getAppointments()).size()-1);
    
    // test if appointment is added to event
    verify(event).addAppointment(newAppointment);
    
    // WHEN
    presenter.getConflicts();
    
    // THEN
    // test if getConflicts is called in facade
    verify(facade).getConflicts(presenter.getReservation());
    
    // WHEN
    presenter.nextFreeDateButtonPressed(startDate, endDate);
    
    // THEN
    // test if newAppointment is called in facade
    verify(facade, Mockito.times(2)).newAppointment(startDate,endDate);
    
    //test if getAllocatables is called in facade
    verify(facade).getAllocatables();
    
    // TODO: add test for return value in view
    
    // WHEN
    presenter.appointmentSelected(0);
    
    // THEN
    // test if getAppointments is called
    Appointment[] appArray = verify(event, Mockito.times(4)).getAppointments();
    
    
    // test if view is updated with the right Appointment
    //verify(editView).updateAppointmentOptionsPanel(appArray[0]);
    
  }
  
}
