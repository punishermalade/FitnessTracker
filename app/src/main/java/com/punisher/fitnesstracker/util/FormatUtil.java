package com.punisher.fitnesstracker.util;

import android.util.Log;
import android.widget.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a class that contains different function to format time, date and distance.
 */
public class FormatUtil {

    public static NumberPicker.Formatter getTwoDigitFormatter() {
        return getDigitFormatter(2);
    }

    public static NumberPicker.Formatter getDigitFormatter(int n) {
        final int nbDigit = n;

        return new NumberPicker.Formatter() {
            public String format(int value) {
                return String.format("%0" + String.valueOf(nbDigit) + "d", value);
            }
        };
    }

    public static String getDistance(int d) {
        float distanceInMeter = (float)d;
        float distance = distanceInMeter / 1000;
        return String.format("%.2f", distance);
    }

    public static String formatDuration(int d) {
        int min = d / 60;
        int sec = d % 60;
        return String.format("%02d:%02d", min, sec);
    }

    public static String formatDate(Date d) {
        SimpleDateFormat format = new SimpleDateFormat("EEEE',' d MMMM yyyy 'at' HH:mm");
        return format.format(d);
    }

    public static String formatAverage(int dist, int dur) {
        float speed = (float)dist / (float)dur;
        float result = 1000 / speed;
        return formatDuration(Math.round(result));
    }
}
