package org.rapla.client.plugin.view.resoursedates;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.rapla.client.edit.reservation.impl.ReservationController;
import org.rapla.client.factory.ResourceDatesInterface;
import org.rapla.client.factory.ViewServiceProviderInterface;
import org.rapla.client.timePicker.HourMinutePicker;
import org.rapla.client.timePicker.HourMinutePicker.PickerFormat;
import org.rapla.client.timePicker.TimeBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class ResourceDatesView implements ViewServiceProviderInterface, ResourceDatesInterface{
	
	TerminList dateList = new TerminList();
	DateBox dateBegin = new DateBox();
	HourMinutePicker timeBegin = new HourMinutePicker(PickerFormat._24_HOUR);
	DateBox dateEnd = new DateBox();
	HourMinutePicker timeEnd = new HourMinutePicker(PickerFormat._24_HOUR);
	SingleDate addTermin = new SingleDate();

	@Override
	public Widget createContent() {
	    Integer height = (int) (Window.getClientHeight() * 0.90 * 0.80);
	    Integer width = (int) (Window.getClientWidth() * 0.90 * 0.80);
		
		FlowPanel mainContent = new FlowPanel();
		
		dateList.setHeight(height + "px");
		dateList.setStyleName("dateList");
		
		SingleDate eins = new SingleDate();
		SingleDate zwei = new SingleDate();
		SingleDate drei = new SingleDate();
		SingleDate vier = new SingleDate();
		SingleDate fuenf = new SingleDate();
		try {
			eins = new SingleDate("02.01.2015", 
					"16:30",
					"17:45",
					new Double(11.75));
			zwei = new SingleDate("01.01.2015", 
					"16:30",
					"17:45",
					new Double(11.75));
			drei = new SingleDate("01.01.2015", 
					"15:30",
					"17:45",
					new Double(11.75));
			vier = new SingleDate("01.01.2015", 
					"16:30",
					"17:45",
					new Double(11.75));
			fuenf = new SingleDate("01.01.2015", 
					"15:30",
					"17:45",
					new Double(11.75));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		MultiDate multi = new MultiDate();
		multi.addSingleDate(eins);
		multi.addSingleDate(zwei);
		multi.addSingleDate(drei);
		
		dateList.add(vier);	
		dateList.add(multi.getMultiDateLabel());
		dateList.add(fuenf);	

		
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
		
		
		
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
	    
	    
	    // Datum und Uhzreit BEGIN
	    HorizontalPanel begin= new HorizontalPanel();
	    begin.setSpacing(5);
		begin.setStyleName("dateInfoLineComplete");
		
		
		Label beginText = new Label("Begin:");
		beginText.setStyleName("beschriftung");
		begin.add(beginText);
		begin.setCellVerticalAlignment(beginText, HasVerticalAlignment.ALIGN_MIDDLE);
		
		dateBegin.setFormat(new DateBox.DefaultFormat(dateFormat));
		begin.add(dateBegin);
		begin.setCellVerticalAlignment(dateBegin, HasVerticalAlignment.ALIGN_MIDDLE);
		
		
		begin.add(timeBegin);
		begin.setCellVerticalAlignment(timeBegin, HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label beginTimeText = new Label("Uhr");
		beginTimeText.setStyleName("beschriftung");
		begin.add(beginTimeText);
		begin.setCellVerticalAlignment(beginTimeText, HasVerticalAlignment.ALIGN_MIDDLE);


		CheckBox cbWholeDay = new CheckBox("ganzt\u00E4gig");
		begin.add(cbWholeDay);
		begin.setCellVerticalAlignment(cbWholeDay, HasVerticalAlignment.ALIGN_MIDDLE);
		
		begin.setCellWidth(beginText, "50px");
		
		//Datum und Uhrzeit ENDE
		HorizontalPanel end = new HorizontalPanel();
		end.setSpacing(4);
		end.setStyleName("dateInfoLineComplete");
		
		Label endText = new Label("Ende: ");
		endText.setStyleName("beschriftung");
		end.add(endText);
		end.setCellVerticalAlignment(endText, HasVerticalAlignment.ALIGN_MIDDLE);
		
		dateEnd.setFormat(new DateBox.DefaultFormat(dateFormat));
		end.add(dateEnd);
		end.setCellVerticalAlignment(dateEnd, HasVerticalAlignment.ALIGN_MIDDLE);
		
		end.add(timeEnd.asWidget());
		end.setCellVerticalAlignment(timeEnd, HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label endTimeText = new Label("Uhr");
		endTimeText.setStyleName("beschriftung");
		end.add(endTimeText);
		end.setCellVerticalAlignment(endTimeText, HasVerticalAlignment.ALIGN_MIDDLE);
		
		end.setCellWidth(endText, "50px");
		
		
		//Add termin
		
		buttonPlus.addClickHandler(new ClickHandler() {

			
			@Override
			public void onClick(ClickEvent event) {
				try {
					Date beginTmp = new Date(dateBegin.getValue().getTime() + (timeBegin.getMinutes()*60000));
					Date endTmp = new Date(dateBegin.getValue().getTime() + (timeEnd.getMinutes()*60000));
					addTermin = new SingleDate(dateBegin.getValue(),beginTmp, endTmp);
					FocusPanel test = new FocusPanel();
					test.add(addTermin);
					test.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							setDateClick(addTermin);
							timeBegin.setTime("am", 2, 3);
						}
						
					});

					dateList.add(test);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();	
				}
				
			}
		});
		
		// Checkbox WIEDERHOLEN
		HorizontalPanel repeat = new HorizontalPanel();
		DisclosurePanel cbRepeat = new DisclosurePanel("Wiederholen");
		cbRepeat.setStyleName("dateInfoLineLeft");
		
		CheckBox daily = new CheckBox("t\u00E4glich");
		CheckBox weekly = new CheckBox("w\u00F6chtenlich");
		CheckBox monthly = new CheckBox("monatlich");
		CheckBox year = new CheckBox("j\u00E4hrlich");
		
		repeat.add(daily);
		repeat.add(weekly);
		repeat.add(monthly);
		repeat.add(year);
		
		cbRepeat.add(repeat);
		
		
		// Ausgewählte Resourcen
		FlowPanel chosenResources = new FlowPanel();
		chosenResources.setStyleName("dateInfoLineComplete");
		
		FlowPanel resource1 = createResourceContainer("Kurse");		
		FlowPanel resource2 = createResourceContainer("Raum");
		FlowPanel resource3 = createResourceContainer("Professor");
		
		resource1.add(createResource("WWI12B1"));
		resource2.add(createResource("B353"));
		resource3.add(createResource("Roland Kuestermann"));
		
		Label headerChosenRes  = new Label("Ausgewaehlte Resourcen:");
		headerChosenRes.setStyleName("beschriftung");
		
		chosenResources.add(headerChosenRes);
		chosenResources.add(resource1);
		chosenResources.add(resource2);
		chosenResources.add(resource3);


		
		DisclosurePanel addResources = new DisclosurePanel("Resourcen Hinzufuegen");
		addResources.setStyleName("dateInfoLineComplete");
		
		FlowPanel chooseContainer = new FlowPanel();
		chooseContainer.setStyleName("chooseContainer");
	
		
		// Baumstruktur für verfügbare Resourcen
		Tree resourceTree = new Tree();
		
		TreeItem rooms = new TreeItem();
		rooms.setText("Raeume");
		
		TreeItem rooms1 = new TreeItem();
		TreeItem rooms2 = new TreeItem();
		
	    rooms1.setText("A");
	    rooms2.setText("B");
	    
	    TreeItem room1_1 = new TreeItem(new CheckBox("305"));
	    TreeItem room1_2 = new TreeItem(new CheckBox("306"));
	    TreeItem room1_3 = new TreeItem(new CheckBox("307"));
	    rooms1.addItem(room1_1);
	    rooms1.addItem(room1_2);
	    rooms1.addItem(room1_3);
	    
	    TreeItem room2_1 = new TreeItem(new CheckBox("201"));
	    TreeItem room2_2 = new TreeItem(new CheckBox("202"));
	    TreeItem room2_3 = new TreeItem(new CheckBox("202"));
	    rooms2.addItem(room2_1);	
	    rooms2.addItem(room2_2);
	    rooms2.addItem(room2_3);

	    rooms.addItem(rooms1);
	    rooms.addItem(rooms2);
	    
	    resourceTree.addItem(rooms);

	    
	    
	    chooseContainer.add(resourceTree);
	    chooseContainer.setWidth(width  * 0.85 + "px");
		
	    addResources.setContent(chooseContainer);
			
		
		dateInfos.add(begin);
		dateInfos.add(end);
		dateInfos.add(cbRepeat);
		//dateInfos.add(new HTML("<hr  style=\"width:90%;\" />"));
		dateInfos.add(chosenResources);
		dateInfos.add(addResources);
		
		
		
		mainContent.add(dateList);
		mainContent.add(buttonBar);
		mainContent.add(dateInfos);
		
		return mainContent;
	}
	
	private FlowPanel createResourceContainer(String name){
		
		FlowPanel container = new FlowPanel();
		container.setStyleName("resourceContainer");
		Label titel = new Label(name);
		titel.setStyleName("beschriftung");
		titel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		container.add(titel);
		
		return container;
	}
	
	private FlowPanel createResource(String name){
		
		FlowPanel resource = new FlowPanel();
		resource.setStyleName("resource");
		
		Label titel = new Label(name);
		titel.setStyleName("resourceTitel");
		resource.add(titel);
		
		Button cross = new Button("X");
		cross.setStyleName("closeCross");
		resource.add(cross);
				
		return resource;
	}
	
	public void setDateClick(DateParent date){
		dateList.setActive(date);
		int index = dateList.getDateParentIndex(date);

		if(dateList.getDate(index).getType().equals("SingleDate")){
			SingleDate tmpSingle = (SingleDate) dateList.getDate(index);
			dateBegin.setValue(tmpSingle.getDateObject());
			dateEnd.setValue(tmpSingle.getDateObject());

			timeBegin.setTime("suffix", tmpSingle.getBeginTime().getHours(), tmpSingle.getBeginTime().getMinutes());
			timeEnd.setTime("suffix", tmpSingle.getEndTime().getHours(), tmpSingle.getEndTime().getMinutes());
		}else{
			MultiDate tmpMulti = (MultiDate) dateList.getDate(0);
		}
		if (dateList.getDateParentIndex(date) == -1)
			timeBegin.setTime("am", 2, 3);
		
	}
	
	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
