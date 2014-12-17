package org.rapla.client.edit.reservation.controller;

import org.rapla.client.edit.reservation.GWTReservationController;
import org.rapla.client.edit.reservation.view.RaplaEditView;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

import java.util.logging.Logger;

import javax.inject.Inject;

/**
 * Created by nam on 10.12.14.
 */
public class EditController extends RaplaComponent implements GWTReservationController {


  final Logger logger = Logger.getLogger("EditController");

  @Inject
  RaplaEditView raplaEditView;

  public EditController(RaplaContext context) {
    super(context);
  }

  @Override
  public void edit(Reservation event, boolean isNew) throws RaplaException {
    logger.info("building the view");
    raplaEditView.init();
    logger.info("view successfully created");

  }
}
