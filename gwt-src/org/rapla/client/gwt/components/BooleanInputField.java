package org.rapla.client.gwt.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class BooleanInputField extends FlowPanel
{
    public interface BooleanValueChange
    {
        void valueChanged(Boolean newValue);
    }

    public BooleanInputField(final String label, Boolean value, final BooleanValueChange changeHandler)
    {
        super();
        setStyleName("integerInput inputWrapper");
        final Label title = new Label(label);
        title.setStyleName("label");
        final CheckBox cb = new CheckBox();
        cb.setStyleName("input");
        cb.setValue(value);
        cb.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                changeHandler.valueChanged(cb.getValue());
            }
        });
        add(title);
        add(cb);
    }
}
