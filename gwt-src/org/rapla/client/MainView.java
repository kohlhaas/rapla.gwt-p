package org.rapla.client;

import java.util.List;

import org.rapla.client.plugin.view.ContentProvider;

public interface MainView {

    interface Presenter
    {
        void setSelectedViewIndex(int index);
        
        void addClicked();
    }
    
    void show(List<String> viewNames);

    void replaceContent(ContentProvider provider);

    void setPresenter(Presenter presenter);

}
