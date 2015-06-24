package org.rapla.client.gwt.components;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class TextInputField extends FlowPanel
{
    public interface TextValueChanged
    {
        void valueChanged(String newValue);
    }

    public TextInputField(final String labelText, String valueText, final TextValueChanged changeHandler)
    {
        super();
        setStyleName("textInput inputWrapper");
        final Label label = new Label(labelText);
        label.setStyleName("label");
        final TextBox input = new TextBox();
        input.setValue(valueText);
        input.setStyleName("input");
        input.addChangeHandler(new ChangeHandler()
        {
            @Override
            public void onChange(ChangeEvent event)
            {
                changeHandler.valueChanged(input.getValue());
            }
        });
        add(label);
        add(input);
    }

}
