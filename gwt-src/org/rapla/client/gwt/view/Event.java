package org.rapla.client.gwt.view;

import org.rapla.plugin.abstractcalendar.server.HTMLRaplaBlock;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class Event extends FlowPanel implements DragStartHandler, DragEndHandler
{
    private static final String DRAG_CSS = "dragging";
    private HTMLRaplaBlock htmlBlock;
    public Event(HTMLRaplaBlock htmlBlock)
    {
        super();
        this.htmlBlock = htmlBlock;
        addStyleName("event");
        add(new HTML(htmlBlock.getName()));
        getElement().getStyle().setBackgroundColor(htmlBlock.getBackgroundColor());
        getElement().setDraggable(Element.DRAGGABLE_TRUE);
        addDomHandler(this, DragStartEvent.getType());
        addDomHandler(this, DragEndEvent.getType());
    }

    @Override
    public void onDragEnd(DragEndEvent event)
    {
        this.removeStyleName(DRAG_CSS);
    }

    @Override
    public void onDragStart(DragStartEvent event)
    {
        this.addStyleName(DRAG_CSS);
    }

}
