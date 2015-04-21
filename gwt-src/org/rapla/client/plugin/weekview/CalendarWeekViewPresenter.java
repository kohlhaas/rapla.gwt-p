package org.rapla.client.plugin.weekview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Named;

import org.rapla.client.base.CalendarPlugin;
import org.rapla.client.event.DetailSelectEvent;
import org.rapla.client.plugin.weekview.CalendarWeekView.Presenter;
import org.rapla.components.calendarview.Block;
import org.rapla.components.calendarview.Builder;
import org.rapla.components.calendarview.Builder.PreperationResult;
import org.rapla.components.calendarview.html.AbstractHTMLView;
import org.rapla.components.util.DateTools;
import org.rapla.components.xmlbundle.I18nBundle;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.CalendarOptions;
import org.rapla.facade.CalendarSelectionModel;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;
import org.rapla.plugin.abstractcalendar.GroupAllocatablesStrategy;
import org.rapla.plugin.abstractcalendar.RaplaBuilder;
import org.rapla.plugin.abstractcalendar.server.HTMLRaplaBlock;
import org.rapla.plugin.abstractcalendar.server.HTMLRaplaBuilder;

import com.google.web.bindery.event.shared.EventBus;

public class CalendarWeekViewPresenter<W> implements Presenter, CalendarPlugin
{

    private CalendarWeekView<W> view;
    @Inject
    private Logger logger;
    @Inject
    private EventBus eventBus;
    @Inject
    private CalendarSelectionModel model;

    @Inject
    ClientFacade facade;

    @Inject
    HTMLRaplaBuilder builderProvider;

    @Inject
    private RaplaLocale raplaLocale;

    @Inject
    private @Named(RaplaComponent.RaplaResourcesId) I18nBundle i18n;

    @Inject
    public CalendarWeekViewPresenter(CalendarWeekView view)
    {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public String getName()
    {
        return "week";
    }

    @Override
    public W provideContent()
    {
        return view.provideContent();
    }

    @Override
    public void selectReservation(Reservation selectedObject)
    {
        DetailSelectEvent event2 = new DetailSelectEvent(selectedObject);
        eventBus.fireEvent(event2);
        logger.info("selection changed");

    }

    @Override
    public void updateContent() throws RaplaException
    {
        HTMLWeekViewPresenter weekView = new HTMLWeekViewPresenter(view);
        configure(weekView);
        Date startDate = weekView.getStartDate();
        Date endDate = weekView.getEndDate();
        model.setStartDate(startDate);
        model.setEndDate(endDate);

        String weeknumber = i18n.format("calendarweek.abbreviation", startDate);
        weekView.setWeeknumber(weeknumber);
        RaplaBuilder builder = builderProvider;
        builder.setNonFilteredEventsVisible(false);
        builder.setFromModel(model, startDate, endDate);
        GroupAllocatablesStrategy strategy = new GroupAllocatablesStrategy(raplaLocale.getLocale());
        boolean compactColumns = getCalendarOptions().isCompactColumns() || builder.getAllocatables().size() == 0;
        //compactColumns = false;
        strategy.setFixedSlotsEnabled(!compactColumns);
        strategy.setResolveConflictsEnabled(true);
        builder.setBuildStrategy(strategy);
        weekView.rebuild(builder);
        //String calendarviewHTML = weekview.getHtml();
        //this.view.update(calendarviewHTML);
    }

    private void configure(HTMLWeekViewPresenter weekView) throws RaplaException
    {
        CalendarOptions opt = getCalendarOptions();
        weekView.setRowsPerHour(opt.getRowsPerHour());
        weekView.setWorktimeMinutes(opt.getWorktimeStartMinutes(), opt.getWorktimeEndMinutes());
        weekView.setFirstWeekday(opt.getFirstDayOfWeek());
        int days = getDays(opt);
        weekView.setDaysInView(days);
        Set<Integer> excludeDays = opt.getExcludeDays();
        if (days < 3)
        {
            excludeDays = new HashSet<Integer>();
        }
        weekView.setExcludeDays(excludeDays);
        weekView.setLocale(raplaLocale);
        weekView.setToDate(model.getSelectedDate());
    }

    private CalendarOptions getCalendarOptions() throws RaplaException
    {
        return RaplaComponent.getCalendarOptions(facade.getUser(), facade);
    }

    //    protected HTMLWeekView createCalendarView() {
    //        HTMLWeekView weekView = new HTMLWeekView()
    //        {
    //            public void rebuild() {
    //                super.rebuild();
    //            }
    //        };
    //        return weekView;
    //    }

    /** overide this for daily views*/
    protected int getDays(CalendarOptions calendarOptions)
    {
        return calendarOptions.getDaysInWeekview();
    }

    public int getIncrementSize()
    {
        return Calendar.WEEK_OF_YEAR;
    }

    static public class HTMLWeekViewPresenter extends AbstractHTMLView
    {
        private CalendarWeekView view;

        private int endMinutes;
        private int minMinute;
        private int maxMinute;
        private int startMinutes;
        int m_rowsPerHour = 2;
        HTMLDaySlot[] daySlots;
        ArrayList<Block> blocks = new ArrayList<Block>();
        String weeknumber;

        public HTMLWeekViewPresenter(CalendarWeekView view)
        {
            this.view = view;
        }

        /** The granularity of the selection rows.
         * <ul>
         * <li>1:  1 rows per hour =   1 Hour</li>
         * <li>2:  2 rows per hour = 1/2 Hour</li>
         * <li>3:  3 rows per hour = 20 Minutes</li>
         * <li>4:  4 rows per hour = 15 Minutes</li>
         * <li>6:  6 rows per hour = 10 Minutes</li>
         * <li>12: 12 rows per hour =  5 Minutes</li>
         * </ul>
         * Default is 2.
         */
        public void setRowsPerHour(int rows)
        {
            m_rowsPerHour = rows;
        }

        public void setWeeknumber(String weeknumber)
        {
            this.weeknumber = weeknumber;
        }

        public int getRowsPerHour()
        {
            return m_rowsPerHour;
        }

        public void setWorktime(int startHour, int endHour)
        {
            this.startMinutes = startHour * 60;
            this.endMinutes = endHour * 60;
        }

        public void setWorktimeMinutes(int startMinutes, int endMinutes)
        {
            this.startMinutes = startMinutes;
            this.endMinutes = endMinutes;
        }

        public void setToDate(Date weekDate)
        {
            calcMinMaxDates(weekDate);
        }

        public Collection<Block> getBlocks()
        {
            return blocks;
        }

        /** must be called after the slots are filled*/
        protected boolean isEmpty(int column)
        {
            return daySlots[column].isEmpty();
        }

        protected int getColumnCount()
        {
            return getDaysInView();
        }

        public void rebuild(Builder b)
        {
            int columns = getColumnCount();
            blocks.clear();
            daySlots = new HTMLDaySlot[columns];

            String[] headerNames = new String[columns];

            for (int i = 0; i < columns; i++)
            {
                String headerName = createColumnHeader(i);
                headerNames[i] = headerName;
            }

            // calculate the blocks
            int start = startMinutes;
            int end = endMinutes;
            minuteBlock.clear();
            PreperationResult prepareBuild = b.prepareBuild(getStartDate(), getEndDate());
            start = Math.min(prepareBuild.getMinMinutes(), start);
            end = Math.max(prepareBuild.getMaxMinutes(), end);
            if (start < 0)
                throw new IllegalStateException("builder.getMin() is smaller than 0");
            if (end > 24 * 60)
                throw new IllegalStateException("builder.getMax() is greater than 24");
            
            int minMinute = start;
            int maxMinute = end;
            for (int i = 0; i < daySlots.length; i++)
            {
                daySlots[i] = new HTMLDaySlot(2, headerNames[i]);
            }

            b.build(this, prepareBuild.getBlocks());
            boolean useAM_PM = getRaplaLocale().isAmPmFormat();
            for (int minuteOfDay = minMinute; minuteOfDay < maxMinute; minuteOfDay++)
            {
                boolean isLine = (minuteOfDay) % (60 / m_rowsPerHour) == 0;
                if (isLine || minuteOfDay == minMinute)
                {
                    minuteBlock.add(minuteOfDay);
                }
            }

            List<HTMLDaySlot> daylist = new ArrayList<>();
            for (int i = 0; i < daySlots.length; i++)
            {
                if (isExcluded(i))
                    continue;
                daylist.add(daySlots[i]);
            }
            List<RowSlot> timelist = new ArrayList<>();

            int row = 0;
            for (Integer minuteOfDay : minuteBlock)
            {
                row++;
                if (minuteBlock.last().equals(minuteOfDay))
                {
                    break;
                }
                boolean fullHour = (minuteOfDay) % 60 == 0;
                boolean isLine = (minuteOfDay) % (60 / m_rowsPerHour) == 0;
                if (fullHour || minuteOfDay == minMinute)
                {
                    int rowspan = calcRowspan(minuteOfDay, ((minuteOfDay / 60) + 1) * 60);
                    String timeString = getRaplaLocale().formatTime(minuteOfDay);
                    timelist.add(new RowSlot(timeString, rowspan));
                }
                for (int day = 0; day < columns; day++)
                {
                    if (isExcluded(day))
                        continue;

                    for (int slotnr = 0; slotnr < daySlots[day].size(); slotnr++)
                    {
                        Slot slot = daySlots[day].getSlotAt(slotnr);
                        Block block = slot.getBlock(minuteOfDay);
                        if (block != null)
                        {
                            int endMinute = Math.min(maxMinute, DateTools.getMinuteOfDay(block.getEnd().getTime()));
                            int rowspan = calcRowspan(minuteOfDay, endMinute);
                            if (block instanceof HTMLRaplaBlock)
                            {
                                ((HTMLRaplaBlock) block).setRowCount(rowspan);
                                ((HTMLRaplaBlock) block).setRow(row);
                            }
                            slot.setLastEnd(endMinute);
                        }
                    }
                }
            }
            view.update(daylist, timelist, weeknumber);
        }

        static public class RowSlot
        {
            public RowSlot(String rowname, int rowspan)
            {
                this.rowname = rowname;
                this.rowspan = rowspan;
            }

            String rowname;
            int rowspan;

            public String getRowname()
            {
                return rowname;
            }

            public int getRowspan()
            {
                return rowspan;
            }

        }

        private int calcRowspan(int start, int end)
        {
            if (start == end)
            {
                return 1;
            }
            SortedSet<Integer> tailSet = minuteBlock.tailSet(start);
            int col = 0;
            for (Integer minute : tailSet)
            {
                if (minute < end)
                {
                    col++;
                }
                else
                {
                    break;
                }
            }
            return col;
        }

        protected void printBlock(StringBuffer result, @SuppressWarnings("unused") int firstEventMarkerId, Block block)
        {
            String string = block.toString();
            result.append(string);
        }

        protected String createColumnHeader(int i)
        {
            Date date = DateTools.addDays(getStartDate(), i);
            String headerName = getRaplaLocale().formatDayOfWeekDateMonth(date);
            return headerName;
        }

        SortedSet<Integer> minuteBlock = new TreeSet<Integer>();

        public void addBlock(Block block, int column, int slot)
        {
            checkBlock(block);
            HTMLDaySlot multiSlot = daySlots[column];
            int startMinute = Math.max(minMinute, DateTools.getMinuteOfDay(block.getStart().getTime()));
            int endMinute = (Math.min(maxMinute, DateTools.getMinuteOfDay(block.getEnd().getTime())));
            blocks.add(block);
            //            startBlock.add( startMinute);
            //       endBlock.add( endMinute);
            minuteBlock.add(startMinute);
            minuteBlock.add(endMinute);
            multiSlot.putBlock(block, slot, startMinute);

        }

        static public class HTMLDaySlot extends ArrayList<Slot>
        {
            private static final long serialVersionUID = 1L;
            private boolean empty = true;
            String header;

            public HTMLDaySlot(int size, String header)
            {
                super(size);
                this.header = header;
            }

            public void putBlock(Block block, int slotnr, int startMinute)
            {
                while (slotnr >= size())
                {
                    addSlot();
                }
                getSlotAt(slotnr).putBlock(block, startMinute);
                empty = false;
            }

            public int addSlot()
            {
                Slot slot = new Slot();
                add(slot);
                return size();
            }

            public Slot getSlotAt(int index)
            {
                return get(index);
            }

            public String getHeader()
            {
                return header;
            }

            public boolean isEmpty()
            {
                return empty;
            }
        }

        public static class Slot
        {
            //            int[] EMPTY = new int[]{-2};
            //      int[] SKIP = new int[]{-1};
            int lastEnd = 0;
            HashMap<Integer, Block> map = new HashMap<Integer, Block>();

            public Slot()
            {
            }

            void putBlock(Block block, int startMinute)
            {
                map.put(startMinute, block);
            }

            public Block getBlock(Integer startMinute)
            {
                return map.get(startMinute);
            }

            public Collection<Block> getBlocks()
            {
                return map.values();
            }

            public int getLastEnd()
            {
                return lastEnd;
            }

            void setLastEnd(int lastEnd)
            {
                this.lastEnd = lastEnd;
            }

        }
    }

}
