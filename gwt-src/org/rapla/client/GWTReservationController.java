package org.rapla.client;

import org.rapla.entities.domain.Reservation;
import org.rapla.framework.RaplaException;

public interface GWTReservationController {
    public void edit(Reservation event, boolean isNew) throws RaplaException;
}
