package org.rapla.client.plugin.weekview.gwt;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.rapla.client.base.AbstractView;
import org.rapla.client.gwt.view.CalendarView;
import org.rapla.client.gwt.view.NavigatorView;
import org.rapla.client.gwt.view.NavigatorView.NavigatorAction;
import org.rapla.client.plugin.weekview.CalendarWeekView;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.HTMLDaySlot;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.RowSlot;
import org.rapla.framework.logger.Logger;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;

public class CalendarWeekViewImpl extends AbstractView<org.rapla.client.plugin.weekview.CalendarWeekView.Presenter> implements CalendarWeekView<IsWidget>,
        NavigatorAction
{

    private final CalendarView calendar;
    private final NavigatorView navigatorView;

    @Inject
    public CalendarWeekViewImpl(Logger logger)
    {
        navigatorView = new NavigatorView("week", this);
        calendar = new CalendarView("week", logger);
    }

    @Override
    public IsWidget provideContent()
    {
        FlowPanel container = new FlowPanel();
        container.add(navigatorView);
        container.add(calendar);
        return container;
    }

    @Override
    public void update(final List<HTMLDaySlot> daylist, final List<RowSlot> timelist, final String weeknumber)
    {
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
}
