package org.rapla.client.plugin.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;

public class ViewController extends FlowPanel {

	private FlowPanel content;

	public ViewController() {
		content = new FlowPanel();
		ViewPlugins plugins = GWT.create(ViewPlugins.class);
		final List<View> views = plugins.getViews();
		final ListBox listBox = new ListBox();
		add(listBox);
		int index = 0;
		for (final View view : views) {
			listBox.insertItem(view.getName(), index);
		}
		listBox.setSelectedIndex(0);
		add(content);
		content.add(views.get(0).createView());
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = listBox.getSelectedIndex();
				final View view = views.get(index);
				GWT.runAsync(new RunAsyncCallback() {

					@Override
					public void onSuccess() {
						content.clear();
						content.add(view.createView());
					}

					@Override
					public void onFailure(Throwable reason) {
						Window.alert("Error loading plugin");
					}
				});
			}
		});
	}

}
