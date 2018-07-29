package com.cool.task.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * quartz cron tool
 *
 * @author Vincent
 */
public class CronUtils {

    /**
     * the difference between param and current to quartz cron
     * @param after true future /   false before
     * @return the corn
     */
    public static String cron(Integer time, TimeUnit timeUnit, boolean after){
        Calendar cal = Calendar.getInstance();
        if (after){
            cal.setTime(new Date(new Date().getTime() + (timeUnit.toMillis(time))));
        }else {
            cal.setTime(new Date(new Date().getTime() - (timeUnit.toMillis(time))));
        }

        return cron(cal);
    }

    public static String cron(Integer time, TimeUnit timeUnit){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(new Date().getTime() + (timeUnit.toMillis(time))));
        return cron(cal);
    }

    private static String cron(Calendar cal){
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);


        Map<String, String> map = new HashMap<>(16);
        map.put("yyyy", String.valueOf(year));
        map.put("MM", String.valueOf(month));
        map.put("dd", String.valueOf(day));
        map.put("HH", String.valueOf(hour));
        map.put("mm", String.valueOf(minute));
        map.put("ss", String.valueOf(second));

        return map.get("ss") + " " + map.get("mm") + " " + map.get("HH") + " " + map.get("dd") + " " + map.get("MM") + " ? ";
    }

    public static void main(String[] args) {
        System.out.println(cron(10,TimeUnit.MINUTES,false));
    }
}
