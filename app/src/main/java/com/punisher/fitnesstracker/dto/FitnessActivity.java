package com.punisher.fitnesstracker.dto;

import java.util.Date;

/**
 * DTO that holds a single activity data
 */
public class FitnessActivity {

    public enum FitnessType { RUNNING, SWIMMING, BIKING }

    private Date _dayOfActivity = null;
    private Date _timeOfActivity = null;
    private int _distance = 0;
    private int _duration = 0;
    private FitnessType _fitnessType;

    public FitnessActivity() {
        _dayOfActivity = new Date(System.currentTimeMillis());
        _timeOfActivity = new Date(System.currentTimeMillis());
        _fitnessType = null;
    }

    public Date getDayOfActivity() {
        return _dayOfActivity;
    }

    public void setDayOfActivity(Date d) {
        _dayOfActivity = d;
    }

    public Date getTimeOfActivity() {
        return _timeOfActivity;
    }

    public void setTimeOfActivity(Date d) {
        _timeOfActivity = d;
    }

    public int getDistance() {
        return _distance;
    }

    public void setDistance(int d) {
        _distance = d;
    }

    public int getDuration() {
        return _duration;
    }

    public void setDuration(int t) {
        _duration = t;
    }

    public void setFitnessType(FitnessType t) {
        _fitnessType = t;
    }

    public FitnessType getFitnessType() {
        return _fitnessType;
    }

    @Override
    public String toString() {
        return getDayOfActivity().toGMTString() + " " +
                getTimeOfActivity().toGMTString() + " " +
                getFitnessType().toString() + " " +
                String.valueOf(getDuration()) + " " +
                String.valueOf(getDistance());
    }
}