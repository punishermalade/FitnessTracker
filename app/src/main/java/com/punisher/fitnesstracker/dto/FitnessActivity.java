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
    private String _id = "";
    private int _average = -1;

    public FitnessActivity() {
        _dayOfActivity = new Date(System.currentTimeMillis());
        _timeOfActivity = new Date(System.currentTimeMillis());
        _fitnessType = null;
    }

    public String getID() { return _id; }

    public void setID(String id) { _id = id; }

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
        return  getID() + " " +
                getDayOfActivity().toGMTString() + " " +
                getTimeOfActivity().toGMTString() + " " +
                getFitnessType().toString() + " " +
                String.valueOf(getDuration()) + " " +
                String.valueOf(getDistance());
    }

    /**
     * calculate the average time for this FitnessActivity
     * @return the average time
     */
    public int getAverage() {
        if (_average == -1) { // average is not calculated yet
            float speed = (float)_distance / (float)_duration;
            float result = 1000 / speed;
            _average =  Math.round(result);
        }
        return _average;
    }
}