package org.rapla.client.plugin.weekview.gwt;

import java.util.Collection;

import org.rapla.client.base.AbstractView;
import org.rapla.client.plugin.view.Event;
import org.rapla.client.plugin.view.Event.ResolveDrop;
import org.rapla.client.plugin.weekview.CalendarWeekView;
import org.rapla.entities.domain.Reservation;

import com.google.gwt.dom.client.DataTransfer;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.user.client.impl.DOMImpl;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;

public class CalendarWeekViewImpl extends AbstractView<org.rapla.client.plugin.weekview.CalendarWeekView.Presenter> implements CalendarWeekView<IsWidget>,
        ResolveDrop
{

    final FlexTable grid;

    public CalendarWeekViewImpl()
    {
        grid = new FlexTable();
        grid.setStyleName("week table");
    }

    private void setupTable()
    {
        String[] days = new String[] { "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag" };
        grid.setText(0, 0, "");
        for (int i = 0; i < days.length; i++)
        {
            grid.setText(0, i + 1, days[i]);
        }
        // hour starts at 8
        for (int hour = 0; hour < 10; hour++)
        {
            grid.setText(hour+1, 0, (hour +8)+ "Uhr");
        }
        for(int i = 1; i < days.length; i++){
            for(int j = 1; j < 10; j++) {
                grid.setWidget(j, i, new FlowPanel());
            }
        }
    }

    @Override
    public void update(Collection<Reservation> result)
    {
        grid.clear();
        setupTable();
        final Event flowPanel = new Event(this);
        final HTML html = new HTML("Test");
        flowPanel.add(html);
        grid.setWidget(3, 3, flowPanel);
//        grid.getFlexCellFormatter().setRowSpan(3, 3, 2);
    }

    @Override
    public IsWidget provideContent()
    {
        return grid;
    }

    @Override
    public void dropFinished(DragEndEvent event, Event source)
    {
        final NativeEvent nativeElement = event.getNativeEvent();
        // problem if browser is made smaller
        final int absoluteLeft = nativeElement.getClientX();
        final int absoluteTop = nativeElement.getClientY();
        // calculate drop area
        final int gridAbsoluteLeft = grid.getElement().getOffsetLeft();
        final int gridAbsoluteTop = grid.getElement().getOffsetTop();
        final int gridOffsetWidth = grid.getElement().getClientWidth();
        final int gridOffsetHeight = grid.getElement().getClientHeight();
        // 
        // 7 days + 1 empty (left top)
        final int daysDisplayed = 7 + 1;
        // 10 hours + 1 empty (left top)
        final int hoursDisplayed = 10 + 1;
        // calculate relative from table origin
        final int left = absoluteLeft - gridAbsoluteLeft;
        final int top = absoluteTop - gridAbsoluteTop;
        if (left < 0 || top < 0 || left > gridOffsetWidth || top > gridOffsetHeight)
        {
            return;
        }
        // calc cell
        final int gridElementLeft = (left / (gridOffsetWidth / daysDisplayed));
        final int gridElementTop = (top / (gridOffsetHeight / hoursDisplayed));
        grid.setWidget(gridElementTop, gridElementLeft, source);
        event.stopPropagation();

    }
}
