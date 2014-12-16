package org.rapla.client.plugin.view.resoursedates;

import org.rapla.client.plugin.view.ContentDrawer;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ResourceDatesDrawer implements ContentDrawer {

	@Override
	public Widget createContent() {
	    Integer height = (int) (Window.getClientHeight() * 0.90 * 0.80);
		
		FlowPanel mainContent = new FlowPanel();
		
		FlowPanel dateList = new FlowPanel();
		dateList.setHeight(height + "px");
		dateList.setStyleName("dateList");
		
		dateList.add(createSingleDate(
						"01.01.2015", 
						"16:30",
						"17:45",
						"1.75 Vorlesungsstunden"
					));
		dateList.add(createSingleDate(
				"02.01.2015", 
				"16:30",
				"17:45",
				"4 Vorlesungsstunden"
			));
		
		Label test = new Label("Test");
		dateList.add(test);
		
		
		FlowPanel buttonBar = new FlowPanel();
		buttonBar.setHeight(height + "px");
		buttonBar.setStyleName("datesButtonBar");
		

		//Image buttonNextGap = new Image("button_luecke.png");
		Label buttonNextGap = new Label(">>");
		buttonNextGap.setStyleName("buttonsResourceDates");		
		
		//Image buttonGarbageCan = new Image("button_eimer.png");
		Label buttonGarbageCan = new Label("X");
		buttonGarbageCan.setStyleName("buttonsResourceDates");		
		
		//Image buttonPlus = new Image("button_plus.png");
		Label buttonPlus = new Label("+");
		buttonPlus.setStyleName("buttonsResourceDates");
		
		buttonBar.add(buttonPlus);
		buttonBar.add(buttonGarbageCan);
		buttonBar.add(buttonNextGap);
		
		
		FlowPanel dateInfos = new FlowPanel();
		dateInfos.setHeight(height + "px");
		dateInfos.setStyleName("dateInfos");
		
		dateInfos.add(test);
		
		
		mainContent.add(dateList);
		mainContent.add(buttonBar);
		mainContent.add(dateInfos);
		
		return mainContent;
	}

	private Widget createSingleDate(String date, String begin, String end,
			String infoText) {
		
		FlowPanel main = new FlowPanel();
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
		
		return main;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
