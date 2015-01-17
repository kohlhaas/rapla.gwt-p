package org.rapla.client.edit.reservation.sample.gwt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
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

	Tree resourceTree;
	
	CheckBox cbWholeDay;

	FlowPanel chosenResources;
	DisclosurePanel dateDisclosurePanel;
	DisclosurePanel cbRepeatType;
	
	Label addDateInfo;
	Button addDate;
	Button rewriteDate;
	
	HorizontalPanel repeat;
	RadioButton daily;
	RadioButton weekly;
	RadioButton monthly;
	RadioButton year;
	RadioButton noReccuring;
	int height, width;


	@Override
	public IsWidget provideContent() {
		
	
		return contentPanel;
	}

	@Override
	public void show() {
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
			buttonPlus.setStyleName("buttonsResourceDatesClickable");

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
			dateBegin.setStyleName("dateInput");
			dateBegin.setFormat(new DateBox.DefaultFormat(dateFormat));
			begin.add(dateBegin);

			timeBegin = new SmallTimeBox(new Date(-3600000));
			begin.add(timeBegin);
			final Label beginTimeText = new Label("Uhr");
			beginTimeText.setStyleName("beschriftung");
			begin.add(beginTimeText);
			begin.setCellVerticalAlignment(beginTimeText,
					HasVerticalAlignment.ALIGN_MIDDLE);

			
			// Datum und Uhrzeit ENDE
			HorizontalPanel end = new HorizontalPanel();
			end.setSpacing(4);
			end.setStyleName("dateInfoLineComplete");
			
			Label endText = new Label("Ende: ");
			endText.setStyleName("beschriftung");
			end.add(endText);
			end.setCellVerticalAlignment(endText, HasVerticalAlignment.ALIGN_MIDDLE);

			dateEnd = new DateBox();
			dateEnd.setStyleName("dateInput");
			dateEnd.setFormat(new DateBox.DefaultFormat(dateFormat));
			end.add(dateEnd);

			timeEnd = new SmallTimeBox(new Date(-3600000));
			timeEnd.setTitle("endTime");
			end.add(timeEnd.asWidget());

			final Label endTimeText = new Label("Uhr");
			endTimeText.setStyleName("beschriftung");
			end.add(endTimeText);
			end.setCellVerticalAlignment(endTimeText,
					HasVerticalAlignment.ALIGN_MIDDLE);

			end.setCellWidth(endText, "50px");
			
			
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
			weekly = new RadioButton("repeat", "w\u00F6chentlich");
			monthly = new RadioButton("repeat", "monatlich");
			year = new RadioButton("repeat", "j\u00E4hrlich");
			noReccuring = new RadioButton("repeat", "keine Wiederholung");

			repeat.add(daily);
			repeat.add(weekly);
			repeat.add(monthly);
			repeat.add(year);
			repeat.add(noReccuring);

			cbRepeatType.add(repeat);
			
			HorizontalPanel addDateWithLabel = new HorizontalPanel();
			addDate = new Button("Termin hinzuf\u00FCgen");
			rewriteDate = new Button("Termin \u00FCberschreiben");
			rewriteDate.setVisible(false);
			addDateInfo = new Label();
			addDateWithLabel.add(addDate);
			addDateWithLabel.add(rewriteDate);
			addDateWithLabel.add(addDateInfo);
			addDateWithLabel.setCellVerticalAlignment(addDate, HasVerticalAlignment.ALIGN_MIDDLE);
			addDateWithLabel.setCellVerticalAlignment(rewriteDate, HasVerticalAlignment.ALIGN_MIDDLE);
			addDateWithLabel.setCellVerticalAlignment(addDateInfo, HasVerticalAlignment.ALIGN_MIDDLE);
			
			rewriteDate.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onRewriteDateClicked();
				}
				
			});
			
			addDate.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onRewriteDateClicked();
				}
				
			});
		

			// Ausgewählte Ressourcen laden
			chosenResources = new FlowPanel();
			chosenResources.setStyleName("dateInfoLineComplete");

			// Ausgewählte Resourcen laden
			//loadChosenResources();
			

			Label headerChosenRes  = new Label("Ausgewaehlte Ressourcen:");
			headerChosenRes.setStyleName("beschriftung");
			
			chosenResources.setStyleName("dateInfoLineComplete");
			chosenResources.add(headerChosenRes);
			
			//-------
			
			
//			for(FlowPanel helpList: getPanelResourceContainer()){
//				chosenResources.add(helpList);
//			}

			DisclosurePanel addResources = new DisclosurePanel("Resourcen Hinzufuegen");
			addResources.addOpenHandler(new OpenHandler<DisclosurePanel>() {
					
				  @Override
				  public void onOpen(OpenEvent<DisclosurePanel> event) {
				    dateDisclosurePanel.setOpen(false);
					buttonPlus.setStyleName("buttonsResourceDatesClickable");
				  }
			});
			
			addResources.setStyleName("dateInfoLineComplete");

			FlowPanel chooseContainer = new FlowPanel();
			chooseContainer.setStyleName("chooseContainer");

			// Baumstruktur für verfügbare Resourcen
			resourceTree = new Tree();

			// Auswählbare Ressourcen laden
		//	loadResourcesToChoose();
			
			 //createResourceTree();
			
				
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
			dateDisclosurePanel.setOpen(false);
			dateInfos.add(dateDisclosurePanel);
			
			dateInfos.add(new HTML("<hr  style=\"width:90%;\" />"));
			dateInfos.add(chosenResources);
			dateInfos.add(addResources);

			mainContent.add(dateList);
			mainContent.add(buttonBar);
			mainContent.add(dateInfos);

	 
	    contentPanel.add(mainContent);
		
	}

	@Override
	public void hide() {
		contentPanel.setVisible(false);
		
	}

	@Override
	public void update(List<Appointment> appointments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeightAndWidth(int height, int width) {
		// TODO Auto-generated method stub
		
	}



}
