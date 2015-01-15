package org.rapla.client.edit.reservation.history;

import com.google.gwt.user.client.ui.TextBox;

public class TextBoxStep implements Step {
	
	TextBox textBox;
	String previousValue;
	String newValue;
	
	public TextBoxStep(TextBox textBox, Object previousValue) {
		this.textBox = textBox;
		this.previousValue = (String) previousValue;
		this.newValue = textBox.getValue();
	}
	
	@Override
	public String up() {
		textBox.setValue(newValue, false);
		return newValue;
	}

	@Override
	public String down() {
		textBox.setValue(previousValue, false);
		return previousValue;
	}

	@Override
	public TextBox getWidget() {
		return textBox;
	}
	

}
