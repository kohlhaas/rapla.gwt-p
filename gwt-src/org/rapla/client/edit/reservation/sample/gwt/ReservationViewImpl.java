package org.rapla.client.edit.reservation.sample.gwt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationEditSubView;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.client.edit.reservation.sample.ResourceDatesView;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.framework.RaplaException;

import com.blogspot.ctasada.gwt.eureka.client.ui.SmallTimeBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import org.rapla.client.edit.reservation.sample.InfoView;
import org.rapla.client.mwi14_1.ImageImport;

public class ReservationViewImpl extends AbstractView<Presenter> implements
		ReservationView<IsWidget> {

	// for general popup
	private FlowPanel mainPanel;
	private VerticalPanel layout;
	private FlowPanel tabBarPanel;
	private FlowPanel buttonsPanel;
	public Panel popup;
	private TabBar bar;
	//private Button cancel, save, delete;
	private Image cancel, save, delete, undo, redo;
	private int height, width;

	// for info tab
	private VerticalPanel contentLeft;
	private VerticalPanel contentRight;
	private HorizontalPanel infoTab;
	private ListBox eventTypesListBox;
	private Tree resources;
	private TextBox titelInput;
	private TextBox vorlesungsStundenInput;
	private ListBox studiengangListBox;
	private Collection<String> studiengangListBoxAuswahl;
	private static final ImageResource IMG_ICON_SAVE = ImageImport.INSTANCE.saveIcon();
	private static final ImageResource IMG_ICON_CANCEL = ImageImport.INSTANCE.cancelIcon();
	private static final ImageResource IMG_ICON_DELETE = ImageImport.INSTANCE.deleteIcon();
	private static final ImageResource IMG_ICON_UNDO = ImageImport.INSTANCE.undoIcon();
	private static final ImageResource IMG_ICON_REDO = ImageImport.INSTANCE.redoIcon();
	
	// for resources and dates tab
	private ArrayList<List<String>> toBeReservedResources = new ArrayList<List<String>>();
	private ArrayList<List<String>> reservedResources = new ArrayList<List<String>>();

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

	// SmallTimeBox timeBegin;
	// SmallTimeBox timeEnd;

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

	ReservationEditSubView currentView;
	Panel currentTabContent;
	//private DisclosurePanel subView;

	public void show(Reservation event) {
		popup = RootPanel.get("raplaPopup");
		popup.setVisible(true);

		// popup.setGlassEnabled(true);
		// popup.setAnimationEnabled(true);
		// popup.setAnimationType(PopupPanel.AnimationType.ROLL_DOWN);

		height = (int) (Window.getClientHeight() * 0.90);
		width = (int) (Window.getClientWidth() * 0.90);
		popup.setHeight(height + "px");
		popup.setWidth(width + "px");

		popup.clear();

		bar = new TabBar();
		bar.addTab("Veranstaltungsinformationen");
		bar.addTab("Termine und Ressourcen");
		bar.setWidth(width + "px");
		
		cancel = new Image(IMG_ICON_CANCEL);
		//cancel = new Button("abbrechen");
		cancel.setStyleName("cancelButton");
		cancel.setTitle("Abbrechen");
		cancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent e) {
				getPresenter().onCancelButtonClicked();
			}
		});

		save = new Image(IMG_ICON_SAVE);
		//save = new Button("speichern");
		save.setStyleName("saveButton");
		save.setTitle("Veranstaltung speichern");
		save.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent e) {
				getPresenter().onSaveButtonClicked();
			}
		});
		
		delete = new Image(IMG_ICON_DELETE);
		//delete = new Button("l\u00F6schen");
		delete.setStyleName("deleteButton");
		delete.setTitle("Veranstaltung l\u00F6schen");
		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent e) {
				getPresenter().onDeleteButtonClicked();
			}
		});

		
		undo = new Image(IMG_ICON_UNDO);
		undo.setStyleName("undoButton");
		undo.setTitle("R\u00FCckg\u00E4ngig");
		
		redo = new Image(IMG_ICON_REDO);
		redo.setStyleName("redoButton");
		redo.setTitle("Wiederholen");
		
		layout = new VerticalPanel();
		tabBarPanel = new FlowPanel();
		buttonsPanel = new FlowPanel();
		
		tabBarPanel.add(bar);

		bar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				try {
					getPresenter().onTabChanged(bar.getSelectedTab());
				} catch (RaplaException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		buttonsPanel.add(cancel);
		buttonsPanel.add(save);
		buttonsPanel.add(delete);
		buttonsPanel.add(undo);	
		buttonsPanel.add(redo);
	

		layout.add(buttonsPanel);
		layout.add(tabBarPanel);

		layout.setCellHeight(buttonsPanel, "40px");
		layout.setCellHeight(tabBarPanel, "50px");

		popup.add(layout);

		bar.selectTab(0);

	}

	public void update(ReservationEditSubView tempView) {
		//
		// if (currentTabContent != null) {
		// currentTabContent.clear();
		// }

		if (tempView instanceof InfoView) {
			// ((InfoView) currentView).show();
			if (currentView != null)
				((ResourceDatesView) this.currentView).hide();
			currentTabContent = (Panel) ((InfoView) tempView).provideContent();
			((InfoView) tempView).show();

		} else if (tempView instanceof ResourceDatesView) {
			// ((ResourceDatesView) currentView).show();
			if (currentView != null)
				((InfoView) this.currentView).hide();
			currentTabContent = (Panel) ((ResourceDatesView) tempView)
					.provideContent();
			((ResourceDatesView) tempView).show();

		}

		this.currentView = tempView;
		
		if (currentTabContent != null) {
			popup.add(currentTabContent);
		}

	}

	public void mapFromReservation(Reservation event) {
		Locale locale = getRaplaLocale().getLocale();
		// tb.setText( event.getName( locale));
		// contentRes.clear();
		Allocatable[] resources = event.getAllocatables();
		{
			StringBuilder builder = new StringBuilder();
			for (Allocatable res : resources) {
				builder.append(res.getName(locale));
			}
			// contentRes.add(new Label("Ressourcen: " +builder.toString() ));

		}
	}

	public void hide() {
		popup.setVisible(false);
	}

	@Override
	public void addSubView(ReservationEditSubView<IsWidget> view) {
		IsWidget provideContent = view.provideContent();
		// subView.add( provideContent.asWidget());
	}

	@Override
	public ReservationEditSubView getCurrentSubView() {
		return currentView;
	}

	@Override
	public void setCurrentSubView(ReservationEditSubView currentSubView) {
		this.currentView = currentSubView;
	}
	
	

}
