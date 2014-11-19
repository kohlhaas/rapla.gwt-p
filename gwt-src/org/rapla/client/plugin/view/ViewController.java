package org.rapla.client.plugin.view;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.inject.Inject;

import org.rapla.client.event.AddEvent;
import org.rapla.client.event.RaplaEventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ViewController implements ContentDrawer {

	private ListBox listBox;

	private List<ViewPlugin> views;
	
	@Inject
	public void setViews(Set<ViewPlugin> views)
	{
	    this.views = new ArrayList<ViewPlugin>(views);
	}

	public ViewController() {
		listBox = new ListBox();
		listBox.setSelectedIndex(0);
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = listBox.getSelectedIndex();
				final ViewPlugin view = views.get(index);
				GWT.runAsync(new RunAsyncCallback() {

					@Override
					public void onSuccess() {
						RaplaEventBus.getInstance().fireEvent(
								new ViewSelectionChangedEvent());
					}

					@Override
					public void onFailure(Throwable reason) {
						Window.alert("Error loading plugin: " + view.getName());
					}
				});
			}
		});
	}

	public ContentDrawer getSelectedContentDrawer() {
		return views.get(listBox.getSelectedIndex()).getContentDrawer();
	}

	@Override
	public Widget createContent() {
		final FlowPanel content = new FlowPanel();
		int index = 0;
		for (final ViewPlugin view : views) {
			listBox.insertItem(view.getName(), index);
			index++;
		}
		content.add(listBox);
		Label add = new Label("+");
		add.setStyleName("addButton");
		add.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RaplaEventBus.getInstance().fireEvent(new AddEvent());
			}
		});
		content.add(add);
		return content;
	}

	@Override
	public void updateContent() {

	}

}
