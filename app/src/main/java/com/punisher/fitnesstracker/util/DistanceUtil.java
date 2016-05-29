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

    /**
     * break down the distance into 5 par ts for the pickers in the UI
     * @param dist the distance in meters
     * @return an array containing the unit values. If dist is 12345, the array will be [1, 2, 3, 4, 5]
     */
    public static int[] getPickersFromMeter(int dist) {
        int rest = dist;
        int picker[] = new int[5];

        picker[0] = dist / (METERS_IN_KM * 10);
        rest -= picker[0] * 10000;

        picker[1] = rest / 1000;
        rest -= picker[1] * 1000;

        picker[2] = rest / 100;
        rest -= picker[2] * 100;

        picker[3] = rest / 10;
        rest -= picker[3] * 10;

        picker[4] = rest;

        return picker;
    }
}
