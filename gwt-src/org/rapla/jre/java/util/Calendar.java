package java.util;

public class Calendar {
    private final static Calendar defaultCal = new Calendar();

    public final static int SUNDAY = 1;
    public final static int MONDAY = 2;
    public final static int TUESDAY = 3;
    public final static int WEDNESDAY = 4;
    public final static int THURSDAY = 5;
    public final static int FRIDAY = 6;
    public final static int SATURDAY = 7;
    
    static public Calendar getInstance()
    {
        return defaultCal;
    }
    
    public int getFirstDayOfWeek()
    {
        return MONDAY;
    }
}
