package cn.hurraysoft.HelloWorldTask;

import java.util.Calendar;
import java.util.Timer;

public class SimpleBirthdayReminderScheduling{

    public static final long MILLIS_IN_YEAR = 1000 * 60 * 60 * 24 * 365;

    public static void main(String[] args){
        Calendar cal = Calendar.getInstance();
        cal.set(2018, Calendar.NOVEMBER, 30);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new HelloWorldTask(), cal.getTime(), MILLIS_IN_YEAR);
    }
}
