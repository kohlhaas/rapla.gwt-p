package org.rapla.client.plugin.weekview;

import java.util.Collection;

import org.rapla.client.base.View;
import org.rapla.client.plugin.weekview.CalendarWeekView.Presenter;
import org.rapla.entities.domain.Reservation;

public interface CalendarWeekView<W> extends View<Presenter> {

    public interface Presenter {

        void selectReservation(Reservation selectedObject);
    }

    void update(Collection<Reservation> result);

    W provideContent();

}
