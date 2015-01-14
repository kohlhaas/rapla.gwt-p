package org.rapla.client.mwi14_1.timePicker;

/**
 * 
 * @author Carlos Tasada
 *
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ResourcesBundle extends ClientBundle {
	ResourcesBundle INSTANCE = GWT.create(ResourcesBundle.class);

	@Source("timepicker_AM.png")
	ImageResource timePickerAM();

	@Source("timepicker_AM.png")
	ImageResource timePickerPM();

	@Source("timepicker_AM.png")
	ImageResource timePickerAMSmall();

	@Source("timepicker_AM.png")
	ImageResource timePickerPMSmall();
}