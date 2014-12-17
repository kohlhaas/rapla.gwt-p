package org.rapla.client.factory;

import java.util.List;

public interface InfoViewInterface extends ViewServiceProviderInterface {
	
	
public void setEventTypes(List<String> eventTypes); 

public String getSelectedEventType();
}

