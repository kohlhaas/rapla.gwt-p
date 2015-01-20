
package org.rapla.client.edit.reservation.sample.gwt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.mockito.cglib.transform.impl.AddPropertyTransformer;
import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ResourceDatesView;
import org.rapla.client.edit.reservation.sample.ResourceDatesView.Presenter;
import org.rapla.entities.domain.Appointment;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.blogspot.ctasada.gwt.eureka.client.ui.*;

public class ResourceDatesViewImpl extends AbstractView<Presenter>  implements ResourceDatesView<IsWidget>{

	private ArrayList<List<String>> toBeReservedResources = new ArrayList<List<String>>();
	private ArrayList<List<String>> reservedResources = new ArrayList<List<String>>();
	
	Panel contentPanel;
	FlowPanel mainContent;
	TerminList dateList;
	FlowPanel buttonBar;


	Label buttonNextGap;
	Label buttonGarbageCan;
	Label buttonPlus;
	// TO-DO: Is this really a Label? Or should it be a Button? Can a Label be
	// used, too?

	DateBox dateBegin;
	DateBox dateEnd;

	SmallTimeBox timeBegin;
	SmallTimeBox timeEnd;
	Label beginTimeText;
	Label endTimeText;

	Tree resourceTree;
	
	CheckBox cbWholeDay;

	FlowPanel chosenResources;
	DisclosurePanel dateDisclosurePanel;
	DisclosurePanel cbRepeatType;
	
	Label addDateInfo;
	Button rewriteDate;
	
	HorizontalPanel repeat;
	RadioButton daily;
	RadioButton weekly;
	RadioButton monthly;
	RadioButton year;
	RadioButton noReccuring;
	int height, width;
	RaplaDate tmp;

	HorizontalPanel end;

	@Override
	public IsWidget provideContent() {
		
		return contentPanel;
	}

	@Override
	public void createContent() {
		
		 height = (int) (Window.getClientHeight() * 0.90 * 0.80);
		 width = (int) (Window.getClientWidth() * 0.90 * 0.80);  

		
		contentPanel = new SimplePanel();
		contentPanel.clear();
	 	
	 	mainContent = new FlowPanel();
	 	dateList = new TerminList();
		dateList.setHeight(height + "px");
		dateList.setStyleName("dateList");

			
			FlowPanel firstDateListWidget = new FlowPanel();
			firstDateListWidget.setStyleName("wildcardPanel");
			Label explainer = new Label("Durch das Dr\u00FCcken des roten Plus-Buttons \u00F6ffnet sich das Fenster zur Erstellung von Terminen");
			explainer.setStyleName("wildcard");
			firstDateListWidget.add(explainer);	
			dateList.add(firstDateListWidget);
			
			
			buttonBar = new FlowPanel();
			buttonBar.setHeight(height + "px");
			buttonBar.setStyleName("datesButtonBar");
			

			// Image buttonNextGap = new Image("button_luecke.png");
			buttonNextGap = new Label(">>");
			buttonNextGap.setStyleName("buttonsResourceDates");

			// Image buttonGarbageCan = new Image("button_eimer.png");
			buttonGarbageCan = new Label("X");
			buttonGarbageCan.setStyleName("buttonsResourceDates");
			buttonGarbageCan.setTitle("Termin l\u00F6schen");
			buttonGarbageCan.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent e) {
		                getPresenter().onGarbageCanButtonClicked();
		            }});
				
			
			// Image buttonPlus = new Image("button_plus.png");
			buttonPlus = new Label("+");
			buttonPlus.setTitle("Termin erstellen");
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
					.getFormat(PredefinedFormat.DATE_MEDIUM);

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
			dateBegin.setValue(new Date(System.currentTimeMillis()));
			dateBegin.setStyleName("dateInput");
			dateBegin.setFormat(new DateBox.DefaultFormat(dateFormat));
			begin.add(dateBegin);

			timeBegin = new SmallTimeBox(new Date(-3600000));
			begin.add(timeBegin);
			beginTimeText = new Label("Uhr");
			beginTimeText.setStyleName("beschriftung");
			begin.add(beginTimeText);
			begin.setCellVerticalAlignment(beginTimeText,
					HasVerticalAlignment.ALIGN_MIDDLE);

			begin.setCellWidth(beginText, "50px");
			begin.setCellWidth(dateBegin, "180px");
			begin.setCellWidth(timeBegin, "80px");
			begin.setCellWidth(beginTimeText, "50px");
			
			// Datum und Uhrzeit ENDE
			end = new HorizontalPanel();
			end.setSpacing(5);
			end.setStyleName("dateInfoLineComplete");
			
			Label endText = new Label("Ende: ");
			endText.setStyleName("beschriftung");
			end.add(endText);
			end.setCellVerticalAlignment(endText, HasVerticalAlignment.ALIGN_MIDDLE);

			dateEnd = new DateBox();
			dateEnd.setValue(new Date(System.currentTimeMillis()));
			dateEnd.setStyleName("dateInput");
			dateEnd.setFormat(new DateBox.DefaultFormat(dateFormat));
			end.add(dateEnd);

			timeEnd = new SmallTimeBox(new Date(-3600000));
			timeEnd.setTitle("endTime");
			end.add(timeEnd.asWidget());

			endTimeText = new Label("Uhr");
			endTimeText.setStyleName("beschriftung");
			end.add(endTimeText);
			end.setCellVerticalAlignment(endTimeText,
					HasVerticalAlignment.ALIGN_MIDDLE);

			end.setCellWidth(endText, "50px");
			
			end.setCellWidth(endText, "50px");
			end.setCellWidth(dateEnd, "180px");
			end.setCellWidth(timeEnd, "80px");
			end.setCellWidth(endTimeText, "50px");
			
			cbWholeDay = new CheckBox("ganzt\u00E4gig");
			begin.add(cbWholeDay);
			cbWholeDay.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {

					getPresenter().onWholeDaySelected();
				}
				
			});
			
			begin.setCellVerticalAlignment(cbWholeDay,
					HasVerticalAlignment.ALIGN_MIDDLE);

			begin.setCellWidth(beginText, "50px");

			// Add termin

			buttonPlus.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onButtonPlusClicked();

				}
			});
			
			// Checkbox WIEDERHOLEN
			repeat = new HorizontalPanel();
			cbRepeatType = new DisclosurePanel("Wiederholen");
			cbRepeatType.setStyleName("dateInfoLineLeft");

			daily = new RadioButton("repeat","t\u00E4glich");
			daily.addClickHandler(new RepeatClickHandler());
			weekly = new RadioButton("repeat", "w\u00F6chentlich");
			weekly.addClickHandler(new RepeatClickHandler());
			monthly = new RadioButton("repeat", "monatlich");
			monthly.addClickHandler(new RepeatClickHandler());
			year = new RadioButton("repeat", "j\u00E4hrlich");
			year.addClickHandler(new RepeatClickHandler());
			noReccuring = new RadioButton("repeat", "keine Wiederholung");
			noReccuring.addClickHandler(new RepeatClickHandler());

			repeat.add(daily);
			repeat.add(weekly);
			repeat.add(monthly);
			repeat.add(year);
			repeat.add(noReccuring);

			cbRepeatType.add(repeat);
			
			HorizontalPanel addDateWithLabel = new HorizontalPanel();
			rewriteDate = new Button("Termin \u00FCberschreiben");
			rewriteDate.setVisible(false);
			addDateInfo = new Label();
			addDateWithLabel.add(rewriteDate);
			addDateWithLabel.add(addDateInfo);
			addDateWithLabel.setCellVerticalAlignment(rewriteDate, HasVerticalAlignment.ALIGN_MIDDLE);
			addDateWithLabel.setCellVerticalAlignment(addDateInfo, HasVerticalAlignment.ALIGN_MIDDLE);
			
			rewriteDate.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onRewriteDateClicked();
				}
				
			});

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

			DisclosurePanel addResources = new DisclosurePanel("Resourcen Hinzufuegen");
			addResources.addOpenHandler(new OpenHandler<DisclosurePanel>() {
					
				  @Override
				  public void onOpen(OpenEvent<DisclosurePanel> event) {
				   getPresenter().onResourcesAdded();
				  }
			});
			
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
			

		    VerticalPanel dateContentWrapper = new VerticalPanel();
		    dateContentWrapper.add(begin);	
		    dateContentWrapper.add(end);
		    dateContentWrapper.add(cbRepeatType);
		    dateContentWrapper.add(addDateWithLabel);
		    dateDisclosurePanel = new DisclosurePanel();
		    dateDisclosurePanel.add(dateContentWrapper);
		    dateDisclosurePanel.setOpen(true);
		
			dateInfos.add(dateDisclosurePanel);
			
			dateInfos.add(new HTML("<hr  style=\"width:90%;\" />"));
			dateInfos.add(chosenResources);
			dateInfos.add(addResources);

			mainContent.add(dateList);
			mainContent.add(buttonBar);
			mainContent.add(dateInfos);

	 
	    contentPanel.add(mainContent);
		
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

	private CheckBox createCB(String name, String category){
		
		CheckBox helper = new CheckBox(name);
		helper.setTitle(category);
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
		
		if(isChosenResource(name, category)){
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
	public void hide() {
		contentPanel.setVisible(false);
		
	}

	@Override
	public void update(List<Appointment> appointments) {
		// TODO Auto-generated method stub
		
	}


	class RepeatClickHandler implements ClickHandler{
		boolean active = false;
		Label blank;
		HorizontalPanel repeatSettings;
		ListBox repeatType;

		@Override
		public void onClick(ClickEvent event) {
			
			if(!(noReccuring.getValue())){
					if(end.getWidgetCount() <= 5){
					active = true;
					end.remove(dateEnd);
					blank = new Label("");
					end.insert(blank,1);
					end.setCellWidth(blank, "180px");
					repeatType = new ListBox();
					repeatType.addItem("Bis Datum");
					repeatType.addItem("x Mal");
					end.add(repeatType);
					end.setCellVerticalAlignment(repeatType, HasVerticalAlignment.ALIGN_MIDDLE);
					end.add(dateEnd);
					cbRepeatType.add(repeatSettings);
					}
					
			}if(noReccuring.getValue()){
				active = false;
				end.remove(1);
				//end.remove(repeatType);
				//end.remove(dateEnd);
				end.insert(dateEnd, 1);
				end.setCellWidth(dateEnd, "180px");
				end.remove(4);
				end.remove(4);
				noReccuring.setValue(false);
				cbRepeatType.setOpen(false);
			}
			
		}
		
	}
	
	@Override
	public void addDateWidget() {
		RaplaDate addTermin = new RaplaDate();
			
		Date beginTmp = new Date(dateBegin.getValue().getTime() + timeBegin.getTime() + 3600000);
		Date endTmp = new Date(dateEnd.getValue().getTime() + timeEnd.getTime() + 3600000);
		
		
		if(beginTmp.after(endTmp)){
		addDateInfo.setStyleName("error");	
		addDateInfo.setText("Begin- nach Endtermin!");
		}else{
			addDateInfo.setStyleName("");
			addDateInfo.setText("");
			if(daily.getValue() || weekly.getValue() || monthly.getValue() || year.getValue()){
				List<RaplaDate> tmp = new ArrayList<>();
				int type;
				if(daily.getValue()){
					type = 1;
				}else if (weekly.getValue()){
					type = 2;
				}else if( monthly.getValue()){
					type = 3;
				}else{
					type = 4;
				}
				tmp = RaplaDate.recurringDates(dateBegin.getValue(), dateEnd.getValue(), timeBegin.getTime() + 3600000,timeEnd.getTime() + 3600000, type);
				try {
					tmp.add(new RaplaDate(beginTmp, new Date(dateBegin.getValue().getTime() + timeEnd.getTime() + 3600000), true));
					addTermin = new RaplaDate(tmp);
					dateList.add(addTermin);
					addTermin.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent e) {
			                getPresenter().onAddTerminButtonClicked(e);
			            }});
					clearDateTimeInputFields();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
			try {	
				addTermin = new RaplaDate(beginTmp, endTmp, true);
				addTermin.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent e) {
		                getPresenter().onAddTerminButtonClicked(e);
		            }});
				dateList.add(addTermin);
				clearDateTimeInputFields();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}
	
	private void clearDateTimeInputFields(){
		dateBegin.setValue(new Date(System.currentTimeMillis()));
		dateEnd.setValue(new Date(System.currentTimeMillis()));
		timeEnd.setValue((long) -3600000);
		timeBegin.setValue((long) -3600000);
		cbWholeDay.setValue(false);
		timeBegin.setVisible(true);
		timeEnd.setVisible(true);
		timeEnd.setValue((long) -3600000);
		beginTimeText.setVisible(true);
		endTimeText.setVisible(true);
		
		rewriteDate.setVisible(false);
		buttonGarbageCan.setStyleName("buttonsResourceDates");
		noReccuring.setValue(true);
		cbRepeatType.setOpen(false);
	}

	@Override
	public void RewriteDate() {
		int active = dateList.getActive();
		dateList.removeDate(active);
		addDateWidget();		
	}

	@Override
	public void setVisiblityOfDateElements() {

		if( cbWholeDay.getValue()){
			timeBegin.setVisible(false);
			timeEnd.setVisible(false);
			timeEnd.setValue((long) (3600000*23)-60);
			beginTimeText.setVisible(false);
			endTimeText.setVisible(false);
		}else{
			timeBegin.setVisible(true);
			timeEnd.setVisible(true);
			timeEnd.setValue((long) -3600000);
			beginTimeText.setVisible(true);
			endTimeText.setVisible(true);
	}
	}

	@Override
	public void clearInputFields() {
		if(buttonGarbageCan.getStyleName().equals("buttonsResourceDatesClickable")){
			dateList.removeDate(dateList.getActive());
			clearDateTimeInputFields();
			buttonGarbageCan.setStyleName("buttonsResourceDates");			
	}}

	@Override
	public void addResources() {
			dateDisclosurePanel.setOpen(false);
			buttonPlus.setStyleName("buttonsResourceDatesClickable");		
	}

	
	

	@Override
	public void setRaplaDate(RaplaDate tmp) {

		this.tmp = tmp;
		dateList.setActive(tmp);
		if(!(dateList.getActive() == -1)){
			dateBegin.setValue(tmp.getBeginTime());
			dateEnd.setValue(tmp.getEndTime());
			timeBegin.setValue((long)-3600000 + tmp.getStartHourMinute());
			timeEnd.setValue((long)-3600000 + tmp.getEndHourMinute());
			buttonGarbageCan.setStyleName("buttonsResourceDatesClickable");
			rewriteDate.setVisible(true);
			tmp.setStyleName("singleDateClicked");
		}else{
			clearDateTimeInputFields();
			buttonGarbageCan.setStyleName("buttonsResourceDates");
			tmp.removeStyleName("singleDateClicked");
			tmp.setStyleName("singleDate");
		}
		

	}

	@Override
	public void openEditView() {
		try {
			if(!dateDisclosurePanel.isOpen()){
			dateDisclosurePanel.setOpen(true);
			buttonPlus.setStyleName("buttonsResourceDates");
			}else{
				addDateWidget();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void show() {
		contentPanel.setVisible(true);
		
	}


}
