package org.rapla.mwi14_2.edit.reservation.controller;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.mwi14_2.edit.reservation.view.*;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import java.util.logging.Logger;

/**
 * Created by nam on 10.12.14.
 */
public class EditController extends RaplaComponent implements ReservationController {


  final Logger logger = Logger.getLogger("EditController");

  //@Inject
  RaplaEditView raplaEditView;

  public EditController(RaplaContext context) {
    super(context);
    raplaEditView = new AppointmentsEditView();
  }

  @Override
  public void edit(Reservation event, boolean isNew) throws RaplaException {
    logger.info("building the view");
    raplaEditView.init();
    raplaEditView.setTitle("Termine");
    logger.info("view successfully created");

  }
}
