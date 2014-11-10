package org.rapla.client.plugin.view.list;

import org.rapla.client.plugin.view.ViewPlugin;

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.binder.GinBinder;
import com.google.gwt.inject.client.multibindings.GinMultibinder;

public class ListViewPluginGinModule implements GinModule {

    @Override
    public void configure(GinBinder binder) {
        GinMultibinder<ViewPlugin> uriBinder = GinMultibinder.newSetBinder(binder, ViewPlugin.class);
        uriBinder.addBinding().to(ListViewPlugin.class);
    }

}
