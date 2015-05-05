package org.rapla.client.gwt.components;

import java.util.Date;

import org.rapla.components.util.ParseDateException;
import org.rapla.components.util.SerializableDateTimeFormat;
import org.rapla.framework.RaplaLocale;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class DateComponent extends SimplePanel implements ValueChangeHandler<String>, HasValueChangeHandlers<Date>
{
    private final TextBox tb = new TextBox();

    public DateComponent(Date initDate, RaplaLocale locale)
    {
        super();
        tb.setStyleName("dateComponent");
        add(tb);
        tb.setValue(locale.formatDate(initDate), false);
        if (!isHtml5DateInputSupported())
        {
            final DatePicker datePicker = new DatePicker();
            final PopupPanel popupPanel = new PopupPanel(true, true);
            popupPanel.add(datePicker);
            tb.addFocusHandler(new FocusHandler()
            {
                @Override
                public void onFocus(FocusEvent event)
                {
                    popupPanel.showRelativeTo(tb);
                }
            });
            datePicker.addValueChangeHandler(new ValueChangeHandler<Date>()
            {
                @Override
                public void onValueChange(ValueChangeEvent<Date> event)
                {
                    final Date value = event.getValue();
                    final String newDate = DateTimeFormat.getFormat("yyyy-MM-dd").format(value);
                    tb.setValue(newDate, true);
                }
            });
            tb.addValueChangeHandler(new ValueChangeHandler<String>()
            {
                @Override
                public void onValueChange(ValueChangeEvent<String> event)
                {
                    try
                    {
                        Date newValue = SerializableDateTimeFormat.INSTANCE.parseDate(event.getValue(), false);
                        datePicker.setValue(newValue, false);
                    }
                    catch (Exception e)
                    {
                        GWT.log("error parsing date: " + event.getValue(), e);
                    }
                }
            });
        }
        tb.getElement().setAttribute("type", "date");
        tb.addValueChangeHandler(this);
    }

    private native boolean isHtml5DateInputSupported()/*-{
		var datefield = document.createElement("input")
		datefield.setAttribute("type", "date")
		return datefield.type == "date"
    }-*/;

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Date> handler)
    {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event)
    {
        final String dateInIso = event.getValue();
        try
        {
            final Date newValue = SerializableDateTimeFormat.INSTANCE.parseDate(dateInIso, false);
            ValueChangeEvent.fire(this, newValue);
        }
        catch (ParseDateException e)
        {
            GWT.log("error parsing date " + dateInIso, e);
        }
    }
}
