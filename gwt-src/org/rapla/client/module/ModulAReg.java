package org.rapla.client.module;

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.binder.GinBinder;
import com.google.gwt.inject.client.multibindings.GinMultibinder;

public class ModulAReg implements GinModule {

    @Override
    public void configure(GinBinder binder) {
        GinMultibinder<View> uriBinder = GinMultibinder.newSetBinder(binder, View.class);
        uriBinder.addBinding().to(ModuleA.class);
    }
    
}
