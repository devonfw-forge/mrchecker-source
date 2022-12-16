package com.capgemini.mrchecker.test.core.utils;

import java.time.Duration;

public class DurationUtils {
    private DurationUtils() {
    }

    public static String getReadableDuration(Duration d) {
        long days = d.toDays();
        d = d.minusDays(days);
        long hours = d.toHours();
        d = d.minusHours(hours);
        long minutes = d.toMinutes();
        d = d.minusMinutes(minutes);
        long seconds = d.getSeconds();
        d = d.minusSeconds(seconds);
        long millis = d.toMillis();
        String result = ((days == 0 ? "" : days + "d ") + (hours == 0 ? "" : hours + "h ") + (minutes == 0 ? "" : minutes + "m ") + (seconds == 0 ? "" : seconds + "s ") + (millis == 0 ? "" : millis + "ms")).trim();
        if (result.isEmpty()) {
            return "0ms";
        }
        return result;
    }
}
