package org.rapla.client.gwt.view;

import java.util.Date;

import org.rapla.components.util.DateTools;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class NavigatorView extends FlowPanel
{
    public static interface NavigatorAction
    {
        void selectedDate(Date selectedDate);

        void next();

        void previous();
    }

    public NavigatorView(final String parentStyle, final NavigatorAction navigatorAction)
    {
        super();
        setStyleName(parentStyle);
        addStyleName("navigator");
        final DatePicker datePicker = new DatePicker();
        datePicker.setStyleName("datePicker");
        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event)
            {
                final Date date = event.getValue();
                navigatorAction.selectedDate(date);
            }
        });
        this.add(datePicker);
        final Button today = new Button("today");
        today.setStyleName("button now");
        today.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                final Date today = DateTools.cutDate(new Date());
                navigatorAction.selectedDate(today);
            }
        });
        this.add(today);
        final Button previousButton = new Button("previous");
        previousButton.setStyleName("button prev");
        previousButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                navigatorAction.previous();
            }
        });
        this.add(previousButton);
        final Button nextButton = new Button("next");
        nextButton.setStyleName("button next");
        nextButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                // callback
                navigatorAction.next();
            }
        });
        this.add(nextButton);
    }

}
