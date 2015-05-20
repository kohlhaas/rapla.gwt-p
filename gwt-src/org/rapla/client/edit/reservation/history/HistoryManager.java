package org.rapla.client.edit.reservation.history;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class HistoryManager {
	static HistoryManager instance;
	List<Step> steps;
	ListIterator<Step> currentPosition;
	Map<Widget,Object> lastValue;
	
	private HistoryManager() {
		steps = new LinkedList<Step>();
		currentPosition = steps.listIterator();
		lastValue = new HashMap<>();
	}
	
	public static HistoryManager getInstance() {
		if(instance == null) {
			instance = new HistoryManager();
		}
		return instance;
	}

	public boolean canUndo() {
		return currentPosition.hasPrevious();
	}
	
	public void undo() {
		Step lastStep = currentPosition.previous();
		lastValue.put(lastStep.getWidget(), lastStep.down());
	}
	
	public boolean canRedo() {
		return currentPosition.hasNext();
	}
	
	public void redo() {
		Step nextStep = currentPosition.next();
		lastValue.put(nextStep.getWidget(), nextStep.up());
	}
	
	public void trackWidget(Widget widget) {
		Class<? extends Widget> className = widget.getClass();
		if (className == TextBox.class) {
			TextBox textBox = (TextBox) widget;
			lastValue.put(textBox, textBox.getValue());
			textBox.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					TextBox textBox = (TextBox) event.getSource();
					Step newStep = new TextBoxStep(textBox, lastValue.get(textBox));
					HistoryManager.getInstance().addStep(newStep);
				}
			});
		}
		else if (className == DateBox.class) {
			DateBox dateBox = (DateBox) widget;
			lastValue.put(dateBox, dateBox.getValue());
			dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					DateBox dateBox = (DateBox) event.getSource();
					Step newStep = new DateBoxStep(dateBox, lastValue.get(dateBox));
					HistoryManager.getInstance().addStep(newStep);
				}});
		}
		else if (widget instanceof Panel) {
			// assumption: if a FlowPanel is passed it contains RadioButton objects
			Panel parentPanel = (Panel) widget;
			Iterator<Widget> radioButtons = parentPanel.iterator();
			while(radioButtons.hasNext()) {
				Widget child = radioButtons.next();
				try {
					RadioButton radioButton = (RadioButton) child;
					if(radioButton.getValue()) {
						lastValue.put(parentPanel, radioButton);
					}
					radioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(ValueChangeEvent<Boolean> event) {
							RadioButton radioButton = (RadioButton) event.getSource();
							Panel parentPanel = (Panel) radioButton.getParent();
							Step newStep = new RadioButtonStep(parentPanel, radioButton, (RadioButton) lastValue.get(parentPanel));
							HistoryManager.getInstance().addStep(newStep);
						}
					});
					
				}
				catch (ClassCastException e) {
					Logger.getGlobal().log(Level.WARNING, "Child of this panel expected to be RadioButton, but was " + child.getClass() );
				}
			}
		}
		else {
			Logger.getGlobal().log(Level.WARNING, "History Manager can't handle class type "+ className  +" yet.");
		}
	}
	
	public String output() {
		String output = "";
		for(int i=0; i<steps.size(); i++) {
			Step step = steps.get(i);
			output += "<br>" + i + ": ";
			
		}
		output += "<br>Iterator is between " + currentPosition.previousIndex() + " and " + currentPosition.nextIndex();
		return output;
	}
	
	private void addStep(Step step) {
		while(currentPosition.hasNext()) {
			steps.remove(currentPosition.next());
		}
		currentPosition.add(step);
		//if(steps.size()>0)
		//currentPosition.next();
		
		lastValue.put(step.getWidget(), step.up());
	}

}
