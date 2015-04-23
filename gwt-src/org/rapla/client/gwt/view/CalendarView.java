package org.rapla.client.gwt.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.HTMLDaySlot;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.RowSlot;
import org.rapla.client.plugin.weekview.CalendarWeekViewPresenter.HTMLWeekViewPresenter.Slot;
import org.rapla.components.calendarview.Block;
import org.rapla.framework.logger.Logger;
import org.rapla.plugin.abstractcalendar.server.HTMLRaplaBlock;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class CalendarView extends FlexTable
{

    private final Map<Element, Event> events = new HashMap<Element, Event>();
    private final Logger logger;

    public CalendarView(String tableStylePrefix, Logger logger)
    {
        super();
        this.logger = logger;
        setStyleName(tableStylePrefix);
        addStyleName("table");
    }

    public void update(final List<HTMLDaySlot> daylist, final List<RowSlot> timelist, final String weeknumber)
    {
        this.clear();
        this.removeAllRows();
        setupTable(daylist, timelist, weeknumber);
    }

    private void setupTable(final List<HTMLDaySlot> daylist, final List<RowSlot> timelist, final String weeknumber)
    {
        final FlexCellFormatter flexCellFormatter = this.getFlexCellFormatter();
        this.setText(0, 0, weeknumber);
        final int actualColumnCount = createXAchsis(daylist, flexCellFormatter);
        final int actualRowCount = createYAchsis(timelist, flexCellFormatter);
        final boolean[][] spanCells = new boolean[actualRowCount][actualColumnCount];
        initSpanCells(daylist, timelist, spanCells);
        createEvents(daylist, spanCells, flexCellFormatter);
        createDragAndDropSupport(spanCells, actualColumnCount, actualRowCount);
    }

    /**
     */
    private static final class OriginSupport
    {
        private Event event;
    }

    /**
     * Creates the needed drag and drop listener within the table.
     * 
     * @param spanCells the information which cells have a span (so are not within the table)
     * @param columnCount the column count 
     * @param rowCount the row count
     */
    private void createDragAndDropSupport(final boolean[][] spanCells, final int columnCount, final int rowCount)
    {
        final OriginSupport originSupport = new OriginSupport();
        for (int j = 1; j < rowCount; j++)
        {
            final int row = j;
            for (int i = 1; i < columnCount; i++)
            {
                if (spanCells[row][i])
                {
                    continue;
                }
                final int column = calcColumn(spanCells, row, i);
                if (!this.isCellPresent(row, column))
                {
                    this.setText(row, column, " ");
                }
            }
        }
        // Drag and Drop support
        addDomHandler(new DragEnterHandler()
        {
            
            @Override
            public void onDragEnter(DragEnterEvent event)
            {
                com.google.gwt.user.client.Event event2 = (com.google.gwt.user.client.Event) event.getNativeEvent();
                final Element tc = CalendarView.this.getEventTargetCell(event2);
                tc.getStyle().setBackgroundColor("#ffa");
            }
        }, DragEnterEvent.getType());
        addDomHandler(new DragOverHandler()
        {
            @Override
            public void onDragOver(DragOverEvent event)
            {
            }
        }, DragOverEvent.getType());
        addDomHandler(new DragLeaveHandler()
        {
            @Override
            public void onDragLeave(DragLeaveEvent event)
            {
                com.google.gwt.user.client.Event event2 = (com.google.gwt.user.client.Event) event.getNativeEvent();
                final Element tc = CalendarView.this.getEventTargetCell(event2);
                tc.getStyle().clearBackgroundColor();
            }
        }, DragLeaveEvent.getType());
        addDomHandler(new DragStartHandler()
        {
            @Override
            public void onDragStart(final DragStartEvent event)
            {
                com.google.gwt.user.client.Event event2 = (com.google.gwt.user.client.Event) event.getNativeEvent();
                final Element tc = CalendarView.this.getEventTargetCell(event2);
                final Event myEvent = events.get(tc.getFirstChildElement());
                originSupport.event = myEvent;
                try
                {// enable if needed
                    event.setData("dragging", "start");
                }
                catch (Exception e)
                {
                }
            }
        }, DragStartEvent.getType());
        addDomHandler(new DropHandler()
        {
            @Override
            public void onDrop(final DropEvent event)
            {
                com.google.gwt.user.client.Event event2 = (com.google.gwt.user.client.Event) event.getNativeEvent();
                final Element targetCell = CalendarView.this.getEventTargetCell(event2);
                targetCell.getStyle().clearBackgroundColor();
                final TableCellElement td = TableCellElement.as(targetCell);
                final Element tr = td.getParentElement();
                final int column = DOM.getChildIndex(tr, td);
                final Element tbody = tr.getParentElement();
                final int row = DOM.getChildIndex(tbody, tr);
                setWidget(row, column, originSupport.event);
            }
        }, DropEvent.getType());
    }

    private void createEvents(final List<HTMLDaySlot> daylist, final boolean[][] spanCells, final FlexCellFormatter flexCellFormatter)
    {
        events.clear();
        // create events
        int column = 1;
        for (final HTMLDaySlot daySlot : daylist)
        {
            if (daySlot.isEmpty())
            {
                column++;
            }
            else
            {
                for (final Slot slot : daySlot)
                {
                    final Collection<Block> blocks = slot.getBlocks();
                    for (final Block block : blocks)
                    {
                        final HTMLRaplaBlock htmlBlock = (HTMLRaplaBlock) block;
                        final int blockRow = htmlBlock.getRow();
                        final int blockColumn = calcColumn(spanCells, blockRow, column);
                        final Event event = new Event(htmlBlock);
                        this.setWidget(blockRow, blockColumn, event);
                        events.put(event.getElement(), event);
                        final int rowCount = htmlBlock.getRowCount();
                        for (int i = 1; i < rowCount; i++)
                        {
                            spanCells[blockRow + i][column] = true;
                        }
                        flexCellFormatter.setRowSpan(blockRow, blockColumn, rowCount);
                    }
                    column++;
                }
            }
        }
    }

    private void initSpanCells(final List<HTMLDaySlot> daylist, final List<RowSlot> timelist, final boolean[][] spanCells)
    {
        int header = 0;
        spanCells[0][header] = false;// left top corner
        for (final HTMLDaySlot daySlot : daylist)
        {
            header++;
            // headername is never one
            spanCells[0][header] = false;
            final int slotCount = Math.max(1, daySlot.size());
            for (int i = 1; i < slotCount; i++)
            {// span is one
                header++;
                spanCells[0][header] = true;
            }
        }
        int timeentry = 0;
        for (final RowSlot rowSlot : timelist)
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

    private int createYAchsis(final List<RowSlot> timelist, final FlexCellFormatter flexCellFormatter)
    {
        int actualRowCount = 1;
        for (final RowSlot timeEntry : timelist)
        {
            final String rowname = timeEntry.getRowname();
            this.setText(actualRowCount, 0, rowname);
            final int rowspan = timeEntry.getRowspan();
            flexCellFormatter.setRowSpan(actualRowCount, 0, rowspan);
            flexCellFormatter.setAlignment(actualRowCount, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_TOP);
            actualRowCount += rowspan;
        }
        return actualRowCount;
    }

    private int createXAchsis(final List<HTMLDaySlot> daylist, final FlexCellFormatter flexCellFormatter)
    {
        int actualColumnCount = 1;
        for (int i = 0; i < daylist.size(); i++)
        {
            final HTMLDaySlot htmlDaySlot = daylist.get(i);
            final int slotCount = Math.max(1, htmlDaySlot.size());
            final int column = i + 1;
            this.setText(0, column, htmlDaySlot.getHeader());
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

}
