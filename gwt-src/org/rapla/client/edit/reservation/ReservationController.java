package org.rapla.client.edit.reservation;

import org.rapla.entities.domain.Reservation;
import org.rapla.framework.RaplaException;

public interface ReservationController {
    public void edit(Reservation event, boolean isNew) throws RaplaException;
}
