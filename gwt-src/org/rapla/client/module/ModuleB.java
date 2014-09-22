package org.rapla.client.module;

import javax.inject.Inject;

import org.rapla.client.data.Data;

public class ModuleB implements View{
    @Inject
    private Data data;
    
    
    public long doSomething(){
        long l = data.getLong();
        return l;
    }


    @Override
    public String getName() {
        return "B";
    }
}
