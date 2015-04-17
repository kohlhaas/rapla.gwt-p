package org.rapla.client.plugin.view;

import com.google.gwt.dom.client.DataTransfer;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.user.client.ui.FlowPanel;

public class Event extends FlowPanel implements DragStartHandler, DragEndHandler
{
    private static final String DRAG_CSS = "dragging";
    private final ResolveDrop dropResolver;

    public static interface ResolveDrop
    {
        void dropFinished(DragEndEvent event, Event source);
    }

    public Event(ResolveDrop dropResolver)
    {
        super();
        this.dropResolver = dropResolver;
        addStyleName("event");
        if (dropResolver != null)
        {
            getElement().setDraggable(Element.DRAGGABLE_TRUE);
            addDomHandler(this, DragStartEvent.getType());
            addDomHandler(this, DragEndEvent.getType());
        }
    }

    @Override
    public void onDragEnd(DragEndEvent event)
    {
        this.removeStyleName(DRAG_CSS);
        dropResolver.dropFinished(event, this);
    }

    @Override
    public void onDragStart(DragStartEvent event)
    {
        this.addStyleName(DRAG_CSS);
    }

}
