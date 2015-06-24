package org.rapla.client.gwt.components;

import java.util.Collection;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class DropDownInputField extends FlowPanel
{
    public interface DropDownValueChanged
    {
        void valueChanged(String newValue);
    }

    public static class DropDownItem
    {
        private final String name;
        private final String id;

        public DropDownItem(String name, String id)
        {
            this.name = name;
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public String getId()
        {
            return id;
        }

    }

    public DropDownInputField(final String label, final DropDownValueChanged changeHandler, Collection<DropDownItem> values)
    {
        this(label, changeHandler, values, false);
    }

    public DropDownInputField(final String label, final DropDownValueChanged changeHandler, Collection<DropDownItem> values, boolean multiSelect)
    {
        super();
        setStyleName("dropDownInput inputWrapper");
        final Label title = new Label(label);
        title.setStyleName("label");
        final ListBox titleInput = new ListBox();
        titleInput.setMultipleSelect(multiSelect);
        for (DropDownItem dropDownItem : values)
        {
            titleInput.addItem(dropDownItem.getName(), dropDownItem.getId());
        }
        titleInput.setStyleName("input");
        titleInput.addChangeHandler(new ChangeHandler()
        {
            @Override
            public void onChange(ChangeEvent event)
            {
                changeHandler.valueChanged(titleInput.getSelectedValue());
            }
        });
        add(title);
        add(titleInput);
    }

}
