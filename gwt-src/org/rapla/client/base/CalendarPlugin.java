package org.rapla.client.base;

public interface CalendarPlugin<W> {

    String getName();

    W provideContent();

    void updateContent();
}
