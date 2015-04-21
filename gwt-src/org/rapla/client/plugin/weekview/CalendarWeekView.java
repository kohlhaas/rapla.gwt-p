package org.rapla.client.plugin.weekview;

import java.util.Date;
import java.util.List;

import org.rapla.client.base.View;
import org.rapla.client.plugin.weekview.CalendarWeekView.Presenter;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.HTMLDaySlot;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.RowSlot;
import org.rapla.entities.domain.Reservation;

public interface CalendarWeekView<W> extends View<Presenter>
{

    public interface Presenter
    {

        void selectReservation(Reservation selectedObject);

        void selectDate(Date newDate);

        void next();

        void previous();
    }

    W provideContent();

    void update(List<HTMLDaySlot> daylist, List<RowSlot> timelist, String weeknumber);

}
