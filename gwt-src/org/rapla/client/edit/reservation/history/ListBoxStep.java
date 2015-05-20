package org.rapla.client.edit.reservation.history;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ListBoxStep implements Step {
	
	ListBox listBox;
	int newIndex;
	int oldIndex;
	
	public ListBoxStep(ListBox listBox, int previousValue) {
		this.listBox = listBox;
		this.oldIndex = previousValue;
		this.newIndex = listBox.getSelectedIndex();
	}

	@Override
	public Object up() {
		listBox.setSelectedIndex(newIndex);
		HistoryManager.getInstance().ignoreNextChangeTo(listBox);
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), listBox);
		return newIndex;
	}

	@Override
	public Object down() {
		listBox.setSelectedIndex(oldIndex);
		HistoryManager.getInstance().ignoreNextChangeTo(listBox);
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), listBox);
		return oldIndex;
	}

	@Override
	public Widget getWidget() {
		return listBox;
	}

}
