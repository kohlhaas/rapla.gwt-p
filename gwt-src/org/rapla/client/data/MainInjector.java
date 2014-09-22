package org.rapla.client.data;

import org.rapla.client.module.GUI;
import org.rapla.client.module.ModulAReg;
import org.rapla.client.module.ModulBReg;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ModulAReg.class,ModulBReg.class})
public interface MainInjector extends Ginjector{
    //public Data getData();
    //public ModuleA getModule();
    //public ModuleB getModuleB();
    public GUI getGUI();
}
