package org.rapla.client.gwt.components;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;

public class LongInputField extends FlowPanel
{
    public interface LongValueChange
    {
        void valueChanged(Long newValue);
    }

    public LongInputField(final String label, Long value, final LongValueChange changeHandler)
    {
        super();
        setStyleName("integerInput inputWrapper");
        final Label inputLabel = new Label(label);
        inputLabel.setStyleName("label");
        final IntegerBox input = new IntegerBox();
        input.setStyleName("input");
        if (value != null)
            input.setValue(value.intValue());
        input.getElement().setAttribute("type", "number");
        input.addChangeHandler(new ChangeHandler()
        {
            @Override
            public void onChange(ChangeEvent event)
            {
                final Integer newValue = input.getValue();
                changeHandler.valueChanged(newValue != null ? new Long(newValue) : newValue);
            }
        });
        add(inputLabel);
        add(input);
    }
}
