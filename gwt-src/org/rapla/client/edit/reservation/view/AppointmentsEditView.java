package org.rapla.client.edit.reservation.view;

import java.util.Arrays;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * Created by nam on 12/17/14.
 */
public class AppointmentsEditView implements RaplaEditView {

	private TextCell textCell;
	private CellList<String> appointmentList;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private HTML title;

	@Override
	public void setTitle(String title) {
		 this.title = new HTML("<h1>" + title + "</h1>");

	}

	@Override
	public void init() {
		textCell= new TextCell();
		appointmentList = new CellList<String>(textCell);
		startDatePicker = new DatePicker();
		endDatePicker = new DatePicker();
		title = new HTML();

		appointmentList.setRowData(0, Arrays.asList("Termin 1", "Termin 2", "Termin 3"));
    	
    	VerticalPanel startDatePanel = new VerticalPanel();
    	startDatePanel.add( new Label("Startdatum") );
    	startDatePanel.add(startDatePicker);
    	
    	VerticalPanel endDatePanel = new VerticalPanel();
    	endDatePanel.add( new Label("Enddatum") );
    	endDatePanel.add(endDatePicker);
    	
    	HorizontalPanel datePanel = new HorizontalPanel();
    	datePanel.add(startDatePanel);
    	datePanel.add(endDatePanel);
    	
    	RootPanel panel = RootPanel.get("raplaRoot");
    	panel.add(title);
        panel.add(appointmentList);
        panel.add(datePanel);

	}
}
