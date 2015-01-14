package org.rapla.client.mwi14_1.factory;

import org.rapla.client.mwi14_1.ReservationController;
import org.rapla.client.mwi14_1.view.infos.InfoView;
import org.rapla.client.mwi14_1.view.resoursedates.ResourceDatesView;

public class ViewFactory {
	public static ViewServiceProviderInterface getInstance(ViewEnumTypes viewId){ //mit enum ersetzten
		ViewServiceProviderInterface view;
		
	if (viewId.equals(ViewEnumTypes.INFOVIEW_DESKTOP)){
		  view = new InfoView();	
	} 
	else {
		 view = new ResourceDatesView();	
	}
	
	 return view;
	}
}
