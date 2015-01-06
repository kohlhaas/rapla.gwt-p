package org.rapla.mwi14_2.edit.reservation.controller;

import org.rapla.mwi14_2.edit.reservation.view.*;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import de.vksi.c4j.ContractReference;

import java.util.logging.Logger;

/**
 * Created by nam on 10.12.14.
 */
//have to implement org.rapla.client.edit.reservation.ReservationController --> in Application, if this class wants to be the entry point

@ContractReference(EditControllerContract.class)
public class EditController extends RaplaComponent  {


  final Logger logger = Logger.getLogger("EditController");

  //@Inject
  RaplaEditView raplaEditView;

  public EditController(RaplaContext context) {
    super(context);
    raplaEditView = new AppointmentsEditView();
  }

  public void edit(Reservation event, boolean isNew) throws RaplaException {
    logger.info("building the view");
    raplaEditView.init();
    raplaEditView.setTitle("Termine");
    logger.info("view successfully created");

  }
}
