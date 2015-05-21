package org.rapla.client.edit.reservation;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rapla.client.edit.reservation.sample.InfoViewPresenter;
import org.rapla.client.edit.reservation.sample.ReservationEditSubView;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;
import org.rapla.client.edit.reservation.sample.ResourceDatesView;
import org.rapla.client.edit.reservation.sample.ResourceDatesViewPresenter;
import org.rapla.client.edit.reservation.sample.gwt.RaplaDate;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.internal.RaplaJDKLoggingAdapter;
import org.rapla.framework.internal.RaplaLocaleImpl;
import org.rapla.framework.logger.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
	  
	  @Test
		public void  ShouldGetView() {
			// TODO Auto-generated method stub
			
		}

	  @Test
		public void ShouldSetReservation(ResourceDatesView resourceView, Reservation event) {
		  
		  //when 
		  controller.getView();
		  

		}

		@Test
		public void ShouldClearInputFieldsOnGarbageButtonClicked(ResourceDatesView resourceView) {
			
			//when
			controller.onGarbageCanButtonClicked();
			//then
				verify(resourceView).clearInputFields();
		}

		@Test
		public void ShouldClosePanelOnWholeDaySelected(ResourceDatesView resourceView) {
			//when
			controller.onWholeDaySelected();
			//then
			verify(resourceView).setVisiblityOfDateElements();
		}
		

		@Test
		public void ShouldOpenPanelOnButtonPlusClicked(ResourceDatesView resourceView) {
			//when
			controller.onButtonPlusClicked();
	//then
			verify(resourceView).openEditView();
			
		

			
		}

		@Test
		public void ShouldOpenWidgetOnAddDateClicked(ResourceDatesView resourceView) {
			//when 
			controller.onAddDateClicked();
			//then
			verify(resourceView).addDateWidget();
			
		}

		@Test
		public void ShouldAddTerminOnAddTerminButtonClicked(ResourceDatesView resourceView, ClickEvent clickEvent) {
			//when
			controller.onAddTerminButtonClicked(clickEvent);
			RaplaDate tmp = (RaplaDate) clickEvent.getSource();
			//then
			verify(resourceView).setRaplaDate(tmp);
			
		}

		@Test
		public void ShouldOpenPanelOnRepeatTypeClicked(ResourceDatesView resourceView, ClickEvent clickEvent) {
			//when
			controller.onrepeatTypeClicked(clickEvent);
			Widget sender = (Widget) clickEvent.getSource();
//then
			verify(resourceView).setRepeatTypeSettings(sender);
			
		}

		@Test
		public void ShouldSetResourcesToAll(ResourceDatesView resourceView) {
			// TODO Auto-generated method stub
			//When 
			controller.onSetResourcesToAllClicked();
			//then
			verify(resourceView).setResourcesToAllDates();
			
		}

		@Test
		public void ShouldOpenFilter(ResourceDatesView resourceView) {
			
			// TODO Auto-generated method stub
			//when
			controller.onFilterClicked();
		//then
			verify(resourceView).setVisiblityOfFilter();
			
		}
		
		

		@Test
		public void ShouldOpenPanelOnErrorPanelButtonClick(ResourceDatesView resourceView, ClickEvent clickEvent) {
			// TODO Auto-generated method stub
			//when
			controller.onErrorPanelButtonClick(clickEvent);
			//then
			verify(resourceView).setErrorPanelButtonClickAction(clickEvent);
		}
}
