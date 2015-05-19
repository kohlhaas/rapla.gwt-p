package org.rapla.client.edit.reservation.sample.gwt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ResourceDatesView;
import org.rapla.client.edit.reservation.sample.ResourceDatesView.Presenter;
import org.rapla.client.mwi14_1.ImageImport;
import org.rapla.entities.domain.Appointment;
import org.rapla.framework.logger.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.blogspot.ctasada.gwt.eureka.client.ui.*;

public class ResourceDatesViewImpl extends AbstractView<Presenter> implements
		ResourceDatesView<IsWidget> {

	private ArrayList<List<String>> toBeReservedResources = new ArrayList<List<String>>();
	private ArrayList<List<String>> reservedResources = new ArrayList<List<String>>();
	private static final ImageResource IMG_CROSS = ImageImport.INSTANCE
			.crossIcon();
	private static final ImageResource IMG_PLUS = ImageImport.INSTANCE
			.plusIcon();
	private static final ImageResource IMG_NEXT = ImageImport.INSTANCE
			.nextIcon();
	private static final ImageResource IMG_LOUPE = ImageImport.INSTANCE
			.loupeIcon();
	private static final ImageResource IMG_FILTER = ImageImport.INSTANCE
			.filterIcon();
	private static final ImageResource IMG_CROSS_GREY = ImageImport.INSTANCE
			.crossGreyIcon();
	private static final ImageResource IMG_PLUS_GREY = ImageImport.INSTANCE
			.plusGreyIcon();
	private static final ImageResource IMG_NEXT_GREY = ImageImport.INSTANCE
			.nextGreyIcon();
	private static final ImageResource IMG_CHANGE = ImageImport.INSTANCE
			.changeIcon();

	@Inject
	Logger logger;

	Panel contentPanel;
	FlowPanel mainContent;
	TerminList dateList;
	FlowPanel buttonBar;

	Image buttonNextGap, buttonGarbageCan, buttonPlus;

	DateBox dateBegin;
	DateBox dateEnd;

	SmallTimeBox timeBegin;
	SmallTimeBox timeEnd;
	Label beginTimeText;
	Label endTimeText;

	DisclosurePanel addResources;
	Tree resourceTree;

	CheckBox cbWholeDay;

	FlowPanel chosenResources;
	DisclosurePanel dateDisclosurePanel;
	DisclosurePanel cbRepeatType;

	FlowPanel dateInfos;
	Label addDateInfo;

	HorizontalPanel repeat, suche;
	RadioButton daily;
	RadioButton weekly;
	RadioButton monthly;
	RadioButton year;
	RadioButton noReccuring;
	ListBox repeatType;
	Label repeatText;

	int height, width;
	RaplaDate currentDate;

	HorizontalPanel end;
	HorizontalPanel begin;
	HorizontalPanel repeatSettings = new HorizontalPanel();

	Button setResourcesToAll;

	ListBox filterEintr;
	FlowPanel errorPanel;

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
		Label explainer = new Label(
				"Durch das Dr\u00FCcken des Plus-Buttons bei ausgef\u00FCllten Termindaten, wird ein Termin hinzugef\u00FCgt");
		explainer.setStyleName("wildcard");
		firstDateListWidget.add(explainer);
		dateList.add(firstDateListWidget);

		FlowPanel placeholderSetResourcesToAll = new FlowPanel();
		placeholderSetResourcesToAll.setStyleName("resourceButtonPanel");
		setResourcesToAll = new Button(
				"Ressourcen f\u00FCr alle \u00FCbernehmen");
		setResourcesToAll.setStyleName("resourceButton");
		setResourcesToAll.setVisible(false);
		placeholderSetResourcesToAll.add(setResourcesToAll);
		dateList.add(placeholderSetResourcesToAll);

		setResourcesToAll.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getPresenter().onSetResourcesToAllClicked();

			}
		});

		this.dateList.setFirstWidget(true);

		addResources = new DisclosurePanel("Ressourcen hinzuf\u00FCgen");
		addResources.setStyleName("dateInfoLineComplete");

		buttonBar = new FlowPanel();
		buttonBar.setHeight(height + "px");
		buttonBar.setStyleName("datesButtonBar");

		buttonNextGap = new Image(IMG_NEXT_GREY);
		// buttonNextGap = new Label(">>");
		buttonNextGap.setStyleName("buttonsResourceDates");

		buttonGarbageCan = new Image(IMG_CROSS_GREY);
		// buttonGarbageCan = new Label("X");
		buttonGarbageCan.setStyleName("buttonsResourceDates");
		buttonGarbageCan.setTitle("Termin l\u00F6schen");
		buttonGarbageCan.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				getPresenter().onGarbageCanButtonClicked();
			}
		});

		buttonPlus = new Image(IMG_PLUS);
		// buttonPlus = new Label("+");
		buttonPlus.setTitle("Termin erstellen");
		buttonPlus.setStyleName("buttonsResourceDates");

		buttonBar.add(buttonPlus);
		buttonBar.add(buttonGarbageCan);
		buttonBar.add(buttonNextGap);

		dateInfos = new FlowPanel();
		dateInfos.setHeight(height + "px");
		dateInfos.setStyleName("dateInfos");

		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat(PredefinedFormat.DATE_MEDIUM);

		// initialize and declarate Panel and Elements for Begin Time and Date
		begin = new HorizontalPanel();
		begin.setSpacing(5);
		begin.setStyleName("dateInfoLineComplete");

		Label beginText = new Label("Beginn:");
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

		// initialize and declarate Panel and Elements for End Time and Date
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
		cbWholeDay.addClickHandler(new ClickHandler() {

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

		// Checkbox reccuring dates
		repeat = new HorizontalPanel();
		cbRepeatType = new DisclosurePanel("Wiederholen");
		cbRepeatType.setStyleName("dateInfoLineLeft");

		daily = new RadioButton("repeat", "t\u00E4glich");
		daily.addClickHandler(new RepeatClickHandler());
		weekly = new RadioButton("repeat", "w\u00F6chentlich");
		weekly.addClickHandler(new RepeatClickHandler());
		monthly = new RadioButton("repeat", "monatlich");
		monthly.addClickHandler(new RepeatClickHandler());
		year = new RadioButton("repeat", "j\u00E4hrlich");
		year.addClickHandler(new RepeatClickHandler());
		noReccuring = new RadioButton("repeat", "keine Wiederholung");
		noReccuring.addClickHandler(new RepeatClickHandler());

		repeatType = new ListBox();
		repeatType.addItem("Bis Datum");
		repeatType.addItem("x Mal");

		repeatText = new Label("Beginn: ");
		repeatText.setStyleName("beschriftung");

		repeat.add(daily);
		repeat.add(weekly);
		repeat.add(monthly);
		repeat.add(year);
		repeat.add(noReccuring);

		cbRepeatType.add(repeat);

		HorizontalPanel addDateWithLabel = new HorizontalPanel();
		addDateInfo = new Label();
		addDateWithLabel.add(addDateInfo);
		addDateWithLabel.setCellVerticalAlignment(addDateInfo,
				HasVerticalAlignment.ALIGN_MIDDLE);

		// Ausgewählte Ressourcen laden
		chosenResources = new FlowPanel();
		chosenResources.setStyleName("dateInfoLineComplete");

		// Ausgewählte Resourcen laden
		// ArrayList<List<String>> testRessourcen = new
		// ArrayList<List<String>>();
		//
		// List<String> rooms = new ArrayList<String>();
		// rooms.add("Raeume");
		// rooms.add("A 204");
		//
		// List<String> cources = new ArrayList<String>();
		// cources.add("Kurse");
		// cources.add("WWI12B1");
		//
		// List<String> profs = new ArrayList<String>();
		// // profs.addAll(this.loadProfs());
		// profs.add("Professoren");
		// profs.add("Kuestermann");
		//
		// testRessourcen.add(rooms);
		// testRessourcen.add(cources);
		// testRessourcen.add(profs);
		//
		// loadChosenResources(testRessourcen);
		//

		Label headerChosenRes = new Label("Ausgew\u00E4hlte Ressourcen:");
		headerChosenRes.setStyleName("beschriftung");

		chosenResources.setStyleName("dateInfoLineComplete");
		chosenResources.add(headerChosenRes);
		
		FlowPanel explainer2Panel = new FlowPanel();
		explainer2Panel.setStyleName("wildcardPanel");
		
		Label explainer2 = new Label("Es wurden bisher keine Ressourcen ausgewählt");
		explainer2.setStyleName("wildcard");
		
		explainer2Panel.add(explainer2);
		
		chosenResources.add(explainer2Panel);
		
		// -------

		for (FlowPanel helpList : getPanelResourceContainer()) {
			chosenResources.add(helpList);
		}

		addResources = new DisclosurePanel("Ressourcen hinzuf\u00FCgen");
		addResources.setStyleName("dateInfoLineComplete");

		FlowPanel chooseContainer = new FlowPanel();
		chooseContainer.setStyleName("chooseContainer");

		// Baumstruktur f\u00FCr verf\u00FCgbare Resourcen
		resourceTree = new Tree();

		// Auswählbare Ressourcen laden

		//
		// loadResourcesToChoose();

		// createResourceTree();

		// Filter
		Image filter = new Image(IMG_FILTER);
		filter.setStyleName("buttonFilter");
		filter.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				getPresenter().onFilterClicked();
			}

		});

		filterEintr = new ListBox();
		filterEintr.addItem("Verf\u00FCgbare Ressourcen");
		filterEintr.addItem("Nicht Verf\u00FCgabre Ressourcen");
		filterEintr.addItem("Kurse");
		filterEintr.addItem("R\u00E4ume");
		filterEintr.addItem("Professoren");
		filterEintr.setStyleName("filterWindow");
		filterEintr.setMultipleSelect(true);
		filterEintr.setVisible(false);
		
		// Suchfeld
		suche = new HorizontalPanel();
		suche.setStyleName("suchfeld");
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		oracle.add("WWI12B1");
		oracle.add("K\u00FCstermann");
		oracle.add("Daniel");
		oracle.add("B343");

		SuggestBox searchField = new SuggestBox(oracle);
		searchField.setWidth("300px");
		searchField.setStyleName("searchInput");

		Image loupe = new Image(IMG_LOUPE);
		loupe.setStyleName("buttonLoupe");

		suche.add(searchField);
		suche.add(loupe);
		suche.add(filter);
		suche.add(filterEintr);

		chooseContainer.add(suche);
		chooseContainer.add(resourceTree);
		// chooseContainer.setWidth(width * 0.85 + "px");

		addResources.setContent(chooseContainer);

		VerticalPanel dateContentWrapper = new VerticalPanel();
		dateContentWrapper.add(begin);
		dateContentWrapper.add(end);
		dateContentWrapper.add(cbRepeatType);
		dateContentWrapper.add(addDateWithLabel);
		dateContentWrapper.add(repeatSettings);
		dateContentWrapper.setBorderWidth(0);
//		dateDisclosurePanel = new DisclosurePanel();
//		dateDisclosurePanel.add(dateContentWrapper);
//		dateDisclosurePanel.setOpen(true);
			
		dateInfos.add(dateContentWrapper);

	//	dateInfos.add(new HTML("<hr  style=\"width:90%;\" />"));
		dateInfos.add(chosenResources);
		dateInfos.add(addResources);

		mainContent.add(dateList);
		mainContent.add(buttonBar);
		mainContent.add(dateInfos);

		contentPanel.add(mainContent);

	}

	public void createResourceTree() {

		for (List<String> hList : toBeReservedResources) {
			String header = hList.get(0);
			hList.remove(0);
			Collections.sort(hList);
			hList.add(0, header);
		}

		// Create ResourceTree
		for (int i = 0; i < toBeReservedResources.size(); i++) {
			resourceTree.addItem(new TreeItem());
			resourceTree.getItem(i).setText(
					toBeReservedResources.get(i).get(0).toString());
			resourceTree.getItem(i).setTitle(
					toBeReservedResources.get(i).get(0).toString());
			for (int j = 1; j < toBeReservedResources.get(i).size(); j++) {

				resourceTree.getItem(i)
						.addItem(
								createCB(toBeReservedResources.get(i).get(j)
										.toString(),
										toBeReservedResources.get(i).get(0)
												.toString()));

			}
		}

	}

	public void loadChosenResources(ArrayList<List<String>> res) {

		reservedResources.clear();
		reservedResources = copyResourceArray(res);

		// List<String> rooms = new ArrayList<String>();
		// rooms.add("Raeume");
		// rooms.add("A 204");
		//
		// List<String> cources = new ArrayList<String>();
		// cources.add("Kurse");
		// cources.add("WWI12B1");
		//
		// List<String> profs = new ArrayList<String>();
		// profs.add("Professoren");
		// profs.add("Kuestermann");
		//
		//
		// reservedResources.add(rooms);
		// reservedResources.add(profs);
		// reservedResources.add(cources);

	}

	//
	// private void loadResourcesToChoose() {
	//
	//
	//
	// ArrayList<List<String>> ressourcenZwei = new ArrayList<List<String>>();
	//
	// List<String> rooms = new ArrayList<String>();
	// rooms.add("Raeume");
	// rooms.add("A 203");
	// rooms.add("A 204");
	// rooms.add("A 205");
	// rooms.add("A 206");
	//
	// List<String> cources = new ArrayList<String>();
	// cources.add("Kurse");
	// cources.add("WWI12B1");
	// cources.add("WWI12B2");
	// cources.add("WWI12B3");
	// cources.add("WWI12B4");
	//
	// List<String> profs = new ArrayList<String>();
	// profs.add("Professoren");
	// profs.add("Kuestermann");
	// profs.add("Wengler");
	// profs.add("Wallrath");
	// profs.add("Freytag");
	//
	// ressourcenZwei.add(rooms);
	// ressourcenZwei.add(cources);
	// ressourcenZwei.add(profs);
	//
	//
	// toBeReservedResources.clear();
	// toBeReservedResources = copyResourceArray(ressourcenZwei);
	//
	// //
	// // toBeReservedResources.add(room);
	// // toBeReservedResources.add(cource);
	// // toBeReservedResources.add(prof);
	//
	// }

	private FlowPanel createResourceContainer(String name) {

		FlowPanel container = new FlowPanel();
		container.setStyleName("resourceContainer");
		Label titel = new Label(name);
		titel.setStyleName("beschriftung");
		titel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		container.add(titel);

		return container;
	}

	private FlowPanel createResource(String name, String categorie) {

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
		cross.addClickHandler(new ClickHandler() {

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

		for (List<String> helperList : reservedResources) {

			if (helperList.get(0).equals(cText)) {
				for (int i = 0; i < helperList.size(); i++) {
					if (helperList.get(i).equals(rText)) {
						helperList.remove(i);
						break;
					}
				}

			}
		}

		for (List<String> helperList : reservedResources) {
			if (helperList.size() <= 1) {
				reservedResources.remove(helperList);
			}
		}

		for (List<String> hList : reservedResources) {
			String header = hList.get(0);
			hList.remove(0);
			Collections.sort(hList);
			hList.add(0, header);
		}

	}

	private CheckBox createCB(String name, String category) {

		CheckBox helper = new CheckBox(name);
		helper.setTitle(category);
		helper.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				CheckBox clicker = (CheckBox) event.getSource();
				if (clicker.getValue()) {
					addToResources(clicker.getText(), clicker.getTitle());
					refreshResourceContainer();
				} else {
					deleteFromResources(clicker.getText(), clicker.getTitle());
					refreshResourceContainer();
				}

			}

		});

		if (isChosenResource(name, category)) {
			helper.setValue(true);
		}

		return helper;

	}

	private boolean isChosenResource(String rtext, String ctext) {

		for (List<String> hList : reservedResources) {
			if (hList.get(0).equals(ctext)) {
				for (String hString : hList) {
					if (hString.equals(rtext)) {
						return true;
					}
				}
			}
		}
		return false;

	}

	private void refreshResourceContainer() {

		deleteResourceContainer();

		for (List<String> hList : reservedResources) {
			String header = hList.get(0);
			hList.remove(0);
			Collections.sort(hList);
			hList.add(0, header);
		}

		for (FlowPanel helpList : getPanelResourceContainer()) {
			chosenResources.add(helpList);
		}

	}

	private void deleteResourceContainer() {

		int i = 1;
		while (i < chosenResources.getWidgetCount()) {
			chosenResources.remove(i);
		}
	}

	private void addToResources(String rText, String rCategorie) {

		boolean added = false;
		for (List<String> helperList : reservedResources) {

			if (helperList.get(0).equals(rCategorie)) {
				helperList.add(rText);
				added = true;
			}
		}

		if (!added) {
			List<String> list = new ArrayList<String>();
			list.add(rCategorie);
			list.add(rText);
			reservedResources.add(list);
		}

	}

	private List<FlowPanel> getPanelResourceContainer() {

		List<FlowPanel> container = new ArrayList<FlowPanel>();

		for (int i = 0; i < reservedResources.size(); i++) {
			container.add(createResourceContainer(reservedResources.get(i)
					.get(0).toString()));
			for (int j = 1; j < reservedResources.get(i).size(); j++) {
				container.get(i).add(
						createResource(reservedResources.get(i).get(j)
								.toString(), reservedResources.get(i).get(0)
								.toString()));
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

	class RepeatClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			getPresenter().onrepeatTypeClicked(event);
		}
	}

	@Override
	public void addDateWidget() {

		logger.warn("datelist widget countp1: " + dateList.getWidgetCount());
		RaplaDate addTermin = new RaplaDate();

		Date beginTmp = new Date(dateBegin.getValue().getTime()
				+ timeBegin.getTime() + 3600000);
		Date endTmp = new Date(dateEnd.getValue().getTime() + timeEnd.getTime()
				+ 3600000);

		if (beginTmp.after(endTmp)) {
			addDateInfo.setStyleName("error");
			addDateInfo.setText("Beginn- nach Endtermin!");
		} else {
			addDateInfo.setStyleName("");
			addDateInfo.setText("");
			if (daily.getValue() || weekly.getValue() || monthly.getValue()
					|| year.getValue()) {
				List<RaplaDate> tmp = new ArrayList<>();
				int type;
				if (daily.getValue()) {
					type = 1;
				} else if (weekly.getValue()) {
					type = 2;
				} else if (monthly.getValue()) {
					type = 3;
				} else {
					type = 4;
				}
				tmp = RaplaDate.recurringDates(dateBegin.getValue(),
						dateEnd.getValue(), timeBegin.getTime() + 3600000,
						timeEnd.getTime() + 3600000,
						copyResourceArray(reservedResources), type);
				try {
					tmp.add(new RaplaDate(beginTmp,
							new Date(dateBegin.getValue().getTime()
									+ timeEnd.getTime() + 3600000),
							copyResourceArray(reservedResources), true));
					addTermin = new RaplaDate(tmp, type);
					addTermin.setStyleName("singleDate");
					dateList.add(addTermin);
					addTermin.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent e) {
							getPresenter().onAddTerminButtonClicked(e);
						}
					});
					clearDateTimeInputFields();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					addTermin = new RaplaDate(beginTmp, endTmp,
							copyResourceArray(reservedResources), true);
					addTermin.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent e) {
							getPresenter().onAddTerminButtonClicked(e);
						}
					});
					dateList.add(addTermin);
					clearDateTimeInputFields();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		logger.warn("datelist widget countp2: " + dateList.getWidgetCount());
	}

	private void clearDateTimeInputFields() {
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

		setResourcesToAll.setVisible(false);
		buttonGarbageCan.setResource(IMG_CROSS_GREY);
		buttonPlus.setResource(IMG_PLUS);
		setRepeatTypeSettings(noReccuring);
	}

	@Override
	public void setVisiblityOfDateElements() {

		if (cbWholeDay.getValue()) {
			timeBegin.setVisible(false);
			timeEnd.setVisible(false);
			timeEnd.setValue((long) (3600000 * 23) - 60);
			beginTimeText.setVisible(false);
			endTimeText.setVisible(false);
		} else {
			timeBegin.setVisible(true);
			timeEnd.setVisible(true);
			timeEnd.setValue((long) -3600000);
			beginTimeText.setVisible(true);
			endTimeText.setVisible(true);
		}
	}

	@Override
	public void clearInputFields() {
		// if(buttonGarbageCan.getStyleName().equals("buttonsResourceDatesClickable")){
		dateList.removeDate(dateList.getActive());
		clearDateTimeInputFields();
		buttonGarbageCan.setResource(IMG_CROSS_GREY);
		buttonPlus.setResource(IMG_PLUS);
		buttonPlus.setTitle("Termin erstellen");
		// }
	}

	// ClickHandler: Actions when clicking on a created Date
	@Override
	public void setRaplaDate(RaplaDate clickedDate) {
		logger.warn("setRaplaDate");
		int prevActive = dateList.getActive() == -1 ? -1 : dateList.getActive();
		logger.warn("Active index1: " + dateList.getActive());
		this.currentDate = clickedDate;
		dateList.setActive(currentDate);
		logger.warn("Active index2: " + dateList.getActive());

		// Loading the Date, Time and other date modification elements
		if (!(dateList.getActive() == -1)) {
			if (dateList.getWidgetCount() > 2)
				setResourcesToAll.setVisible(true);
			logger.warn("1 ");

			dateBegin.setValue(currentDate.getBeginTime());
			dateEnd.setValue(currentDate.getEndTime());
			timeBegin.setValue((long) -3600000
					+ currentDate.getStartHourMinute());
			timeEnd.setValue((long) -3600000 + currentDate.getEndHourMinute());
			if(currentDate.isReccuringDate()){
			setRepeatTypeSettings(daily);
			}
			

			buttonGarbageCan.setResource(IMG_CROSS);
			buttonPlus.setResource(IMG_CHANGE);
			buttonPlus.setTitle("Termin \u00FCberschreiben");

			logger.warn("1b ");
			reservedResources.clear();
			if (currentDate.getResources().size() > 0) {
				// reservedResources = copyResourceArray(currentDate
				// .getResources());
				this.loadChosenResources(currentDate.getResources());
			} else {
				ArrayList<List<String>> res = new ArrayList<List<String>>();
				this.loadChosenResources(res);
			}
			refreshResourceContainer();
			refreshResourceTree();

			logger.warn("1c ");
			dateList.setStyle(dateList.getRaplaDateIndex(clickedDate),
					"singleDateClicked");
			logger.warn("1c2 ");
			if (prevActive != -1) {
				dateList.removeStyle(prevActive, "singleDateClicked");
				dateList.setStyle(prevActive, "singleDate");
			}

			logger.warn("1d ");

		} else {
			logger.warn("2 ");

			clearDateTimeInputFields();
			buttonGarbageCan.setResource(IMG_CROSS_GREY);
			buttonPlus.setResource(IMG_PLUS);
			buttonPlus.setTitle("Termin erstellen");
			currentDate.removeStyleName("singleDateClicked");
			currentDate.setStyleName("singleDate");
		}
		logger.warn("end ");

	}

	@Override
	public void openEditView() {
		logger.warn("openEditView ");
		logger.warn("datelist widget count: " + dateList.getWidgetCount());
		if (buttonPlus.getTitle().equals("Termin \u00FCberschreiben")) {
			logger.warn("Termin \u00FCberschreiben");

			logger.warn("last pos: " + dateList.getLastPosition());
			int active = dateList.getActive();
			logger.warn("Active index: " + active);
			dateList.removeDate(active);
			addDateWidget();
			buttonPlus.setTitle("Termin erstellen");

		} else {
			addDateWidget();
		}

	}

	@Override
	public void show() {
		contentPanel.setVisible(true);

	}

	@Override
	public void setRepeatTypeSettings(Widget sender) {
		// TODO Auto-generated method stub
		if (sender != noReccuring) {
			end.remove(dateEnd);
			begin.remove(dateBegin);

			repeatSettings.add(repeatText);
			repeatSettings.add(dateBegin);
			repeatSettings.add(repeatType);
			repeatSettings.add(dateEnd);
			repeatSettings.setCellWidth(repeatText, "50px");
			repeatSettings.setCellWidth(dateBegin, "180px");
			repeatSettings.setCellWidth(dateEnd, "180px");
			repeatSettings.setCellVerticalAlignment(repeatType,
					HasVerticalAlignment.ALIGN_MIDDLE);
			repeatSettings.setCellVerticalAlignment(repeatText,
					HasVerticalAlignment.ALIGN_MIDDLE);
			if(sender == daily){
				daily.setValue(true);
				cbRepeatType.setOpen(true);
			}
		}
		if (sender == noReccuring) {
			// end.remove(1);
			repeatSettings.remove(repeatText);
			repeatSettings.remove(dateBegin);
			repeatSettings.remove(repeatType);
			repeatSettings.remove(dateEnd);

			begin.insert(dateBegin, 1);
			begin.setCellWidth(dateBegin, "180px");

			// end.remove(repeatType);
			// end.remove(dateEnd);
			end.insert(dateEnd, 1);
			end.setCellWidth(dateEnd, "180px");
			noReccuring.setValue(true);
			cbRepeatType.setOpen(false);

		}

	}

	@Override
	public void setResourcesToAllDates() {
		// TODO Auto-generated method stub
		// reset view
		setRaplaDate(dateList.getDate(dateList.getActive()));
		final List<RaplaDate> errorListAll = dateList.checkConflict();
		List<RaplaDate> errorList = new ArrayList<>();
		HashSet<RaplaDate> errorSet = new HashSet<>();
		errorSet.addAll(errorListAll);
		errorList.addAll(errorSet);
		Collections.sort(errorList);
		final HashMap<FlowPanel, RaplaDate> keyValueMap = new HashMap<>();
		FlowPanel dateElement;
		if (!errorList.isEmpty()) {
			errorPanel = new FlowPanel();
			VerticalPanel top = new VerticalPanel();
			Label headerInfo = new Label(
					"\u00DCberschneidung von Ressourcen bei folgenden Terminen");
			headerInfo.setStyleName("errorPanelTitle");
			top.add(headerInfo);
			top.setCellHorizontalAlignment(headerInfo,
					HasHorizontalAlignment.ALIGN_CENTER);

			HorizontalPanel raplaDatePanel = new HorizontalPanel();
			raplaDatePanel.addStyleName("raplaDatePanel");
			final VerticalPanel conflictPanel = new VerticalPanel();
			conflictPanel.setStyleName("conflictPanel");
			final Label conflictPanelLabel = new Label(
					"Konflikte bestehen mit:");
			conflictPanelLabel.addStyleName("conflictPanelLabel");
			conflictPanel.add(conflictPanelLabel);

			ClickHandler handler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// getPresenter().onErrorRaplaDateClick();
					FlowPanel sender = (FlowPanel) event.getSource();
					RaplaDate senderDate = keyValueMap.get(sender);
					/*
					 * Iterator<Entry<FlowPanel, RaplaDate>> it =
					 * keyValueMap.entrySet().iterator(); while (it.hasNext()) {
					 * HashMap.Entry<FlowPanel, RaplaDate> pair =
					 * (HashMap.Entry<FlowPanel, RaplaDate>)it.next();
					 * pair.getKey().removeStyleName("dateElementClicked");
					 * it.remove(); // avoids a ConcurrentModificationException
					 * }
					 * 
					 * sender.setStyleName("dateElementClicked");
					 */
					reservedResources.clear();
					reservedResources = copyResourceArray(senderDate
							.getResources());

					refreshResourceContainer();
					refreshResourceTree();

					conflictPanel.clear();
					conflictPanel.add(conflictPanelLabel);
					conflictPanel.add(chosenResources);
					loadChosenResources(senderDate.getResources());
					Label conflictLabel = new Label("Bestehenden Terminen:");
					conflictLabel.setStyleName("beschriftung");
					conflictPanel.add(conflictLabel);
					for (int i = 0; i < errorListAll.size(); i++) {
						if (senderDate == errorListAll.get(i)) {
							// check if even or odd
							Label errorPanelElementTitle = new Label(
									"Titel der Vorlesung");
							errorPanelElementTitle
									.setStyleName("errorPanelElementTitle");
							if ((i & 1) == 0) {
								// even
								conflictPanel.add(errorPanelElementTitle);
								conflictPanel.add(errorListAll.get(i + 1)
										.createSingleDate(
												errorListAll.get(i + 1)));
							} else {
								conflictPanel.add(errorPanelElementTitle);
								conflictPanel.add(errorListAll.get(i - 1)
										.createSingleDate(
												errorListAll.get(i - 1)));
							}
						}
					}

				}
			};

			for (int i = 0; i < errorList.size(); i++) {
				VerticalPanel conflictingDatePanel = new VerticalPanel();
				RaplaDate conflictingDate = errorList.get(i);
				Label errorPanelElementTitle = new Label("Titel der Vorlesung");
				conflictingDatePanel.add(errorPanelElementTitle);
				errorPanelElementTitle.setStyleName("errorPanelElementTitle");
				dateElement = conflictingDate.createSingleDate(conflictingDate);
				dateElement.setWidth("200px");
				dateElement.addDomHandler(handler, ClickEvent.getType());
				keyValueMap.put(dateElement, conflictingDate);
				conflictingDatePanel.add(dateElement);
				// conflictingDatePanel.add(resourceTree);
				raplaDatePanel.add(conflictingDatePanel);
				raplaDatePanel.setCellHorizontalAlignment(conflictingDatePanel,
						HasHorizontalAlignment.ALIGN_CENTER);
			}

			VerticalPanel bottom = new VerticalPanel();
			bottom.setStyleName("errorPanelButtom");
			bottom.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			bottom.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
			FlowPanel bottomElements = new FlowPanel();
			bottomElements.setStyleName("bottomElements");
			Button takeChanges = new Button("\u00C4nderungen \u00FCbernehmen");
			takeChanges.setStyleName("errorPanelButton");
			takeChanges.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					getPresenter().onErrorPanelButtonClick(event);

					try {
						showLabelSaved();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			Button close = new Button("Konflikte ignorieren");
			close.setStyleName("errorPanelButton");
			close.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					getPresenter().onErrorPanelButtonClick(event);
				}
			});

			bottomElements.add(takeChanges);
			bottomElements.add(close);
			bottom.add(bottomElements);

			top.addStyleName("errorPanelTop");

			HorizontalPanel troubleshootingPanel = new HorizontalPanel();
			troubleshootingPanel.add(conflictPanel);
			troubleshootingPanel.add(addResources);
			troubleshootingPanel.setCellWidth(conflictPanel, "35%");
			addResources.setStyleName("addResourcesErrorStyle");
			troubleshootingPanel.setStyleName("troubleshootingPanel");

			addResources.setOpen(true);
			errorPanel.add(top);
			errorPanel.add(raplaDatePanel);
			errorPanel.add(troubleshootingPanel);
			errorPanel.add(bottom);
			errorPanel.setStyleName("errorPanel");
			dateList.add(errorPanel);

		}
		dateList.setResources(currentDate);

	}

	public ArrayList<List<String>> copyResourceArray(ArrayList<List<String>> res) {

		ArrayList<List<String>> copyArray = new ArrayList<List<String>>();
		List<String> copyList;// = new ArrayList<String>();

		for (List<String> tempList : res) {
			copyList = new ArrayList<String>();
			for (String tempString : tempList) {
				copyList.add(tempString);
			}
			copyArray.add(copyList);
		}

		return copyArray;
	}

	@Override
	public void setVisiblityOfFilter() {
		// TODO Auto-generated method stub

		if (filterEintr.isVisible()) {
			filterEintr.setVisible(false);
		} else {
			filterEintr.setVisible(true);
		}

	}

	@Override
	public void setErrorPanelButtonClickAction(ClickEvent event) {
		// TODO Auto-generated method stub
		Button sender = (Button) event.getSource();
		if (sender.getText().equals("Konflikte ignorieren")) {
			dateList.remove(dateList.getLastPosition() + 2);
			dateInfos.add(chosenResources);
			dateInfos.add(addResources);
			addResources.removeStyleName("addResourcesErrorStyle");
		}
	}

	@Override
	public void setResourcesPerson(List<String> dynamicTypesNames) {
		dynamicTypesNames.add(0, "Professoren");
		this.toBeReservedResources.add(dynamicTypesNames);

	}

	@Override
	public void setResourcesRoom(List<String> dynamicTypesNames) {
		dynamicTypesNames.add(0, "R\u00E4ume");
		this.toBeReservedResources.add(dynamicTypesNames);

	}

	@Override
	public void setResourcesCourse(List<String> dynamicTypesNames) {
		dynamicTypesNames.add(0, "Kurse");
		this.toBeReservedResources.add(dynamicTypesNames);

	}

	@Override
	public void clear() {
		this.toBeReservedResources.clear();
		this.resourceTree.clear();
		this.deleteResourceContainer();
		// this.reservedResources=null;
		this.refreshResourceTree();

	}

	@Override
	public List<RaplaDate> getDates() {
		// List<RaplaDate> list = new ArrayList<RaplaDate>();
		// Iterator<Widget> iterator = this.dateList.iterator();
		// while(iterator.hasNext()){
		// RaplaDate date = iterator.next();
		// }

		List<RaplaDate> dateList2 = this.dateList.getDates();
		logger.warn("Dates list 1: " + this.dateList.getDates().size());

//		for (RaplaDate raplaDate : dateList2) {
//			raplaDate.setResources(this.reservedResources);
//		}

		if (this.dateList.getDates().size() > 0) {
			logger.warn("Resource list 1a: "
					+ this.dateList.getDates().get(0).getResources().size());
			if (this.dateList.getDates().get(0).getResources().size() > 0) {
				logger.warn("Resource 1 list: "
						+ this.dateList.getDates().get(0).getResources().get(0)
								.size());
			}
		}

		dateList.clear();
		// dateList.getDates().clear();
		logger.warn("Dates list 2: " + this.dateList.getDates().size());
		//
		// logger.warn("Resource list 2a: "
		// + this.dateList.getDates().get(0).getResources().size());

		return dateList2;
	}

	@Override
	public void setDates(List<RaplaDate> dateList) {
		this.dateList.getDates().clear();
		logger.warn("Dates list: " + this.dateList.getDates().size());

		if (dateList.size() > 0) {
			logger.warn("set Dates 1");

			for (RaplaDate date : dateList) {
				date.setStyleName("singleDate");
				// dateList.add(date);
				date.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent e) {
						getPresenter().onAddTerminButtonClicked(e);
					}
				});
			}

			FlowPanel placeholderSetResourcesToAll = new FlowPanel();
			placeholderSetResourcesToAll.setStyleName("resourceButtonPanel");
			setResourcesToAll = new Button(
					"Ressourcen f\u00FCr alle \u00FCbernehmen");
			setResourcesToAll.setStyleName("resourceButton");
			setResourcesToAll.setVisible(false);
			placeholderSetResourcesToAll.add(setResourcesToAll);
			this.dateList.add(placeholderSetResourcesToAll);

			setResourcesToAll.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onSetResourcesToAllClicked();

				}
			});
			this.dateList.setFirstWidget(false);

			this.dateList.setDates(dateList);
			// this.deleteResourceContainer();
			this.loadChosenResources(dateList.get(0).getResources());
			logger.warn("Dates list 1: " + this.dateList.getDates().size());
			if (this.dateList.getDates().get(0).getResources() != null) {
				logger.warn("Resource list: "
						+ this.dateList.getDates().get(0).getResources().size());
				logger.warn("Resource 1 list: "
						+ this.dateList.getDates().get(0).getResources().get(0)
								.size());

				// refreshResourceContainer();
			} else {

				logger.warn("Resource list: empty ");
			}
			refreshResourceContainer();
		} else {
			// this.dateList = new TerminList();
			for (int i = 0; i < this.dateList.getWidgetCount(); i++) {
				this.dateList.remove(i);
			}
			logger.warn("set Dates 2");
			FlowPanel firstDateListWidget = new FlowPanel();
			firstDateListWidget.setStyleName("wildcardPanel");
			Label explainer = new Label(
					"Durch das Dr\u00FCcken des Plus-Buttons bei ausgef\u00FCllten Termindaten, wird ein Termin hinzugef\u00FCgt");
			explainer.setStyleName("wildcard");
			firstDateListWidget.add(explainer);
			this.dateList.add(firstDateListWidget);

			FlowPanel placeholderSetResourcesToAll = new FlowPanel();
			placeholderSetResourcesToAll.setStyleName("resourceButtonPanel");
			setResourcesToAll = new Button(
					"Ressourcen f\u00FCr alle \u00FCbernehmen");
			setResourcesToAll.setStyleName("resourceButton");
			setResourcesToAll.setVisible(false);
			placeholderSetResourcesToAll.add(setResourcesToAll);
			this.dateList.add(placeholderSetResourcesToAll);

			setResourcesToAll.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onSetResourcesToAllClicked();

				}

			});

			this.dateList.setFirstWidget(true);

			logger.warn("Dates list 2: " + this.dateList.getDates().size());

			ArrayList<List<String>> testRessourcen = new ArrayList<List<String>>();

			this.loadChosenResources(testRessourcen);

			refreshResourceContainer();
		}
	}

	public void showLabelSaved() throws InterruptedException {

		Label changes = new Label("Änderungen wurden übernommen");
		dateInfos.add(changes);
		changes.setVisible(true);
	}

}
