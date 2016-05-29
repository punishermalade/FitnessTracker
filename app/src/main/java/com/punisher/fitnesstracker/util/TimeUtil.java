package com.punisher.fitnesstracker.util;

/**
 * Represents utility function to calculate time duration from the different pickers
 */
public class TimeUtil {

    public static final int MIN_IN_HOUR = 60;
    public static final int SEC_IN_MIN = 60;
    public static final int SEC_IN_HOUR = MIN_IN_HOUR * SEC_IN_MIN;

    public static int getSeconds(int hour, int minutes, int sec) {
        return (hour * SEC_IN_HOUR) + (minutes * SEC_IN_MIN) + sec;
    }

    public static int getSecondsFromPicker(int h, int m1, int m2, int s1, int s2) {

        String sMin = String.valueOf(m1) + String.valueOf(m2);
        int min = Integer.parseInt(sMin);

        String sSec = String.valueOf(s1) + String.valueOf(s2);
        int sec = Integer.parseInt(sSec);

        return getSeconds(h, min, sec);

    }

    public static int[] getPickerFromSeconds(int dur) {
        int rest = dur;
        int picker[] = new int[5];

        picker[0] = dur / SEC_IN_HOUR;
        rest -= picker[0] * SEC_IN_HOUR;

        picker[1] = (dur % SEC_IN_HOUR) / MIN_IN_HOUR;
        rest -= picker[1] * MIN_IN_HOUR;

        if (picker[1] < 10) {
            picker[2] = picker[1];
            picker[1] = 0;
        }
        else {
            picker[2] = picker[1] % 10;
            picker[1] = picker[1] / 10;
        }

        picker[3] = rest;

        if (picker[3] < 10) {
            picker[4] = picker[3];
            picker[3] = 0;
        }
        else {
            picker[4] = picker[3] % 10;
            picker[3] = picker[3] / 10;
        }

        return picker;
    }
}