package org.rapla.client.plugin.weekview.gwt;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.rapla.client.base.AbstractView;
import org.rapla.client.gwt.view.NavigatorView;
import org.rapla.client.gwt.view.NavigatorView.NavigatorAction;
import org.rapla.client.gwt.view.WeekviewGWT;
import org.rapla.client.gwt.view.WeekviewGWT.Callback;
import org.rapla.client.plugin.weekview.CalendarWeekView;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.HTMLDaySlot;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.RowSlot;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;
import org.rapla.plugin.abstractcalendar.server.HTMLRaplaBlock;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;

public class CalendarWeekViewImpl extends AbstractView<org.rapla.client.plugin.weekview.CalendarWeekView.Presenter> implements CalendarWeekView<IsWidget>,
        NavigatorAction, Callback
{

    private final WeekviewGWT calendar;
    private final NavigatorView navigatorView;
    private final ListBox calendars;
    Logger logger;

    @Inject
    public CalendarWeekViewImpl(Logger logger, RaplaLocale locale)
    {
        navigatorView = new NavigatorView("week", this, locale);
        calendar = new WeekviewGWT("week", logger, this);
        this.logger = logger;
        this.calendars = new ListBox();
        calendars.addChangeHandler(new ChangeHandler()
        {
            @Override
            public void onChange(ChangeEvent event)
            {
                getPresenter().changeCalendar(calendars.getSelectedValue());
            }
        });
    }

    @Override
    public IsWidget provideContent()
    {
        FlowPanel container = new FlowPanel();
        container.add(calendars);
        container.add(navigatorView);
        container.add(calendar);
        return container;
    }

    @Override
    public void update(final List<HTMLDaySlot> daylist, final List<RowSlot> timelist, final String weeknumber, final Date selectedDate,
            final List<String> calendarNames)
    {
        final String currentSelected = calendars.getSelectedValue();
        calendars.clear();
        for (String calendarName : calendarNames)
        {
            calendars.addItem(calendarName);
        }
        final int indexOf = calendarNames.indexOf(currentSelected);
        if (indexOf >= 0)
        {
            calendars.setSelectedIndex(indexOf);
        }
        else
        {
            calendars.setSelectedIndex(0);
        }
        navigatorView.setDate(selectedDate);
        calendar.update(daylist, timelist, weeknumber);
    }

    @Override
    public void selectedDate(Date selectedDate)
    {
        getPresenter().selectDate(selectedDate);
    }

    @Override
    public void next()
    {
        getPresenter().next();
    }

    @Override
    public void previous()
    {
        getPresenter().previous();
    }

    @Override
    public void updateReservation(HTMLRaplaBlock block, HTMLDaySlot daySlot, Integer rowSlot)
    {
        try
        {
            getPresenter().updateReservation(block, daySlot, rowSlot);
        }
        catch (RaplaException e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void selectReservation(HTMLRaplaBlock block)
    {
        getPresenter().selectReservation(block);
    }
}
