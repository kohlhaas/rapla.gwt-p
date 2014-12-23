package org.rapla.client.plugin.view.resoursedates;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;


public class SingleDate{
	
	private String date, begin, end, infoText;
	private FlowPanel main = new FlowPanel();

	public FlowPanel getDate() {
		return main;
	}

	public SingleDate(String date, String begin, String end,
			String infoText) {
		this.date = date;
		this.begin = begin;
		this.end = end;
		this.infoText = infoText;
		
		createSingleDate();
		
	}
	
	private void createSingleDate() {
		
		main.setStyleName("singleDate");
		
		FlowPanel times = new FlowPanel();
		times.setStyleName("singleDateTimes");
		times.add(new Label(begin));
		times.add(new Label(end));
		
		FlowPanel infos = new FlowPanel();
		Label dateLabel = new Label(date);
		dateLabel.setStyleName("singleDateDateLabel");
		Label infoLabel = new Label(infoText);
		
		infos.add(dateLabel);
		infos.add(infoLabel);
		
		main.add(times);
		main.add(infos);
	}

}
