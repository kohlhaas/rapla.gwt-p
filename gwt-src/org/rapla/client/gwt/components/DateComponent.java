package org.rapla.client.gwt.components;

import java.util.Date;

import org.rapla.components.util.ParseDateException;
import org.rapla.components.util.SerializableDateTimeFormat;
import org.rapla.framework.RaplaLocale;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class DateComponent extends SimplePanel implements ValueChangeHandler<String>
{
    public interface DateValueChanged
    {
        void valueChanged(Date newValue);
    }

    private final TextBox tb = new TextBox();
    private final RaplaLocale locale;
    private final DatePicker datePicker;
    private final DateValueChanged changeHandler;

    public DateComponent(Date initDate, RaplaLocale locale, DateValueChanged changeHandler)
    {
        super();
        this.locale = locale;
        this.changeHandler = changeHandler;
        tb.setStyleName("dateComponent");
        add(tb);
        tb.setValue(locale.formatDate(initDate), false);
        if (!InputUtils.isHtml5DateInputSupported())
        {
            datePicker = new DatePicker();
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
        else
        {
            datePicker = null;
        }
        tb.getElement().setAttribute("type", "date");
        tb.addValueChangeHandler(this);
    }

    public void setDate(Date date)
    {
        this.tb.setValue(locale.formatDate(date), false);
        if (datePicker != null)
        {
            datePicker.setValue(date, false);
            datePicker.setCurrentMonth(date);
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event)
    {
        final String dateInIso = event.getValue();
        try
        {
            final Date newValue = SerializableDateTimeFormat.INSTANCE.parseDate(dateInIso, false);
            changeHandler.valueChanged(newValue);
        }
        catch (ParseDateException e)
        {
            GWT.log("error parsing date " + dateInIso, e);
        }
    }
}
