package org.rapla.client.gwt.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.rapla.components.util.DateTools;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class DateComponent extends FlowPanel implements HasValueChangeHandlers<Date>
{
    private static final int ESCAPE = 27;
    private Date date;
    private final DatePicker datePicker = new DatePicker();
    private final TextBox tb;
    private final List<ValueChangeHandler<Date>> valueChangeHandlers = new ArrayList<ValueChangeHandler<Date>>();
    private PopupPanel popup;

    public DateComponent(final Date initDate)
    {
        datePicker.setStyleName("datePicker");
        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event)
            {
                setDate(event.getValue());
            }
        });
        popup = new PopupPanel(true, true)
        {
            @Override
            protected void onPreviewNativeEvent(NativePreviewEvent event)
            {
                if (Event.ONKEYUP == event.getTypeInt())
                {
                    if (event.getNativeEvent().getKeyCode() == ESCAPE)
                    {
                        // Dismiss when escape is pressed
                        popup.hide();
                    }
                }
            }
        };
        popup.add(datePicker);
        tb = new TextBox();
        tb.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<String> event)
            {
                // TODO Auto-generated method stub
                //                setDate(value);
            }
        });
        tb.addMouseWheelHandler(new MouseWheelHandler()
        {
            @Override
            public void onMouseWheel(MouseWheelEvent event)
            {
                final int deltaY = event.getDeltaY();
                final int cursorPos = tb.getCursorPos();
                // TODO
                if (cursorPos < 2)
                {
                    setDate(DateTools.addDays(date, deltaY));
                }
                else if (cursorPos < 5)
                {
                    setDate(DateTools.addMonths(date, deltaY));
                }
                else
                {
                    setDate(DateTools.addYears(date, deltaY));
                }
            }
        });
        this.add(tb);
        final Button datePickerButton = new Button("picker");
        datePickerButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
//                if (popup.isShowing())
//                {
//                    popup.hide();
//                }
//                else
//                {
                    popup.showRelativeTo(datePickerButton);
//                }
            }
        });
        add(datePickerButton);
        setDate(initDate == null ? DateTools.cutDate(new Date()) : initDate);
    }

    private void setDate(Date value)
    {
        this.date = value;
        this.datePicker.setValue(value, false);
        this.tb.setValue(DateTools.formatDate(value), false);
        ValueChangeEvent.fire(this, date);
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Date> valueChangeHandler)
    {
        this.valueChangeHandlers.add(valueChangeHandler);
        return new HandlerRegistration()
        {
            @Override
            public void removeHandler()
            {
                valueChangeHandlers.remove(valueChangeHandler);
            }
        };
    }

}
