package com.punisher.fitnesstracker.util;

/**
 * Represents utility function to calculate distance from pickers
 */
public class DistanceUtil {

    public static final int METERS_IN_KM = 1000;

    public static int getMeters(int k, int m) {
        return (k * METERS_IN_KM) + m;
    }

    public static int getMeters(int k1, int k2, int m1, int m2, int m3) {
        String sKm = String.valueOf(k1) + String.valueOf(k2);
        String sM = String.valueOf(m1) + String.valueOf(m2) + String.valueOf(m3);
        int km = Integer.parseInt(sKm);
        int m = Integer.parseInt(sM);
        return getMeters(km, m);

    }
}
