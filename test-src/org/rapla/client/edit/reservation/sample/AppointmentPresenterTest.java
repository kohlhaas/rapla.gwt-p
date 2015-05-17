package org.rapla.client.edit.reservation.sample;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.internal.RaplaJDKLoggingAdapter;
import org.rapla.framework.internal.RaplaLocaleImpl;

@RunWith(JukitoRunner.class)
public class AppointmentPresenterTest extends AbstractView<Presenter> {

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
  

  public static class Module extends JukitoModule {
      protected void configureTest() {
          bind(org.rapla.framework.logger.Logger.class).toProvider(RaplaJDKLoggingAdapter.class);
          bind( RaplaLocale.class).to(RaplaLocaleImpl.class).in(Singleton.class);
      }
    }
  
  @Test
  public void setReservationTest() throws RaplaException {
    AppointmentView editView = presenter.getView();
    
    // WHEN
    presenter.setReservation(event);
    
    // THEN
    // test if presenter is called
    verify(editView).setPresenter(presenter);

    // test if appointment show is called
    verify(editView).show(event);
    
  }
  
  @Test
  public void newAppointmentTest() throws RaplaException{
  AppointmentView editView = presenter.getView();
	
  	// BEFORE
  	presenter.setReservation(event);
    // WHEN 
    //presenter.newAppointmentButtonPressed(); TODO: add app test, had to change it
    
    // THEN
    // test if newAppointment is called in facade
    Date startDate = new Date(facade.today().getTime() + DateTools.MILLISECONDS_PER_HOUR * 8);
    Date endDate = new Date(startDate.getTime() + DateTools.MILLISECONDS_PER_HOUR);
    Appointment appointment = verify(facade).newAppointment(startDate,endDate);
    
    // test if appointment list is updated
    verify(editView).updateAppointmentList(Arrays.asList(event.getAppointments()), Arrays.asList(event.getAppointments()).size()-1);
    
    // test if appointment is added to event
    verify(event).addAppointment(appointment);
    
 }
  
  @Test
  public void getConflictsTest () throws RaplaException{
  AppointmentView editView = presenter.getView();
    
  // WHEN
    presenter.getConflicts();
    
    // THEN
    // test if getConflicts is called in facade
    verify(facade).getConflicts(presenter.getReservation());
    
 }
  
  @Test
  public void nextFreeDateTest () throws RaplaException{
  AppointmentView editView = presenter.getView();
  
  Date startDate = new Date(facade.today().getTime() + DateTools.MILLISECONDS_PER_HOUR * 8);
  Date endDate = new Date(startDate.getTime() + DateTools.MILLISECONDS_PER_HOUR);
  
    // WHEN
    presenter.nextFreeDateButtonPressed(startDate, endDate);
    
    // THEN
    // test if newAppointment is called in facade
    verify(facade).newAppointment(startDate,endDate);
    
    //test if getAllocatables is called in facade
    verify(facade).getAllocatables();        
  }
  
  @Test
  public void appointmentSelectedTest() throws RaplaException{
	  AppointmentView editView = presenter.getView();
	  
	// BEFORE
	presenter.setReservation(event);  
	// presenter.newAppointmentButtonPressed(); TODO: see above
	
	// WHEN
	presenter.appointmentSelected(0);
	    
	// THEN
	// test if getAppointments is called 
	verify(event,Mockito.times(2)).getAppointments(); 
	// test if view is updated
	verify(editView).updateAppointmentOptionsPanel(event.getAppointments()[0]);
			
	    
	   //TODO:  
	    // test if view is updated with the right Appointment
	    //verify(editView).updateAppointmentOptionsPanel(appArray[0]);
  }
  
  @Test
  public void removeAppointmentTest() throws RaplaException{
	  AppointmentView editView = presenter.getView();
	  
	  //BEFORE
	  presenter.setReservation(event);
	 // presenter.newAppointmentButtonPressed();
	  
	  // WHEN
	  presenter.removeAppointmentButtonPressed(0);
	  
	  // THEN
	  // test if getAppointments is called
	  Appointment[] AppointmentList = verify(event,Mockito.times(2)).getAppointments(); 
	  // TODO: test if appointment is removed
	  verify(event).removeAppointment(event.getAppointments()[0]);
  
  }
  
  @Test
  public void addResourceTest() throws RaplaException{
	  AppointmentView editView = presenter.getView();
	  
	  //BEFORE
	  presenter.setReservation(event);
	 // presenter.newAppointmentButtonPressed();
	  
	  // WHEN
	  //TODO: Wie wird das aufgerufen?
	  presenter.addResourceButtonPressed(1, "Kurs", Locale.GERMAN);
	  
	  // THEN
	  // test if 
  }
  
  @Test
  public void getAllocatablesTest() throws RaplaException{
	  AppointmentView editView = presenter.getView();
	  	  
	  // WHEN
	  presenter.getAllocatables();
	  
	  // THEN
	  // test if facade is called
	  verify(facade).getAllocatables();
  }
  
  
}
