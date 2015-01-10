package org.rapla.client.edit.reservation.sample;

import static org.mockito.Mockito.verify;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.jukito.TestSingleton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.internal.RaplaJDKLoggingAdapter;
import org.rapla.framework.internal.RaplaLocaleImpl;

@RunWith(JukitoRunner.class)
public class ReservationPresenterTest {

  @Inject
  ReservationPresenter controller;
  
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
  public void shouldCallShowOnEdit(Reservation event,ReservationView editView) throws RaplaException {
    boolean isNew = false;
    // WHEN
    controller.edit(event, isNew);
    
    // THEN
    // test if presenter is called
    verify(editView).setPresenter(controller);

    // test if event is shown
    verify(editView).show(event);
    
    // WHEN
    controller.onSaveButtonClicked();
    
    // THEN
    // test if store is called
    verify(facade).store( event );
  }
  
  @Test
  public void testDelete( Reservation event) throws RaplaException {
    boolean isNew = false;
    // WHEN
    controller.edit(event, isNew);
    
    // WHEN
    controller.onDeleteButtonClicked();
    
    // THEN
    // test if remove is called
    verify(facade).remove( event );
    
  }
}