package org.rapla.client.edit.reservation;

import javax.inject.Singleton;

import org.jukito.JukitoModule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rapla.client.Application;
import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.InfoView;
import org.rapla.client.edit.reservation.sample.InfoViewPresenter;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.internal.RaplaJDKLoggingAdapter;
import org.rapla.framework.internal.RaplaLocaleImpl;
import org.rapla.framework.logger.Logger;

@RunWith(JukitoRunner.class)

public class ReservationPresenterTest {

	
	  @Inject ReservationPresenter controller;
	  @Inject InfoViewPresenter infoViewPresenter;
	  
	  @Inject ClientFacade facade;
	  Reservation event;
	  
	  @Before
	  public void setupMocks(ClientFacade facade, Reservation event) {
	      this.facade = facade;
	      this.event = event;
	  }
	  
	  public static class Module extends JukitoModule {
		    protected void configureTest() {
		        bind(org.rapla.framework.logger.Logger.class).toProvider((Class<? extends Provider<? extends Logger>>) RaplaJDKLoggingAdapter.class);
		        bind( RaplaLocale.class).to(RaplaLocaleImpl.class).in(Singleton.class);
		    }
	  }
	  
	  @Test
	  public void shouldStoreReservationOnSave(Reservation event,ReservationView editView) throws RaplaException {
	    boolean isNew = false;
	 // WHEN
	    // WHEN
	    controller.edit(event, isNew);
	    
	    // THEN
	    // test if presenter is called
	    verify(editView).setPresenter(controller);

	    // test if event is shown
	    verify(editView).show(event);
	    controller.onSaveButtonClicked();
	    
	    // THEN
	    // test if store is called
	    verify(facade).store( event );
	  }
	  
	  
	@Test 
	  public void shouldDeleteReservationOnDelete(Reservation event,ReservationView editView) throws RaplaException {
	    boolean isNew = false;
	 // WHEN
	    // WHEN
	    controller.edit(event, isNew);
	    
	    // THEN
	    // test if presenter is called
	    verify(editView).setPresenter(controller);

	    verify(editView).show(event);
	    controller.onDeleteButtonClicked();
	    
	    // THEN
	    // test if store is called
	    verify(facade).remove( event );
	  }

	  
	@Test 
	  public void shouldCancelReservationCreation(Reservation event,ReservationView editView) throws RaplaException {
	    boolean isNew = false;
	    // WHEN
	    controller.edit(event, isNew);
	    
	    // THEN
	    // test if presenter is called
	    verify(editView).setPresenter(controller);

	    
	    controller.onCancelButtonClicked();
	    
	    // THEN
	    verify(editView).hide();
	  }
	  
	@Test 
	  public void shouldSaveTemporaryChanges(Reservation event,ReservationView editView, InfoView infoView) throws RaplaException, ParseException {
		    // WHEN
		//	editView.setPresenter(controller);
	//	   controller.onTabChanged(1);
		    // THEN
		   controller.saveTemporaryChanges();
		   
	  }
	  
	@Test 
	  public void shouldloadDataFromReservationToView(Reservation event,ReservationView editView,String input, Integer index, List<String> personsString, InfoView infoView) throws RaplaException {
		
		controller.loadPersonsIntoView(editView.getCurrentSubView());
	//	verify(infoView).getItemTextOfListBoxStudiengang(index);
	//	verify(infoView).setSelectedIndexOfListBoxStudiengang(index);
	//	verify(infoView).setVorlesungsStundenInput(input);
		
	  }
	
	@Test 
	  public void shouldloadPersonsIntoView(Reservation event,ReservationView editView, List<String> personsString, InfoView infoView) throws RaplaException {
		
		controller.loadPersonsIntoView(editView.getCurrentSubView());
		//verify(infoView).setContentOfListBoxStudiengang(personsString);
	    
	  }
	  
	@Test 
	  public void shouldChangeTab(Reservation event,ReservationView editView, InfoView infoView) throws RaplaException {
	    boolean isNew = false;
	 // WHEN
	    // WHEN
	    controller.edit(event, isNew);
	    verify(editView).setPresenter(controller);

	   // verify(infoView).setPresenter(infoViewPresenter);
	
		
	 //   verify(editView).setCurrentSubView(infoViewPresenter.getView());
	  }
	
	@Test 
	  public void shouldEnableDeleteButton(Reservation event,ReservationView editView, boolean isNew) throws RaplaException {
		assert isNew = false;
	    
	  }
	
	
	  
	  
	  
	
	
	
	
}
