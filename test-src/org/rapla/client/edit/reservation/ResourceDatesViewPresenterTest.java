package org.rapla.client.edit.reservation;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.rapla.client.edit.reservation.sample.InfoViewPresenter;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;
import org.rapla.client.edit.reservation.sample.ResourceDatesViewPresenter;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.internal.RaplaJDKLoggingAdapter;
import org.rapla.framework.internal.RaplaLocaleImpl;
import org.rapla.framework.logger.Logger;


@RunWith(JukitoRunner.class)
public class ResourceDatesViewPresenterTest {

	

	  @Inject ResourceDatesViewPresenter controller;
	  
	  ClientFacade facade;
	  Reservation event;
	  
	  @Before
	  public void setupMocks(ClientFacade facade, Reservation event) {
	      this.facade = facade;
	  }
	  
	  public static class Module extends JukitoModule {
		    protected void configureTest() {
		        bind(org.rapla.framework.logger.Logger.class).toProvider((Class<? extends Provider<? extends Logger>>) RaplaJDKLoggingAdapter.class);
		        bind( RaplaLocale.class).to(RaplaLocaleImpl.class).in(Singleton.class);
		    }
	  }
}
