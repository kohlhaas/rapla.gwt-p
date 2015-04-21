package org.rapla.client.plugin.weekview.gwt;

import java.util.List;

import org.rapla.client.base.AbstractView;
import org.rapla.client.gwt.util.ElementWrapper;
import org.rapla.client.gwt.view.Event;
import org.rapla.client.plugin.weekview.CalendarWeekView;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.HTMLDaySlot;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.RowSlot;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.Slot;
import org.rapla.components.calendarview.Block;
import org.rapla.plugin.abstractcalendar.server.HTMLRaplaBlock;

import com.google.gwt.dom.client.DataTransfer;
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
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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

    private void setupTable(List<HTMLDaySlot> daylist, List<RowSlot> timelist, String weeknumber)
    {
        FlexCellFormatter flexCellFormatter = grid.getFlexCellFormatter();
        grid.setText(0, 0, weeknumber);
        int actualColumnCount = createXAchsis(daylist, flexCellFormatter);
        int actualRowCount = createYAchsis(timelist, flexCellFormatter);
        final boolean[][] spanCells = new boolean[actualRowCount][actualColumnCount];
        initSpanCells(daylist, timelist, spanCells);
        createEvents(daylist, spanCells, flexCellFormatter);
        createDragAndDropSupport(spanCells, actualColumnCount, actualRowCount);
    }

    private static final class OriginSupport
    {
        int row;
        int colum;
    }

    protected void createDragAndDropSupport(final boolean[][] spanCells, int actualColumnCount, int actualRowCount)
    {
        final OriginSupport originSupport = new OriginSupport();
        // Drag and Drop support
        for (int j = 1; j < actualRowCount; j++)
        {
            final int row = j;
            for (int i = 1; i < actualColumnCount; i++)
            {
                if (spanCells[row][i])
                {
                    continue;
                }
                final int column = calcColumn(spanCells, row, i);
                if (!grid.isCellPresent(row, column))
                {
                    grid.setText(row, column, " ");
                }
                final Element element = grid.getCellFormatter().getElement(row, column);
                final ElementWrapper elementWrapper = new ElementWrapper(element);
                elementWrapper.onAttach();
                elementWrapper.addDomHandler(new DragStartHandler()
                {
                    @Override
                    public void onDragStart(DragStartEvent event)
                    {
                        final DataTransfer dataTransfer = event.getDataTransfer();
                        try
                        {
                            dataTransfer.setData("row", row + "");
                            dataTransfer.setData("column", column + "");
                        }
                        catch (Exception e)
                        {
                            originSupport.row = row;
                            originSupport.colum = column;
                        }
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
                        int sourceRow;
                        int sourceColumn;
                        final DataTransfer dataTransfer = event.getDataTransfer();
                        try
                        {
                            final String rowString = dataTransfer.getData("row");
                            final String columnString = dataTransfer.getData("column");
                            sourceRow = Integer.parseInt(rowString);
                            sourceColumn = Integer.parseInt(columnString);
                        }
                        catch (Exception e)
                        {
                            sourceColumn = originSupport.colum;
                            sourceRow = originSupport.row;
                        }
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

    protected void createEvents(List<HTMLDaySlot> daylist, final boolean[][] spanCells, FlexCellFormatter flexCellFormatter)
    {
        // create events
        int column = 1;
        for (int daySlotNumber = 0; daySlotNumber < daylist.size(); daySlotNumber++)
        {
            HTMLDaySlot daySlot = daylist.get(daySlotNumber);
            if (daySlot.isEmpty())
            {
                column++;
            }
            else
            {
                for (int slotNumber = 0; slotNumber < daySlot.size(); slotNumber++)
                {
                    final Slot slot = daySlot.get(slotNumber);
                    final int lastEnd = slot.getLastEnd();
                    for (int slotMinute = 0; slotMinute < lastEnd; slotMinute++)
                    {
                        final Block block = slot.getBlock(slotMinute);
                        if (block != null && block instanceof HTMLRaplaBlock)
                        {
                            final HTMLRaplaBlock htmlBlock = (HTMLRaplaBlock) block;
                            final int blockRow = htmlBlock.getRow();
                            final int blockColumn = calcColumn(spanCells, blockRow, column);
                            final Event event = new Event(htmlBlock);
                            grid.setWidget(blockRow, blockColumn, event);
                            final int rowCount = htmlBlock.getRowCount();
                            for (int i = 1; i < rowCount; i++)
                            {
                                spanCells[blockRow + i][column] = true;
                            }
                            flexCellFormatter.setRowSpan(blockRow, blockColumn, rowCount);
                        }
                    }
                    column++;
                }
            }
        }
    }

    protected void initSpanCells(List<HTMLDaySlot> daylist, List<RowSlot> timelist, final boolean[][] spanCells)
    {
        int header = 0;
        spanCells[0][header] = false;// left top corner
        for (final HTMLDaySlot daySlot : daylist)
        {
            header++;
            // headername is never one
            spanCells[0][header] = false;
            int slotCount = Math.max(1, daySlot.size());
            for (int i = 1; i < slotCount; i++)
            {// span is one
                header++;
                spanCells[0][header] = true;
            }
        }
        int timeentry = 0;
        for (RowSlot rowSlot : timelist)
        {
            timeentry++;
            spanCells[timeentry][0] = false;
            final int rowspan = rowSlot.getRowspan();
            for (int i = 1; i < rowspan; i++)
            {
                timeentry++;
                spanCells[timeentry][0] = true;
            }
        }
    }

    protected int createYAchsis(List<RowSlot> timelist, FlexCellFormatter flexCellFormatter)
    {
        int actualRowCount = 1;
        for (RowSlot timeEntry : timelist)
        {
            String rowname = timeEntry.getRowname();
            grid.setText(actualRowCount, 0, rowname);
            int rowspan = timeEntry.getRowspan();
            flexCellFormatter.setRowSpan(actualRowCount, 0, rowspan);
            flexCellFormatter.setAlignment(actualRowCount, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_TOP);
            actualRowCount += rowspan;
        }
        return actualRowCount;
    }

    protected int createXAchsis(List<HTMLDaySlot> daylist, FlexCellFormatter flexCellFormatter)
    {
        int actualColumnCount = 1;
        for (int i = 0; i < daylist.size(); i++)
        {
            HTMLDaySlot htmlDaySlot = daylist.get(i);
            int slotCount = Math.max(1, htmlDaySlot.size());
            final int column = i + 1;
            grid.setText(0, column, htmlDaySlot.getHeader());
            flexCellFormatter.setColSpan(0, column, slotCount);
            flexCellFormatter.setAlignment(0, column, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_TOP);
            actualColumnCount += slotCount;
        }
        return actualColumnCount;
    }

    private int calcColumn(final boolean[][] spanCells, final int row, final int column)
    {
        int numSpanCellsToRemove = 0;
        final boolean[] columns = spanCells[row];
        for (int i = 0; i < column; i++)
        {
            if (columns[i])
            {
                numSpanCellsToRemove++;
            }
        }
        return column - numSpanCellsToRemove;
    }

    @Override
    public IsWidget provideContent()
    {
        return grid;
    }

    @Override
    public void update(List<HTMLDaySlot> daylist, List<RowSlot> timelist, String weeknumber)
    {
        grid.clear();
        grid.removeAllRows();
        setupTable(daylist, timelist, weeknumber);
    }
}
