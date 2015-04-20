package org.rapla.client.gwt.view;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.user.client.ui.FlowPanel;

public class Event extends FlowPanel implements DragStartHandler, DragEndHandler
{
    private static final String DRAG_CSS = "dragging";
    public Event()
    {
        super();
        addStyleName("event");
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
