package org.rapla.client.module;

import org.rapla.client.data.Data;

public class ModuleA implements View{
    
    @com.google.inject.Inject
    private Data data;
    
    public ModuleA() {
    }
    
    public long doSomething(){
        long l = data.getLong();
        return l;
    }

    @Override
    public String getName() {
        return "A";
    }
    

}
