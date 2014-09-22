package org.rapla.client.module;

import java.util.Set;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;

public class GUI {

    @Inject Set<View> views;

    public String getViews()
    {
        StringBuilder res = new StringBuilder();
        for ( View view:views)
        {
            res.append( view.getName());
        }
        return res.toString();
    }


}
