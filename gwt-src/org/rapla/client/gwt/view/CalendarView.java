package org.rapla.client.gwt.view;

import java.util.Collection;
import java.util.List;

import org.rapla.client.gwt.util.ElementWrapper;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

public class CalendarView extends FlexTable
{
    public CalendarView(String tableStylePrefix)
    {
        super();
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
     * Simple class replacing the informations transfered in the <code>DataTransfer</code> object whenever this object has no setData method.<br/>
     * This object should not be used whenever possible.<br/>
     * At the moment this object is only used by the IE.
     */
    private static final class OriginSupport
    {
        int row;
        int colum;
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
        // Drag and Drop support
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
                final Element element = this.getCellFormatter().getElement(row, column);
                final ElementWrapper elementWrapper = new ElementWrapper(element);
                elementWrapper.onAttach();
                elementWrapper.addDomHandler(new DragStartHandler()
                {
                    @Override
                    public void onDragStart(final DragStartEvent event)
                    {
                        final DataTransfer dataTransfer = event.getDataTransfer();
                        try
                        {
                            dataTransfer.setData("row", row + "");
                            dataTransfer.setData("column", column + "");
                        }
                        catch (final Exception e)
                        {
                            originSupport.row = row;
                            originSupport.colum = column;
                        }
                    }
                }, DragStartEvent.getType());
                elementWrapper.addDomHandler(new DragOverHandler()
                {
                    @Override
                    public void onDragOver(final DragOverEvent event)
                    {
                        element.getStyle().setBackgroundColor("#ffa");
                    }
                }, DragOverEvent.getType());
                elementWrapper.addDomHandler(new DragLeaveHandler()
                {
                    @Override
                    public void onDragLeave(final DragLeaveEvent event)
                    {
                        element.getStyle().clearBackgroundColor();
                    }
                }, DragLeaveEvent.getType());
                elementWrapper.addDomHandler(new DropHandler()
                {
                    @Override
                    public void onDrop(final DropEvent event)
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
                        catch (final Exception e)
                        {
                            sourceColumn = originSupport.colum;
                            sourceRow = originSupport.row;
                        }
                        // only do something whenever the place has been changed
                        if (sourceRow != row || sourceColumn != column)
                        {
                            final Widget widget = CalendarView.this.getWidget(sourceRow, sourceColumn);
                            final Event source = (Event) widget;
                            // TODO: call controller to update event
                            CalendarView.this.setWidget(row, column, source);
                        }
                    }
                }, DropEvent.getType());
            }
        }
    }

    private void createEvents(final List<HTMLDaySlot> daylist, final boolean[][] spanCells, final FlexCellFormatter flexCellFormatter)
    {
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
                        final int rowCount = htmlBlock.getRowCount();
                        final int maxRowCount = Math.min(rowCount, spanCells.length - blockRow);
                        for (int i = 1; i < maxRowCount; i++)
                        {
                            spanCells[blockRow + i][column] = true;
                        }
                        flexCellFormatter.setRowSpan(blockRow, blockColumn, maxRowCount);
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
