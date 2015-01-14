package org.rapla.client.mwi14_1.factory;

import java.util.Collection;
import java.util.List;

import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.dynamictype.Attribute;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;

public interface InfoViewInterface extends ViewServiceProviderInterface {
	
	
public void setEventTypes(List<String> eventTypes); 

public String getSelectedEventType();
public void  setSelectedEventType(String select);

public void setDynamicFields(Attribute[] attributes);

//~~
public Tree getResources();

public void setResources(Tree resources);

public TextBox getTitelInput();

public void setTitelInput(TextBox titelInput);

public TextBox getVorlesungsStundenInput();

public void setVorlesungsStundenInput(TextBox vorlesungsStundenInput);

public ListBox getStudiengangListBox();

public void setStudiengangListBox(ListBox studiengangListBox);

public Collection<String> getStudiengangListBoxAuswahl();

public void setStudiengangListBoxAuswahl(Collection<String> studiengangListBoxAuswahl);

}

