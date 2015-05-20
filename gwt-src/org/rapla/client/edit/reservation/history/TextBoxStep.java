package org.rapla.client.edit.reservation.history;

import com.google.gwt.user.client.ui.TextBoxBase;

public class TextBoxStep implements Step {
	
	TextBoxBase textBox;
	String previousValue;
	String newValue;
	
	public TextBoxStep(TextBoxBase textBox, Object previousValue) {
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
	public TextBoxBase getWidget() {
		return textBox;
	}
	

}
