package org.rapla.client.gwt;

import java.util.List;

import javax.inject.Inject;

import org.rapla.client.ApplicationView;
import org.rapla.client.base.CalendarPlugin;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

public class ApplicationViewImpl implements ApplicationView<IsWidget> {

    FlowPanel drawingContent = new FlowPanel();
    RootPanel root;
    private ListBox listBox;
    Presenter presenter;

    {
        drawingContent.setStyleName("raplaDrawingContent");
        root = RootPanel.get("raplaRoot");
    }

    @Inject
    public ApplicationViewImpl() {
    }
    
    public void setPresenter(Presenter presenter) 
    {
        this.presenter = presenter;
    }

    public void show(List<String> viewNames)
    {
        listBox = new ListBox();
        final FlowPanel content = new FlowPanel();
        root.add( content );
        int index = 0;
        for (final String name : viewNames) {
            listBox.insertItem(name, index);
            index++;
        }
        content.add(listBox);
        Label add = new Label("+");
        add.setStyleName("addButton");
        add.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                presenter.addClicked();
            }
        });
        content.add(add);
        listBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                int index = listBox.getSelectedIndex();
                presenter.setSelectedViewIndex(index);
            }
        });
    }
    
    @Override
    public void replaceContent(CalendarPlugin<IsWidget> contentProvider) {
        if (drawingContent != null)
        {
            root.remove( drawingContent);
        }
        drawingContent = new FlowPanel();
        IsWidget content = contentProvider.provideContent();
        drawingContent.add(content);
        root.add(drawingContent);
    }
    
}