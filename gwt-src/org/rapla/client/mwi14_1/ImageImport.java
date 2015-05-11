package org.rapla.client.mwi14_1;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageImport extends ClientBundle {
	ImageImport INSTANCE = GWT.create(ImageImport.class);


	@Source("iconSave.png")
	ImageResource saveIcon();	
	
	@Source("iconCancel.png")
	ImageResource cancelIcon();	
	
	@Source("iconDelete.png")
	ImageResource deleteIcon();		
	
	@Source("cross.png")
	ImageResource crossIcon();		
	
	@Source("next.png")
	ImageResource nextIcon();
	
	@Source("plus.png")
	ImageResource plusIcon();	
	
	@Source("redo.png")
	ImageResource redoIcon();	
	
	@Source("undo.png")
	ImageResource undoIcon();		
}
