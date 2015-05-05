package org.rapla.client.edit.reservation.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public class BasicButtonHandler implements ClickHandler {

	Logger logger = Logger.getLogger("BasicButtonHandler");
	ReservationController controller;
	ClientFacade facade;
	public @Inject BasicButtonHandler(ClientFacade facade,
			ReservationController controller) {
		this.facade = facade;
		this.controller = controller;
	}

	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		Object source = event.getSource();

		if (source instanceof Button) { // check that the source is really a
										// button
			String buttonText = ((Button) source).getText(); // cast the source
																// to a button
			switch (buttonText) {
			case "speichern":
				
				try {
					controller.saveTemporaryChanges();
				} catch (RaplaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Classification classification = controller.reservationTmp
						.getClassification();
				Attribute first = classification.getType().getAttributes()[0];
				String text = controller.reservationName;
				classification.setValue(first, text);
				try {
					facade.store(controller.reservationTmp);
				} catch (RaplaException e1) {
					// TODO add exception handling
					logger.log(Level.SEVERE, e1.getMessage(), e1);
				}
				controller.popupContent.hide();

				break;

			case "abbrechen":
				controller.popupContent.hide();
				break;

			case "l\u00F6schen":
				if (!controller.isNew()) {
					try {
						facade.remove(controller.reservationTmp);
					} catch (RaplaException e1) {
						// TODO add exception handling
						logger.log(Level.SEVERE, e1.getMessage(), e1);
					}
				}
				controller.popupContent.hide();
				break;
			default:
				// this should not happen

			}

		} else {
			// Not a Button, can't be...
		}
	}

}
