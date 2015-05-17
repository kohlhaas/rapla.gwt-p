package org.rapla.client.edit.reservation.sample;

import static org.mockito.Mockito.verify;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.jukito.TestSingleton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.internal.RaplaJDKLoggingAdapter;
import org.rapla.framework.internal.RaplaLocaleImpl;
import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;

@RunWith(JukitoRunner.class)
public class ReservationPresenterTest  {

  @Inject
  ReservationPresenter presenter;
  
  @Inject
  RaplaLocale raplaLocale;
  
  ClientFacade facade;
  
  @Before
  public void setupMocks(ClientFacade facade) {
      this.facade = facade;
  }

  public static class Module extends JukitoModule {
      protected void configureTest() {
          bind(org.rapla.framework.logger.Logger.class).toProvider(RaplaJDKLoggingAdapter.class);
          bind( RaplaLocale.class).to(RaplaLocaleImpl.class).in(Singleton.class);
          bindMock( ReservationView.class).in( TestSingleton.class);
      }
    }
  
  @Test
  public void editTest(Reservation event, ReservationView editView) throws RaplaException {
    boolean isNew = false;
    // WHEN
    presenter.edit(event, isNew);
    
    // THEN
    // test if presenter is called
    verify(editView).setPresenter(presenter);

    // test if event is shown
    verify(editView).show(event);
    
    isNew = true;
    //WHEN
    presenter.edit(event, isNew);
    
    // THEN
    // test if presenter is called
    verify(editView).setPresenter(presenter);

    // test if event is shown
    verify(editView,Mockito.times(2)).show(event);
  }
  
  @Test
  public void onSavedButtonTest(Reservation event) throws RaplaException {
    
	//BEFORE
	presenter.edit(event, true);
	  
    // WHEN
    presenter.onSaveButtonClicked();
    
    // THEN
    // test if store is called
    verify(facade).store(event);
  }
  
  @Test
  public void deleteButtonTest( Reservation event) throws RaplaException {
    boolean isNew = false;
    // WHEN
    presenter.edit(event, isNew);
    
    // WHEN
    presenter.onDeleteButtonClicked();
    
    // THEN
    // test if remove is called
    verify(facade).remove( event );
   
  }
  
  @Test
  public void cancelButtonTest(ReservationView editView){
	  //WHEN	  
	  presenter.onCancelButtonClicked();
	  
	  //THEN
	  //test if window is hidden
	  verify(editView).hide();
	 
  }
  
  @Test
  public void getEventTypesTest(Reservation event, ReservationView editView) throws RaplaException {
	  //WHEN	
	  presenter.getAllEventTypes();
	  
	  //THEN
	  //test if facade is called
	  verify(facade).getDynamicTypes("reservation");

  }
  
  @Test
  public void getCategoryTest(){
	  Locale locale = raplaLocale.getLocale();
	  //WHEN	
	  presenter.getCategory(locale, "Sprachen");
	  //THEN
	  //test if superCategory is get from facade
	  verify(facade).getSuperCategory();
	  //TODO: NullPointerException: Warum??
  }
  
  @Test
  public void changeAttributeTest(){
	  Locale locale = raplaLocale.getLocale();
	  //WHEN	
	  presenter.changeAttributesOfCLassification((Map<String, Object>) presenter.getAllCurrentAttributes(locale),locale);;
	  //THEN
	  
	  
  }
}