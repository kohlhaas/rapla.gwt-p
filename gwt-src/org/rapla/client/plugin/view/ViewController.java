package org.rapla.client.plugin.view;

import java.util.List;

import org.rapla.client.content.ContentDrawer;
import org.rapla.client.event.RaplaEventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ViewController implements ContentDrawer {

	private ListBox listBox;

	private List<ViewPlugin> views;

	public ViewController() {
		ViewPlugins plugins = GWT.create(ViewPlugins.class);
		views = plugins.getViews();
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
		int index = 0;
		for (final ViewPlugin view : views) {
			listBox.insertItem(view.getName(), index);
			index++;
		}
		return listBox;
	}

	@Override
	public void updateContent() {

	}

}
