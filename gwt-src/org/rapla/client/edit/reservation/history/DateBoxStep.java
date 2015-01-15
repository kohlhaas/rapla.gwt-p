package org.rapla.client.edit.reservation.history;

import java.util.Date;

import com.google.gwt.user.datepicker.client.DateBox;

public class DateBoxStep implements Step {
	
	DateBox dateBox;
	Date previousValue;
	Date newValue;
	
	public DateBoxStep(DateBox dateBox, Object previousValue) {
		this.dateBox = dateBox;
		this.previousValue = (Date) previousValue;
		this.newValue = dateBox.getValue();
	}
	
	@Override
	public Date up() {
		dateBox.setValue(newValue, false);
		return newValue;
	}

	@Override
	public Date down() {
		dateBox.setValue(previousValue, false);
		return previousValue;
	}

	@Override
	public DateBox getWidget() {
		return dateBox;
	}
	

}
