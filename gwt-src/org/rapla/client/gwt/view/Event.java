package org.rapla.client.gwt.view;

import org.rapla.plugin.abstractcalendar.server.HTMLRaplaBlock;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class Event extends FlowPanel
{
    private HTMLRaplaBlock htmlBlock;

    public Event(HTMLRaplaBlock htmlBlock)
    {
        super();
        this.htmlBlock = htmlBlock;
        addStyleName("event");
        add(new HTML(htmlBlock.getName()));
        getElement().getStyle().setBackgroundColor(htmlBlock.getBackgroundColor());
        getElement().setDraggable(Element.DRAGGABLE_TRUE);
    }
    
    public HTMLRaplaBlock getHtmlBlock()
    {
        return htmlBlock;
    }

}
