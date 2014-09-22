package org.rapla.client.data;

@com.google.inject.Singleton
public class Data {

    private long l;
    public Data() {
        l = System.currentTimeMillis();
    }

    public long getLong() {
        return l;
    }

}
