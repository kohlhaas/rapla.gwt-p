package org.rapla.client.edit.reservation.history;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class RadioButtonStep implements Step {
	
	private Panel parentPanel;
	private RadioButton selectedRadioButton;
	private RadioButton previousRadioButton;
	
	public RadioButtonStep(Panel parentPanel, RadioButton selectedRadioButton, RadioButton previousRadioButton) {
		this.parentPanel = parentPanel;
		this.selectedRadioButton = selectedRadioButton;
		this.previousRadioButton = previousRadioButton;
	}

	@Override
	public Object up() {
		Iterator<Widget> radioButtons = parentPanel.iterator();
		while(radioButtons.hasNext()) {
			RadioButton radioButton = (RadioButton) radioButtons.next();
			radioButton.setValue(false);
		}
		selectedRadioButton.setValue(true);
		return selectedRadioButton;
	}

	@Override
	public Object down() {
		Iterator<Widget> radioButtons = parentPanel.iterator();
		while(radioButtons.hasNext()) {
			RadioButton radioButton = (RadioButton) radioButtons.next();
			radioButton.setValue(false);
		}
		previousRadioButton.setValue(true);
		return previousRadioButton;
	}

	@Override
	public Widget getWidget() {
		return parentPanel;
	}

}
