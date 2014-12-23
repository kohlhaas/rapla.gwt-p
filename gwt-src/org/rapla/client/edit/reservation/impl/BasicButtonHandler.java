package org.rapla.client.edit.reservation.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

import org.rapla.facade.RaplaComponent;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.client.edit.reservation.GWTReservationController;



public class BasicButtonHandler extends RaplaComponent implements ClickHandler {

	 Logger logger = Logger.getLogger("BasicButtonHandler");
	 ReservationController controller;
	
	  public @Inject BasicButtonHandler (RaplaContext context, ReservationController controller)
	    {
	        super(context);
	        this.controller = controller;
	    }
	
	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		  Object source = event.getSource();

          if (source instanceof Button) {  //check that the source is really a button
              String buttonText = ((Button) source).getText();  //cast the source to a button
              switch (buttonText) {
              case "speichern":  
            	  Classification classification = controller.reservationTmp.getClassification();
                  Attribute first = classification.getType().getAttributes()[0];
                  String text = controller.reservationName.getValue();
                  classification.setValue(first, text);
                  try {
                      ClientFacade facade = getClientFacade();
                      facade.store(controller.reservationTmp);
                  } catch (RaplaException e1) {
                      //TODO add exception handling
                      logger.log(Level.SEVERE, e1.getMessage(), e1);
                  }
                  controller.popupContent.hide();
                  
                       break;
                       
              case "abbrechen":  
            	  controller.popupContent.hide();
                  break;         
                  
              case "l\u00F6schen":  
            	  try {
                      ClientFacade facade = getClientFacade();
                      facade.remove(controller.reservationTmp);
                  } catch (RaplaException e1) {
                      //TODO add exception handling
                      logger.log(Level.SEVERE, e1.getMessage(), e1);
                  }
                  controller.popupContent.hide();
                  break;         
              default:
            	  //this should not happen
            	  
              }
              
          } else {
             // Not a Button, can't be...
          }
	}

}
