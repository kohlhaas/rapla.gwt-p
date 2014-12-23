package org.rapla.client.plugin.view.resoursedates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MultiDate {
	private List<SingleDate> singleDates = new ArrayList<>();
	private FlowPanel main = new FlowPanel();
	private DisclosurePanel singleDatesContainer = new DisclosurePanel();
	private double vorlesungsStunden;

	public MultiDate() {

	}

	public void addSingleDate(SingleDate date) {
		singleDates.add(date);
		vorlesungsStunden = vorlesungsStunden + date.getVorlesungsstunden();
	}

	public Widget getMultiDateLabel() {

		main.setStyleName("singleDate");
		Collections.sort(singleDates);
		FlowPanel helper = new FlowPanel();
		
		for (Iterator<SingleDate> iter = singleDates.iterator(); iter.hasNext(); ) {
			SingleDate sd = iter.next();
			helper.add(sd.getSingleDate());
		}
		// a DisclosurePanel can only contain two Widgets
		singleDatesContainer.add(helper);

		singleDatesContainer.setStyleName("singleDatesContainer");

		FlowPanel infos = new FlowPanel();
		infos.setStyleName("multiDateInfos");
		Label dateLabel = new Label(singleDates.get(0).getDate() + " - "
				+ singleDates.get(singleDates.size() - 1).getDate());
		dateLabel.setStyleName("singleDateDateLabel");
		Label infoLabel = new Label(vorlesungsStunden + " Vorlesungsstunden");

		infos.add(dateLabel);
		infos.add(infoLabel);
		
		singleDatesContainer.setHeader(infos);
		main.add(singleDatesContainer);

		return main;
	}
}
