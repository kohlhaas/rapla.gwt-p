package org.rapla.client.plugin.weekview.gwt;

import java.util.Collection;

import org.rapla.client.base.AbstractView;
import org.rapla.client.gwt.util.ElementWrapper;
import org.rapla.client.gwt.view.Event;
import org.rapla.client.plugin.weekview.CalendarWeekView;
import org.rapla.entities.domain.Reservation;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class CalendarWeekViewImpl extends AbstractView<org.rapla.client.plugin.weekview.CalendarWeekView.Presenter> implements CalendarWeekView<IsWidget>
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
            grid.setText(hour + 1, 0, (hour + 8) + "Uhr");
        }
        for (int i = 1; i < days.length; i++)
        {
            final int column = i;
            for (int j = 1; j < 10; j++)
            {
                final int row = j;
                grid.setText(row, column, "");
                final Element element = grid.getCellFormatter().getElement(row, column);
                final ElementWrapper elementWrapper = new ElementWrapper(element);
                elementWrapper.onAttach();
                elementWrapper.addDomHandler(new DragStartHandler()
                {
                    @Override
                    public void onDragStart(DragStartEvent event)
                    {
                        event.getDataTransfer().setData("row", row + "");
                        event.getDataTransfer().setData("column", column + "");
                    }
                }, DragStartEvent.getType());
                elementWrapper.addDomHandler(new DragOverHandler()
                {
                    @Override
                    public void onDragOver(DragOverEvent event)
                    {
                        element.getStyle().setBackgroundColor("#ffa");
                    }
                }, DragOverEvent.getType());
                elementWrapper.addDomHandler(new DragLeaveHandler()
                {
                    @Override
                    public void onDragLeave(DragLeaveEvent event)
                    {
                        element.getStyle().clearBackgroundColor();
                    }
                }, DragLeaveEvent.getType());
                elementWrapper.addDomHandler(new DropHandler()
                {
                    @Override
                    public void onDrop(DropEvent event)
                    {
                        element.getStyle().clearBackgroundColor();
                        final String rowString = event.getDataTransfer().getData("row");
                        final String columnString = event.getDataTransfer().getData("column");
                        final int sourceRow = Integer.parseInt(rowString);
                        final int sourceColumn = Integer.parseInt(columnString);
                        // only do something whenever the place has been changed
                        if (sourceRow != row || sourceColumn != column)
                        {
                            final Widget widget = grid.getWidget(sourceRow, sourceColumn);
                            Event source = (Event) widget;
                            // TODO: call controller to update event
                            grid.setWidget(row, column, source);
                        }
                    }
                }, DropEvent.getType());
            }
        }
    }

    @Override
    public void update(Collection<Reservation> result)
    {
        grid.clear();
        setupTable();
        {
            final Event flowPanel = new Event();
            final HTML html = new HTML("Test");
            flowPanel.add(html);
            grid.setWidget(3, 3, flowPanel);
        }
        {
            final Event flowPanel = new Event();
            final HTML html = new HTML("Test2");
            flowPanel.add(html);
            grid.setWidget(4, 3, flowPanel);
        }
        //        grid.getFlexCellFormatter().setRowSpan(3, 3, 2);
    }

    @Override
    public IsWidget provideContent()
    {
        return grid;
    }
}
