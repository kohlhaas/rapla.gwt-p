package org.rapla.client.plugin.view.list;

import org.rapla.client.content.ContentDrawer;
import org.rapla.client.content.DetailSelectEvent;
import org.rapla.client.event.RaplaEventBus;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class ListDrawer implements ContentDrawer {

	private CellList<String> list;
	private ListDataProvider<String> data;

	public ListDrawer() {
		list = new CellList<>(new TextCell());
		list.setStyleName("RaplaListDrawerList");
		data = new ListDataProvider<>();
		data.addDataDisplay(list);
		for (int i = 0; i < 100; i++)
			data.getList().add("Test" + i);
		list.setPageSize(data.getList().size());
		final SingleSelectionModel<String> singleSelectionModel = new SingleSelectionModel<>();
		list.setSelectionModel(singleSelectionModel);
		singleSelectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				RaplaEventBus.getInstance().fireEvent(
						new DetailSelectEvent(singleSelectionModel
								.getSelectedObject()));
			}
		});
	}

	@Override
	public Widget createContent() {
		return list;
	}

	@Override
	public void updateContent() {

	}

}
