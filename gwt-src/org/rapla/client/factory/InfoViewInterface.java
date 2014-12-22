package org.rapla.client.factory;

import java.util.Collection;
import java.util.List;

import org.rapla.entities.domain.Allocatable;

public interface InfoViewInterface extends ViewServiceProviderInterface {
	
	
public void setEventTypes(List<String> eventTypes); 

public String getSelectedEventType();

public void setDynamicFields(List<String> dynamicFields);
}

