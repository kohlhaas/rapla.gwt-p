package org.rapla.client.plugin.view.resoursedates;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.rapla.client.factory.ResourceDatesInterface;
import org.rapla.client.factory.ViewServiceProviderInterface;
import org.rapla.client.timePicker.HourMinutePicker;
import org.rapla.client.timePicker.HourMinutePicker.PickerFormat;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class ResourceDatesView implements ViewServiceProviderInterface,
		ResourceDatesInterface {

	private ArrayList<List<String>> toBeReservedResources = new ArrayList<List<String>>();
	
	private ArrayList<List<String>> reservedResources = new ArrayList<List<String>>();
		
	
	FlowPanel mainContent;
	FlowPanel dateList;
	FlowPanel buttonBar;

	Label buttonNextGap;
	Label buttonGarbageCan;
	Label buttonPlus;
	// TO-DO: Is this really a Label? Or should it be a Button? Can a Label be
	// used, too?

	DateBox dateBegin;
	DateBox dateEnd;

	HourMinutePicker timeBegin;
	HourMinutePicker timeEnd;

	Tree resourceTree;
	
	CheckBox cbWholeDay;
	CheckBox cbRepeat;

	FlowPanel chosenResources;
	DisclosurePanel dateDisclosurePanel;

	@Override
	public Widget createContent(){
		Integer height = (int) (Window.getClientHeight() * 0.90 * 0.80);
		Integer width = (int) (Window.getClientWidth() * 0.90 * 0.80);

		mainContent = new FlowPanel();

		dateList = new FlowPanel();
		dateList.setHeight(height + "px");
		dateList.setStyleName("dateList");

			//Dummy create
			try {
				Date now = new Date(System.currentTimeMillis());
				Date plusOneHour = new Date(System.currentTimeMillis() + 5400000);
				Date extra = new Date(System.currentTimeMillis() + 960000000);
				RaplaDate eins = new RaplaDate(extra,extra,false);
				RaplaDate zwei = new RaplaDate(now,plusOneHour,true);
				RaplaDate drei = new RaplaDate(now,plusOneHour,true);
				RaplaDate vier = new RaplaDate(now,plusOneHour,true);
				RaplaDate fuenf = new RaplaDate(now,plusOneHour,true);
				
				List<RaplaDate> list = new ArrayList<>();
				list.add(eins);
				list.add(zwei);
				list.add(drei);

				RaplaDate multi = new RaplaDate(list);

				dateList.add(vier);
				dateList.add(multi);
				dateList.add(fuenf);

			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


		buttonBar = new FlowPanel();
		buttonBar.setHeight(height + "px");
		buttonBar.setStyleName("datesButtonBar");
		

		// Image buttonNextGap = new Image("button_luecke.png");
		buttonNextGap = new Label(">>");
		buttonNextGap.setStyleName("buttonsResourceDates");

		// Image buttonGarbageCan = new Image("button_eimer.png");
		buttonGarbageCan = new Label("X");
		buttonGarbageCan.setStyleName("buttonsResourceDates");

		// Image buttonPlus = new Image("button_plus.png");
		buttonPlus = new Label("+");
		buttonPlus.setStyleName("buttonsResourceDates");

		// TO-DO: Is this really a Label? Or should it be a Button? Can a Label
		// be used, too?

		buttonBar.add(buttonPlus);
		buttonBar.add(buttonGarbageCan);
		buttonBar.add(buttonNextGap);

		FlowPanel dateInfos = new FlowPanel();
		dateInfos.setHeight(height + "px");
		dateInfos.setStyleName("dateInfos");

		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat(PredefinedFormat.DATE_FULL);

		// Datum und Uhzreit BEGIN
		HorizontalPanel begin = new HorizontalPanel();
		begin.setSpacing(5);
		begin.setStyleName("dateInfoLineComplete");

		Label beginText = new Label("Begin:");
		beginText.setStyleName("beschriftung");
		begin.add(beginText);
		begin.setCellVerticalAlignment(beginText,
				HasVerticalAlignment.ALIGN_MIDDLE);

		dateBegin = new DateBox();
		dateBegin.setFormat(new DateBox.DefaultFormat(dateFormat));
		begin.add(dateBegin);
		begin.setCellVerticalAlignment(dateBegin,
				HasVerticalAlignment.ALIGN_MIDDLE);

		timeBegin = new HourMinutePicker(PickerFormat._24_HOUR);
		begin.add(timeBegin);
		begin.setCellVerticalAlignment(timeBegin,
				HasVerticalAlignment.ALIGN_MIDDLE);

		final Label beginTimeText = new Label("Uhr");
		beginTimeText.setStyleName("beschriftung");
		begin.add(beginTimeText);
		begin.setCellVerticalAlignment(beginTimeText,
				HasVerticalAlignment.ALIGN_MIDDLE);

		final Label endTimeText = new Label("Uhr");
		
		cbWholeDay = new CheckBox("ganzt\u00E4gig");
		begin.add(cbWholeDay);
		cbWholeDay.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {

				if(((CheckBox) event.getSource()).getValue()){
					timeBegin.setVisible(false);
					timeEnd.setVisible(false);
					timeBegin.setTime("am", 0, 0);
					timeEnd.setTime("am", 0, 0);
					beginTimeText.setVisible(false);
					endTimeText.setVisible(false);
				}else{
					timeBegin.setVisible(true);
					timeEnd.setVisible(true);
					timeBegin.clear();
					timeEnd.clear();
					beginTimeText.setVisible(true);
					endTimeText.setVisible(true);
				}
				
			}
			
		});
		
		begin.setCellVerticalAlignment(cbWholeDay,
				HasVerticalAlignment.ALIGN_MIDDLE);

		begin.setCellWidth(beginText, "50px");

		// Datum und Uhrzeit ENDE
		HorizontalPanel end = new HorizontalPanel();
		end.setSpacing(4);
		end.setStyleName("dateInfoLineComplete");

		Label endText = new Label("Ende: ");
		endText.setStyleName("beschriftung");
		end.add(endText);
		end.setCellVerticalAlignment(endText, HasVerticalAlignment.ALIGN_MIDDLE);

		dateEnd = new DateBox();
		dateEnd.setFormat(new DateBox.DefaultFormat(dateFormat));
		end.add(dateEnd);
		end.setCellVerticalAlignment(dateEnd, HasVerticalAlignment.ALIGN_MIDDLE);

		timeEnd = new HourMinutePicker(PickerFormat._24_HOUR);
		// timeEnd.setWidth("35px");
		// timeEnd.setMaxLength(5);
		timeEnd.setVisible(true);
		timeEnd.setTitle("endTime");
		end.add(timeEnd.asWidget());
		end.setCellVerticalAlignment(timeEnd, HasVerticalAlignment.ALIGN_MIDDLE);


		endTimeText.setStyleName("beschriftung");
		end.add(endTimeText);
		end.setCellVerticalAlignment(endTimeText,
				HasVerticalAlignment.ALIGN_MIDDLE);

		end.setCellWidth(endText, "50px");

		// Add termin

		buttonPlus.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					dateDisclosurePanel.setOpen(true);
					/*
					Date beginTmp = new Date(dateBegin.getValue().getTime()
							+ (timeBegin.getMinutes() * 60000));
					Date endTmp = new Date(dateBegin.getValue().getTime()
							+ (timeEnd.getMinutes() * 60000));
					SingleDate addTermin = new SingleDate(dateBegin.getValue(),
							beginTmp, endTmp, true);

					dateList.add(addTermin);
					*/
				} catch (Exception e) {
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

		// Ausgewählte Ressourcen laden
		chosenResources = new FlowPanel();
		chosenResources.setStyleName("dateInfoLineComplete");

		// Ausgewählte Resourcen laden
		loadChosenResources();
		

		Label headerChosenRes  = new Label("Ausgewaehlte Ressourcen:");
		headerChosenRes.setStyleName("beschriftung");
		
		chosenResources.setStyleName("dateInfoLineComplete");
		chosenResources.add(headerChosenRes);
		
		//-------
		
		
		for(FlowPanel helpList: getPanelResourceContainer()){
			chosenResources.add(helpList);
		}

		DisclosurePanel addResources = new DisclosurePanel(
				"Resourcen Hinzufuegen");
		addResources.setStyleName("dateInfoLineComplete");

		FlowPanel chooseContainer = new FlowPanel();
		chooseContainer.setStyleName("chooseContainer");

		// Baumstruktur für verfügbare Resourcen
		resourceTree = new Tree();

		// Auswählbare Ressourcen laden
		loadResourcesToChoose();
		
		createResourceTree();
		
			
	    chooseContainer.add(resourceTree);
	    chooseContainer.setWidth(width  * 0.85 + "px");
		
	    addResources.setContent(chooseContainer);
		
	    
		//dateInfos.add(begin);
		//dateInfos.add(end);
		//dateInfos.add(cbRepeat);
	    FlowPanel dateContentWrapper = new FlowPanel();
	    dateContentWrapper.add(begin);
	    dateContentWrapper.add(end);
	    dateContentWrapper.add(cbRepeat);
	    dateDisclosurePanel = new DisclosurePanel();
	    dateDisclosurePanel.add(dateContentWrapper);
		dateDisclosurePanel.setOpen(false);
		dateInfos.add(dateDisclosurePanel);
		
		// dateInfos.add(new HTML("<hr  style=\"width:90%;\" />"));
		dateInfos.add(chosenResources);
		dateInfos.add(addResources);

		mainContent.add(dateList);
		mainContent.add(buttonBar);
		mainContent.add(dateInfos);

		return mainContent;
	}



	private void createResourceTree() {
		
		for(List<String> hList : toBeReservedResources){
			String header = hList.get(0);
			hList.remove(0);
			Collections.sort(hList);
			hList.add(0, header);
		}
	
		
		// Create ResourceTree
		for(int i=0; i<toBeReservedResources.size();i++){
			resourceTree.addItem(new TreeItem());
			resourceTree.getItem(i).setText(toBeReservedResources.get(i).get(0).toString());
			resourceTree.getItem(i).setTitle(toBeReservedResources.get(i).get(0).toString());
			for(int j=1;j<toBeReservedResources.get(i).size();j++){
				
				resourceTree.getItem(i).addItem(createCB(toBeReservedResources.get(i).get(j).toString(),toBeReservedResources.get(i).get(0).toString()));

			}
		}
		
	}

	private void loadChosenResources() {
		
		
		List<String> rooms = new ArrayList<String>();
		rooms.add("Raeume");
		rooms.add("A 204");
		
		List<String> cources = new ArrayList<String>();
		cources.add("Kurse");
		cources.add("WWI12B1");
		
		List<String> profs = new ArrayList<String>();
		profs.add("Professoren");
		profs.add("Kuestermann");		
		
		
	    reservedResources.add(rooms);
	    reservedResources.add(profs);
	    reservedResources.add(cources);
		
		
	}

	private void loadResourcesToChoose() {
		//Ressourcen
		
		List<String> room = new ArrayList<String>();
		room.add("Raeume");
		room.add("A 204");
		room.add("A 206");
		room.add("A 205");
		room.add("A 203");
		
		List<String> cource = new ArrayList<String>();
		cource.add("Kurse");
		cource.add("WWI12B1");
		cource.add("WWI12B2");
		cource.add("WWI12B3");
		cource.add("WWI12B4");
		
		List<String> prof = new ArrayList<String>();
		prof.add("Professoren");
		prof.add("Kuestermann");
		prof.add("Freytag");
		prof.add("Daniel");
		prof.add("Wengler");		
		
		
		toBeReservedResources.add(room);
		toBeReservedResources.add(cource);
		toBeReservedResources.add(prof);
		
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
	
	private FlowPanel createResource(String name, String categorie){
		
		final String value = name;
		final String container = categorie;
		FlowPanel resource = new FlowPanel();
		resource.setStyleName("resource");
		resource.setTitle(name);
		
		Label titel = new Label(name);
		titel.setStyleName("resourceTitel");
		resource.add(titel);
		
		Button cross = new Button("X");
		cross.setStyleName("closeCross");

		resource.add(cross);
		cross.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
		
				deleteFromResources(value, container);
				refreshResourceContainer();
				refreshResourceTree();
			}
			
		});
				
		return resource;
	}
	
	private void refreshResourceTree() {
		resourceTree.clear();
		createResourceTree();
		
	}

	private void deleteFromResources(String rText, String cText) {
		
		for(List<String> helperList : reservedResources){
			
			if(helperList.get(0).equals(cText)){
				for(int i=0;i<helperList.size();i++){
					if(helperList.get(i).equals(rText)){
						helperList.remove(i);
						break;
					}
				}

			}
		}
		
		for(List<String> helperList : reservedResources){
			if(helperList.size()<=1){
				reservedResources.remove(helperList);
			}
		}
		
		for(List<String> hList : reservedResources){
			String header = hList.get(0);
			hList.remove(0);
			Collections.sort(hList);
			hList.add(0, header);
		}
		
	}

	private CheckBox createCB(String name, String categorie){
		
		CheckBox helper = new CheckBox(name);
		helper.setTitle(categorie);
		helper.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				CheckBox clicker = (CheckBox) event.getSource();
				if(clicker.getValue()){
					addToResources(clicker.getText(), clicker.getTitle());
					refreshResourceContainer();
				}else{
					deleteFromResources(clicker.getText(), clicker.getTitle());
					refreshResourceContainer();
				}

			}
			
		});
		
		if(isChosenResource(name, categorie)){
			helper.setValue(true);
		}
		
		return helper;
		
	}
	
	
	private boolean isChosenResource(String rtext, String ctext) {
		
		for(List<String> hList : reservedResources){
			if(hList.get(0).equals(ctext)){
				for(String hString : hList){
					if(hString.equals(rtext)){
						return true;
					}
				}
			}
		}
		return false;
		
		
	}

	
	private void refreshResourceContainer() {
		
		deleteResourceContainer();
		
		for(List<String> hList : reservedResources){
			String header = hList.get(0);
			hList.remove(0);
			Collections.sort(hList);
			hList.add(0, header);
		}
		
		for(FlowPanel helpList: getPanelResourceContainer()){
			chosenResources.add(helpList);
		}

	}

	private void deleteResourceContainer() {
		
		int i = 1;
		while( i < chosenResources.getWidgetCount()){
			chosenResources.remove(i);
		}
	}

	private void addToResources(String rText, String rCategorie) {
		
		boolean added = false;
		for(List<String> helperList : reservedResources){
			
			if(helperList.get(0).equals(rCategorie)){
				helperList.add(rText);
				added = true;
			}
		}
		
		if(!added){
			List<String> list = new ArrayList<String>();
			list.add(rCategorie);
			list.add(rText);
			reservedResources.add(list);
		}
		
		
		
	}
	

	private List<FlowPanel> getPanelResourceContainer(){
		
		List<FlowPanel> container = new ArrayList<FlowPanel>();
		
		for(int i=0;i<reservedResources.size();i++){
			container.add(createResourceContainer(reservedResources.get(i).get(0).toString()));
			for(int j=1;j<reservedResources.get(i).size();j++){
				container.get(i).add(createResource(reservedResources.get(i).get(j).toString(), reservedResources.get(i).get(0).toString()));
			}
		}

		return container;
	}	
	
	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

	public FlowPanel getMainContent() {
		return mainContent;
	}

	public void setMainContent(FlowPanel mainContent) {
		this.mainContent = mainContent;
	}

	public FlowPanel getDateList() {
		return dateList;
	}

	public void setDateList(FlowPanel dateList) {
		this.dateList = dateList;
	}

	public FlowPanel getButtonBar() {
		return buttonBar;
	}

	public void setButtonBar(FlowPanel buttonBar) {
		this.buttonBar = buttonBar;
	}

	public DateBox getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(DateBox dateBegin) {
		this.dateBegin = dateBegin;
	}

	public DateBox getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(DateBox dateEnd) {
		this.dateEnd = dateEnd;
	}

	public HourMinutePicker getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(HourMinutePicker timeBegin) {
		this.timeBegin = timeBegin;
	}

	public HourMinutePicker getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(HourMinutePicker timeEnd) {
		this.timeEnd = timeEnd;
	}

	public Tree getResourceTree() {
		return resourceTree;
	}

	public void setResourceTree(Tree resourceTree) {
		this.resourceTree = resourceTree;
	}

	public CheckBox getCbRepeat() {
		return cbRepeat;
	}

	public void setCbRepeat(CheckBox cbRepeat) {
		this.cbRepeat = cbRepeat;
	}

	public FlowPanel getChosenResources() {
		return chosenResources;
	}

	public void setChosenResources(FlowPanel chosenResources) {
		this.chosenResources = chosenResources;
	}

	public Label getButtonNextGap() {
		return buttonNextGap;
	}

	public void setButtonNextGap(Label buttonNextGap) {
		this.buttonNextGap = buttonNextGap;
	}

	public Label getButtonGarbageCan() {
		return buttonGarbageCan;
	}

	public void setButtonGarbageCan(Label buttonGarbageCan) {
		this.buttonGarbageCan = buttonGarbageCan;
	}

	public Label getButtonPlus() {
		return buttonPlus;
	}

	public void setButtonPlus(Label buttonPlus) {
		this.buttonPlus = buttonPlus;
	}

	public ArrayList<List<String>> getToBeReservedResources() {
		return toBeReservedResources;
	}

	public void setToBeReservedResources(ArrayList<List<String>> toBeReservedResources) {
		this.toBeReservedResources = toBeReservedResources;
	}

}
